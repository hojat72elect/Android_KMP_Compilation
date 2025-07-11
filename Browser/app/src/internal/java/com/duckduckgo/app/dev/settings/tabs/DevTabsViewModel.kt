

package com.duckduckgo.app.dev.settings.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.tabs.model.TabDataRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val randomUrls = listOf(
    "https://duckduckgo.com",
    "https://blog.duckduckgo.com",
    "https://duck.com",
    "https://privacy.com",
    "https://spreadprivacy.com",
    "https://wikipedia.org",
    "https://privacyguides.org",
    "https://tosdr.org",
    "https://signal.org",
    "https://eff.org",
    "https://fsf.org",
    "https://opensource.org",
    "https://archive.org",
    "https://torproject.org",
    "https://linux.org",
    "https://gnu.org",
    "https://apache.org",
    "https://debian.org",
    "https://ubuntu.com",
    "https://openbsd.org",
)

@ContributesViewModel(ActivityScope::class)
class DevTabsViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val tabDataRepository: TabDataRepository,
) : ViewModel() {

    data class ViewState(
        val tabCount: Int = 0,
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    init {
        tabDataRepository.flowTabs
            .onEach { tabs ->
                _viewState.update { it.copy(tabCount = tabs.count()) }
            }
            .flowOn(dispatcher.io())
            .launchIn(viewModelScope)
    }

    fun addTabs(count: Int) {
        viewModelScope.launch {
            repeat(count) {
                val randomIndex = randomUrls.indices.random()
                tabDataRepository.add(
                    url = randomUrls[randomIndex],
                )
            }
        }
    }

    fun clearTabs() {
        viewModelScope.launch(dispatcher.io()) {
            tabDataRepository.deleteAll()
        }
    }
}
