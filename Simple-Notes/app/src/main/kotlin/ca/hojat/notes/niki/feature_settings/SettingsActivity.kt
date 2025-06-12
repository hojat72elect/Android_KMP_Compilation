package ca.hojat.notes.niki.feature_settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.ActivitySettingsBinding
import ca.hojat.notes.niki.shared.dialogs.ExportNotesDialog
import ca.hojat.notes.niki.shared.dialogs.ManageAutoBackupsDialog
import ca.hojat.notes.niki.shared.dialogs.RadioGroupDialog
import ca.hojat.notes.niki.shared.extensions.beVisibleIf
import ca.hojat.notes.niki.shared.extensions.cancelScheduledAutomaticBackup
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getProperPrimaryColor
import ca.hojat.notes.niki.shared.extensions.requestUnlockNotes
import ca.hojat.notes.niki.shared.extensions.scheduleNextAutomaticBackup
import ca.hojat.notes.niki.shared.extensions.showErrorToast
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.updateTextColors
import ca.hojat.notes.niki.shared.extensions.updateWidgets
import ca.hojat.notes.niki.shared.extensions.viewBinding
import ca.hojat.notes.niki.shared.extensions.widgetsDB
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_BG_COLOR
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_ID
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_KEY_ID
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_NOTE_ID
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_SHOW_TITLE
import ca.hojat.notes.niki.shared.helpers.CUSTOMIZED_WIDGET_TEXT_COLOR
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_100_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_125_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_150_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_175_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_200_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_250_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_300_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_50_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_60_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_75_PERCENT
import ca.hojat.notes.niki.shared.helpers.FONT_SIZE_90_PERCENT
import ca.hojat.notes.niki.shared.helpers.GRAVITY_CENTER
import ca.hojat.notes.niki.shared.helpers.GRAVITY_END
import ca.hojat.notes.niki.shared.helpers.GRAVITY_START
import ca.hojat.notes.niki.shared.helpers.IS_CUSTOMIZING_COLORS
import ca.hojat.notes.niki.shared.helpers.NavigationIcon
import ca.hojat.notes.niki.shared.helpers.NotesHelper
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.helpers.isOreoPlus
import ca.hojat.notes.niki.shared.helpers.isRPlus
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.RadioItem
import ca.hojat.notes.niki.shared.data.models.Widget
import ca.hojat.notes.niki.feature_app_widget.WidgetConfigureActivity
import java.util.Locale
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class SettingsActivity : BaseActivity() {
    private val notesFileType = "application/json"
    private val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateMaterialActivityViews(
            binding.settingsCoordinator,
            binding.settingsHolder,
            useTransparentNavigation = true,
            useTopSearchMenu = false
        )
        setupMaterialScrollListener(binding.settingsNestedScrollview, binding.settingsToolbar)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.settingsToolbar, NavigationIcon.Arrow)

        setupCustomizeColors()
        setupLanguage()
        setupAutosaveNotes()
        setupDisplaySuccess()
        setupClickableLinks()
        setupMonospacedFont()
        setupShowKeyboard()
        setupShowNotePicker()
        setupShowWordCount()
        setupEnableLineWrap()
        setupFontSize()
        setupGravity()
        setupCursorPlacement()
        setupIncognitoMode()
        setupCustomizeWidgetColors()
        setupNotesExport()
        setupNotesImport()
        setupEnableAutomaticBackups()
        setupManageAutomaticBackups()
        updateTextColors(binding.settingsNestedScrollview)

        arrayOf(
            binding.settingsColorCustomizationSectionLabel,
            binding.settingsGeneralSettingsLabel,
            binding.settingsTextLabel,
            binding.settingsStartupLabel,
            binding.settingsSavingLabel,
            binding.settingsMigratingLabel,
            binding.settingsBackupsLabel,
        ).forEach {
            it.setTextColor(getProperPrimaryColor())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        updateMenuItemColors(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                toast(R.string.importing)
                importNotes(uri)
            }
        }

    private val saveDocument =
        registerForActivityResult(ActivityResultContracts.CreateDocument(notesFileType)) { uri ->
            if (uri != null) {
                toast(R.string.exporting)
                NotesHelper(this).getNotes { notes ->
                    requestUnlockNotes(notes) { unlockedNotes ->
                        val notLockedNotes = notes.filterNot { it.isLocked() }
                        val notesToExport = unlockedNotes + notLockedNotes
                        exportNotes(notesToExport, uri)
                    }
                }
            }
        }

    private fun setupCustomizeColors() {
        binding.settingsColorCustomizationHolder.setOnClickListener {
            startCustomizationActivity()
        }
    }

    private fun setupLanguage() {
        binding.settingsLanguage.text = "English" // right now we don't support other languages.
        binding.settingsLanguageHolder.setOnClickListener {
            toast("This feature is not available right now.", Toast.LENGTH_SHORT)
        }
    }

    private fun setupAutosaveNotes() {
        binding.settingsAutosaveNotes.isChecked = config.autosaveNotes
        binding.settingsAutosaveNotesHolder.setOnClickListener {
            binding.settingsAutosaveNotes.toggle()
            config.autosaveNotes = binding.settingsAutosaveNotes.isChecked
        }
    }

    private fun setupDisplaySuccess() {
        binding.settingsDisplaySuccess.isChecked = config.displaySuccess
        binding.settingsDisplaySuccessHolder.setOnClickListener {
            binding.settingsDisplaySuccess.toggle()
            config.displaySuccess = binding.settingsDisplaySuccess.isChecked
        }
    }

    private fun setupClickableLinks() {
        binding.settingsClickableLinks.isChecked = config.clickableLinks
        binding.settingsClickableLinksHolder.setOnClickListener {
            binding.settingsClickableLinks.toggle()
            config.clickableLinks = binding.settingsClickableLinks.isChecked
        }
    }

    private fun setupMonospacedFont() {
        binding.settingsMonospacedFont.isChecked = config.monospacedFont
        binding.settingsMonospacedFontHolder.setOnClickListener {
            binding.settingsMonospacedFont.toggle()
            config.monospacedFont = binding.settingsMonospacedFont.isChecked
            updateWidgets()
        }
    }

    private fun setupShowKeyboard() {
        binding.settingsShowKeyboard.isChecked = config.showKeyboard
        binding.settingsShowKeyboardHolder.setOnClickListener {
            binding.settingsShowKeyboard.toggle()
            config.showKeyboard = binding.settingsShowKeyboard.isChecked
        }
    }

    private fun setupShowNotePicker() {
        NotesHelper(this).getNotes {
            binding.settingsShowNotePickerHolder.beVisibleIf(it.size > 1)
        }

        binding.settingsShowNotePicker.isChecked = config.showNotePicker
        binding.settingsShowNotePickerHolder.setOnClickListener {
            binding.settingsShowNotePicker.toggle()
            config.showNotePicker = binding.settingsShowNotePicker.isChecked
        }
    }

    private fun setupShowWordCount() {
        binding.settingsShowWordCount.isChecked = config.showWordCount
        binding.settingsShowWordCountHolder.setOnClickListener {
            binding.settingsShowWordCount.toggle()
            config.showWordCount = binding.settingsShowWordCount.isChecked
        }
    }

    private fun setupEnableLineWrap() {
        binding.settingsEnableLineWrap.isChecked = config.enableLineWrap
        binding.settingsEnableLineWrapHolder.setOnClickListener {
            binding.settingsEnableLineWrap.toggle()
            config.enableLineWrap = binding.settingsEnableLineWrap.isChecked
        }
    }

    private fun setupFontSize() {
        binding.settingsFontSize.text = getFontSizePercentText(config.fontSizePercentage)
        binding.settingsFontSizeHolder.setOnClickListener {
            val items = arrayListOf(
                RadioItem(FONT_SIZE_50_PERCENT, getFontSizePercentText(FONT_SIZE_50_PERCENT)),
                RadioItem(FONT_SIZE_60_PERCENT, getFontSizePercentText(FONT_SIZE_60_PERCENT)),
                RadioItem(FONT_SIZE_75_PERCENT, getFontSizePercentText(FONT_SIZE_75_PERCENT)),
                RadioItem(FONT_SIZE_90_PERCENT, getFontSizePercentText(FONT_SIZE_90_PERCENT)),
                RadioItem(FONT_SIZE_100_PERCENT, getFontSizePercentText(FONT_SIZE_100_PERCENT)),
                RadioItem(FONT_SIZE_125_PERCENT, getFontSizePercentText(FONT_SIZE_125_PERCENT)),
                RadioItem(FONT_SIZE_150_PERCENT, getFontSizePercentText(FONT_SIZE_150_PERCENT)),
                RadioItem(FONT_SIZE_175_PERCENT, getFontSizePercentText(FONT_SIZE_175_PERCENT)),
                RadioItem(FONT_SIZE_200_PERCENT, getFontSizePercentText(FONT_SIZE_200_PERCENT)),
                RadioItem(FONT_SIZE_250_PERCENT, getFontSizePercentText(FONT_SIZE_250_PERCENT)),
                RadioItem(FONT_SIZE_300_PERCENT, getFontSizePercentText(FONT_SIZE_300_PERCENT))
            )

            RadioGroupDialog(this@SettingsActivity, items, config.fontSizePercentage) {
                config.fontSizePercentage = it as Int
                binding.settingsFontSize.text = getFontSizePercentText(config.fontSizePercentage)
                updateWidgets()
            }
        }
    }

    private fun getFontSizePercentText(fontSizePercentage: Int): String = "$fontSizePercentage%"

    private fun setupGravity() {
        binding.settingsGravity.text = getGravityText()
        binding.settingsGravityHolder.setOnClickListener {
            val items = listOf(GRAVITY_START, GRAVITY_CENTER, GRAVITY_END).map {
                RadioItem(
                    it,
                    getGravityOptionLabel(it)
                )
            }
            RadioGroupDialog(this@SettingsActivity, ArrayList(items), config.gravity) {
                config.gravity = it as Int
                binding.settingsGravity.text = getGravityText()
                updateWidgets()
            }
        }
    }

    private fun getGravityOptionLabel(gravity: Int): String {
        val leftToRightDirection =
            TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR
        val leftRightLabels = listOf(R.string.left, R.string.right)
        val startEndLabels = if (leftToRightDirection) {
            leftRightLabels
        } else {
            leftRightLabels.reversed()
        }
        return getString(
            when (gravity) {
                GRAVITY_START -> startEndLabels.first()
                GRAVITY_CENTER -> R.string.center
                else -> startEndLabels.last()
            }
        )
    }

    private fun getGravityText() = getGravityOptionLabel(config.gravity)

    private fun setupCursorPlacement() {
        binding.settingsCursorPlacement.isChecked = config.placeCursorToEnd
        binding.settingsCursorPlacementHolder.setOnClickListener {
            binding.settingsCursorPlacement.toggle()
            config.placeCursorToEnd = binding.settingsCursorPlacement.isChecked
        }
    }

    private fun setupCustomizeWidgetColors() {
        var widgetToCustomize: Widget? = null

        binding.settingsWidgetColorCustomizationHolder.setOnClickListener {
            Intent(this, WidgetConfigureActivity::class.java).apply {
                putExtra(IS_CUSTOMIZING_COLORS, true)

                widgetToCustomize?.apply {
                    putExtra(CUSTOMIZED_WIDGET_ID, widgetId)
                    putExtra(CUSTOMIZED_WIDGET_KEY_ID, id)
                    putExtra(CUSTOMIZED_WIDGET_NOTE_ID, noteId)
                    putExtra(CUSTOMIZED_WIDGET_BG_COLOR, widgetBgColor)
                    putExtra(CUSTOMIZED_WIDGET_TEXT_COLOR, widgetTextColor)
                    putExtra(CUSTOMIZED_WIDGET_SHOW_TITLE, widgetShowTitle)
                }

                startActivity(this)
            }
        }

        ensureBackgroundThread {
            val widgets = widgetsDB.getWidgets().filter { it.widgetId != 0 }
            if (widgets.size == 1) {
                widgetToCustomize = widgets.first()
            }
        }
    }

    private fun setupIncognitoMode() {
        binding.settingsUseIncognitoModeHolder.beVisibleIf(isOreoPlus())
        binding.settingsUseIncognitoMode.isChecked = config.useIncognitoMode
        binding.settingsUseIncognitoModeHolder.setOnClickListener {
            binding.settingsUseIncognitoMode.toggle()
            config.useIncognitoMode = binding.settingsUseIncognitoMode.isChecked
        }
    }

    private fun setupNotesExport() {
        binding.settingsExportNotesHolder.setOnClickListener {
            ExportNotesDialog(this) { filename ->
                saveDocument.launch(filename)
            }
        }
    }

    private fun setupNotesImport() {
        binding.settingsImportNotesHolder.setOnClickListener {
            getContent.launch(notesFileType)
        }
    }

    private fun exportNotes(notes: List<Note>, uri: Uri) {
        if (notes.isEmpty()) {
            toast(R.string.no_entries_for_exporting)
        } else {
            try {
                val outputStream = contentResolver.openOutputStream(uri)!!

                val jsonString = Json.encodeToString(notes)
                outputStream.use {
                    it.write(jsonString.toByteArray())
                }
                toast(R.string.exporting_successful)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }

    private fun importNotes(uri: Uri) {
        try {
            val jsonString = contentResolver.openInputStream(uri)!!.use { inputStream ->
                inputStream.bufferedReader().readText()
            }
            val objects = Json.decodeFromString<List<Note>>(jsonString)
            if (objects.isEmpty()) {
                toast(R.string.no_entries_for_importing)
                return
            }
            NotesHelper(this).importNotes(this, objects) { importResult ->
                when (importResult) {
                    NotesHelper.ImportResult.IMPORT_OK -> toast(R.string.importing_successful)
                    NotesHelper.ImportResult.IMPORT_PARTIAL -> toast(R.string.importing_some_entries_failed)
                    NotesHelper.ImportResult.IMPORT_NOTHING_NEW -> toast(R.string.no_new_items)
                    else -> toast(R.string.importing_failed)
                }
            }
        } catch (_: SerializationException) {
            toast(R.string.invalid_file_format)
        } catch (_: IllegalArgumentException) {
            toast(R.string.invalid_file_format)
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }

    private fun setupEnableAutomaticBackups() {
        binding.settingsBackupsLabel.beVisibleIf(isRPlus())
        binding.settingsEnableAutomaticBackupsHolder.beVisibleIf(isRPlus())
        binding.settingsEnableAutomaticBackups.isChecked = config.autoBackup
        binding.settingsEnableAutomaticBackupsHolder.setOnClickListener {
            val wasBackupDisabled = !config.autoBackup
            if (wasBackupDisabled) {
                ManageAutoBackupsDialog(
                    activity = this,
                    onSuccess = {
                        enableOrDisableAutomaticBackups(true)
                        scheduleNextAutomaticBackup()
                    }
                )
            } else {
                cancelScheduledAutomaticBackup()
                enableOrDisableAutomaticBackups(false)
            }
        }
    }

    private fun setupManageAutomaticBackups() {
        binding.settingsManageAutomaticBackupsHolder.beVisibleIf(isRPlus() && config.autoBackup)
        binding.settingsManageAutomaticBackupsHolder.setOnClickListener {
            ManageAutoBackupsDialog(
                activity = this,
                onSuccess = {
                    scheduleNextAutomaticBackup()
                }
            )
        }
    }

    private fun enableOrDisableAutomaticBackups(enable: Boolean) {
        config.autoBackup = enable
        binding.settingsEnableAutomaticBackups.isChecked = enable
        binding.settingsManageAutomaticBackupsHolder.beVisibleIf(enable)
    }
}
