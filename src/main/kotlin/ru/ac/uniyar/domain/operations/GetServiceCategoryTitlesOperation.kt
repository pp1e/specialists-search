package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.tables.ServiceCategoryTable

class GetServiceCategoryTitlesOperation(private val database: Database) {
    fun list(): Map<Int, String> =
        database
            .from(ServiceCategoryTable)
            .select(
                ServiceCategoryTable.id,
                ServiceCategoryTable.title
            )
            .mapNotNull { it[ServiceCategoryTable.id]!! to it[ServiceCategoryTable.title]!! }
            .toMap()
}
