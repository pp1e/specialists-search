package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.utils.ErrorsHandler

data class NewAdvertDataVM(
    val webForm: WebForm = WebForm(),
    val customErrors: Map<String, String> = emptyMap(),
    val serviceCategoryTitles: Map<Int, String>,
) : ViewModel {
    val errorsHandler = ErrorsHandler(webForm.errors, customErrors)
}
