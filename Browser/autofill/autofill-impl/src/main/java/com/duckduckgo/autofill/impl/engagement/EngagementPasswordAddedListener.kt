

package com.duckduckgo.autofill.impl.engagement

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.statistics.pixels.Pixel.PixelType.Unique
import com.duckduckgo.autofill.impl.PasswordStoreEventListener
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames
import com.duckduckgo.browser.api.UserBrowserProperties
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
class EngagementPasswordAddedListener @Inject constructor(
    private val userBrowserProperties: UserBrowserProperties,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
    private val pixel: Pixel,
) : PasswordStoreEventListener {

    // keep in-memory state to avoid sending the pixel multiple times if called repeatedly in a short time
    private var credentialAdded = false

    @Synchronized
    override fun onCredentialAdded(id: Long) {
        if (credentialAdded) return

        credentialAdded = true

        appCoroutineScope.launch(dispatchers.io()) {
            val daysInstalled = userBrowserProperties.daysSinceInstalled()
            Timber.v("onCredentialAdded. daysInstalled: $daysInstalled")
            if (daysInstalled < 7) {
                pixel.fire(AutofillPixelNames.AUTOFILL_ENGAGEMENT_ONBOARDED_USER, type = Unique())
            }
        }
    }
}
