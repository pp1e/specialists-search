package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.tables.CityTable

class GetCityTitlesOperation(private val database: Database) {
    fun list(): Map<Int, String> =
        database
            .from(CityTable)
            .select(
                CityTable.id,
                CityTable.title
            )
            .mapNotNull { it[CityTable.id]!! to it[CityTable.title]!! }
            .toMap()
}
