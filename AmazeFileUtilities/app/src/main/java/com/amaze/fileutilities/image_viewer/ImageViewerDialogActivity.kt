package com.amaze.fileutilities.image_viewer

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.amaze.fileutilities.PermissionsActivity
import com.amaze.fileutilities.R
import com.amaze.fileutilities.databinding.ImageViewerDialogActivityBinding
import com.amaze.fileutilities.utilis.showToastInCenter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImageViewerDialogActivity : PermissionsActivity() {

    var log: Logger = LoggerFactory.getLogger(ImageViewerDialogActivity::class.java)

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ImageViewerDialogActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val mimeType = intent.type
        val imageUri = intent.data
        if (imageUri == null) {
            showToastInCenter(resources.getString(R.string.unsupported_content))
            return
        }
        log.info(
            "Loading image from path ${imageUri.path} " +
                    "and mimetype $mimeType"
        )
        val bundle = bundleOf(
            ImageViewerFragment.VIEW_TYPE_ARGUMENT
                    to LocalImageModel(uri = imageUri, mimeType = mimeType)
        )
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ImageViewerFragment>(R.id.fragment_container_view, args = bundle)
        }
    }
}
