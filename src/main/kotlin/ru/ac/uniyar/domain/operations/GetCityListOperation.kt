package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.asc
import org.ktorm.dsl.from
import org.ktorm.dsl.like
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.City
import ru.ac.uniyar.domain.entities.City.Companion.fromResultSet
import ru.ac.uniyar.domain.tables.CityTable

class GetCityListOperation(private val database: Database) {
    fun list(title: String = ""): List<City> =
        database
            .from(CityTable)
            .select(CityTable.id, CityTable.title)
            .where(CityTable.title like "%$title%")
            .orderBy(CityTable.title.asc())
            .mapNotNull(::fromResultSet)
}
