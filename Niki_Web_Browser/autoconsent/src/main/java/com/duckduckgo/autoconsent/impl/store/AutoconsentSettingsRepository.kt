package com.duckduckgo.autoconsent.impl.store

import android.content.Context
import com.duckduckgo.autoconsent.impl.remoteconfig.AutoconsentFeature
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope

interface AutoconsentSettingsRepository : AutoconsentSettingsDataStore {
    companion object {
        fun create(
            context: Context,
            autoconsentFeature: AutoconsentFeature,
            appCoroutineScope: CoroutineScope,
            dispatcherProvider: DispatcherProvider,
        ): AutoconsentSettingsRepository {
            val store = RealAutoconsentSettingsDataStore(context, autoconsentFeature, appCoroutineScope, dispatcherProvider)
            return RealAutoconsentSettingsRepository(store)
        }
    }
}

internal class RealAutoconsentSettingsRepository constructor(
    private val autoconsentSettingsDataStore: AutoconsentSettingsDataStore,
) : AutoconsentSettingsRepository, AutoconsentSettingsDataStore by autoconsentSettingsDataStore
