package ru.ac.uniyar.handlers

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.webForm
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.operations.OperationsHolder
import ru.ac.uniyar.models.EditProfileVM
import ru.ac.uniyar.templates.ContextAwareViewRender
import ru.ac.uniyar.utils.lensOrDefault

val editProfileFormBody = Body.webForm(
    validator = Validator.Feedback,
    nameField,
    cityIDField
).toLens()

fun showEditProfileForm(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canChangeCity)
        return@handler Response(Status.UNAUTHORIZED)
    val webForm = WebForm(
        fields = mapOf(
            "cityID" to listOf(user.city!!.id.toString()), // Здесь !! уместно,
            // так как редактировать профиль могут только пользователь и специалист,
            // которые обязательно вводят город при регистрации
            "name" to listOf(user.name)
        )
    )
    Response(Status.OK).with(
        view(it) of EditProfileVM(
            cityTitles = operationsHolder.getCityTitlesOperation.list(),
            webForm = webForm
        )
    )
}

fun editProfile(view: ContextAwareViewRender, userLens: BiDiLens<Request, User?>, operationsHolder: OperationsHolder): HttpHandler = handler@{
    val webForm = editProfileFormBody(it)
    val cityTitles = operationsHolder.getCityTitlesOperation.list()

    val cityID = lensOrDefault(cityIDField, webForm, -1)
    var customErrors = mapOf<String, String>()
    if (!cityTitles.containsKey(cityID))
        customErrors = customErrors.plus(cityIDField.meta.name to "Такого города не существует!")
    val user = userLens(it) ?: return@handler Response(Status.UNAUTHORIZED)
    if (!user.role.permissions.canChangeCity)
        return@handler Response(Status.UNAUTHORIZED)

    if (webForm.errors.isEmpty() and customErrors.isEmpty()) {
        operationsHolder.updateUserOperation.update(
            login = user.login,
            name = nameField(webForm),
            cityID = cityID,
        )
        Response(Status.FOUND).headers(headers = listOf(Pair("location", "/home")))
    } else
        Response(Status.OK).with(view(it) of EditProfileVM(cityTitles, webForm, customErrors))
}
