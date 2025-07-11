

package com.duckduckgo.voice.impl

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION_CODES
import android.speech.SpeechRecognizer
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.extensions.toSanitizedLanguageTag
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

data class VoiceSearchAvailabilityConfig(
    val deviceManufacturer: String,
    val sdkInt: Int,
    val languageTag: String,
    val isOnDeviceSpeechRecognitionSupported: Boolean,
)

interface VoiceSearchAvailabilityConfigProvider {
    fun get(): VoiceSearchAvailabilityConfig
}

@ContributesBinding(AppScope::class)
class DefaultVoiceSearchAvailabilityConfigProvider @Inject constructor(
    private val context: Context,
    private val appBuildConfig: AppBuildConfig,
) : VoiceSearchAvailabilityConfigProvider {

    @SuppressLint("NewApi")
    override fun get(): VoiceSearchAvailabilityConfig = VoiceSearchAvailabilityConfig(
        deviceManufacturer = appBuildConfig.manufacturer,
        sdkInt = appBuildConfig.sdkInt,
        languageTag = appBuildConfig.deviceLocale.toSanitizedLanguageTag(),
        isOnDeviceSpeechRecognitionSupported = if (appBuildConfig.sdkInt >= VERSION_CODES.S) {
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
        } else {
            false
        },
    )
}
