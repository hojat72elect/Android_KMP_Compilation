

package com.duckduckgo.autofill.impl.email.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.autofill.impl.email.incontext.EmailProtectionInContextSignupFeature
import com.duckduckgo.di.scopes.AppScope

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = EmailProtectionInContextSignupFeature::class,
    featureName = "incontextSignup",
    settingsStore = EmailProtectionInContextRemoteSettingsPersister::class,
)
@Suppress("unused")
private interface UnusedEmailProtectionInContextSignupRemoteFeatureCodegenTrigger
