

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate a Retrofit service interface implementation.
 *
 * Usage:
 * ```kotlin
 * @ContributesServiceApi(SomeDaggerScope::class)
 * interface MyServiceApi {
 *
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesServiceApi(
    /** The scope in which to include this contributed PluginPoint */
    val scope: KClass<*>,

    /**
     * The type that the plugin point will be bound to. This is useful when the plugin interfaces are defined in
     * modules where we don't want or can't generate code, eg. API gradle modules.
     *
     * usage:
     * ```kotlin
     * @ContributesServiceApi(
     *   scope = AppScope::class,
     * )
     * interface MyRetrofitServiceApi {...}
     * ```
     */
    val boundType: KClass<*> = Unit::class,
)
