package ca.hojat.gamehub.common_ui.images

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ImageLoaderInitializer {
    fun init()
}

@BindType
class CoilInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageLoaderInitializer {

    override fun init() {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(MEMORY_CACHE_MAX_HEAP_PERCENTAGE)
                        .build()
                }
                .build()
        )
    }

    private companion object {
        const val MEMORY_CACHE_MAX_HEAP_PERCENTAGE = 0.5
    }
}
