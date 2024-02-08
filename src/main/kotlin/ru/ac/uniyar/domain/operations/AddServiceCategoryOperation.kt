package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.tables.ServiceCategoryTable
import java.time.LocalDate

class AddServiceCategoryOperation(private val database: Database) {
    fun add(title: String) {
        database.insert(ServiceCategoryTable) {
            set(it.date, LocalDate.now())
            set(it.title, title)
        }
    }
}
