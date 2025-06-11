

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.VpnScope

@ContributesPluginPoint(
    scope = VpnScope::class,
    boundType = VpnServiceCallbacks::class,
)
@Suppress("unused")
interface VpnServiceCallbacksPluginPoint
