

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

/**
 * Anvil annotation to generate the Dagger subcomponents for all Android types like Activities, Fragments, Services, etc.
 *
 * Usage:
 * ```kotlin
 * @ContributesSubComponent(SomeDaggerScope::class)
 * class MyActivity {
 *
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectWith(
    /** The parent component scope to the contributed subcomponent */
    val scope: KClass<*>,

    /** (optional) The binding class key to bind the dagger component */
    val bindingKey: KClass<*> = Unit::class,

    /** (optional) set to true if you want to delay the generation of the dagger component to the :app module */
    val delayGeneration: Boolean = false,
)
