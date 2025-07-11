

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate and contribute the Map<ActivityParams, Class<ActivityParams>> to the activity starter.
 * It is also possible to define a [screenName], that can be used to deeplink to a screen from RMF.
 *
 * The [screenName] should be named as [feature].<screenName>. For instance, for the VPN feature has many sub-screens, eg. main, settings and so
 * they could be named "vpn.main", "vpn.settings" etc.
 * Not all screens will have a parent feature, for instance the main settings screen would be named just "settings"
 *
 * Usage:
 * ```kotlin
 * @ContributeToActivityStarter(ExampleActivityParams::class, screenName = "example")
 * class MyActivity {
 *
 * }
 *
 * data class ExampleActivityParams(...) : ActivityParams
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ContributeToActivityStarter(
    /** The type of the input parameters received by the Activity */
    val paramsType: KClass<*>,
    /** Declares the deeplink name for the Activity */
    val screenName: String = "",
)
