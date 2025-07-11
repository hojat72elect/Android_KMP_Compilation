

package com.duckduckgo.feature.toggles.api

import javax.inject.Qualifier
import kotlin.reflect.KClass

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteFeatureStoreNamed(
    val value: KClass<*> = Unit::class,
)
