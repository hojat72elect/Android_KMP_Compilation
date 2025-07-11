package ca.hojat.notes.niki.shared.interfaces

interface ChecklistItemsListener {
    fun refreshItems()

    fun saveChecklist(callback: () -> Unit = {})
}
