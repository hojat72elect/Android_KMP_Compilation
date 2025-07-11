

package com.duckduckgo.user.agent.store

interface UserAgentFeatureToggleRepository : UserAgentFeatureToggleStore

class RealUserAgentFeatureToggleRepository(
    userAgentFeatureToggleStore: UserAgentFeatureToggleStore,
) : UserAgentFeatureToggleRepository, UserAgentFeatureToggleStore by userAgentFeatureToggleStore
