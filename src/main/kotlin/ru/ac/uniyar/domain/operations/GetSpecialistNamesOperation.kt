package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.tables.SpecialistTable

class GetSpecialistNamesOperation(private val database: Database) {
    fun list(): Map<Int, String> =
        database
            .from(SpecialistTable)
            .select(
                SpecialistTable.id,
                SpecialistTable.fullName
            )
            .mapNotNull { it[SpecialistTable.id]!! to it[SpecialistTable.fullName]!! }
            .toMap()
}
