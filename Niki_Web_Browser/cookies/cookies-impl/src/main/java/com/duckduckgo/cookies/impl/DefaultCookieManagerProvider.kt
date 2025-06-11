

package com.duckduckgo.cookies.impl

import android.webkit.CookieManager
import com.duckduckgo.cookies.api.CookieManagerProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class DefaultCookieManagerProvider @Inject constructor() : CookieManagerProvider {

    @Volatile
    private var instance: CookieManager? = null

    override fun get(): CookieManager? {
        return runCatching {
            instance ?: synchronized(this) {
                instance ?: CookieManager.getInstance().also { instance = it }
            }
        }.getOrNull()
    }
}
