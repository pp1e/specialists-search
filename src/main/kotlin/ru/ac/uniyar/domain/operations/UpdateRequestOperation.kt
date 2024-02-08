package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.entities.Status
import ru.ac.uniyar.domain.tables.RequestTable

class UpdateRequestOperation(private val database: Database) {
    fun update(
        id: Int,
        status: Status,
        commentary: String = ""
    ) {
        database.update(RequestTable) {
            set(it.status, status.id)
            set(it.commentary, commentary)
            where {
                it.id eq id
            }
        }
    }
}
