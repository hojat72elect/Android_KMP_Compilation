

package com.duckduckgo.cookies.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.duckduckgo.cookies.store.contentscopescripts.ContentScopeScriptsCookieDao
import com.duckduckgo.cookies.store.thirdpartycookienames.ThirdPartyCookieNamesDao

@Database(
    exportSchema = true,
    version = 3,
    entities = [
        FirstPartyCookiePolicyEntity::class,
        CookieExceptionEntity::class,
        CookieEntity::class,
        CookieNamesEntity::class,
    ],
)
abstract class CookiesDatabase : RoomDatabase() {
    abstract fun cookiesDao(): CookiesDao
    abstract fun cookieNamesDao(): ThirdPartyCookieNamesDao
    abstract fun contentScopeScriptsCookieDao(): ContentScopeScriptsCookieDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
