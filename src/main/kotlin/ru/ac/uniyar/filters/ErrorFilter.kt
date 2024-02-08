package ru.ac.uniyar.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.models.ErrorVM
import ru.ac.uniyar.templates.ContextAwareViewRender

class ErrorFilter(val view: ContextAwareViewRender) {
    val errorFilter = Filter { next: HttpHandler ->
        { request: Request ->
            val response = next(request)
            if (
                (response.status.clientError or response.status.serverError) and
                (response.header("ContentType") == null)
            )
                response.with(
                    view(request) of ErrorVM(errorFromStatus(response.status, request.uri.path))
                )
            else
                response
        }
    }
}

fun errorFromStatus(status: Status, path: String): String = when (status) {
    Status.UNAUTHORIZED -> "У вас недостаточно прав для просмотра страницы $path!"
    else ->
        "Страницы $path не существует!" +
            " Вернитесь на главную страницу и попробуйте ещё раз"
}
