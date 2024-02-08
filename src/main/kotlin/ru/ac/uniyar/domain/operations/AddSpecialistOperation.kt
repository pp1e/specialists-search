package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import ru.ac.uniyar.domain.tables.SpecialistTable
import java.time.LocalDate

class AddSpecialistOperation(private val database: Database) {
    fun add(
        fullName: String,
        education: String,
        phoneNumber: String,
        userLogin: String
    ) {
        database.insert(SpecialistTable) {
            set(it.date, LocalDate.now())
            set(it.fullName, fullName)
            set(it.education, education)
            set(it.phoneNumber, phoneNumber)
            set(it.userLogin, userLogin)
        }
    }
}
