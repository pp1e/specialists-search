package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.AdvertTable

class GetAdvertsCountForCategoryInCityOperation(private val database: Database) {
    fun get(cityID: Int, serviceCategoryID: Int): Int =
        database
            .from(AdvertTable)
            .select(
                AdvertTable.serviceCategoryID,
                AdvertTable.cityID
            )
            .where((cityID eq AdvertTable.cityID) and (serviceCategoryID eq AdvertTable.serviceCategoryID))
            .totalRecords
}
