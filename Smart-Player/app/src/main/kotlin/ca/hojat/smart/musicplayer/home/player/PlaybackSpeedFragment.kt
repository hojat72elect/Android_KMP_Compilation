package ca.hojat.smart.musicplayer.home.player

import android.annotation.SuppressLint
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.FragmentPlaybackSpeedBinding
import ca.hojat.smart.musicplayer.shared.extensions.applyColorFilter
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getProperBackgroundColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperTextColor
import ca.hojat.smart.musicplayer.shared.extensions.onSeekBarChangeListener
import ca.hojat.smart.musicplayer.shared.extensions.updateTextColors
import ca.hojat.smart.musicplayer.shared.helpers.Config
import ca.hojat.smart.musicplayer.shared.data.PlaybackSpeedListener
import ca.hojat.smart.musicplayer.shared.ui.views.MySeekBar
import ca.hojat.smart.musicplayer.shared.ui.views.MyTextView
import kotlin.math.max
import kotlin.math.min

class PlaybackSpeedFragment : BottomSheetDialogFragment() {


    private var seekBar: MySeekBar? = null
    private var listener: PlaybackSpeedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = requireContext().config
        val binding = FragmentPlaybackSpeedBinding.inflate(inflater, container, false)
        val background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.bottom_sheet_bg,
            requireContext().theme
        )
        (background as LayerDrawable).findDrawableByLayerId(R.id.bottom_sheet_background)
            .applyColorFilter(requireContext().getProperBackgroundColor())

        binding.apply {
            seekBar = playbackSpeedSeekbar
            root.setBackgroundDrawable(background)
            requireContext().updateTextColors(playbackSpeedHolder)
            playbackSpeedSlow.applyColorFilter(requireContext().getProperTextColor())
            playbackSpeedFast.applyColorFilter(requireContext().getProperTextColor())
            playbackSpeedSlow.setOnClickListener { reduceSpeed() }
            playbackSpeedFast.setOnClickListener { increaseSpeed() }
            initSeekbar(playbackSpeedSeekbar, playbackSpeedLabel, config)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initSeekbar(seekbar: MySeekBar, speedLabel: MyTextView, config: Config) {
        val formattedValue = formatPlaybackSpeed(config.playbackSpeed)
        speedLabel.text = "${formattedValue}x"
        seekbar.max = MAX_PROGRESS

        val playbackSpeedProgress = config.playbackSpeedProgress
        if (playbackSpeedProgress == -1) {
            config.playbackSpeedProgress = HALF_PROGRESS
        }
        seekbar.progress = config.playbackSpeedProgress

        var lastUpdatedProgress = config.playbackSpeedProgress
        var lastUpdatedFormattedValue = formattedValue

        seekbar.onSeekBarChangeListener { progress ->
            val playbackSpeed = getPlaybackSpeed(progress)
            if (playbackSpeed.toString() != lastUpdatedFormattedValue) {
                lastUpdatedProgress = progress
                lastUpdatedFormattedValue = playbackSpeed.toString()
                config.playbackSpeed = playbackSpeed
                config.playbackSpeedProgress = progress

                speedLabel.text = "${formatPlaybackSpeed(playbackSpeed)}x"
                listener?.updatePlaybackSpeed(playbackSpeed)
            } else {
                seekbar.progress = lastUpdatedProgress
            }
        }
    }

    private fun getPlaybackSpeed(progress: Int): Float {
        var playbackSpeed = when {
            progress < HALF_PROGRESS -> {
                val lowerProgressPercent = progress / HALF_PROGRESS.toFloat()
                val lowerProgress =
                    (1 - MIN_PLAYBACK_SPEED) * lowerProgressPercent + MIN_PLAYBACK_SPEED
                lowerProgress
            }

            progress > HALF_PROGRESS -> {
                val upperProgressPercent = progress / HALF_PROGRESS.toFloat() - 1
                val upperDiff = MAX_PLAYBACK_SPEED - 1
                upperDiff * upperProgressPercent + 1
            }

            else -> 1f
        }
        playbackSpeed = min(max(playbackSpeed, MIN_PLAYBACK_SPEED), MAX_PLAYBACK_SPEED)
        val stepMultiplier = 1 / STEP
        return Math.round(playbackSpeed * stepMultiplier) / stepMultiplier
    }

    private fun reduceSpeed() {
        var currentProgress = seekBar?.progress ?: return
        val currentSpeed = requireContext().config.playbackSpeed
        while (currentProgress > 0) {
            val newSpeed = getPlaybackSpeed(--currentProgress)
            if (newSpeed != currentSpeed) {
                seekBar!!.progress = currentProgress
                break
            }
        }
    }

    private fun increaseSpeed() {
        var currentProgress = seekBar?.progress ?: return
        val currentSpeed = requireContext().config.playbackSpeed
        while (currentProgress < MAX_PROGRESS) {
            val newSpeed = getPlaybackSpeed(++currentProgress)
            if (newSpeed != currentSpeed) {
                seekBar!!.progress = currentProgress
                break
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatPlaybackSpeed(value: Float) = String.format("%.2f", value)

    fun setListener(playbackSpeedListener: PlaybackSpeedListener) {
        listener = playbackSpeedListener
    }

    companion object {
        private const val MIN_PLAYBACK_SPEED = 0.25f
        private const val MAX_PLAYBACK_SPEED = 3f
        private const val MAX_PROGRESS =
            (MAX_PLAYBACK_SPEED * 100 + MIN_PLAYBACK_SPEED * 100).toInt()
        private const val HALF_PROGRESS = MAX_PROGRESS / 2
        private const val STEP = 0.05f
    }
}
