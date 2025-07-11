

package com.duckduckgo.app

import androidx.lifecycle.Observer

/**
 * Concrete class of Observer<T> to validate if any value has been received when observing a LiveData
 *
 * @param skipFirst: skip first value emitted by LiveData stream.
 * SkipFirst is usually:
 * Set to False to observe a SingleLiveEvent or any LiveData that starts emitting after subscription.
 * Set to True when observing any LiveData that emits its lastValue on subscription.
 */
class ValueCaptorObserver<T>(private var skipFirst: Boolean = true) : Observer<T> {
    var hasReceivedValue = false
        private set

    var lastValueReceived: T? = null
        private set

    override fun onChanged(t: T) {
        if (skipFirst) {
            skipFirst = false
            return
        }
        lastValueReceived = t
        hasReceivedValue = true
    }
}
