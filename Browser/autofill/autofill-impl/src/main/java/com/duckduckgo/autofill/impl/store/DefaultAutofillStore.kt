

package com.duckduckgo.autofill.impl.store

import com.duckduckgo.autofill.api.store.AutofillStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultAutofillStore @Inject constructor(
    private val autofillStore: InternalAutofillStore,
) : AutofillStore by autofillStore
