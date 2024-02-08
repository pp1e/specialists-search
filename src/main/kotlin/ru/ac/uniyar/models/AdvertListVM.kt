package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.Advert

data class AdvertListVM(
    val advertPage: PageStore<Advert>,
    val fields: Map<String, String>
) : ViewModel
