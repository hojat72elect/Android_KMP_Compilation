

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate remote features
 *
 * Usage:
 * ```kotlin
 * @ContributesRemoteFeature(
 *   scope = AppScope::class,
 *   featureName = "myFeatureName",
 *   settingsStore = MyFeatureSettingsStore::class,
 *   exceptionStore = MyFeatureExceptionStore::class,
 *   toggleStore = MyFeatureToggleStore::class,
 * )
 * interface MyFeature {
 *
 * }
 * ```
 * where:
 * - `settingsStore` is optional and should be defined only when the feature contains settings
 * - `exceptionStore` is optional and should be defined only when the feature contains exceptions
 * - `toggleStore` is optional. If not provided the framework internally uses shared prefs. Provide it if you need specific
 * functionalities for this store, like eg. multi-process
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesRemoteFeature(
    /** The scope in which to include this contributed PluginPoint */
    val scope: KClass<*>,

    /**
     * Type that the feature will be bound to
     */
    val boundType: KClass<*> = Unit::class,

    /** The name of the remote feature */
    val featureName: String,

    /** The class that implements the [FeatureSettings.Store] interface */
    @Deprecated("Not needed anymore. Settings is now supported in top-level and sub-features and Toggle#getSettings returns it")
    val settingsStore: KClass<*> = Unit::class,

    /** The class that implements the [Toggle.Store] interface */
    val toggleStore: KClass<*> = Unit::class,
)
