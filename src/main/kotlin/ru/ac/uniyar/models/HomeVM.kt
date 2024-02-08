package ru.ac.uniyar.models

import org.http4k.template.ViewModel

data class HomeVM(
    val specialistsCount: Int,
    val avgAdverts: Int
) : ViewModel
