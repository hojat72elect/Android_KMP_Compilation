

package com.duckduckgo.networkprotection.impl.di

import javax.inject.Qualifier

/** Identifies a production version of the config toggles DAO */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ProdNetPConfigTogglesDao
