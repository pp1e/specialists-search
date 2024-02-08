package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.AdvertTable

class GetAdvertsCountForCategoryOperation(private val database: Database) {
    fun get(serviceCategoryID: Int): Int =
        database
            .from(AdvertTable)
            .select(AdvertTable.serviceCategoryID)
            .where(serviceCategoryID eq AdvertTable.serviceCategoryID)
            .totalRecords
}
