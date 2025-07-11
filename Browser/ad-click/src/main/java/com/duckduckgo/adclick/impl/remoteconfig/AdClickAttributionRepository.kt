package com.duckduckgo.adclick.impl.remoteconfig

import com.duckduckgo.adclick.impl.store.AdClickAttributionAllowlistEntity
import com.duckduckgo.adclick.impl.store.AdClickAttributionDetectionEntity
import com.duckduckgo.adclick.impl.store.AdClickAttributionExpirationEntity
import com.duckduckgo.adclick.impl.store.AdClickAttributionLinkFormatEntity
import com.duckduckgo.adclick.impl.store.AdClickDao
import com.duckduckgo.adclick.impl.store.AdClickDatabase
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

interface AdClickAttributionRepository {
    fun updateAll(
        linkFormats: List<AdClickAttributionLinkFormat>,
        allowList: List<AdClickAttributionAllowlist>,
        navigationExpiration: Long,
        totalExpiration: Long,
        heuristicDetection: String?,
        domainDetection: String?,
    )

    val linkFormats: List<AdClickAttributionLinkFormatEntity>
    val allowList: List<AdClickAttributionAllowlistEntity>
    val expirations: List<AdClickAttributionExpirationEntity>
    val detections: List<AdClickAttributionDetectionEntity>
}

class RealAdClickAttributionRepository(
    val database: AdClickDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : AdClickAttributionRepository {

    private val adClickAttributionDao: AdClickDao = database.adClickDao()

    override val linkFormats = CopyOnWriteArrayList<AdClickAttributionLinkFormatEntity>()
    override val allowList = CopyOnWriteArrayList<AdClickAttributionAllowlistEntity>()
    override val expirations = CopyOnWriteArrayList<AdClickAttributionExpirationEntity>()
    override val detections = CopyOnWriteArrayList<AdClickAttributionDetectionEntity>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(
        linkFormats: List<AdClickAttributionLinkFormat>,
        allowList: List<AdClickAttributionAllowlist>,
        navigationExpiration: Long,
        totalExpiration: Long,
        heuristicDetection: String?,
        domainDetection: String?,
    ) {
        adClickAttributionDao.setAll(
            linkFormats = linkFormats.map { AdClickAttributionLinkFormatEntity(it.url, it.adDomainParameterName.orEmpty()) },
            allowList = allowList.map { AdClickAttributionAllowlistEntity(it.blocklistEntry.orEmpty(), it.host.orEmpty()) },
            expirations = listOf(
                AdClickAttributionExpirationEntity(
                    navigationExpiration = navigationExpiration,
                    totalExpiration = totalExpiration,
                ),
            ),
            detections = listOf(
                AdClickAttributionDetectionEntity(
                    heuristicDetection = heuristicDetection.orEmpty(),
                    domainDetection = domainDetection.orEmpty(),
                ),
            ),
        )
        loadToMemory()
    }

    private fun loadToMemory() {
        linkFormats.clear()
        linkFormats.addAll(adClickAttributionDao.getLinkFormats())

        allowList.clear()
        allowList.addAll(adClickAttributionDao.getAllowList())

        expirations.clear()
        expirations.addAll(adClickAttributionDao.getExpirations())

        detections.clear()
        detections.addAll(adClickAttributionDao.getDetections())
    }
}
