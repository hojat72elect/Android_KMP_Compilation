

package com.duckduckgo.cookies.store.contentscopescripts

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.cookies.store.CookieEntity
import com.duckduckgo.cookies.store.CookiesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ContentScopeScriptsCookieRepository {
    fun updateAll(
        cookieEntity: CookieEntity,
    )
    fun getCookieEntity(): CookieEntity
}

class RealContentScopeScriptsCookieRepository constructor(
    database: CookiesDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : ContentScopeScriptsCookieRepository {

    private val contentScopeScriptsCookieDao: ContentScopeScriptsCookieDao = database.contentScopeScriptsCookieDao()
    private var cookieEntity = CookieEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(cookieEntity: CookieEntity) {
        contentScopeScriptsCookieDao.updateAll(cookieEntity)
        loadToMemory()
    }

    override fun getCookieEntity(): CookieEntity {
        return cookieEntity
    }

    private fun loadToMemory() {
        cookieEntity =
            contentScopeScriptsCookieDao.get() ?: CookieEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
