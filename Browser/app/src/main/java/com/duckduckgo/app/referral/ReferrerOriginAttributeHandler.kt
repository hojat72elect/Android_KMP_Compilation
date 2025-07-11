

package com.duckduckgo.app.referral

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.verifiedinstallation.installsource.VerificationCheckPlayStoreInstall
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface ReferrerOriginAttributeHandler {
    fun process(referrerParts: List<String>)
}

@ContributesBinding(AppScope::class)
class ReferrerOriginAttributeHandlerImpl @Inject constructor(
    private val appReferrerDataStore: AppReferrerDataStore,
    private val playStoreInstallChecker: VerificationCheckPlayStoreInstall,
) : ReferrerOriginAttributeHandler {

    override fun process(referrerParts: List<String>) {
        runCatching {
            Timber.v("Looking for origin attribute referrer data")
            var originAttributePart = extractOriginAttribute(referrerParts)

            if (originAttributePart == null && playStoreInstallChecker.installedFromPlayStore()) {
                Timber.v("No origin attribute referrer data available; assigning one")
                originAttributePart = DEFAULT_ATTRIBUTION_FOR_PLAY_STORE_INSTALLS
            }

            persistOriginAttribute(originAttributePart)
        }
    }

    private fun extractOriginAttribute(referrerParts: List<String>): String? {
        val originAttributePart = referrerParts.find { it.startsWith("$ORIGIN_ATTRIBUTE_KEY=") }
        if (originAttributePart == null) {
            Timber.v("Did not find referrer origin attribute key")
            return null
        }

        Timber.v("Found referrer origin attribute: %s", originAttributePart)

        return originAttributePart.removePrefix("$ORIGIN_ATTRIBUTE_KEY=").also {
            Timber.i("Found referrer origin attribute value: %s", it)
        }
    }

    private fun persistOriginAttribute(originAttributePart: String?) {
        appReferrerDataStore.utmOriginAttributeCampaign = originAttributePart
    }

    companion object {
        const val ORIGIN_ATTRIBUTE_KEY = "origin"
        const val DEFAULT_ATTRIBUTION_FOR_PLAY_STORE_INSTALLS = "funnel_playstore"
    }
}
