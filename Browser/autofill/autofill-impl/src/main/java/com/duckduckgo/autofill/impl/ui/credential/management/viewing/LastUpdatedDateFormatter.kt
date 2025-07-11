

package com.duckduckgo.autofill.impl.ui.credential.management.viewing

import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface LastUpdatedDateFormatter {
    fun format(timeInMillis: Long): String
}

@ContributesBinding(FragmentScope::class)
class RealLastUpdatedDateFormatter @Inject constructor() : LastUpdatedDateFormatter {

    override fun format(timeInMillis: Long): String = formatter.format(Date(timeInMillis))

    companion object {
        private val formatter by lazy {
            SimpleDateFormat("MMM dd, yyyy HH:mm aaa", Locale.getDefault())
        }
    }
}
