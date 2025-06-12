package com.amaze.fileutilities.home_page.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.amaze.fileutilities.R
import com.amaze.fileutilities.utilis.PreferencesConstants
import com.amaze.fileutilities.utilis.Utils
import com.amaze.fileutilities.utilis.getAppCommonSharedPreferences

class AppearancePrefFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    companion object {
        private const val KEY_COLUMNS = "columns"
        private const val KEY_CONFIRM_ON_EXIT = "pref_confirm_before_exit"
        private val KEYS = listOf(
            KEY_COLUMNS
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.appearance_prefs)
        val sharedPrefs = requireContext().getAppCommonSharedPreferences()
        val enableConfirmOnExitChange = Preference.OnPreferenceChangeListener { _, newValue ->
            sharedPrefs.edit().putBoolean(
                PreferencesConstants.KEY_CONFIRM_BEFORE_EXIT, newValue as Boolean
            ).apply()
            true
        }
        val confirmOnExitCheckbox = findPreference<CheckBoxPreference>(KEY_CONFIRM_ON_EXIT)
        confirmOnExitCheckbox?.setDefaultValue(
            sharedPrefs.getBoolean(
                PreferencesConstants.KEY_CONFIRM_BEFORE_EXIT,
                PreferencesConstants.DEFAULT_CONFIRM_BEFORE_EXIT
            )
        )
        confirmOnExitCheckbox?.onPreferenceChangeListener = enableConfirmOnExitChange
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
            KEY_COLUMNS -> {
                val columnsIdx = prefs.getInt(
                    PreferencesConstants.KEY_GRID_VIEW_COLUMN_COUNT,
                    PreferencesConstants.DEFAULT_GRID_VIEW_COLUMN_COUNT
                )
                val dialog = Utils.buildGridColumnsDialog(requireContext(), columnsIdx - 2) {
                    prefs.edit()
                        .putInt(PreferencesConstants.KEY_GRID_VIEW_COLUMN_COUNT, it).apply()
                }
                dialog.show()
            }
        }
        return true
    }
}
