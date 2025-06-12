package com.amaze.fileutilities.utilis

import java.util.PriorityQueue

/**
 * A [PriorityQueue] that will at most only contain [fixedSize] many elements.
 * If more elements than [fixedSize] are added, the smallest elements based on [comparator] will be removed.
 * Therefore, this priority queue will only contain the largest elements that were added to it.
 */
class FixedSizePriorityQueue<E>(
    private val fixedSize: Int,
    comparator: Comparator<E>
) : PriorityQueue<E>(fixedSize + 1, comparator) {
    // initial capacity is set to fixedSize + 1 because we first add the new element and then fix the size
    /**
     * Adds [element] to the priority queue.
     * If there are already [fixedSize] many elements in the queue then the smallest element is removed.
     */
    override fun add(element: E): Boolean {
        super.add(element)
        // Makes sure that the size of the priority queue doesn't exceed fixedSize
        if (this.size > fixedSize) {
            this.remove()
        }
        return true
    }
}
