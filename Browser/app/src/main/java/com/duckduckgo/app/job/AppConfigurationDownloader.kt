

package com.duckduckgo.app.job

import com.duckduckgo.app.surrogates.api.ResourceSurrogateListDownloader
import com.duckduckgo.app.survey.api.SurveyDownloader
import com.duckduckgo.app.trackerdetection.api.TrackerDataDownloader
import com.duckduckgo.httpsupgrade.api.HttpsUpgradeDataDownloader
import io.reactivex.Completable
import timber.log.Timber

interface ConfigurationDownloader {
    fun downloadTask(): Completable
}

class AppConfigurationDownloader(
    private val trackerDataDownloader: TrackerDataDownloader,
    private val httpsUpgradeDataDownloader: HttpsUpgradeDataDownloader,
    private val resourceSurrogateDownloader: ResourceSurrogateListDownloader,
    private val surveyDownloader: SurveyDownloader,
) : ConfigurationDownloader {

    override fun downloadTask(): Completable {
        val tdsDownload = trackerDataDownloader.downloadTds()
        val clearLegacyLists = trackerDataDownloader.clearLegacyLists()
        val surrogatesDownload = resourceSurrogateDownloader.downloadList()
        val httpsUpgradeDownload = httpsUpgradeDataDownloader.download()
        val surveyDownload = surveyDownloader.download()

        return Completable.mergeDelayError(
            listOf(
                tdsDownload,
                clearLegacyLists,
                surrogatesDownload,
                httpsUpgradeDownload,
                surveyDownload,
            ),
        ).doOnComplete {
            Timber.i("Download task completed successfully")
        }
    }
}
