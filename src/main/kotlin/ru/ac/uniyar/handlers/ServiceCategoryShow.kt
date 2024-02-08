package ru.ac.uniyar.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.ServiceCategoryListVM
import ru.ac.uniyar.templates.ContextAwareViewRender

fun showServiceCategoryList(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val page = qPage(it)
    val title = titleFilter(it) ?: ""
    val viewModel = ServiceCategoryListVM(
        serviceCategoryPage = PageStore(
            elements = operationsHolder.getServiceCategoryListOperation.list(title),
            currentPage = page
        ),
        fields = mapOf("title" to title)
    )
    Response(Status.OK).with(view(it) of viewModel)
}
