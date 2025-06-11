

package com.duckduckgo.autofill.impl.importing.gpm.webflow

import com.duckduckgo.autofill.impl.importing.gpm.feature.AutofillImportPasswordConfigStore
import com.duckduckgo.di.scopes.FragmentScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface ImportGooglePasswordUrlToStageMapper {
    suspend fun getStage(url: String?): String
}

@ContributesBinding(FragmentScope::class)
class ImportGooglePasswordUrlToStageMapperImpl @Inject constructor(
    private val importPasswordConfigStore: AutofillImportPasswordConfigStore,
) : ImportGooglePasswordUrlToStageMapper {

    override suspend fun getStage(url: String?): String {
        val config = importPasswordConfigStore.getConfig()
        val stage = config.urlMappings.firstOrNull { url?.startsWith(it.url) == true }?.key ?: UNKNOWN
        return stage.also { Timber.d("Mapped as stage $it for $url") }
    }

    companion object {
        const val UNKNOWN = "webflow-unknown"
    }
}
