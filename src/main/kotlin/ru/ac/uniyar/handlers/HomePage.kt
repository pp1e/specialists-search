package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.HomeVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import kotlin.math.round

fun startPage(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val specialistsCount = operationsHolder.getSpecialistCountOperation.get()
    Response(Status.OK).with(
        view(it) of HomeVM(
            specialistsCount = specialistsCount,
            avgAdverts = round(operationsHolder.getAdvertsCountOperation.get().toDouble() / specialistsCount).toInt()
        )
    )
}

fun redirectToStartPage(): HttpHandler = {
    Response(Status.FOUND).headers(headers = listOf(Pair("location", "/home")))
}
