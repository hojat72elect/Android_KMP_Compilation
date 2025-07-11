package ca.hojat.smart.gallery.shared.helpers

import android.content.Context
import android.text.format.DateFormat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import ca.hojat.smart.gallery.R
import ca.hojat.smart.gallery.shared.extensions.getAppIconColors
import ca.hojat.smart.gallery.shared.extensions.getInternalStoragePath
import ca.hojat.smart.gallery.shared.extensions.getSDCardPath
import ca.hojat.smart.gallery.shared.extensions.getSharedPrefs
import java.text.SimpleDateFormat
import java.util.LinkedList
import java.util.Locale

open class BaseConfig(val context: Context) {
    protected val prefs = context.getSharedPrefs()

    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
    }

    var lastVersion: Int
        get() = prefs.getInt(LAST_VERSION, 0)
        set(lastVersion) = prefs.edit { putInt(LAST_VERSION, lastVersion) }

    var primaryAndroidDataTreeUri: String
        get() = prefs.getString(PRIMARY_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(PRIMARY_ANDROID_DATA_TREE_URI, uri) }

    var sdAndroidDataTreeUri: String
        get() = prefs.getString(SD_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(SD_ANDROID_DATA_TREE_URI, uri) }

    var otgAndroidDataTreeUri: String
        get() = prefs.getString(OTG_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(OTG_ANDROID_DATA_TREE_URI, uri) }

    var primaryAndroidObbTreeUri: String
        get() = prefs.getString(PRIMARY_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(PRIMARY_ANDROID_OBB_TREE_URI, uri) }

    var sdAndroidObbTreeUri: String
        get() = prefs.getString(SD_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(SD_ANDROID_OBB_TREE_URI, uri) }

    var otgAndroidObbTreeUri: String
        get() = prefs.getString(OTG_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(OTG_ANDROID_OBB_TREE_URI, uri) }

    var sdTreeUri: String
        get() = prefs.getString(SD_TREE_URI, "")!!
        set(uri) = prefs.edit { putString(SD_TREE_URI, uri) }

    var otgTreeUri: String
        get() = prefs.getString(OTG_TREE_URI, "")!!
        set(otgTreeUri) = prefs.edit { putString(OTG_TREE_URI, otgTreeUri) }

    var otgPartition: String
        get() = prefs.getString(OTG_PARTITION, "")!!
        set(otgPartition) = prefs.edit { putString(OTG_PARTITION, otgPartition) }

    var otgPath: String
        get() = prefs.getString(OTG_REAL_PATH, "")!!
        set(otgPath) = prefs.edit { putString(OTG_REAL_PATH, otgPath) }

    var sdCardPath: String
        get() = prefs.getString(SD_CARD_PATH, getDefaultSDCardPath())!!
        set(sdCardPath) = prefs.edit { putString(SD_CARD_PATH, sdCardPath) }

    private fun getDefaultSDCardPath() =
        if (prefs.contains(SD_CARD_PATH)) "" else context.getSDCardPath()

    var internalStoragePath: String
        get() = prefs.getString(INTERNAL_STORAGE_PATH, getDefaultInternalPath())!!
        set(internalStoragePath) = prefs.edit {
            putString(INTERNAL_STORAGE_PATH, internalStoragePath)
        }

    private fun getDefaultInternalPath() =
        if (prefs.contains(INTERNAL_STORAGE_PATH)) "" else getInternalStoragePath()

    var textColor: Int
        get() = prefs.getInt(
            TEXT_COLOR,
            ContextCompat.getColor(context, R.color.default_text_color)
        )
        set(textColor) = prefs.edit { putInt(TEXT_COLOR, textColor) }

    var backgroundColor: Int
        get() = prefs.getInt(
            BACKGROUND_COLOR,
            ContextCompat.getColor(context, R.color.default_background_color)
        )
        set(backgroundColor) = prefs.edit { putInt(BACKGROUND_COLOR, backgroundColor) }

    var primaryColor: Int
        get() = prefs.getInt(
            PRIMARY_COLOR,
            ContextCompat.getColor(context, R.color.default_primary_color)
        )
        set(primaryColor) = prefs.edit { putInt(PRIMARY_COLOR, primaryColor) }

    var accentColor: Int
        get() = prefs.getInt(
            ACCENT_COLOR,
            ContextCompat.getColor(context, R.color.default_accent_color)
        )
        set(accentColor) = prefs.edit { putInt(ACCENT_COLOR, accentColor) }

    var appIconColor: Int
        get() = prefs.getInt(
            APP_ICON_COLOR,
            ContextCompat.getColor(context, R.color.default_app_icon_color)
        )
        set(appIconColor) {
            isUsingModifiedAppIcon =
                appIconColor != ContextCompat.getColor(context, R.color.color_primary)
            prefs.edit { putInt(APP_ICON_COLOR, appIconColor) }
        }

    var lastIconColor: Int
        get() = prefs.getInt(
            LAST_ICON_COLOR,
            ContextCompat.getColor(context, R.color.color_primary)
        )
        set(lastIconColor) = prefs.edit { putInt(LAST_ICON_COLOR, lastIconColor) }

    var widgetBackgroundColor: Int
        get() = prefs.getInt(
            WIDGET_BG_COLOR,
            ContextCompat.getColor(context, R.color.default_widget_bg_color)
        )
        set(widgetBgColor) = prefs.edit { putInt(WIDGET_BG_COLOR, widgetBgColor) }

    var widgetTextColor: Int
        get() = prefs.getInt(
            WIDGET_TEXT_COLOR,
            ContextCompat.getColor(context, R.color.default_widget_text_color)
        )
        set(widgetTextColor) = prefs.edit { putInt(WIDGET_TEXT_COLOR, widgetTextColor) }


    var lastCopyPath: String
        get() = prefs.getString(LAST_COPY_PATH, "")!!
        set(lastCopyPath) = prefs.edit { putString(LAST_COPY_PATH, lastCopyPath) }

    var keepLastModified: Boolean
        get() = prefs.getBoolean(KEEP_LAST_MODIFIED, true)
        set(keepLastModified) = prefs.edit {
            putBoolean(KEEP_LAST_MODIFIED, keepLastModified)
        }

    var useEnglish: Boolean
        get() = prefs.getBoolean(USE_ENGLISH, false)
        set(useEnglish) {
            wasUseEnglishToggled = true
            prefs.edit { putBoolean(USE_ENGLISH, useEnglish) }
        }

    var wasUseEnglishToggled: Boolean
        get() = prefs.getBoolean(WAS_USE_ENGLISH_TOGGLED, false)
        set(wasUseEnglishToggled) = prefs.edit {
            putBoolean(WAS_USE_ENGLISH_TOGGLED, wasUseEnglishToggled)
        }

    var wasSharedThemeEverActivated: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, false)
        set(wasSharedThemeEverActivated) = prefs.edit {
            putBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, wasSharedThemeEverActivated)
        }

    var isUsingSharedTheme: Boolean
        get() = prefs.getBoolean(IS_USING_SHARED_THEME, false)
        set(isUsingSharedTheme) = prefs.edit {
            putBoolean(IS_USING_SHARED_THEME, isUsingSharedTheme)
        }

    var isUsingAutoTheme: Boolean
        get() = prefs.getBoolean(IS_USING_AUTO_THEME, false)
        set(isUsingAutoTheme) = prefs.edit {
            putBoolean(IS_USING_AUTO_THEME, isUsingAutoTheme)
        }

    var isUsingSystemTheme: Boolean
        get() = prefs.getBoolean(IS_USING_SYSTEM_THEME, true)
        set(isUsingSystemTheme) = prefs.edit {
            putBoolean(IS_USING_SYSTEM_THEME, isUsingSystemTheme)
        }

    var wasSharedThemeForced: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_FORCED, false)
        set(wasSharedThemeForced) = prefs.edit {
            putBoolean(WAS_SHARED_THEME_FORCED, wasSharedThemeForced)
        }

    var lastConflictApplyToAll: Boolean
        get() = prefs.getBoolean(LAST_CONFLICT_APPLY_TO_ALL, true)
        set(lastConflictApplyToAll) = prefs.edit {
            putBoolean(LAST_CONFLICT_APPLY_TO_ALL, lastConflictApplyToAll)
        }

    var lastConflictResolution: Int
        get() = prefs.getInt(LAST_CONFLICT_RESOLUTION, CONFLICT_SKIP)
        set(lastConflictResolution) = prefs.edit {
            putInt(LAST_CONFLICT_RESOLUTION, lastConflictResolution)
        }

    var sorting: Int
        get() = prefs.getInt(SORT_ORDER, context.resources.getInteger(R.integer.default_sorting))
        set(sorting) = prefs.edit { putInt(SORT_ORDER, sorting) }

    fun saveCustomSorting(path: String, value: Int) {
        if (path.isEmpty()) {
            sorting = value
        } else {
            prefs.edit { putInt(SORT_FOLDER_PREFIX + path.lowercase(Locale.getDefault()), value) }
        }
    }

    fun getFolderSorting(path: String) =
        prefs.getInt(SORT_FOLDER_PREFIX + path.lowercase(Locale.ROOT), sorting)

    fun removeCustomSorting(path: String) {
        prefs.edit { remove(SORT_FOLDER_PREFIX + path.lowercase(Locale.getDefault())) }
    }

    fun hasCustomSorting(path: String) = prefs.contains(SORT_FOLDER_PREFIX + path.lowercase(Locale.getDefault()))

    var skipDeleteConfirmation: Boolean
        get() = prefs.getBoolean(SKIP_DELETE_CONFIRMATION, false)
        set(skipDeleteConfirmation) = prefs.edit {
            putBoolean(SKIP_DELETE_CONFIRMATION, skipDeleteConfirmation)
        }

    var enablePullToRefresh: Boolean
        get() = prefs.getBoolean(ENABLE_PULL_TO_REFRESH, true)
        set(enablePullToRefresh) = prefs.edit {
            putBoolean(ENABLE_PULL_TO_REFRESH, enablePullToRefresh)
        }

    var scrollHorizontally: Boolean
        get() = prefs.getBoolean(SCROLL_HORIZONTALLY, false)
        set(scrollHorizontally) = prefs.edit {
            putBoolean(SCROLL_HORIZONTALLY, scrollHorizontally)
        }

    var use24HourFormat: Boolean
        get() = prefs.getBoolean(USE_24_HOUR_FORMAT, DateFormat.is24HourFormat(context))
        set(use24HourFormat) = prefs.edit { putBoolean(USE_24_HOUR_FORMAT, use24HourFormat) }


    var isUsingModifiedAppIcon: Boolean
        get() = prefs.getBoolean(IS_USING_MODIFIED_APP_ICON, false)
        set(isUsingModifiedAppIcon) = prefs.edit {
            putBoolean(IS_USING_MODIFIED_APP_ICON, isUsingModifiedAppIcon)
        }

    var appId: String
        get() = prefs.getString(APP_ID, "")!!
        set(appId) = prefs.edit { putString(APP_ID, appId) }

    var wasOrangeIconChecked: Boolean
        get() = prefs.getBoolean(WAS_ORANGE_ICON_CHECKED, false)
        set(wasOrangeIconChecked) = prefs.edit {
            putBoolean(WAS_ORANGE_ICON_CHECKED, wasOrangeIconChecked)
        }

    var wasBeforeRateShown: Boolean
        get() = prefs.getBoolean(WAS_BEFORE_RATE_SHOWN, false)
        set(wasBeforeRateShown) = prefs.edit {
            putBoolean(WAS_BEFORE_RATE_SHOWN, wasBeforeRateShown)
        }


    var appSideloadingStatus: Int
        get() = prefs.getInt(APP_SIDELOADING_STATUS, SIDELOADING_UNCHECKED)
        set(appSideloadingStatus) = prefs.edit {
            putInt(APP_SIDELOADING_STATUS, appSideloadingStatus)
        }

    var dateFormat: String
        get() = prefs.getString(DATE_FORMAT, getDefaultDateFormat())!!
        set(dateFormat) = prefs.edit { putString(DATE_FORMAT, dateFormat) }

    private fun getDefaultDateFormat(): String {
        val format = DateFormat.getDateFormat(context)
        val pattern = (format as SimpleDateFormat).toLocalizedPattern()
        return when (pattern.lowercase().replace(" ", "")) {
            "d.M.y" -> DATE_FORMAT_ONE
            "dd/mm/y" -> DATE_FORMAT_TWO
            "mm/dd/y" -> DATE_FORMAT_THREE
            "y-mm-dd" -> DATE_FORMAT_FOUR
            "dmmmmy" -> DATE_FORMAT_FIVE
            "mmmmdy" -> DATE_FORMAT_SIX
            "mm-dd-y" -> DATE_FORMAT_SEVEN
            "dd-mm-y" -> DATE_FORMAT_EIGHT
            else -> DATE_FORMAT_ONE
        }
    }

    var wasOTGHandled: Boolean
        get() = prefs.getBoolean(WAS_OTG_HANDLED, false)
        set(wasOTGHandled) = prefs.edit { putBoolean(WAS_OTG_HANDLED, wasOTGHandled) }

    var wasAppRated: Boolean
        get() = prefs.getBoolean(WAS_APP_RATED, false)
        set(wasAppRated) = prefs.edit { putBoolean(WAS_APP_RATED, wasAppRated) }

    var wasSortingByNumericValueAdded: Boolean
        get() = prefs.getBoolean(WAS_SORTING_BY_NUMERIC_VALUE_ADDED, false)
        set(wasSortingByNumericValueAdded) = prefs.edit {
            putBoolean(WAS_SORTING_BY_NUMERIC_VALUE_ADDED, wasSortingByNumericValueAdded)
        }

    var lastRenameUsed: Int
        get() = prefs.getInt(LAST_RENAME_USED, RENAME_SIMPLE)
        set(lastRenameUsed) = prefs.edit { putInt(LAST_RENAME_USED, lastRenameUsed) }

    var lastRenamePatternUsed: String
        get() = prefs.getString(LAST_RENAME_PATTERN_USED, "")!!
        set(lastRenamePatternUsed) = prefs.edit {
            putString(LAST_RENAME_PATTERN_USED, lastRenamePatternUsed)
        }

    var lastExportedSettingsFolder: String
        get() = prefs.getString(LAST_EXPORTED_SETTINGS_FOLDER, "")!!
        set(lastExportedSettingsFolder) = prefs.edit {
            putString(LAST_EXPORTED_SETTINGS_FOLDER, lastExportedSettingsFolder)
        }

    var fontSize: Int
        get() = prefs.getInt(FONT_SIZE, context.resources.getInteger(R.integer.default_font_size))
        set(size) = prefs.edit { putInt(FONT_SIZE, size) }

    var favorites: MutableSet<String>
        get() = prefs.getStringSet(FAVORITES, HashSet())!!
        set(favorites) = prefs.edit { remove(FAVORITES).putStringSet(FAVORITES, favorites) }

    // color picker last used colors
    var colorPickerRecentColors: LinkedList<Int>
        get(): LinkedList<Int> {
            val defaultList = arrayListOf(
                ContextCompat.getColor(context, R.color.md_red_700),
                ContextCompat.getColor(context, R.color.md_blue_700),
                ContextCompat.getColor(context, R.color.md_green_700),
                ContextCompat.getColor(context, R.color.md_yellow_700),
                ContextCompat.getColor(context, R.color.md_orange_700)
            )
            return LinkedList(
                prefs.getString(COLOR_PICKER_RECENT_COLORS, null)?.lines()?.map { it.toInt() }
                    ?: defaultList)
        }
        set(recentColors) = prefs.edit {
            putString(COLOR_PICKER_RECENT_COLORS, recentColors.joinToString(separator = "\n"))
        }

    var viewType: Int
        get() = prefs.getInt(VIEW_TYPE, VIEW_TYPE_LIST)
        set(viewType) = prefs.edit { putInt(VIEW_TYPE, viewType) }

    fun getCurrentAppIconColorIndex(context: Context): Int {
        val appIconColor = appIconColor
        context.getAppIconColors().forEachIndexed { index, color ->
            if (color == appIconColor) {
                return index
            }
        }
        return 0
    }

}
