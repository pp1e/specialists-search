package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.Specialist

data class SpecialistListVM(
    val specialistPage: PageStore<Specialist>,
    val fields: Map<String, String>
) : ViewModel
