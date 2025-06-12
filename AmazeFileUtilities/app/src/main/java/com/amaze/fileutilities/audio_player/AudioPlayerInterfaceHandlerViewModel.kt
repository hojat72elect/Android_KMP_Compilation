package com.amaze.fileutilities.audio_player

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amaze.fileutilities.utilis.Utils
import com.amaze.fileutilities.utilis.getSiblingUriFiles
import com.amaze.fileutilities.utilis.isAudioMimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioPlayerInterfaceHandlerViewModel : ViewModel() {

    //    var uriList: ArrayList<Uri>? = null
    // approx value if player is playing
    var isPlaying: Boolean = false
    var forceShowSeekbar = false

    companion object {
        const val WAVEFORM_THRESHOLD_BYTES = 50000000L
    }

    var siblingsLiveData = MutableLiveData<ArrayList<Uri>?>()

    fun processSiblings(uri: Uri) {
        viewModelScope.launch(Dispatchers.Default) {
            /*withContext(Dispatchers.Main) {
                siblingImagesLiveData.value = null
            }*/
            if (siblingsLiveData.value.isNullOrEmpty()) {
                val uriList = ArrayList<Uri>()
                uri.getSiblingUriFiles { it.isAudioMimeType() }.run {
                    if (this != null) {
                        uriList.addAll(
                            this.asReversed()
                        )
                    } else {
                        uriList.add(uri)
                    }
                }
                siblingsLiveData.postValue(uriList)
            }
        }
    }

    fun getPaletteColor(drawable: Drawable, fallbackColor: Int): LiveData<Int?> {
        return liveData(context = viewModelScope.coroutineContext + Dispatchers.Default) {
            emit(null)
            val bitmap = drawable.toBitmap()
            val color = Utils.getColorDark(
                Utils.generatePalette(bitmap),
                fallbackColor
            )
            emit(color)
        }
    }
}
