

package com.duckduckgo.anvil.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesWorker(
    /** The scope in which to include this contributed Worker */
    val scope: KClass<*>,
)
