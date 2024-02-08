package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.LensFailure
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import ru.ac.uniyar.authentication.Role
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.RequestListVM
import ru.ac.uniyar.models.RequestVM
import ru.ac.uniyar.templates.ContextAwareViewRender

fun showRequest(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    try {
        val id = qId(it)
        val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
        val request = operationsHolder.fetchRequestOperation.fetch(id)
        if ((!user.role.permissions.canWatchAllRequests) and (user.login != request.specialist.userLogin))
            return@handler Response(Status.UNAUTHORIZED)
        val viewModel = RequestVM(request = operationsHolder.fetchRequestOperation.fetch(id))
        Response(Status.OK).with(view(it) of viewModel)
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

val requestStatusFormBody = Body.webForm(
    Validator.Feedback,
    commentaryField,
    confirmButton,
    rejectButton,
).toLens()

fun setRequestStatus(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    try {
        val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
        if (!user.role.permissions.canOperateAllRequests)
            return@handler Response(Status.UNAUTHORIZED)

        val webForm = requestStatusFormBody(it)
        var customErrors = emptyMap<String, String>()
        val id = qId(it)
        val request = operationsHolder.fetchRequestOperation.fetch(id)
        val reject = rejectButton(webForm)
        val confirm = confirmButton(webForm)
        val commentary = commentaryField(webForm)

        if ((reject != null) and (commentary == ""))
            customErrors = customErrors.plus(
                commentaryField.meta.name to
                    "При отклонении заявки необходимо добавить комментарий!"
            )
        if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
            if (reject != null)
                operationsHolder.updateRequestOperation.update(
                    id = id,
                    commentary = commentary,
                    status = ru.ac.uniyar.domain.entities.Status.REJECTED,
                )
            else if (confirm != null) {
                operationsHolder.updateRequestOperation.update(
                    id = id,
                    status = ru.ac.uniyar.domain.entities.Status.CONFIRMED
                )
                operationsHolder.updateUserOperation.update(
                    role = Role.SPECIALIST,
                    login = request.specialist.userLogin
                )
            }

            Response(Status.FOUND).headers(headers = listOf(Pair("location", "/request/list")))
        } else
            Response(Status.OK).with(
                view(it) of RequestVM(
                    webForm = webForm,
                    request = request,
                    customErrors = customErrors
                )
            )
    } catch (e: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (e: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}

fun showRequestList(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val page = qPage(it)
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    val viewModel = RequestListVM(
        requestPage = PageStore(
            elements = if (user.role.permissions.canWatchAllRequests)
                operationsHolder.getRequestListOperation.list()
            else
                operationsHolder.getRequestListOperation.list(user.login),
            currentPage = page
        ),
    )
    Response(Status.OK).with(view(it) of viewModel)
}
