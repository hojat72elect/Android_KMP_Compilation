

package com.duckduckgo.subscriptions.impl.ui

import android.os.Bundle
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.subscriptions.impl.databinding.ActivityChangePlanBinding
import com.duckduckgo.subscriptions.impl.ui.ChangePlanActivity.Companion.ChangePlanScreenWithEmptyParams

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(ChangePlanScreenWithEmptyParams::class)
class ChangePlanActivity : DuckDuckGoActivity() {

    private val binding: ActivityChangePlanBinding by viewBinding()

    private val toolbar
        get() = binding.includeToolbar.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(toolbar)
    }

    companion object {
        data object ChangePlanScreenWithEmptyParams : GlobalActivityStarter.ActivityParams
    }
}
