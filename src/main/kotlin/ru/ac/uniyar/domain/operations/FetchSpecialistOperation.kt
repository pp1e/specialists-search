package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.Specialist
import ru.ac.uniyar.domain.tables.SpecialistTable

class FetchSpecialistOperation(private val database: Database) {
    fun fetch(id: Int): Specialist =
        database
            .from(SpecialistTable)
            .select(
                SpecialistTable.id,
                SpecialistTable.date,
                SpecialistTable.fullName,
                SpecialistTable.education,
                SpecialistTable.phoneNumber,
                SpecialistTable.userLogin
            )
            .where(SpecialistTable.id eq id)
            .mapNotNull(Specialist.Companion::fromResultSet)
            .first()

    fun fetch(login: String): Specialist? =
        database
            .from(SpecialistTable)
            .select(
                SpecialistTable.id,
                SpecialistTable.date,
                SpecialistTable.fullName,
                SpecialistTable.education,
                SpecialistTable.phoneNumber,
                SpecialistTable.userLogin
            )
            .where(SpecialistTable.userLogin eq login)
            .mapNotNull(Specialist.Companion::fromResultSet)
            .firstOrNull()
}
