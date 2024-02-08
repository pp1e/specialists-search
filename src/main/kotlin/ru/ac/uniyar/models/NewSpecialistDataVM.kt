package ru.ac.uniyar.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.utils.ErrorsHandler

data class NewSpecialistDataVM(val webForm: WebForm) : ViewModel {
    val errorsHandler = ErrorsHandler(webForm.errors)
}
