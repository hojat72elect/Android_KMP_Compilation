

package com.duckduckgo.app.browser.webshare

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.duckduckgo.js.messaging.api.JsCallbackData
import org.json.JSONObject

class WebShareChooser : ActivityResultContract<JsCallbackData, JsCallbackData>() {

    lateinit var data: JsCallbackData
    override fun createIntent(
        context: Context,
        input: JsCallbackData,
    ): Intent {
        data = input
        val url = runCatching { input.params.getString("url") }.getOrNull().orEmpty()
        val text = runCatching { input.params.getString("text") }.getOrNull().orEmpty()
        val title = runCatching { input.params.getString("title") }.getOrNull().orEmpty()

        val finalText = url.ifEmpty { text }

        val getContentIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, finalText)
            if (title.isNotEmpty()) {
                putExtra(Intent.EXTRA_TITLE, title)
            }
        }

        return Intent.createChooser(getContentIntent, title)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): JsCallbackData {
        val jsCallback = if (this::data.isInitialized) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    JsCallbackData(
                        featureName = data.featureName,
                        method = data.method,
                        id = data.id,
                        params = JSONObject(EMPTY),
                    )
                }
                Activity.RESULT_CANCELED -> {
                    JsCallbackData(
                        featureName = data.featureName,
                        method = data.method,
                        id = data.id,
                        params = JSONObject(ABORT_ERROR),
                    )
                }
                else -> {
                    JsCallbackData(
                        featureName = data.featureName,
                        method = data.method,
                        id = data.id,
                        params = JSONObject(DATA_ERROR),
                    )
                }
            }
        } else {
            JsCallbackData(
                featureName = "",
                method = "",
                id = "",
                params = JSONObject(DATA_ERROR),
            )
        }
        return jsCallback
    }

    companion object {
        const val EMPTY = """{}"""
        const val ABORT_ERROR = """{"failure":{"name":"AbortError","message":"Share canceled"}}"""
        const val DATA_ERROR = """{"failure":{"name":"DataError","message":"Data not found"}}"""
    }
}
