

package com.duckduckgo.app.anr.internal.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.anr.internal.databinding.CrashAnrsListViewBinding
import com.duckduckgo.app.anr.internal.feature.CrashANRsSettingPlugin
import com.duckduckgo.app.anr.internal.setting.VitalsAdapterList.VitalsItems.AnrItem
import com.duckduckgo.app.anr.internal.setting.VitalsAdapterList.VitalsItems.CrashItem
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@InjectWith(ViewScope::class)
class CrashANRsListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    @Inject
    lateinit var globalActivityStarter: GlobalActivityStarter

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var crashANRsRepository: CrashANRsRepository

    @Inject
    @AppCoroutineScope
    lateinit var appCoroutineScope: CoroutineScope

    private val binding: CrashAnrsListViewBinding by viewBinding()
    private lateinit var anrAdapter: VitalsAdapterList

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        configureANRList()

        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            crashANRsRepository.getANRs().combine(crashANRsRepository.getCrashes()) { anrs, crashes ->
                (
                    anrs.map {
                        AnrItem(
                            stackTrace = it.stackTrace,
                            customTab = it.customTab,
                            timestamp = it.timestamp,
                        )
                    } + crashes.map {
                        CrashItem(
                            stackTrace = it.stackTrace,
                            processName = it.processName,
                            customTab = it.customTab,
                            timestamp = it.timestamp,
                        )
                    }
                    ).sortedByDescending { it.timestamp }
            }.flowOn(dispatcherProvider.io()).collect {
                anrAdapter.setItems(it)
            }
        }
    }

    private fun configureANRList() {
        binding.anrList.layoutManager = LinearLayoutManager(context)
        anrAdapter = VitalsAdapterList()
        binding.anrList.adapter = anrAdapter
    }
}

@ContributesMultibinding(ActivityScope::class)
class CrashANRsListViewPlugin @Inject constructor() : CrashANRsSettingPlugin {
    override fun getView(context: Context): View {
        return CrashANRsListView(context)
    }
}
