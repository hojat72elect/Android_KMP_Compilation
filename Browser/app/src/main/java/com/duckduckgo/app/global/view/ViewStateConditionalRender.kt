

package com.duckduckgo.app.global.view

import timber.log.Timber

/**
 * This method will execute the given lambda only if the given view states differ
 */
inline fun renderIfChanged(
    newViewState: Any,
    lastSeenViewState: Any?,
    block: () -> Unit,
) {
    if (newViewState == lastSeenViewState) {
        Timber.v("view state identical to last seen state; skipping rendering for ${newViewState.javaClass.simpleName}")
    } else {
        block()
    }
}
