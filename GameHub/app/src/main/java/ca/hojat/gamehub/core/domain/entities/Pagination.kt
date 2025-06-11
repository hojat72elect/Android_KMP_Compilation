package ca.hojat.gamehub.core.domain.entities

private const val DEFAULT_PAGE_SIZE = 20

/**
 * This is to comply with pagination requirements of the server; not the pagination library of Android.
 */
data class Pagination(
    val offset: Int = 0,
    val limit: Int = DEFAULT_PAGE_SIZE
) {

    fun hasDefaultLimit(): Boolean {
        return (limit == DEFAULT_PAGE_SIZE)
    }

    fun nextOffset(): Pagination {
        return copy(offset = (offset + DEFAULT_PAGE_SIZE))
    }

    fun nextLimit(): Pagination {
        return copy(limit = (limit + DEFAULT_PAGE_SIZE))
    }

}
