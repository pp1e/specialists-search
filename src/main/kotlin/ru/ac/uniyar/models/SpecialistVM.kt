package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.Advert
import ru.ac.uniyar.domain.entities.Specialist

data class SpecialistVM(
    val specialist: Specialist,
    val advertPage: PageStore<Advert>,
    val showContactInfo: Boolean
) : ViewModel
