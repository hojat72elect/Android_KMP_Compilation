package com.amaze.fileutilities.home_page.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.amaze.fileutilities.R

class PreferenceFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    companion object {
        private const val KEY_APPEARANCE = "appearance"
        private const val KEY_ANALYSIS = "analysis"
        private const val KEY_AUDIO_PLAYER = "audio_player"
        private const val KEY_IMAGE_VIEWER = "image_viewer"
        private const val KEY_TRASH_BIN = "trash_bin"
        private val KEYS = listOf(
            KEY_APPEARANCE, KEY_ANALYSIS, KEY_AUDIO_PLAYER,
            KEY_IMAGE_VIEWER, KEY_TRASH_BIN
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
        KEYS.forEach {
            findPreference<Preference>(it)?.onPreferenceClickListener = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.background = null
        return view
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            KEY_APPEARANCE -> {
                (activity as PreferenceActivity).inflatePreferenceFragment(
                    AppearancePrefFragment(),
                    R.string.appearance
                )
            }

            KEY_ANALYSIS -> {
                (activity as PreferenceActivity).inflatePreferenceFragment(
                    AnalysisPrefFragment(),
                    R.string.analysis
                )
            }

            KEY_AUDIO_PLAYER -> {
                (activity as PreferenceActivity)
                    .inflatePreferenceFragment(AudioPlayerPrefFragment(), R.string.audio_player)
            }

            KEY_IMAGE_VIEWER -> {
                (activity as PreferenceActivity)
                    .inflatePreferenceFragment(
                        ImageViewerPrefFragment(),
                        R.string.image_viewer_normal
                    )
            }

            KEY_TRASH_BIN -> {
                (activity as PreferenceActivity)
                    .inflatePreferenceFragment(
                        TrashBinPrefFragment(),
                        R.string.trash_bin
                    )
            }
        }
        return true
    }
}
