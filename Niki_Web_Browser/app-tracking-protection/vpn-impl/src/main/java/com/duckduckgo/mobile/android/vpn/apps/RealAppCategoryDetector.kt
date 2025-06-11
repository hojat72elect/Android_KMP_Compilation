

package com.duckduckgo.mobile.android.vpn.apps

import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.exclusion.AppCategory
import com.duckduckgo.mobile.android.vpn.exclusion.AppCategoryDetector
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealAppCategoryDetector @Inject constructor(context: Context) : AppCategoryDetector {
    private val packageManager = context.packageManager

    override fun getAppCategory(packageName: String): AppCategory {
        return runCatching {
            packageManager.getApplicationInfo(packageName, 0).parseAppCategory()
        }.getOrElse { AppCategory.Undefined }
    }
}
