

package com.duckduckgo.app.di

import com.duckduckgo.app.job.AppConfigurationDownloader
import com.duckduckgo.app.job.ConfigurationDownloader
import com.duckduckgo.app.surrogates.api.ResourceSurrogateListDownloader
import com.duckduckgo.app.survey.api.SurveyDownloader
import com.duckduckgo.app.trackerdetection.api.TrackerDataDownloader
import com.duckduckgo.httpsupgrade.api.HttpsUpgradeDataDownloader
import dagger.Module
import dagger.Provides

@Module
open class AppConfigurationDownloaderModule {

    @Provides
    open fun appConfigurationDownloader(
        trackerDataDownloader: TrackerDataDownloader,
        httpsUpgradeDataDownloader: HttpsUpgradeDataDownloader,
        resourceSurrogateDownloader: ResourceSurrogateListDownloader,
        surveyDownloader: SurveyDownloader,
    ): ConfigurationDownloader {
        return AppConfigurationDownloader(
            trackerDataDownloader,
            httpsUpgradeDataDownloader,
            resourceSurrogateDownloader,
            surveyDownloader,
        )
    }
}
