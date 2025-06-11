

package com.duckduckgo.sync.impl.promotion

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = SyncPromotionFeature::class,
    featureName = "syncPromotion",
)
/**
 * Feature flags for sync promotions. Sync promotions can appear in various places in the app.
 * Note, that it probably doesn't make sense to show a sync promotion if the sync feature flag is disabled.
 * But the sync feature flag is independent of this, and should be checked separately.
 */
interface SyncPromotionFeature {

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun bookmarks(): Toggle

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun passwords(): Toggle
}
