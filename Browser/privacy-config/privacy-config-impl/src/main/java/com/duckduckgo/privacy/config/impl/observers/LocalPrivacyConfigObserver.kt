

package com.duckduckgo.privacy.config.impl.observers

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.impl.PrivacyConfigPersister
import com.duckduckgo.privacy.config.impl.R
import com.duckduckgo.privacy.config.impl.models.JsonPrivacyConfig
import com.duckduckgo.privacy.config.impl.network.JSONObjectAdapter
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.Moshi
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@WorkerThread
@SingleInstanceIn(AppScope::class)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class LocalPrivacyConfigObserver @Inject constructor(
    private val context: Context,
    private val privacyConfigPersister: PrivacyConfigPersister,
    @AppCoroutineScope val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        coroutineScope.launch(dispatcherProvider.io()) { loadPrivacyConfig() }
    }

    private suspend fun loadPrivacyConfig() {
        val privacyConfigJson = getPrivacyConfigFromFile()
        privacyConfigJson?.let {
            privacyConfigPersister.persistPrivacyConfig(it)
        }
    }

    private fun getPrivacyConfigFromFile(): JsonPrivacyConfig? {
        val moshi = Moshi.Builder().add(JSONObjectAdapter()).build()
        val json = context.resources.openRawResource(R.raw.privacy_config).bufferedReader().use { it.readText() }
        val adapter = moshi.adapter(JsonPrivacyConfig::class.java)
        return adapter.fromJson(json)
    }
}
