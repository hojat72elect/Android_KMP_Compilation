

package dagger

import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class WrongScope(
    val comment: String,
    val correctScope: KClass<*>,
)
