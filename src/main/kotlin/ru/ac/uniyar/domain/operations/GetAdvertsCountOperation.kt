package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.tables.AdvertTable

class GetAdvertsCountOperation(private val database: Database) {
    fun get(): Int =
        database
            .from(AdvertTable)
            .select(AdvertTable.id)
            .totalRecords
}
