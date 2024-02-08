package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.CityTable

data class City(
    val id: Int = -1,
    val title: String = "",
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): City? =
            try {
                City(
                    row[CityTable.id]!!,
                    row[CityTable.title]!!,
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
