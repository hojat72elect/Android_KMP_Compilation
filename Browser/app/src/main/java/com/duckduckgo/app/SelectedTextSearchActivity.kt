

package com.duckduckgo.app

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duckduckgo.app.browser.BrowserActivity
import timber.log.Timber

/**
 * Exists purely to pull out the intent extra and launch the query in a new tab.
 * This needs to be its own Activity so that we can customize the label that is user-facing, presented when the user selects some text.
 */
class SelectedTextSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query = extractQuery(intent)
        startActivity(BrowserActivity.intent(this, queryExtra = query, selectedText = true, interstitialScreen = true))
        finish()
    }

    private fun extractQuery(intent: Intent?): String? {
        if (intent == null) return null

        val textSelectionQuery = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
        if (textSelectionQuery != null) return textSelectionQuery

        val webSearchQuery = intent.getStringExtra(SearchManager.QUERY)
        if (webSearchQuery != null) return webSearchQuery

        Timber.w("SelectedTextSearchActivity launched with unexpected intent format")
        return null
    }
}
