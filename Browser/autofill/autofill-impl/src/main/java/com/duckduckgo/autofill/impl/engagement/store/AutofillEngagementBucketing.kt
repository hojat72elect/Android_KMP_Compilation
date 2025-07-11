

package com.duckduckgo.autofill.impl.engagement.store

import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementBucketing.Companion.FEW
import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementBucketing.Companion.LOTS
import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementBucketing.Companion.MANY
import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementBucketing.Companion.NONE
import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementBucketing.Companion.SOME
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutofillEngagementBucketing {
    fun bucketNumberOfCredentials(numberOfCredentials: Int): String

    companion object {
        const val NONE = "none"
        const val FEW = "few"
        const val SOME = "some"
        const val MANY = "many"
        const val LOTS = "lots"
    }
}

@ContributesBinding(AppScope::class)
class DefaultAutofillEngagementBucketing @Inject constructor() : AutofillEngagementBucketing {

    override fun bucketNumberOfCredentials(numberOfCredentials: Int): String {
        return when {
            numberOfCredentials == 0 -> NONE
            numberOfCredentials < 4 -> FEW
            numberOfCredentials < 11 -> SOME
            numberOfCredentials < 50 -> MANY
            else -> LOTS
        }
    }
}
