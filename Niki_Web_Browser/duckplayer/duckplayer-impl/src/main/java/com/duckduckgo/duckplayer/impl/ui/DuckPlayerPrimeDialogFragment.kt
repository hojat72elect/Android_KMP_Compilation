

package com.duckduckgo.duckplayer.impl.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.duckduckgo.duckplayer.impl.R
import com.duckduckgo.duckplayer.impl.databinding.ModalDuckPlayerBinding

const val FROM_DUCK_PLAYER_PAGE = "fromDuckPlayerPage"

class DuckPlayerPrimeDialogFragment : DialogFragment() {

    private lateinit var binding: ModalDuckPlayerBinding
    private val isFromDuckPlayerPage: Boolean by lazy { requireArguments().getBoolean(FROM_DUCK_PLAYER_PAGE) }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.duckduckgo.mobile.android.R.style.Widget_DuckDuckGo_DialogFullScreen)
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            WindowCompat.getInsetsController(it, binding.root).apply {
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.statusBars())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dismiss()
    }
    companion object {
        fun newInstance(fromDuckPlayerPage: Boolean): DuckPlayerPrimeDialogFragment =
            DuckPlayerPrimeDialogFragment().also {
                it.arguments = Bundle().apply {
                    putBoolean(FROM_DUCK_PLAYER_PAGE, fromDuckPlayerPage)
                }
            }
    }
}
