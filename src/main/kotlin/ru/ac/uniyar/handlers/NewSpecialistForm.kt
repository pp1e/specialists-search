package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.webForm
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.NewSpecialistDataVM
import ru.ac.uniyar.templates.ContextAwareViewRender

val specialistFormBody = Body.webForm(
    Validator.Feedback,
    fullNameField,
    educationField,
    phoneNumberField
).toLens()

fun showNewSpecialistForm(view: ContextAwareViewRender): HttpHandler = {
    Response(Status.OK).with(view(it) of NewSpecialistDataVM(WebForm()))
}

fun createNewSpecialist(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val webForm = specialistFormBody(it)
    if (webForm.errors.isEmpty()) {
        operationsHolder.addSpecialistOperation.add(
            fullName = fullNameField(webForm),
            education = educationField(webForm),
            phoneNumber = phoneNumberField(webForm),
            userLogin = ""
        )
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/specialist/list")))
    } else
        Response(Status.OK).with(view(it) of NewSpecialistDataVM(webForm))
}
