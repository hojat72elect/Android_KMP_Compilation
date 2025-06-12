package ca.hojat.smart.musicplayer.shared.ui.dialogs

import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.databinding.DialogCreateNewFolderBinding
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.createAndroidSAFDirectory
import ca.hojat.smart.musicplayer.shared.extensions.createSAFDirectory
import ca.hojat.smart.musicplayer.shared.extensions.getAlertDialogBuilder
import ca.hojat.smart.musicplayer.shared.extensions.getDocumentFile
import ca.hojat.smart.musicplayer.shared.extensions.getFilenameFromPath
import ca.hojat.smart.musicplayer.shared.extensions.getParentPath
import ca.hojat.smart.musicplayer.shared.extensions.humanizePath
import ca.hojat.smart.musicplayer.shared.extensions.isAStorageRootFolder
import ca.hojat.smart.musicplayer.shared.extensions.isAValidFilename
import ca.hojat.smart.musicplayer.shared.extensions.isAccessibleWithSAF
import ca.hojat.smart.musicplayer.shared.extensions.isRestrictedSAFOnlyRoot
import ca.hojat.smart.musicplayer.shared.extensions.needsStupidWritePermissions
import ca.hojat.smart.musicplayer.shared.extensions.setupDialogStuff
import ca.hojat.smart.musicplayer.shared.extensions.showKeyboard
import ca.hojat.smart.musicplayer.shared.extensions.value
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.AlertDialogState
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.ShowKeyboardWhenDialogIsOpenedAndRequestFocus
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.dialogBorder
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.dialogContainerColor
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.dialogElevation
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.dialogShape
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.dialogTextColor
import ca.hojat.smart.musicplayer.shared.ui.compose.alert_dialog.rememberAlertDialogState
import ca.hojat.smart.musicplayer.shared.ui.compose.extensions.MyDevices
import ca.hojat.smart.musicplayer.shared.ui.compose.theme.AppThemeSurface
import ca.hojat.smart.musicplayer.shared.ui.compose.theme.SimpleTheme
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase
import java.io.File

@SuppressLint("SetTextI18n")
class CreateNewFolderDialog(
    val activity: BaseSimpleActivity,
    val path: String,
    val callback: (path: String) -> Unit
) {
    init {
        val view = DialogCreateNewFolderBinding.inflate(activity.layoutInflater, null, false)
        view.folderPath.setText("${activity.humanizePath(path).trimEnd('/')}/")

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    view.root,
                    this,
                    R.string.create_new_folder
                ) { alertDialog ->
                    alertDialog.showKeyboard(view.folderName)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(View.OnClickListener {
                            val name = view.folderName.value
                            when {
                                name.isEmpty() -> ShowToastUseCase(activity, R.string.empty_name)
                                name.isAValidFilename() -> {
                                    val file = File(path, name)
                                    if (file.exists()) {
                                        ShowToastUseCase(activity, R.string.name_taken)
                                        return@OnClickListener
                                    }

                                    createFolder("$path/$name", alertDialog)
                                }

                                else -> ShowToastUseCase(activity, R.string.invalid_name)
                            }
                        })
                }
            }
    }


    private fun createFolder(path: String, alertDialog: AlertDialog) {
        try {
            when {
                activity.isRestrictedSAFOnlyRoot(path) && activity.createAndroidSAFDirectory(path) -> sendSuccess(
                    alertDialog,
                    path
                )

                activity.isAccessibleWithSAF(path) -> activity.handleSAFDialogSdk30(path) {
                    if (it && activity.createSAFDirectory(path)) {
                        sendSuccess(alertDialog, path)
                    }
                }

                activity.needsStupidWritePermissions(path) -> activity.handleSAFDialog(path) {
                    if (it) {
                        try {
                            val documentFile = activity.getDocumentFile(path.getParentPath())
                            val newDir = documentFile?.createDirectory(path.getFilenameFromPath())
                                ?: activity.getDocumentFile(path)
                            if (newDir != null) {
                                sendSuccess(alertDialog, path)
                            } else {
                                ShowToastUseCase(activity, R.string.unknown_error_occurred)
                            }
                        } catch (e: SecurityException) {
                            ShowToastUseCase(activity, "The error : $e")
                        }
                    }
                }

                File(path).mkdirs() -> sendSuccess(alertDialog, path)
                activity.isAStorageRootFolder(path.getParentPath()) -> activity.handleSAFCreateDocumentDialogSdk30(
                    path
                ) {
                    if (it) {
                        sendSuccess(alertDialog, path)
                    }
                }

                else -> ShowToastUseCase(activity, activity.getString(R.string.could_not_create_folder, path.getFilenameFromPath()))
            }
        } catch (e: Exception) {
            ShowToastUseCase(activity, "The error : $e")
        }
    }

    private fun sendSuccess(alertDialog: AlertDialog, path: String) {
        callback(path.trimEnd('/'))
        alertDialog.dismiss()
    }
}

@Composable
fun CreateNewFolderAlertDialog(
    alertDialogState: AlertDialogState,
    path: String,
    modifier: Modifier = Modifier,
    callback: (path: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val view = LocalView.current
    var title by remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier.dialogBorder,
        shape = dialogShape,
        containerColor = dialogContainerColor,
        tonalElevation = dialogElevation,
        onDismissRequest = alertDialogState::hide,
        confirmButton = {
            TextButton(
                onClick = {
                    alertDialogState.hide()
                    //add callback
                    val name = title
                    when {
                        name.isEmpty() -> ShowToastUseCase(context, R.string.empty_name)
                        name.isAValidFilename() -> {
                            val file = File(path, name)
                            if (file.exists()) {
                                ShowToastUseCase(context, R.string.name_taken)
                                return@TextButton
                            }

                            callback("$path/$name")
                        }

                        else -> ShowToastUseCase(context, R.string.invalid_name)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = alertDialogState::hide
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.create_new_folder),
                color = dialogTextColor,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = if (!view.isInEditMode) "${
                        context.humanizePath(path).trimEnd('/')
                    }/" else path,
                    onValueChange = {},
                    label = {
                        Text(text = stringResource(id = R.string.folder))
                    },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = dialogTextColor,
                        disabledBorderColor = SimpleTheme.colorScheme.primary,
                        disabledLabelColor = SimpleTheme.colorScheme.primary,
                    )
                )

                Spacer(modifier = Modifier.padding(vertical = SimpleTheme.dimens.padding.medium))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text(text = stringResource(id = R.string.title))
                    },
                )
            }
        }
    )
    ShowKeyboardWhenDialogIsOpenedAndRequestFocus(focusRequester = focusRequester)
}

@MyDevices
@Composable
private fun CreateNewFolderAlertDialogPreview() {
    AppThemeSurface {
        CreateNewFolderAlertDialog(
            alertDialogState = rememberAlertDialogState(),
            path = "Internal/"
        ) {}
    }
}
