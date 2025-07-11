

package com.duckduckgo.common.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class KeyboardVisibilityUtil(private val rootView: View) {

    fun addKeyboardVisibilityListener(onKeyboardVisible: () -> Unit) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (rootView.isKeyboardVisible()) {
                        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        onKeyboardVisible()
                    }
                }
            },
        )
    }
}

fun View.keyboardVisibilityFlow(): Flow<Boolean> = callbackFlow {
    val layoutObserver = ViewTreeObserver.OnGlobalLayoutListener {
        val isVisible = isKeyboardVisible()
        trySend(isVisible)
    }

    viewTreeObserver.addOnGlobalLayoutListener(layoutObserver)

    awaitClose {
        viewTreeObserver.removeOnGlobalLayoutListener(layoutObserver)
    }
}

private fun View.isKeyboardVisible(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = height
    val keypadHeight = screenHeight - rect.bottom
    return keypadHeight > screenHeight * KEYBOARD_VISIBILITY_THRESHOLD
}

private const val KEYBOARD_VISIBILITY_THRESHOLD = 0.15
