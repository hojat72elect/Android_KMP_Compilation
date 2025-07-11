

package com.duckduckgo.duckplayer.api

import org.json.JSONObject

interface DuckPlayerPageSettingsPlugin {

    fun getSettings(): JSONObject

    fun getName(): String
}
