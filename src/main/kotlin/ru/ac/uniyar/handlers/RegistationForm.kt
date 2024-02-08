package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.webForm
import ru.ac.uniyar.authentication.Role
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.RegistrationVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

val registrationFormBody = Body.webForm(
    validator = Validator.Feedback,
    loginField,
    passwordField,
    passwordRepeatField,
    nameField,
    cityIDField
).toLens()

fun showRegistrationForm(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    Response(Status.OK).with(
        view(it) of RegistrationVM(
            cityTitles = operationsHolder.getCityTitlesOperation.list(),
            webForm = WebForm()
        )
    )
}

fun registerUser(view: ContextAwareViewRender, operationsHolder: OperationsHolder): HttpHandler = {
    val webForm = registrationFormBody(it)
    val cityTitles = operationsHolder.getCityTitlesOperation.list()

    val login = lensOrDefault(loginField, webForm, "")
    val password = lensOrDefault(passwordField, webForm, "")
    val passwordRepeat = lensOrDefault(passwordRepeatField, webForm, "")
    val cityID = lensOrDefault(cityIDField, webForm, -1)
    var customErrors = mapOf<String, String>()
    if (operationsHolder.getUserOperation.get(login) != null)
        customErrors = customErrors.plus(loginField.meta.name to "Пользователь с таким логином уже существует!")
    if (password != passwordRepeat)
        customErrors = customErrors.plus(passwordField.meta.name to "Пароли должны совпадать!")
    if (!cityTitles.containsKey(cityID))
        customErrors = customErrors.plus(cityIDField.meta.name to "Такого города не существует!")

    if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
        operationsHolder.addUserOperation.add(
            User(
                role = Role.USER,
                login = login,
                password = password,
                name = nameField(webForm),
                city = operationsHolder.fetchCityOperation.fetch(cityID)
            )
        )
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/login")))
    } else
        Response(Status.OK).with(view(it) of RegistrationVM(cityTitles, webForm, customErrors))
}
