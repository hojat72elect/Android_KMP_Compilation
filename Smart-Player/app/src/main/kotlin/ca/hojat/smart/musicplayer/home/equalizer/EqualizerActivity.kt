package ca.hojat.smart.musicplayer.home.equalizer

import android.annotation.SuppressLint
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ca.hojat.smart.musicplayer.shared.ui.dialogs.RadioGroupDialog
import ca.hojat.smart.musicplayer.shared.helpers.DARK_GREY
import ca.hojat.smart.musicplayer.shared.helpers.NavigationIcon
import ca.hojat.smart.musicplayer.shared.data.models.RadioItem
import ca.hojat.smart.musicplayer.shared.ui.views.MySeekBar
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.ActivityEqualizerBinding
import ca.hojat.smart.musicplayer.databinding.EqualizerBandBinding
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getContrastColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperPrimaryColor
import ca.hojat.smart.musicplayer.shared.extensions.getProperTextColor
import ca.hojat.smart.musicplayer.shared.extensions.isWhiteTheme
import ca.hojat.smart.musicplayer.shared.extensions.updateTextColors
import ca.hojat.smart.musicplayer.shared.extensions.viewBinding
import ca.hojat.smart.musicplayer.shared.helpers.EQUALIZER_PRESET_CUSTOM
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.playback.SimpleEqualizer
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

class EqualizerActivity : BaseSimpleActivity() {
    private var bands = HashMap<Short, Int>()
    private var bandSeekBars = ArrayList<MySeekBar>()

    private val binding by viewBinding(ActivityEqualizerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateMaterialActivityViews(
            binding.equalizerCoordinator,
            binding.equalizerHolder,
            useTransparentNavigation = true,
            useTopSearchMenu = false
        )
        setupMaterialScrollListener(binding.equalizerNestedScrollview, binding.equalizerToolbar)
        initMediaPlayer()
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.equalizerToolbar, NavigationIcon.Arrow)
    }

    @OptIn(UnstableApi::class)
    @SuppressLint("SetTextI18n")
    private fun initMediaPlayer() {
        val equalizer = SimpleEqualizer.instance
        try {
            if (!equalizer.enabled) {
                equalizer.enabled = true
            }
        } catch (ignored: IllegalStateException) {
        }

        setupBands(equalizer)
        setupPresets(equalizer)
        updateTextColors(binding.equalizerHolder)

        val presetTextColor = if (isWhiteTheme()) {
            DARK_GREY
        } else {
            getProperPrimaryColor().getContrastColor()
        }
        binding.equalizerPreset.setTextColor(presetTextColor)
    }

    @SuppressLint("SetTextI18n")
    private fun setupBands(equalizer: Equalizer) {
        val minValue = equalizer.bandLevelRange[0]
        val maxValue = equalizer.bandLevelRange[1]
        binding.equalizerLabelRight.text = "+${maxValue / 100}"
        binding.equalizerLabelLeft.text = "${minValue / 100}"
        binding.equalizerLabel0.text = (minValue + maxValue).toString()

        bandSeekBars.clear()
        binding.equalizerBandsHolder.removeAllViews()

        val bandType = object : TypeToken<HashMap<Short, Int>>() {}.type
        bands = Gson().fromJson<HashMap<Short, Int>>(config.equalizerBands, bandType) ?: HashMap()

        for (band in 0 until equalizer.numberOfBands) {
            val frequency = equalizer.getCenterFreq(band.toShort()) / 1000.0
            val formatted = formatFrequency(frequency)

            EqualizerBandBinding.inflate(layoutInflater, binding.equalizerBandsHolder, false)
                .apply {
                    binding.equalizerBandsHolder.addView(root)
                    bandSeekBars.add(equalizerBandSeekBar)
                    equalizerBandLabel.text = formatted
                    equalizerBandLabel.setTextColor(getProperTextColor())
                    equalizerBandSeekBar.max = maxValue - minValue

                    equalizerBandSeekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            if (fromUser) {
                                val newProgress = (progress / 100.0).roundToInt() * 100
                                equalizerBandSeekBar.progress = newProgress

                                val newValue = newProgress + minValue
                                try {
                                    if (equalizer.getBandLevel(band.toShort()) != newValue.toShort()) {
                                        equalizer.setBandLevel(band.toShort(), newValue.toShort())
                                        bands[band.toShort()] = newValue
                                    }
                                } catch (ignored: Exception) {
                                }
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {
                            draggingStarted(equalizer)
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            bands[band.toShort()] = equalizerBandSeekBar.progress
                            config.equalizerBands = Gson().toJson(bands)
                        }
                    })
                }
        }
    }

    private fun draggingStarted(equalizer: Equalizer) {
        binding.equalizerPreset.text = getString(R.string.custom)
        config.equalizerPreset = EQUALIZER_PRESET_CUSTOM
        for (band in 0 until equalizer.numberOfBands) {
            bands[band.toShort()] = bandSeekBars[band].progress
        }
    }

    private fun setupPresets(equalizer: Equalizer) {
        try {
            presetChanged(config.equalizerPreset, equalizer)
        } catch (e: Exception) {
            ShowToastUseCase(this, "The error : $e")
            config.equalizerPreset = EQUALIZER_PRESET_CUSTOM
        }

        binding.equalizerPreset.setOnClickListener {
            val items = arrayListOf<RadioItem>()
            (0 until equalizer.numberOfPresets).mapTo(items) {
                RadioItem(it, equalizer.getPresetName(it.toShort()))
            }

            items.add(RadioItem(EQUALIZER_PRESET_CUSTOM, getString(R.string.custom)))
            RadioGroupDialog(this, items, config.equalizerPreset) { presetId ->
                try {
                    config.equalizerPreset = presetId as Int
                    presetChanged(presetId, equalizer)
                } catch (e: Exception) {
                    ShowToastUseCase(this, "The error : $e")
                    config.equalizerPreset = EQUALIZER_PRESET_CUSTOM
                }
            }
        }
    }

    private fun presetChanged(presetId: Int, equalizer: Equalizer) {
        if (presetId == EQUALIZER_PRESET_CUSTOM) {
            binding.equalizerPreset.text = getString(R.string.custom)

            for (band in 0 until equalizer.numberOfBands) {
                val minValue = equalizer.bandLevelRange[0]
                val progress = if (bands.containsKey(band.toShort())) {
                    bands[band.toShort()]
                } else {
                    val maxValue = equalizer.bandLevelRange[1]
                    (maxValue - minValue) / 2
                }

                bandSeekBars[band].progress = progress!!.toInt()
                val newValue = progress + minValue
                equalizer.setBandLevel(band.toShort(), newValue.toShort())
            }
        } else {
            val presetName = equalizer.getPresetName(presetId.toShort())
            if (presetName.isEmpty()) {
                config.equalizerPreset = EQUALIZER_PRESET_CUSTOM
                binding.equalizerPreset.text = getString(R.string.custom)
            } else {
                binding.equalizerPreset.text = presetName
            }

            equalizer.usePreset(presetId.toShort())

            val lowestBandLevel = equalizer.bandLevelRange?.get(0)
            for (band in 0 until equalizer.numberOfBands) {
                val level = equalizer.getBandLevel(band.toShort()).minus(lowestBandLevel!!)
                bandSeekBars[band].progress = level
            }
        }
    }

    // copy-pasted from the file size formatter, should be simplified
    private fun formatFrequency(value: Double): String {
        if (value <= 0) {
            return "0 Hz"
        }

        val units = arrayOf("Hz", "kHz", "gHz")
        val digitGroups = (log10(value) / log10(1000.0)).toInt()
        return "${DecimalFormat("#,##0.#").format(value / 1000.0.pow(digitGroups.toDouble()))} ${units[digitGroups]}"
    }
}
