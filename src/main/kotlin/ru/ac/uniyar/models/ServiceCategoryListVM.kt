package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.db.PageStore
import ru.ac.uniyar.domain.entities.ServiceCategory

data class ServiceCategoryListVM(
    val serviceCategoryPage: PageStore<ServiceCategory>,
    val fields: Map<String, String>
) : ViewModel
