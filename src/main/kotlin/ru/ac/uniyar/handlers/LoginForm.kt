package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import ru.ac.uniyar.authentication.JwtTools
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.LoginVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

val loginFormBody = Body.webForm(
    validator = Validator.Feedback,
    loginField,
    passwordField
).toLens()

fun showLoginForm(view: ContextAwareViewRender): HttpHandler = {
    Response(Status.OK).with(view(it) of LoginVM())
}

fun authenticateUser(view: ContextAwareViewRender, operationsHolder: OperationsHolder, jwtTools: JwtTools): HttpHandler = {
    val webForm = loginFormBody(it)

    val login = lensOrDefault(loginField, webForm, "")
    val user = operationsHolder.getUserOperation.get(login)
    var customErrors = mapOf<String, String>()
    if (user == null)
        customErrors = customErrors.plus(loginField.meta.name to "Такого пользователя не существует!")
    else if (!operationsHolder.checkPasswordOperation.check(login, lensOrDefault(passwordField, webForm, "")))
        customErrors = customErrors.plus(passwordField.meta.name to "Неверный пароль!")

    if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
        Response(Status.FOUND).cookie(
            Cookie(
                name = "auth_token",
                value = jwtTools.createToken(login)
            )
        ).headers(headers = listOf(Pair("location", "/home")))
    } else
        Response(Status.OK).with(view(it) of LoginVM(webForm, customErrors))
}
