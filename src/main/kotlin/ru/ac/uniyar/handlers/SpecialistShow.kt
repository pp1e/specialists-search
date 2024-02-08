package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.SpecialistListVM
import ru.ac.uniyar.models.SpecialistVM
import ru.ac.uniyar.templates.ContextAwareViewRender

fun showSpecialist(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    try {
        val id = qId(it)
        val page = qPage(it)
        val showInfo = qShowInfo(it)
        val viewModel = SpecialistVM(
            specialist = operationsHolder.fetchSpecialistOperation.fetch(id),
            advertPage = PageStore(
                elements = operationsHolder.getAdvertListForSpecialistOperation.list(id),
                currentPage = page
            ),
            showContactInfo = showInfo
        )
        Response(Status.OK).with(view(it) of viewModel)
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

fun showSpecialistList(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val page = qPage(it)
    val fullName = fullNameFilter(it) ?: ""
    val education = educationFilter(it) ?: ""
    val viewModel = SpecialistListVM(
        specialistPage = PageStore(
            operationsHolder.getSpecialistListOperation.list(
                fullName = fullName,
                education = education
            ),
            currentPage = page
        ),
        fields = mapOf(
            "fullName" to fullName,
            "education" to education
        )
    )
    Response(Status.OK).with(view(it) of viewModel)
}
