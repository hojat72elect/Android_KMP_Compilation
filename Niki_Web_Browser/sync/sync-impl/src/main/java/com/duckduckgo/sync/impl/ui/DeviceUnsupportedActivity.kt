

package com.duckduckgo.sync.impl.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.impl.databinding.ActivityDeviceUnsupportedBinding

@InjectWith(ActivityScope::class)
class DeviceUnsupportedActivity : DuckDuckGoActivity() {

    private val binding: ActivityDeviceUnsupportedBinding by viewBinding()

    private val toolbar
        get() = binding.includeToolbar.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar(toolbar)
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, DeviceUnsupportedActivity::class.java)
        }
    }
}
