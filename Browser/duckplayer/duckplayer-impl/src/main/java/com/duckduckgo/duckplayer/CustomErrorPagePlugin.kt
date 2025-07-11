

package com.duckduckgo.duckplayer

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.DuckPlayerPageSettingsPlugin
import com.duckduckgo.duckplayer.impl.DuckPlayerFeature
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import org.json.JSONObject

@ContributesMultibinding(AppScope::class)
class CustomErrorPagePlugin @Inject constructor(
    private val duckPlayerFeature: DuckPlayerFeature,
) : DuckPlayerPageSettingsPlugin {
    override fun getSettings(): JSONObject {
        val customErrorObject = JSONObject()
        customErrorObject.put("state", if (duckPlayerFeature.customError().isEnabled()) "enabled" else "disabled")
        duckPlayerFeature.customError().getSettings()?.let { settings ->
            customErrorObject.put("settings", JSONObject(settings))
        }

        return customErrorObject
    }

    override fun getName(): String = "customError"
}
