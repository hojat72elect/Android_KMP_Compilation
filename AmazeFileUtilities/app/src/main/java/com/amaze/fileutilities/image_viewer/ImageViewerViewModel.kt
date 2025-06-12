package com.amaze.fileutilities.image_viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaze.fileutilities.utilis.getSiblingUriFiles
import com.amaze.fileutilities.utilis.isImageMimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewerViewModel : ViewModel() {

    var siblingImagesLiveData = MutableLiveData<ArrayList<LocalImageModel>?>()

    fun processSiblingImageModels(imageModel: LocalImageModel) {
        viewModelScope.launch(Dispatchers.Default) {

            siblingImagesLiveData.postValue(null)
            if (siblingImagesLiveData.value.isNullOrEmpty()) {
                imageModel.uri.getSiblingUriFiles { it.isImageMimeType() }.run {
                    val imageModelList = ArrayList<LocalImageModel>()
                    if (this != null) {
                        imageModelList.addAll(
                            this.map { LocalImageModel(it, "") }.asReversed()
                        )
                    } else {
                        imageModelList.add(imageModel)
                    }
                    siblingImagesLiveData.postValue(imageModelList)
                }
            }
        }
    }
}
