

package com.duckduckgo.savedsites.api.models

class TreeNode<T>(val value: T) {

    private val children: MutableList<TreeNode<T>> = mutableListOf()
    fun add(child: TreeNode<T>) = children.add(child)
    fun isEmpty() = children.isEmpty()

    fun forEachVisit(
        visitBefore: Visitor<T>,
        visitAfter: Visitor<T>,
    ) {
        visitBefore(this)
        children.forEach {
            it.forEachVisit(visitBefore, visitAfter)
        }
        visitAfter(this)
    }
}

typealias Visitor<T> = (TreeNode<T>) -> Unit
