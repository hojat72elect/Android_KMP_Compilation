

package com.duckduckgo.voice.impl.listeningmode.ui

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VoiceSearchBackgroundBlurRenderer {
    fun addBlur(view: View)
    fun removeBlur(view: View)
}

@ContributesBinding(ActivityScope::class)
class RealVoiceSearchBackgroundBlurRenderer @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) : VoiceSearchBackgroundBlurRenderer {

    @SuppressLint("NewApi")
    override fun addBlur(view: View) {
        if (appBuildConfig.sdkInt >= Build.VERSION_CODES.S) {
            view.setRenderEffect(
                RenderEffect.createBlurEffect(70f, 70f, Shader.TileMode.MIRROR),
            )
        }
    }

    @SuppressLint("NewApi")
    override fun removeBlur(view: View) {
        if (appBuildConfig.sdkInt >= Build.VERSION_CODES.S) {
            view.setRenderEffect(null)
        }
    }
}
