

package com.duckduckgo.app.global

import android.content.Intent
import android.os.BadParcelableException
import android.os.Bundle
import timber.log.Timber

val Intent.intentText: String?
    get() {
        return data?.toString() ?: getStringExtra(Intent.EXTRA_TEXT)
    }

fun Intent.sanitize() {
    try {
        // The strings are empty to force unparcel() call in BaseBundle
        getStringExtra("")
        getBooleanExtra("", false)
    } catch (e: BadParcelableException) {
        Timber.e(e, "Failed to read Parcelable from intent")
        replaceExtras(Bundle())
    } catch (e: RuntimeException) {
        Timber.e(e, "Failed to receive extras from intent")
        replaceExtras(Bundle())
    }
}
