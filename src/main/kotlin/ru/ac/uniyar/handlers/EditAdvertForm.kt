package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.LensFailure
import org.http4k.lens.WebForm
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.NewAdvertDataVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

fun showEditAdvertForm(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    try {
        val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
        if (!user.role.permissions.canOperateAdverts)
            return@handler Response(Status.UNAUTHORIZED)
        val id = qId(it)
        val advert = operationsHolder.fetchAdvertOperation.fetch(id)
        if (advert.specialist.userLogin != user.login)
            return@handler Response(Status.UNAUTHORIZED)

        Response(Status.OK).with(
            view(it) of NewAdvertDataVM(
                webForm = WebForm(
                    fields = mapOf(
                        titleField.meta.name to listOf(advert.title),
                        descriptionField.meta.name to listOf(advert.description),
                        serviceCategoryIDField.meta.name to listOf(advert.serviceCategory.id.toString())
                    )
                ),
                serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesFromRequestsOperation.list(user.login, ru.ac.uniyar.domain.entities.Status.CONFIRMED),
            )
        )
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

fun editAdvert(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    try {
        val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
        if (!user.role.permissions.canOperateAdverts)
            return@handler Response(Status.UNAUTHORIZED)

        val id = qId(it)
        val advert = operationsHolder.fetchAdvertOperation.fetch(id)
        if (advert.specialist.userLogin != user.login)
            return@handler Response(Status.UNAUTHORIZED)

        val webForm = advertFormBody(it)
        var customErrors = emptyMap<String, String>()

        val serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesFromRequestsOperation.list(user.login, ru.ac.uniyar.domain.entities.Status.CONFIRMED)
        val serviceCategoryID = lensOrDefault(serviceCategoryIDField, webForm, -1)

        if (!serviceCategoryTitles.containsKey(serviceCategoryID))
            customErrors = customErrors.plus(serviceCategoryIDField.meta.name to "У вас нет подтвержденной заявки в этой категории!")

        if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
            operationsHolder.updateAdvertOperation.update(
                serviceCategoryID = serviceCategoryID,
                description = descriptionField(webForm),
                title = titleField(webForm),
                id = id
            )
            Response(Status.FOUND).headers(headers = listOf(Pair("location", "/advert/list/$id")))
        } else
            Response(Status.OK).with(
                view(it) of NewAdvertDataVM(
                    webForm = webForm, customErrors = customErrors,
                    serviceCategoryTitles = serviceCategoryTitles,
                )
            )
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
