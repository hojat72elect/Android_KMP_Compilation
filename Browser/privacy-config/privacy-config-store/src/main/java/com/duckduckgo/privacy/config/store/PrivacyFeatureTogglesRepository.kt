

package com.duckduckgo.privacy.config.store

interface PrivacyFeatureTogglesRepository : PrivacyFeatureTogglesDataStore

class RealPrivacyFeatureTogglesRepository(
    privacyFeatureTogglesDataStore: PrivacyFeatureTogglesDataStore,
) : PrivacyFeatureTogglesRepository, PrivacyFeatureTogglesDataStore by privacyFeatureTogglesDataStore
