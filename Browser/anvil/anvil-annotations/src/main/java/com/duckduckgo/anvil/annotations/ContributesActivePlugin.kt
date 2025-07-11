

package com.duckduckgo.anvil.annotations

import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import kotlin.reflect.KClass

/**
 * Anvil annotation to contribute plugins into an Active Plugin Point.
 * Active plugins are also guarded by remote feature flags
 *
 * This annotation is the counterpart of [ContributesActivePluginPoint]
 *
 * Usage:
 * ```kotlin
 * @ContributesActivePlugin(SomeDaggerScope::class)
 * class MyPluginImpl : MyPlugin {
 *
 * }
 *
 * interface MyPlugin : ActivePlugin {...}
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesActivePlugin(
    /** The scope in which to include this contributed PluginPoint */
    val scope: KClass<*>,

    /**
     * This is the type of the plugin the annotated class is extending from
     * This is a required member to help the code generation.
     */
    val boundType: KClass<*>,

    /**
     * The default value of remote feature flag.
     * Default is true (ie. enabled)
     */
    val defaultActiveValue: DefaultFeatureValue = DefaultFeatureValue.TRUE,

    /**
     * The priority for the plugin.
     * Lower priority values mean the associated plugin comes first in the list of plugins.
     *
     * This is equivalent to the [PriorityKey] annotation we use with [ContributesMultibinding] for normal plugins.
     * The [ContributesActivePlugin] coalesce both
     */
    val priority: Int = 0,

    /**
     * When `true` the backing feature flag supports experimentation. Otherwise it will be a regular feature flag.
     *
     * When `true` the (generated) backing feature flag is annotated with the [Experiment] annotation (read its JavaDoc)
     */
    val supportExperiments: Boolean = false,

    /**
     * When `true` the backing feature flag will ALWAYS be enabled for internal builds, regardless of remote config or default value.
     */
    val internalAlwaysEnabled: Boolean = false,
)
