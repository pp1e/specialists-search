package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.UserTable

class GetSpecialistsCountOperation(private val database: Database) {
    fun get(): Int =
        database
            .from(UserTable)
            .select(UserTable.roleID)
            .where(UserTable.roleID eq 4)
            .totalRecords
}
