package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.City
import ru.ac.uniyar.domain.entities.ServiceCategory

data class CityVM(
    val city: City,
    val serviceCategoryPage: PageStore<ServiceCategory>
) : ViewModel
