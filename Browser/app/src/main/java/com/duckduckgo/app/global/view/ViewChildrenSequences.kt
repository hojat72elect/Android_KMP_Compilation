

@file:Suppress("unused")

package com.duckduckgo.app.global.view

import android.view.View
import android.view.ViewGroup
import java.util.ConcurrentModificationException
import java.util.NoSuchElementException

fun View.childrenSequence(): Sequence<View> = ViewChildrenSequence(this)

/**
 * Return the [Sequence] of all children of the received [View], recursively.
 * Note that the sequence is not thread-safe.
 *
 * @return the [Sequence] of children.
 */
fun View.childrenRecursiveSequence(): Sequence<View> = ViewChildrenRecursiveSequence(this)

private class ViewChildrenSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return ViewIterator(view)
    }

    private class ViewIterator(private val view: ViewGroup) : Iterator<View> {
        private var index = 0
        private val count = view.childCount

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            return view.getChildAt(index++)
        }

        override fun hasNext(): Boolean {
            checkCount()
            return index < count
        }

        private fun checkCount() {
            if (count != view.childCount) throw ConcurrentModificationException()
        }
    }
}

private class ViewChildrenRecursiveSequence(private val view: View) : Sequence<View> {
    override fun iterator(): Iterator<View> {
        if (view !is ViewGroup) return emptyList<View>().iterator()
        return RecursiveViewIterator(view)
    }

    private class RecursiveViewIterator(view: View) : Iterator<View> {
        private val sequences = arrayListOf(view.childrenSequence())
        private var current = sequences.removeLast().iterator()

        override fun next(): View {
            if (!hasNext()) throw NoSuchElementException()
            val view = current.next()
            if (view is ViewGroup && view.childCount > 0) {
                sequences.add(view.childrenSequence())
            }
            return view
        }

        override fun hasNext(): Boolean {
            if (!current.hasNext() && sequences.isNotEmpty()) {
                current = sequences.removeLast().iterator()
            }
            return current.hasNext()
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun <T : Any> MutableList<T>.removeLast(): T {
            if (isEmpty()) throw NoSuchElementException()
            return removeAt(size - 1)
        }
    }
}
