

package com.duckduckgo.app.referral

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.browser.api.referrer.AppReferrer
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AppReferrerDataStore {
    var referrerCheckedPreviously: Boolean
    var campaignSuffix: String?
    var installedFromEuAuction: Boolean
    var utmOriginAttributeCampaign: String?
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = AppReferrerDataStore::class,
)
@ContributesBinding(
    scope = AppScope::class,
    boundType = AppReferrer::class,
)
@SingleInstanceIn(AppScope::class)
class AppReferenceSharePreferences @Inject constructor(
    private val context: Context,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : AppReferrerDataStore, AppReferrer {

    override fun setOriginAttributeCampaign(origin: String?) {
        coroutineScope.launch(dispatcherProvider.io()) {
            utmOriginAttributeCampaign = origin
        }
    }

    override var campaignSuffix: String?
        get() = preferences.getString(KEY_CAMPAIGN_SUFFIX, null)
        set(value) = preferences.edit(true) { putString(KEY_CAMPAIGN_SUFFIX, value) }

    override var utmOriginAttributeCampaign: String?
        get() = preferences.getString(KEY_ORIGIN_ATTRIBUTE_CAMPAIGN, null)
        set(value) = preferences.edit(true) { putString(KEY_ORIGIN_ATTRIBUTE_CAMPAIGN, value) }

    override var referrerCheckedPreviously: Boolean
        get() = preferences.getBoolean(KEY_CHECKED_PREVIOUSLY, false)
        set(value) = preferences.edit(true) { putBoolean(KEY_CHECKED_PREVIOUSLY, value) }

    override var installedFromEuAuction: Boolean
        get() = preferences.getBoolean(KEY_INSTALLED_FROM_EU_AUCTION, false)
        set(value) = preferences.edit(true) { putBoolean(KEY_INSTALLED_FROM_EU_AUCTION, value) }

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    companion object {
        const val FILENAME = "com.duckduckgo.app.referral"
        private const val KEY_CAMPAIGN_SUFFIX = "KEY_CAMPAIGN_SUFFIX"
        private const val KEY_ORIGIN_ATTRIBUTE_CAMPAIGN = "KEY_ORIGIN_ATTRIBUTE_CAMPAIGN"
        private const val KEY_CHECKED_PREVIOUSLY = "KEY_CHECKED_PREVIOUSLY"
        private const val KEY_INSTALLED_FROM_EU_AUCTION = "KEY_INSTALLED_FROM_EU_AUCTION"
    }
}
