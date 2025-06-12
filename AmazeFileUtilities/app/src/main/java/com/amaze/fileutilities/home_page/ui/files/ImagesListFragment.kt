package com.amaze.fileutilities.home_page.ui.files

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Consumer
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.databinding.FragmentImagesListBinding
import com.amaze.fileutilities.home_page.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import me.zhanghai.android.fastscroll.PopupStyles

class ImagesListFragment : AbstractMediaInfoListFragment() {
    private val filesViewModel: FilesViewModel by activityViewModels()
    private var _binding: FragmentImagesListBinding? = null
    private lateinit var fileStorageSummaryAndMediaFileInfo:
            Pair<FilesViewModel.StorageSummary, List<MediaFileInfo>?>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var preloader: MediaAdapterPreloader<MediaFileInfo>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesListBinding.inflate(
            inflater, container,
            false
        )
        val root: View = binding.root
        (requireActivity() as MainActivity).setCustomTitle(resources.getString(R.string.images))
        (activity as MainActivity).invalidateBottomBar(false)
        setupAdapter()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity)
            .setCustomTitle(resources.getString(R.string.title_utilities))
        (activity as MainActivity).invalidateBottomBar(true)
        _binding = null
    }

    override fun getFileStorageSummaryAndMediaFileInfoPair(): Pair<FilesViewModel.StorageSummary,
            List<MediaFileInfo>?>? {
        return if (::fileStorageSummaryAndMediaFileInfo.isInitialized)
            fileStorageSummaryAndMediaFileInfo else null
    }

    override fun getMediaAdapterPreloader(isGrid: Boolean): MediaAdapterPreloader<MediaFileInfo> {
        if (preloader == null) {
            preloader = MediaAdapterPreloader(
                requireContext(),
                R.drawable.ic_outline_image_32,
                isGrid
            )
        }
        return preloader!!
    }

    override fun getRecyclerView(): RecyclerView {
        return binding.imagesListView
    }

    override fun getMediaListType(): Int {
        return MediaFileAdapter.MEDIA_TYPE_IMAGES
    }

    override fun getAllOptionsFAB(): List<FloatingActionButton> {
        val fabList = mutableListOf(
            binding.optionsButtonFab, binding.deleteButtonFab,
            binding.shareButtonFab, binding.locateFileButtonFab
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fabList.add(binding.compressButtonFab)
        }
        return fabList
    }

    override fun showOptionsCallback() {
        getPlayNextButton()?.visibility = View.GONE
    }

    override fun hideOptionsCallback() {
        // do nothing
    }

    override fun getItemPressedCallback(mediaFileInfo: MediaFileInfo) {
        // do nothing
    }

    override fun setupAdapter() {
        filesViewModel.usedImagesSummaryTransformations().observe(
            viewLifecycleOwner
        ) { metaInfoAndSummaryPair ->
            binding.imagesListInfoText.text = resources.getString(R.string.loading)
            metaInfoAndSummaryPair?.let {
                val metaInfoList = metaInfoAndSummaryPair.second
                metaInfoList.run {
                    if (this.isEmpty()) {
                        binding.imagesListInfoText.text =
                            resources.getString(R.string.no_files)
                        binding.loadingProgress.visibility = View.GONE
                    } else {
                        binding.imagesListInfoText.visibility = View.GONE
                        binding.loadingProgress.visibility = View.GONE
                    }
                    fileStorageSummaryAndMediaFileInfo = it
                    resetAdapter()

                    binding.fastscroll.visibility = View.GONE
                    val popupStyle = Consumer<TextView> { popupView ->
                        PopupStyles.MD2.accept(popupView)
                        popupView.setTextColor(Color.BLACK)
                        popupView.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            resources.getDimension(R.dimen.twenty_four_sp)
                        )
                    }
                    FastScrollerBuilder(binding.imagesListView).useMd2Style()
                        .setPopupStyle(popupStyle).build()

                }
            }
        }
    }

    override fun adapterItemSelected(checkedCount: Int) {
        // do nothing
    }
}
