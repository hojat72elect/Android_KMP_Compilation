

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate ViewModel factories
 *
 * Usage:
 * ```kotlin
 * @ContributesViewModel(SomeDaggerScope::class)
 * class MyViewModel @Inject constructor(...) {
 *
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesViewModel(
    /** The scope in which to include this contributed ViewModel */
    val scope: KClass<*>,
)
