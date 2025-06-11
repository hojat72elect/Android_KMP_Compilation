

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.autofill.impl.store.InternalAutofillStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesMultibinding(AppScope::class)
class AutofillUserAdditionalPixelParamPlugin @Inject constructor(
    private val internalAutofillStore: InternalAutofillStore,
) : AdditionalPixelParamPlugin {
    override suspend fun params(): Pair<String, String> = Pair(
        "autofillUser",
        "${getSavedPasswordCount() > 10}",
    )

    private suspend fun getSavedPasswordCount(): Int {
        return internalAutofillStore.getCredentialCount().firstOrNull() ?: 0
    }
}
