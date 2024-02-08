package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.tables.AdvertTable
import java.time.LocalDate

class AddAdvertOperation(private val database: Database) {
    fun add(
        serviceCategoryID: Int,
        cityID: Int,
        specialistID: Int,
        title: String,
        description: String,
    ) {
        database.insert(AdvertTable) {
            set(it.date, LocalDate.now())
            set(it.cityID, cityID)
            set(it.serviceCategoryID, serviceCategoryID)
            set(it.specialistID, specialistID)
            set(it.description, description)
            set(it.title, title)
        }
    }
}
