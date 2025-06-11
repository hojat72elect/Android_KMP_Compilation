

package com.duckduckgo.duckplayer.impl

import android.content.res.AssetManager
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface DuckPlayerLocalFilesPath {
    fun assetsPath(): List<String>
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealDuckPlayerLocalFilesPath @Inject constructor(
    private val assetManager: AssetManager,
    dispatcherProvider: DispatcherProvider,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    @IsMainProcess private val isMainProcess: Boolean,
) : DuckPlayerLocalFilesPath {

    private var assetsPaths: List<String> = listOf()

    init {
        if (isMainProcess) {
            appCoroutineScope.launch(dispatcherProvider.io()) {
                assetsPaths = getAllAssetFilePaths("duckplayer")
            }
        }
    }

    override fun assetsPath(): List<String> = assetsPaths

    private fun getAllAssetFilePaths(directory: String): List<String> {
        val filePaths = mutableListOf<String>()
        val files = runCatching { assetManager.list(directory) }.getOrNull() ?: return emptyList()

        files.forEach {
            val fullPath = "$directory/$it"
            if (runCatching { assetManager.list(fullPath)?.isNotEmpty() }.getOrDefault(false) == true) {
                filePaths.addAll(getAllAssetFilePaths(fullPath))
            } else {
                filePaths.add(fullPath.removePrefix("duckplayer/"))
            }
        }
        return filePaths
    }
}
