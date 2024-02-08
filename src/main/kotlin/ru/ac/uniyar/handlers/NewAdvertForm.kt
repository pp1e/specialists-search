package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.webForm
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.NewAdvertDataVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

val advertFormBody = Body.webForm(
    Validator.Feedback,
    serviceCategoryIDField,
    titleField,
    descriptionField,
).toLens()

fun showNewAdvertForm(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canOperateAdverts)
        return@handler Response(Status.UNAUTHORIZED)

    Response(Status.OK).with(
        view(it) of NewAdvertDataVM(
            webForm = WebForm(),
            serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesFromRequestsOperation.list(user.login, ru.ac.uniyar.domain.entities.Status.CONFIRMED),
        )
    )
}

fun createNewAdvert(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canOperateAdverts)
        return@handler Response(Status.UNAUTHORIZED)

    val webForm = advertFormBody(it)
    var customErrors = emptyMap<String, String>()
    val specialist = operationsHolder.fetchSpecialistOperation.fetch(user.login)!! // Специалист не равен null,
    // т.к. выше проверка, является ли пользователь специалистом

    val serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesFromRequestsOperation.list(user.login, ru.ac.uniyar.domain.entities.Status.CONFIRMED)
    val serviceCategoryID = lensOrDefault(serviceCategoryIDField, webForm, -1)

    if (!serviceCategoryTitles.containsKey(serviceCategoryID))
        customErrors = customErrors.plus(serviceCategoryIDField.meta.name to "У вас нет подтвержденной заявки в этой категории!")

    if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
        operationsHolder.addAdvertOperation.add(
            serviceCategoryID = serviceCategoryID,
            cityID = user.city!!.id, // У специалистов всегда есть город
            specialistID = specialist.id,
            description = descriptionField(webForm),
            title = titleField(webForm)
        )
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/advert/list")))
    } else
        Response(Status.OK).with(
            view(it) of NewAdvertDataVM(
                webForm = webForm, customErrors = customErrors,
                serviceCategoryTitles = serviceCategoryTitles,
            )
        )
}
