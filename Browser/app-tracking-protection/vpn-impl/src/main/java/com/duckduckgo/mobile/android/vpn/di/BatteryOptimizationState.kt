

package com.duckduckgo.mobile.android.vpn.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
internal annotation class BatteryOptimizationState
