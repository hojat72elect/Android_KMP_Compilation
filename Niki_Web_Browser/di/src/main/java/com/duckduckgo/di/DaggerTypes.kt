

package com.duckduckgo.di

/** Sugar over multibindings that helps with Kotlin wildcards. */
typealias DaggerSet<T> = Set<@JvmSuppressWildcards T>
typealias DaggerMap<K, V> = Map<@JvmSuppressWildcards K, @JvmSuppressWildcards V>
