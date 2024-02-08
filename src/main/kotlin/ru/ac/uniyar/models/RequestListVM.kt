package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.Request

data class RequestListVM(val requestPage: PageStore<Request>,) : ViewModel
