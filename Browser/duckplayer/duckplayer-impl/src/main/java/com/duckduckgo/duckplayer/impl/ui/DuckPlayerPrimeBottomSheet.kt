

package com.duckduckgo.duckplayer.impl.ui

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.duckduckgo.duckplayer.impl.R
import com.duckduckgo.duckplayer.impl.databinding.ModalDuckPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DuckPlayerPrimeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: ModalDuckPlayerBinding
    private val isFromDuckPlayerPage: Boolean by lazy { requireArguments().getBoolean(FROM_DUCK_PLAYER_PAGE) }

    override fun getTheme(): Int = R.style.DuckPlayerBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ModalDuckPlayerBinding.inflate(inflater, container, false)
        LottieCompositionFactory.fromRawRes(context, R.raw.duckplayer)
        binding.duckPlayerAnimation.setAnimation(R.raw.duckplayer)
        binding.duckPlayerAnimation.playAnimation()
        binding.duckPlayerAnimation.repeatCount = LottieDrawable.INFINITE
        binding.title.text =
            if (isFromDuckPlayerPage) {
                getString(R.string.duck_player_info_modal_title_from_duck_player_page)
            } else {
                getString(R.string.duck_player_info_modal_title_from_overlay)
            }
        binding.dismissButton.setOnClickListener {
            dismiss()
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dismiss()
    }

    companion object {
        fun newInstance(fromDuckPlayerPage: Boolean): DuckPlayerPrimeBottomSheet =
            DuckPlayerPrimeBottomSheet().also {
                it.arguments = Bundle().apply {
                    putBoolean(FROM_DUCK_PLAYER_PAGE, fromDuckPlayerPage)
                }
            }
    }
}
