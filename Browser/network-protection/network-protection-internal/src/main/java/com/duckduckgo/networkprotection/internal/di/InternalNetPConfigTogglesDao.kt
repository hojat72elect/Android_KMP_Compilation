

package com.duckduckgo.networkprotection.internal.di

import javax.inject.Qualifier

/** Identifies a production version of the config toggles DAO */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class InternalNetPConfigTogglesDao
