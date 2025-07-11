

package com.duckduckgo.privacy.config.impl.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/** Identifies a coroutine scope type that is scope to the app lifecycle */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class ConfigPersisterPreferences
