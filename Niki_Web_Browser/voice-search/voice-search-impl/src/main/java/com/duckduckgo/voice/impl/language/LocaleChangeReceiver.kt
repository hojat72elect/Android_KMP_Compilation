

package com.duckduckgo.voice.impl.language

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.di.scopes.ReceiverScope
import com.duckduckgo.voice.impl.VoiceSearchAvailabilityConfigProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

@InjectWith(ReceiverScope::class)
class LocaleChangeReceiver : BroadcastReceiver() {

    @Inject lateinit var languageSupportChecker: LanguageSupportChecker

    @Inject lateinit var configProvider: VoiceSearchAvailabilityConfigProvider

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)

        if (intent.action == Intent.ACTION_LOCALE_CHANGED) {
            languageSupportChecker.checkLanguageSupport(configProvider.get().languageTag)
        }
    }
}
