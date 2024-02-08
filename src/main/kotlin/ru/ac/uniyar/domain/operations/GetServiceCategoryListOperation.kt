package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.ServiceCategory
import ru.ac.uniyar.domain.entities.ServiceCategory.Companion.fromResultAndCount
import ru.ac.uniyar.domain.tables.ServiceCategoryTable

class GetServiceCategoryListOperation(private val database: Database) {
    fun list(title: String = ""): List<ServiceCategory> =
        database
            .from(ServiceCategoryTable)
            .select(
                ServiceCategoryTable.id,
                ServiceCategoryTable.date,
                ServiceCategoryTable.title,
            )
            .where(ServiceCategoryTable.title like "%$title%")
            .orderBy(ServiceCategoryTable.date.desc())
            .mapNotNull {
                fromResultAndCount(
                    it,
                    GetAdvertsCountForCategoryOperation(database)
                        .get(it[ServiceCategoryTable.id]!!)
                )
            }
}
