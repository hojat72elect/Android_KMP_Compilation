

package com.duckduckgo.voice.impl.language

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.speech.RecognitionSupportCallback
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer.createOnDeviceSpeechRecognizer
import androidx.annotation.RequiresApi
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.concurrent.Executors
import javax.inject.Inject
import timber.log.Timber

interface LanguageSupportCheckerDelegate {
    fun checkRecognitionSupport(context: Context, languageTag: String, callback: RecognitionSupportCallback)
}

@ContributesBinding(AppScope::class)
class RealLanguageSupportCheckerDelegate @Inject constructor() : LanguageSupportCheckerDelegate {
    @RequiresApi(VERSION_CODES.TIRAMISU)
    override fun checkRecognitionSupport(context: Context, languageTag: String, callback: RecognitionSupportCallback) {
        runCatching {
            createOnDeviceSpeechRecognizer(context).checkRecognitionSupport(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
                Executors.newSingleThreadExecutor(),
                callback,
            )
        }.onFailure {
            Timber.e(it, "Failed to check voice recognition support")
        }
    }
}
