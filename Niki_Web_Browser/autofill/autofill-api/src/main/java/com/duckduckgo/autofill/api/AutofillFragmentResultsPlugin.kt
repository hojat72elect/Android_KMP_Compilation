

package com.duckduckgo.autofill.api

import android.content.Context
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment

/**
 * A PluginPoint for autofill dialog result handlers
 *
 * This ties into the fragment result API, and allows plugins to handle the result of a fragment
 * For a fragment and its result handler to be associated, they must communicate using the same `resultKey`
 */
interface AutofillFragmentResultsPlugin {

    /**
     * Called when the result handler has a result to process
     * Will be invoked on the main thread.
     */
    @MainThread
    fun processResult(
        result: Bundle,
        context: Context,
        tabId: String,
        fragment: Fragment,
        autofillCallback: AutofillEventListener,
    )

    /**
     * A unique key that will match the result key a fragment specifies when returning a result
     * The tabId is provided to allow plugins to only handle results for a specific tab
     * @return the key used to identify the result of this fragment
     */
    fun resultKey(tabId: String): String
}
