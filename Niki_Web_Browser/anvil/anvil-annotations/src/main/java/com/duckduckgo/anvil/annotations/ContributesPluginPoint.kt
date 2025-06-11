

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate plugin points
 *
 * Usage:
 * ```kotlin
 * @ContributesPluginPoint(SomeDaggerScope::class)
 * interface MyPlugin {
 *
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesPluginPoint(
    /** The scope in which to include this contributed PluginPoint */
    val scope: KClass<*>,

    /**
     * The type that the plugin point will be bound to. This is useful when the plugin interfaces are defined in
     * modules where we don't want or can't generate code, eg. API gradle modules.
     *
     * usage:
     * ```kotlin
     * @ContributesPluginPoint(
     *   scope = AppScope::class,
     *   boundType: MyPlugin::class
     * )
     * interface MyPluginImpl : MyPlugin
     * ```
     */
    val boundType: KClass<*> = Unit::class,
)
