package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.utils.ErrorsHandler

data class LoginVM(
    val webForm: WebForm = WebForm(),
    val customErrors: Map<String, String> = emptyMap()
) : ViewModel {
    val errorsHandler = ErrorsHandler(webForm.errors, customErrors)
}
