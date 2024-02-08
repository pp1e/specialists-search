package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.AdvertListVM
import ru.ac.uniyar.models.AdvertVM
import ru.ac.uniyar.templates.ContextAwareViewRender

fun showAdvert(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    try {
        val id = qId(it)
        val viewModel = AdvertVM(advert = operationsHolder.fetchAdvertOperation.fetch(id),)
        Response(Status.OK).with(view(it) of viewModel)
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

fun showAdvertList(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val page = qPage(it)
    val title = titleFilter(it) ?: ""
    val cityTitle = cityTitleFilter(it) ?: ""
    val serviceCategoryTitle = serviceCategoryTitleFilter(it) ?: ""

    val viewModel = AdvertListVM(
        advertPage = PageStore(
            operationsHolder.getAdvertListOperation.list(
                advertTitle = title,
                cityTitle = cityTitle,
                serviceCategoryTitle = serviceCategoryTitle
            ),
            page
        ),
        fields = mapOf(
            "cityTitle" to cityTitle,
            "title" to title,
            "serviceCategoryTitle" to serviceCategoryTitle
        )
    )
    Response(Status.OK).with(view(it) of viewModel)
}
