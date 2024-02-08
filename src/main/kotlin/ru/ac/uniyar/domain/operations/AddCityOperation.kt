package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.tables.CityTable

class AddCityOperation(private val database: Database) {
    fun add(title: String) {
        database.insert(CityTable) {
            set(it.title, title)
        }
    }
}
