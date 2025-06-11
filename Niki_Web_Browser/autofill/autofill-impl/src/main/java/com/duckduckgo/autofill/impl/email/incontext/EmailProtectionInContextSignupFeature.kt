

package com.duckduckgo.autofill.impl.email.incontext

import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

/**
 * This is the class that represents the feature flag  for Email Protection in-context signup flow
 */
interface EmailProtectionInContextSignupFeature {
    /**
     * @return `true` when the remote config has the global "incontextSignup" feature flag enabled
     * If the remote feature is not present defaults to `true`
     */

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}
