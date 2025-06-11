

package com.duckduckgo.mobile.android.vpn.store

import android.content.Context
import androidx.room.RoomDatabase
import com.duckduckgo.common.utils.DispatcherProvider
import javax.inject.Provider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex

class VpnDatabaseCallbackProvider constructor(
    private val context: Context,
    private val vpnDatabaseProvider: Provider<VpnDatabase>,
    private val dispatcherProvider: DispatcherProvider,
    private val coroutineScope: CoroutineScope,
    private val mutex: Mutex,
) {
    fun provideCallbacks(): RoomDatabase.Callback {
        return VpnDatabaseCallback(context, vpnDatabaseProvider, dispatcherProvider, coroutineScope, mutex)
    }
}
