package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.ServiceCategory
import ru.ac.uniyar.domain.tables.ServiceCategoryTable

class FetchServiceCategoryOperation(private val database: Database) {
    fun fetch(id: Int): ServiceCategory =
        database
            .from(ServiceCategoryTable)
            .select(
                ServiceCategoryTable.id,
                ServiceCategoryTable.date,
                ServiceCategoryTable.title,
            )
            .where(ServiceCategoryTable.id eq id)
            .mapNotNull {
                ServiceCategory.fromResultAndCount(
                    it,
                    GetAdvertsCountForCategoryOperation(database)
                        .get(it[ServiceCategoryTable.id]!!)
                )
            }
            .first()
}
