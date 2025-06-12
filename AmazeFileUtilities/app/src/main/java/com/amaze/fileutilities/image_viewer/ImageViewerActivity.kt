package com.amaze.fileutilities.image_viewer

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.amaze.fileutilities.PermissionsActivity
import com.amaze.fileutilities.R
import com.amaze.fileutilities.databinding.GenericPagerViewerActivityBinding
import com.amaze.fileutilities.utilis.Utils.Companion.showProcessingDialog
import com.amaze.fileutilities.utilis.showToastInCenter
import java.io.File
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImageViewerActivity : PermissionsActivity() {

    var log: Logger = LoggerFactory.getLogger(ImageViewerActivity::class.java)

    private lateinit var viewModel: ImageViewerViewModel
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        GenericPagerViewerActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                setTheme(R.style.Theme_AmazeFileUtilities_FullScreen_Dark)
            } catch (e: Exception) {
                log.warn("failed to set theme Theme_AmazeFileUtilities_FullScreen_Dark")
                setTheme(R.style.Theme_AmazeFileUtilities_FullScreen_Dark_Fallback)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewModel = ViewModelProvider(this).get(ImageViewerViewModel::class.java)

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

        val imageModel = LocalImageModel(uri = imageUri, mimeType = mimeType)
        viewModel.processSiblingImageModels(imageModel)

        val dialog = showProcessingDialog(
            layoutInflater,
            getString(R.string.please_wait)
        ).create()

        viewModel.siblingImagesLiveData.observe(this) {
            if (it == null) {
                dialog.show()
            } else {
                dialog.dismiss()
                val pagerAdapter = ImageViewerAdapter(
                    supportFragmentManager,
                    lifecycle, it
                )
                viewBinding.pager.adapter = pagerAdapter
                var position = 0
                if (it.size > 1) {
                    for (i in it.indices) {
                        // TODO: avoid using file
                        if (File(it[i].uri.path!!).name.equals(File(imageModel.uri.path!!).name)) {
                            position = i
                            break
                        }
                    }
                }
                viewBinding.pager.setCurrentItem(position, false)
            }
        }
    }

    fun getViewpager(): ViewPager2 {
        return viewBinding.pager
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }*/
}
