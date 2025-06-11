package ca.hojat.gamehub.feature_discovery.di

import dagger.MapKey

@MapKey
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class DiscoverKey(val type: Type) {

    enum class Type {
        POPULAR,
        RECENTLY_RELEASED,
        COMING_SOON,
        MOST_ANTICIPATED
    }
}
