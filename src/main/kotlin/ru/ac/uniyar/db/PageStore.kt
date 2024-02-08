package ru.ac.uniyar.db

import kotlin.math.ceil

const val ELEMENTS_ON_PAGE = 5

class PageStore<T>(
    private val elements: List<T>,
    private val currentPage: Int,
) {
    private val pagesCount = ceil(elements.size.toDouble() / ELEMENTS_ON_PAGE).toInt()

    fun elementsOnPage(): List<T> =
        try {
            elements.subList(
                ELEMENTS_ON_PAGE * (currentPage - 1),
                if (ELEMENTS_ON_PAGE * currentPage > elements.size) elements.size else ELEMENTS_ON_PAGE * currentPage,
            )
        } catch (_: Exception) {
            emptyList()
        }

    fun isNextPageAvailable(): Boolean = currentPage < pagesCount

    fun isPrevPageAvailable(): Boolean = currentPage > 1

    fun nextPage(): Int = currentPage + 1

    fun prevPage(): Int = currentPage - 1

    fun currentPageToString(): String = "Страница ${if (elements.isEmpty()) "0" else currentPage}/$pagesCount "
}
