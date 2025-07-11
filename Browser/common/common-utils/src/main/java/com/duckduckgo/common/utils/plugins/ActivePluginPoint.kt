

package com.duckduckgo.common.utils.plugins

@Deprecated(
    message = "Use \"ActivePluginPoint\" instead to ensure \"JvmSuppressWildcards\" is not forgotten",
    replaceWith = ReplaceWith("ActivePluginPoint"),
)
interface InternalActivePluginPoint<out T : @JvmSuppressWildcards ActivePlugin> {
    /** @return the list of plugins of type <T> */
    suspend fun getPlugins(): Collection<T>
}

/**
 * Active plugins SHALL extend from [ActivePlugin]
 *
 * Usage:
 * ```kotlin
 * @ContributesActivePluginPoint(
 *     scope = SomeScope::class,
 * )
 * interface MyActivePlugin : ActivePlugin {...}
 *
 * @ContributesActivePlugin(
 *     scope = SomeScope::class,
 *     boundType = MyActivePlugin::class,
 * )
 * class FooMyActivePlugin @Inject constructor() : MyActivePlugin {...}
 * ```
 */
interface ActivePlugin {
    suspend fun isActive(): Boolean = true
}

/**
 * Use this typealias to collect your [ActivePlugin]s
 *
 * Usage:
 * ```kotlin
 * class MyClass @Inject constructor(
 *   private val pp: ActivePluginPoint<MyActivePlugin>,
 * ) {...}
 * ```
 */
typealias ActivePluginPoint<T> = InternalActivePluginPoint<@JvmSuppressWildcards T>
