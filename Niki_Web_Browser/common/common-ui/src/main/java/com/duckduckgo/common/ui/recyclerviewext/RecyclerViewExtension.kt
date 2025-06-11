

package com.duckduckgo.common.ui.recyclerviewext

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator

fun RecyclerView.disableAnimation() {
    if (isComputingLayout) {
        post {
            itemAnimator = null
        }
    } else {
        itemAnimator = null
    }
}

fun RecyclerView.enableAnimation(animator: ItemAnimator? = DefaultItemAnimator()) {
    if (isComputingLayout) {
        post {
            itemAnimator = animator
        }
    } else {
        itemAnimator = animator
    }
}
