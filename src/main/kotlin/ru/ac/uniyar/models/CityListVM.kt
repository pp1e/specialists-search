package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.City

data class CityListVM(
    val cityPage: PageStore<City>,
    val fields: Map<String, String>
) : ViewModel
