

package com.duckduckgo.subscriptions.internal.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.ViewScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.subscriptions.impl.ui.RestoreSubscriptionActivity.Companion.RestoreSubscriptionScreenWithParams
import com.duckduckgo.subscriptions.internal.SubsSettingPlugin
import com.duckduckgo.subscriptions.internal.databinding.SubsSimpleViewBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

@InjectWith(ViewScope::class)
class RecoverSubscriptionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    @Inject
    lateinit var globalActivityStarter: GlobalActivityStarter

    private val binding: SubsSimpleViewBinding by viewBinding()

    override fun onAttachedToWindow() {
        AndroidSupportInjection.inject(this)
        super.onAttachedToWindow()

        binding.root.setPrimaryText("Already have a subscription")
        binding.root.setSecondaryText("To recover an existing subscription regardless of your location")

        binding.root.setClickListener {
            globalActivityStarter.start(context, RestoreSubscriptionScreenWithParams(isOriginWeb = false))
        }
    }
}

@ContributesMultibinding(ActivityScope::class)
class RecoverSubscriptionViewPlugin @Inject constructor() : SubsSettingPlugin {
    override fun getView(context: Context): View {
        return RecoverSubscriptionView(context)
    }
}
