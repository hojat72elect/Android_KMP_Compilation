

package com.duckduckgo.voice.impl.language

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION_CODES
import android.speech.RecognitionSupport
import android.speech.RecognitionSupportCallback
import androidx.annotation.RequiresApi
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.voice.impl.VoiceSearchAvailabilityConfigProvider
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface LanguageSupportChecker {
    fun isLanguageSupported(): Boolean
    fun checkLanguageSupport(languageTag: String)
}

@SuppressLint("NewApi")
@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealLanguageSupportChecker @Inject constructor(
    private val context: Context,
    configProvider: VoiceSearchAvailabilityConfigProvider,
    private val languageSupportCheckerDelegate: LanguageSupportCheckerDelegate,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
) : LanguageSupportChecker {
    private var isLanguageSupported: Boolean? = null

    init {
        val config = configProvider.get()
        if (config.sdkInt >= VERSION_CODES.TIRAMISU) {
            appCoroutineScope.launch(dispatcherProvider.main()) {
                checkLanguageSupport(config.languageTag)
            }
        }
    }

    @RequiresApi(VERSION_CODES.TIRAMISU)
    override fun checkLanguageSupport(languageTag: String) {
        languageSupportCheckerDelegate.checkRecognitionSupport(
            context,
            languageTag,
            object : RecognitionSupportCallback {
                override fun onSupportResult(recognitionSupport: RecognitionSupport) {
                    isLanguageSupported = languageTag in recognitionSupport.installedOnDeviceLanguages
                }

                override fun onError(error: Int) {
                    isLanguageSupported = false
                }
            },
        )
    }

    override fun isLanguageSupported(): Boolean {
        return isLanguageSupported ?: false
    }
}
