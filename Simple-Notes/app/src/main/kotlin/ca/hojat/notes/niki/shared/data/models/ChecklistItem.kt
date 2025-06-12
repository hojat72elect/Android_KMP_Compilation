package ca.hojat.notes.niki.shared.data.models

import ca.hojat.notes.niki.shared.helpers.SORT_BY_TITLE
import ca.hojat.notes.niki.shared.helpers.SORT_DESCENDING
import ca.hojat.notes.niki.shared.helpers.CollatorBasedComparator
import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItem(
    val id: Int,
    val dateCreated: Long = 0L,
    var title: String,
    var isDone: Boolean
) : Comparable<ChecklistItem> {

    companion object {
        var sorting = 0
    }

    override fun compareTo(other: ChecklistItem): Int {
        var result = when {
            sorting and SORT_BY_TITLE != 0 -> CollatorBasedComparator().compare(title, other.title)
            else -> dateCreated.compareTo(other.dateCreated)
        }

        if (sorting and SORT_DESCENDING != 0) {
            result *= -1
        }

        return result
    }
}
