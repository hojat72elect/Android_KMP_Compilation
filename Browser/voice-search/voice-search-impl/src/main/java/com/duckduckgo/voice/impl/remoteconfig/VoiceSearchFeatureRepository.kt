

package com.duckduckgo.voice.impl.remoteconfig

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.voice.store.LocaleEntity
import com.duckduckgo.voice.store.ManufacturerEntity
import com.duckduckgo.voice.store.MinVersionEntity
import com.duckduckgo.voice.store.VoiceSearchDao
import com.duckduckgo.voice.store.VoiceSearchDatabase
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface VoiceSearchFeatureRepository {
    fun updateAllExceptions(manufacturerExceptions: List<Manufacturer>, localeExceptions: List<Locale>, minVersion: Int)
    val manufacturerExceptions: CopyOnWriteArrayList<Manufacturer>
    val localeExceptions: CopyOnWriteArrayList<Locale>
    val minVersion: Int?
}

class RealVoiceSearchFeatureRepository(
    database: VoiceSearchDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : VoiceSearchFeatureRepository {

    private val voiceSearchDao: VoiceSearchDao = database.voiceSearchDao()
    private var _minVersion: Int? = null
    override val manufacturerExceptions = CopyOnWriteArrayList<Manufacturer>()
    override val localeExceptions = CopyOnWriteArrayList<Locale>()
    override val minVersion: Int?
        get() = _minVersion

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAllExceptions(manufacturerExceptions: List<Manufacturer>, localeExceptions: List<Locale>, minVersion: Int) {
        voiceSearchDao.updateAll(
            manufacturerExceptions.map { ManufacturerEntity(it.name) },
            localeExceptions.map { LocaleEntity(it.name) },
            MinVersionEntity(minVersion),
        )
        loadToMemory()
    }

    private fun loadToMemory() {
        manufacturerExceptions.clear()
        val manufacturerExceptionsEntityList = voiceSearchDao.getManufacturerExceptions()
        manufacturerExceptions.addAll(manufacturerExceptionsEntityList.map { Manufacturer(it.name) })

        localeExceptions.clear()
        val localeExceptionsEntityList = voiceSearchDao.getLocaleExceptions()
        localeExceptions.addAll(localeExceptionsEntityList.map { Locale(it.name) })

        _minVersion = voiceSearchDao.getMinVersion()?.minVersion
    }
}
