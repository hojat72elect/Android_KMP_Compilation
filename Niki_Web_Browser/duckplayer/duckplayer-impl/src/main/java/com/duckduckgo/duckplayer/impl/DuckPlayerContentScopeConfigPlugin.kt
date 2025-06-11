

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.isInternalBuild
import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.DuckPlayer
import com.duckduckgo.duckplayer.api.DuckPlayer.DuckPlayerState.ENABLED
import com.duckduckgo.duckplayer.api.DuckPlayerFeatureName
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

@ContributesMultibinding(AppScope::class)
class DuckPlayerContentScopeConfigPlugin @Inject constructor(
    private val duckPlayerFeatureRepository: DuckPlayerFeatureRepository,
    private val appBuildConfig: AppBuildConfig,
    private val duckPlayer: DuckPlayer,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = DuckPlayerFeatureName.DuckPlayer.value

        val config = duckPlayerFeatureRepository.getDuckPlayerRemoteConfigJson().let { jsonString ->
            if (appBuildConfig.isInternalBuild() && runBlocking { duckPlayer.getDuckPlayerState() == ENABLED }) {
                runCatching {
                    JSONObject(jsonString).takeIf { it.getString("state") == "internal" }?.apply {
                        put("state", "enabled")
                    }?.toString() ?: jsonString
                }.getOrDefault(jsonString)
            } else {
                jsonString
            }
        }

        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
