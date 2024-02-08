package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.entities.Status
import ru.ac.uniyar.domain.tables.RequestTable

class AddRequestOperation(private val database: Database) {
    fun add(
        userLogin: String,
        commentary: String = "",
        serviceCategoryID: Int,
        status: Status = Status.NEW,
        experience: String
    ) {
        database.insert(RequestTable) {
            set(it.status, status.id)
            set(it.userLogin, userLogin)
            set(it.commentary, commentary)
            set(it.serviceCategoryID, serviceCategoryID)
            set(it.experience, experience)
        }
    }
}
