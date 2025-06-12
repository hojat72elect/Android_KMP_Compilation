package com.amaze.fileutilities.home_page.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.amaze.fileutilities.R
import com.amaze.fileutilities.utilis.PreferencesConstants
import com.amaze.fileutilities.utilis.Utils
import com.amaze.fileutilities.utilis.getAppCommonSharedPreferences
import com.amaze.trashbin.TrashBinConfig

class TrashBinPrefFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private lateinit var sharedPrefs: SharedPreferences

    companion object {
        private const val KEY_RETENTION_DAYS = "retention_days"
        private const val KEY_RETENTION_BYTES = "retention_bytes"
        private const val KEY_RETENTION_NUM_OF_FILES = "retention_num_of_files"
        private const val CLEANUP_INTERVAL = "cleanup_interval"
        private val KEYS = listOf(
            KEY_RETENTION_BYTES, KEY_RETENTION_DAYS,
            KEY_RETENTION_NUM_OF_FILES, CLEANUP_INTERVAL
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.trash_bin_prefs)
        sharedPrefs = requireContext().getAppCommonSharedPreferences()
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
        val prefs = requireContext().getAppCommonSharedPreferences()
        when (preference.key) {
            KEY_RETENTION_DAYS -> {
                val days = prefs.getInt(
                    PreferencesConstants.KEY_TRASH_BIN_RETENTION_DAYS,
                    TrashBinConfig.RETENTION_DAYS_INFINITE
                )
                Utils.buildDigitInputDialog(
                    requireContext(), getString(R.string.retention_days_title),
                    getString(R.string.retention_days_summary), days.toLong(), {
                        prefs.edit().putInt(
                            PreferencesConstants.KEY_TRASH_BIN_RETENTION_DAYS,
                            it?.toInt() ?: TrashBinConfig.RETENTION_DAYS_INFINITE
                        ).apply()
                    },
                    lowerBound = 1
                ) {
                    prefs.edit().putInt(
                        PreferencesConstants.KEY_TRASH_BIN_RETENTION_DAYS,
                        TrashBinConfig.RETENTION_DAYS_INFINITE
                    ).apply()
                }
            }

            KEY_RETENTION_BYTES -> {
                var bytes = prefs.getLong(
                    PreferencesConstants.KEY_TRASH_BIN_RETENTION_BYTES,
                    TrashBinConfig.RETENTION_BYTES_INFINITE
                )
                if (bytes != TrashBinConfig.RETENTION_BYTES_INFINITE) {
                    bytes = bytes.div(1024).div(1024)
                }
                Utils.buildDigitInputDialog(
                    requireContext(), getString(R.string.retention_bytes_title),
                    getString(R.string.retention_bytes_summary), bytes,
                    {
                        val bytesInput = it?.times(1024)?.times(1024)
                        prefs.edit().putLong(
                            PreferencesConstants.KEY_TRASH_BIN_RETENTION_BYTES,
                            bytesInput ?: TrashBinConfig.RETENTION_BYTES_INFINITE
                        ).apply()
                    },
                    lowerBound = 1
                ) {
                    prefs.edit().putLong(
                        PreferencesConstants.KEY_TRASH_BIN_RETENTION_BYTES,
                        TrashBinConfig.RETENTION_BYTES_INFINITE
                    ).apply()
                }
            }

            KEY_RETENTION_NUM_OF_FILES -> {
                val days = prefs.getInt(
                    PreferencesConstants.KEY_TRASH_BIN_RETENTION_NUM_OF_FILES,
                    TrashBinConfig.RETENTION_NUM_OF_FILES
                )
                Utils.buildDigitInputDialog(
                    requireContext(), getString(R.string.retention_num_of_files_title),
                    getString(R.string.retention_num_of_files_summary), days.toLong(),
                    {
                        prefs.edit().putInt(
                            PreferencesConstants.KEY_TRASH_BIN_RETENTION_NUM_OF_FILES,
                            it?.toInt() ?: TrashBinConfig.RETENTION_NUM_OF_FILES
                        ).apply()
                    },
                    lowerBound = 1
                ) {
                    prefs.edit().putInt(
                        PreferencesConstants.KEY_TRASH_BIN_RETENTION_NUM_OF_FILES,
                        TrashBinConfig.RETENTION_NUM_OF_FILES
                    ).apply()
                }
            }

            CLEANUP_INTERVAL -> {
                val days = prefs.getInt(
                    PreferencesConstants.KEY_TRASH_BIN_CLEANUP_INTERVAL_HOURS,
                    TrashBinConfig.INTERVAL_CLEANUP_HOURS
                )
                Utils.buildDigitInputDialog(
                    requireContext(), getString(R.string.cleanup_interval_title),
                    getString(R.string.cleanup_interval_summary), days.toLong(),
                    {
                        prefs.edit().putInt(
                            PreferencesConstants.KEY_TRASH_BIN_CLEANUP_INTERVAL_HOURS,
                            it?.toInt() ?: TrashBinConfig.INTERVAL_CLEANUP_HOURS
                        ).apply()
                    },
                    lowerBound = 1
                ) {
                    prefs.edit().putInt(
                        PreferencesConstants.KEY_TRASH_BIN_CLEANUP_INTERVAL_HOURS,
                        TrashBinConfig.INTERVAL_CLEANUP_HOURS
                    ).apply()
                }
            }
        }
        return true
    }
}
