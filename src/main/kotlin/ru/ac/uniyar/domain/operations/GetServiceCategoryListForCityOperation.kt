package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.ServiceCategory
import ru.ac.uniyar.domain.tables.AdvertTable
import ru.ac.uniyar.domain.tables.CityTable
import ru.ac.uniyar.domain.tables.ServiceCategoryTable

class GetServiceCategoryListForCityOperation(private val database: Database) {
    fun list(cityID: Int): List<ServiceCategory> =
        database
            .from(AdvertTable)
            .innerJoin(ServiceCategoryTable, on = ServiceCategoryTable.id eq AdvertTable.serviceCategoryID)
            .innerJoin(CityTable, on = CityTable.id eq AdvertTable.cityID)
            .selectDistinct(
                ServiceCategoryTable.id,
                AdvertTable.cityID,
                ServiceCategoryTable.date,
                ServiceCategoryTable.title,
            )
            .where(cityID eq AdvertTable.cityID)
            .orderBy(ServiceCategoryTable.date.desc())
            .mapNotNull {
                ServiceCategory.fromResultAndCount(
                    it,
                    GetAdvertsCountForCategoryInCityOperation(database)
                        .get(cityID, it[ServiceCategoryTable.id]!!)
                )
            }
}
