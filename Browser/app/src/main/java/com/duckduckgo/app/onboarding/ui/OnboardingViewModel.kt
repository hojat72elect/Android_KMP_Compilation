

package com.duckduckgo.app.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.onboarding.store.AppStage
import com.duckduckgo.app.onboarding.store.UserStageStore
import com.duckduckgo.app.onboarding.ui.page.OnboardingPageFragment
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ContributesViewModel(ActivityScope::class)
class OnboardingViewModel @Inject constructor(
    private val userStageStore: UserStageStore,
    private val pageLayoutManager: OnboardingPageManager,
    private val dispatchers: DispatcherProvider,
    private val onboardingSkipper: OnboardingSkipper,
    private val appBuildConfig: AppBuildConfig,
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun initializePages() {
        pageLayoutManager.buildPageBlueprints()
    }

    fun pageCount(): Int {
        return pageLayoutManager.pageCount()
    }

    fun getItem(position: Int): OnboardingPageFragment? {
        return pageLayoutManager.buildPage(position)
    }

    fun onOnboardingDone() {
        // Executing this on IO to avoid any delay changing threads between Main-IO.
        viewModelScope.launch(dispatchers.io()) {
            userStageStore.stageCompleted(AppStage.NEW)
        }
    }

    fun onOnboardingSkipped() {
        viewModelScope.launch(dispatchers.io()) {
            onboardingSkipper.markOnboardingAsCompleted()
        }
    }

    fun initializeOnboardingSkipper() {
        if (!appBuildConfig.canSkipOnboarding) return

        // delay showing skip button until privacy config downloaded
        viewModelScope.launch {
            onboardingSkipper.privacyConfigDownloaded.collect {
                _viewState.value = _viewState.value.copy(canShowSkipOnboardingButton = it.skipOnboardingPossible)
            }
        }
    }

    suspend fun devOnlyFullyCompleteAllOnboarding() {
        onboardingSkipper.markOnboardingAsCompleted()
    }

    companion object {
        data class ViewState(val canShowSkipOnboardingButton: Boolean = false)
    }
}
