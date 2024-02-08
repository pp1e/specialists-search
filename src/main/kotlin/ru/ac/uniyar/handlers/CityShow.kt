package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.Query
import org.http4k.lens.int
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.CityListVM
import ru.ac.uniyar.models.CityVM
import ru.ac.uniyar.templates.ContextAwareViewRender

fun showCity(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    try {
        val id = qId(it)
        val page = Query.int().defaulted("page", 1).invoke(it)
        val viewModel = CityVM(
            city = operationsHolder.fetchCityOperation.fetch(id),
            serviceCategoryPage = PageStore(
                elements = operationsHolder.getServiceCategoryListForCityOperation.list(id),
                currentPage = page
            )
        )
        Response(Status.OK).with(view(it) of viewModel)
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

fun showCityList(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val page = qPage(it)
    val title = titleFilter(it) ?: ""
    val viewModel = CityListVM(
        cityPage = PageStore(
            elements = operationsHolder.getCityListOperation.list(title = title),
            currentPage = page
        ),
        fields = mapOf("title" to title)
    )
    Response(Status.OK).with(view(it) of viewModel)
}
