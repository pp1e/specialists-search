package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.tables.AdvertTable

class UpdateAdvertOperation(private val database: Database) {
    fun update(
        title: String,
        serviceCategoryID: Int,
        description: String,
        id: Int
    ) {
        database.update(AdvertTable) {
            set(it.description, description)
            set(it.serviceCategoryID, serviceCategoryID)
            set(it.title, title)
            where {
                it.id eq id
            }
        }
    }
}
