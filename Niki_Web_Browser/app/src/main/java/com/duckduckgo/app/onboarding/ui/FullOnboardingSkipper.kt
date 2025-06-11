

package com.duckduckgo.app.onboarding.ui

import com.duckduckgo.app.cta.db.DismissedCtaDao
import com.duckduckgo.app.cta.model.CtaId
import com.duckduckgo.app.cta.model.DismissedCta
import com.duckduckgo.app.onboarding.store.AppStage
import com.duckduckgo.app.onboarding.store.UserStageStore
import com.duckduckgo.app.onboarding.ui.FullOnboardingSkipper.ViewState
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

interface OnboardingSkipper {
    suspend fun markOnboardingAsCompleted()
    val privacyConfigDownloaded: SharedFlow<ViewState>
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
@ContributesBinding(AppScope::class, OnboardingSkipper::class)
@SingleInstanceIn(AppScope::class)
class FullOnboardingSkipper @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val settingsDataStore: SettingsDataStore,
    private val dismissedCtaDao: DismissedCtaDao,
    private val userStageStore: UserStageStore,
) : OnboardingSkipper, PrivacyConfigCallbackPlugin {

    private val _privacyConfigDownloaded = MutableStateFlow(ViewState())
    override val privacyConfigDownloaded = _privacyConfigDownloaded.asStateFlow()

    @Suppress("DEPRECATION")
    override suspend fun markOnboardingAsCompleted() {
        withContext(dispatchers.io()) {
            settingsDataStore.hideTips = true
            dismissedCtaDao.insert(DismissedCta(CtaId.ADD_WIDGET))
            userStageStore.stageCompleted(AppStage.DAX_ONBOARDING)
        }
    }

    override fun onPrivacyConfigDownloaded() {
        _privacyConfigDownloaded.value = ViewState(skipOnboardingPossible = true)
    }

    data class ViewState(
        val skipOnboardingPossible: Boolean = false,
    )
}
