package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.City
import ru.ac.uniyar.domain.tables.CityTable

class FetchCityOperation(private val database: Database) {
    fun fetch(id: Int): City =
        database
            .from(CityTable)
            .select(CityTable.id, CityTable.title)
            .where(CityTable.id eq id)
            .mapNotNull(City.Companion::fromResultSet)
            .first()
}
