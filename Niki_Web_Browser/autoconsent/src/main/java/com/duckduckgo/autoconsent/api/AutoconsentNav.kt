package com.duckduckgo.autoconsent.api

import android.content.Context
import android.content.Intent

/**
 * Public interface to provide navigation Intents related to Cookie Prompt Management
 */
interface AutoconsentNav {
    fun openAutoconsentSettings(context: Context): Intent
}
