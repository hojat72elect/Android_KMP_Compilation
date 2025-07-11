

package com.duckduckgo.app.surrogates.api

import com.duckduckgo.app.surrogates.ResourceSurrogateLoader
import com.duckduckgo.app.surrogates.store.ResourceSurrogateDataStore
import com.duckduckgo.common.utils.extensions.isCached
import io.reactivex.Completable
import java.io.IOException
import javax.inject.Inject
import timber.log.Timber

class ResourceSurrogateListDownloader @Inject constructor(
    private val service: ResourceSurrogateListService,
    private val surrogatesDataStore: ResourceSurrogateDataStore,
    private val resourceSurrogateLoader: ResourceSurrogateLoader,
) {

    fun downloadList(): Completable {
        return Completable.fromAction {
            Timber.d("Downloading Google Analytics Surrogates data")

            val call = service.surrogates()
            val response = call.execute()

            Timber.d("Response received, success=${response.isSuccessful}")

            if (response.isCached && surrogatesDataStore.hasData()) {
                Timber.d("Surrogates data already cached and stored")
                return@fromAction
            }

            if (response.isSuccessful) {
                val bodyBytes = response.body()!!.bytes()
                Timber.d("Updating surrogates data store with new data")
                persistData(bodyBytes)
                resourceSurrogateLoader.loadData()
            } else {
                throw IOException("Status: ${response.code()} - ${response.errorBody()?.string()}")
            }
        }
    }

    private fun persistData(bodyBytes: ByteArray) {
        surrogatesDataStore.saveData(bodyBytes)
    }
}
