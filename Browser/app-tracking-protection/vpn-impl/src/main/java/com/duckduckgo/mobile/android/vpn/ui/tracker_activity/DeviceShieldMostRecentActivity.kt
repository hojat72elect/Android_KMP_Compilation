

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.databinding.ActivityDeviceShieldAllTracerActivityBinding

@InjectWith(ActivityScope::class)
class DeviceShieldMostRecentActivity : DuckDuckGoActivity() {

    private val binding: ActivityDeviceShieldAllTracerActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar(binding.includeToolbar.toolbar)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        internal fun intent(context: Context): Intent {
            return Intent(context, DeviceShieldMostRecentActivity::class.java)
        }
    }
}
