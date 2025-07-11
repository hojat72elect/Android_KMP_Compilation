

package dagger

import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SingleInstanceIn(
    val scope: KClass<*>,
)
