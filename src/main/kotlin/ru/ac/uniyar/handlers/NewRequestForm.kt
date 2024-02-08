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
import ru.ac.uniyar.models.NewRequestDataVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

val fullRequestFormBody = Body.webForm(
    Validator.Feedback,
    educationField,
    phoneNumberField,
    serviceCategoryIDField,
    experienceField,
).toLens()
val requestFormBody = Body.webForm(
    Validator.Feedback,
    serviceCategoryIDField,
    experienceField,
).toLens()

fun showNewRequestForm(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canSubmitRequest)
        return@handler Response(Status.UNAUTHORIZED)
    Response(Status.OK).with(
        view(it) of NewRequestDataVM(
            webForm = WebForm(),
            showFullForm = (operationsHolder.fetchSpecialistOperation.fetch(user.login) == null),
            serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesOperation.list()
        )
    )
}

fun createNewRequest(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canSubmitRequest)
        return@handler Response(Status.UNAUTHORIZED)

    val specialist = operationsHolder.fetchSpecialistOperation.fetch(user.login)
    val webForm = if (specialist == null) fullRequestFormBody(it) else requestFormBody(it)

    var customErrors = emptyMap<String, String>()
    val serviceCategoryTitles = operationsHolder.getServiceCategoryTitlesOperation.list()
    val serviceCategoryID = lensOrDefault(serviceCategoryIDField, webForm, -1)
    if (!serviceCategoryTitles.containsKey(serviceCategoryID))
        customErrors = customErrors.plus(serviceCategoryIDField.meta.name to "Такой категории услуги не существует!")

    if (webForm.errors.isEmpty()) {
        if (specialist == null)
            operationsHolder.addSpecialistOperation.add(
                fullName = user.name,
                userLogin = user.login,
                education = educationField(webForm),
                phoneNumber = phoneNumberField(webForm)
            )
        operationsHolder.addRequestOperation.add(
            userLogin = user.login,
            experience = experienceField(webForm),
            serviceCategoryID = serviceCategoryIDField(webForm)
        )
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/request/list")))
    } else
        Response(Status.OK).with(
            view(it) of NewRequestDataVM(
                webForm = webForm,
                customErrors = customErrors,
                showFullForm = (specialist == null),
                serviceCategoryTitles = serviceCategoryTitles
            )
        )
}
