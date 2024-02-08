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
import ru.ac.uniyar.models.NewCityDataVM
import ru.ac.uniyar.templates.ContextAwareViewRender

val cityFormBody = Body.webForm(
    Validator.Feedback,
    titleField,
).toLens()

fun showNewCityForm(view: ContextAwareViewRender): HttpHandler = {
    Response(Status.OK).with(view(it) of NewCityDataVM(WebForm()))
}

fun createNewCity(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val webForm = cityFormBody(it)
    if (webForm.errors.isEmpty()) {
        operationsHolder.addCityOperation.add(title = titleField(webForm))
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/city/list")))
    } else
        Response(Status.OK).with(view(it) of NewCityDataVM(webForm))
}
