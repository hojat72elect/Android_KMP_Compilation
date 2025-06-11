

package com.duckduckgo.sync.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.sync.store.SyncStore
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SyncAttributeMatcherPlugin @Inject constructor(
    private val syncStore: SyncStore,
) : AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is SyncEnabledMatchingAttribute -> {
                matchingAttribute.remoteValue == syncStore.isSignedIn()
            }

            else -> return null
        }
    }
}
