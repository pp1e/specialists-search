package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.Specialist
import ru.ac.uniyar.domain.entities.Specialist.Companion.fromResultSet
import ru.ac.uniyar.domain.tables.SpecialistTable

class GetSpecialistListOperation(private val database: Database) {
    fun list(
        education: String = "",
        fullName: String = ""
    ): List<Specialist> =
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
            .where(
                (SpecialistTable.fullName like "%$fullName%") and
                    (SpecialistTable.education like "%$education%")
            )
            .orderBy(SpecialistTable.date.desc())
            .mapNotNull(::fromResultSet)
}
