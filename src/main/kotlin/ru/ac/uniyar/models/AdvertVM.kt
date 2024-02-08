package ru.ac.uniyar.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.entities.Advert

data class AdvertVM(val advert: Advert) : ViewModel
