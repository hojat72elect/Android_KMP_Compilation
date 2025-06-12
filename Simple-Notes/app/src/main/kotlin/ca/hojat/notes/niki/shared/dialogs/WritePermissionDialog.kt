package ca.hojat.notes.niki.shared.dialogs

import android.app.Activity
import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Immutable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogWritePermissionBinding
import ca.hojat.notes.niki.databinding.DialogWritePermissionOtgBinding
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.shared.extensions.getAlertDialogBuilder
import ca.hojat.notes.niki.shared.extensions.humanizePath
import ca.hojat.notes.niki.shared.extensions.setupDialogStuff

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class WritePermissionDialog(
    activity: Activity,
    private val writePermissionDialogMode: WritePermissionDialogMode,
    val callback: () -> Unit
) {

    @Immutable
    sealed class WritePermissionDialogMode {
        @Immutable
        data object Otg : WritePermissionDialogMode()

        @Immutable
        data object SdCard : WritePermissionDialogMode()

        @Immutable
        data class OpenDocumentTreeSDK30(val path: String) : WritePermissionDialogMode()

        @Immutable
        data object CreateDocumentSDK30 : WritePermissionDialogMode()
    }

    private var dialog: AlertDialog? = null

    init {
        val sdCardView = DialogWritePermissionBinding.inflate(activity.layoutInflater, null, false)
        val otgView = DialogWritePermissionOtgBinding.inflate(
            activity.layoutInflater,
            null,
            false
        )

        var dialogTitle = R.string.confirm_storage_access_title

        val glide = Glide.with(activity)
        val crossFade = DrawableTransitionOptions.withCrossFade()
        when (writePermissionDialogMode) {
            WritePermissionDialogMode.Otg -> {
                otgView.writePermissionsDialogOtgText.setText(R.string.confirm_usb_storage_access_text)
                glide.load(R.drawable.img_write_storage_otg).transition(crossFade)
                    .into(otgView.writePermissionsDialogOtgImage)
            }

            WritePermissionDialogMode.SdCard -> {
                glide.load(R.drawable.img_write_storage).transition(crossFade)
                    .into(sdCardView.writePermissionsDialogImage)
                glide.load(R.drawable.img_write_storage_sd).transition(crossFade)
                    .into(sdCardView.writePermissionsDialogImageSd)
            }

            is WritePermissionDialogMode.OpenDocumentTreeSDK30 -> {
                dialogTitle = R.string.confirm_folder_access_title
                val humanizedPath = activity.humanizePath(writePermissionDialogMode.path)
                otgView.writePermissionsDialogOtgText.text =
                    Html.fromHtml(
                        activity.getString(
                            R.string.confirm_storage_access_android_text_specific,
                            humanizedPath
                        )
                    )
                glide.load(R.drawable.img_write_storage_sdk_30).transition(crossFade)
                    .into(otgView.writePermissionsDialogOtgImage)

                otgView.writePermissionsDialogOtgImage.setOnClickListener {
                    dialogConfirmed()
                }
            }

            WritePermissionDialogMode.CreateDocumentSDK30 -> {
                dialogTitle = R.string.confirm_folder_access_title
                otgView.writePermissionsDialogOtgText.text =
                    Html.fromHtml(activity.getString(R.string.confirm_create_doc_for_new_folder_text))
                glide.load(R.drawable.img_write_storage_create_doc_sdk_30).transition(crossFade)
                    .into(otgView.writePermissionsDialogOtgImage)

                otgView.writePermissionsDialogOtgImage.setOnClickListener {
                    dialogConfirmed()
                }
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { _, _ -> dialogConfirmed() }
            .setOnCancelListener {
                BaseActivity.funAfterSAFPermission?.invoke(false)
                BaseActivity.funAfterSAFPermission = null
            }
            .apply {
                activity.setupDialogStuff(
                    if (writePermissionDialogMode == WritePermissionDialogMode.SdCard) sdCardView.root else otgView.root,
                    this,
                    dialogTitle
                ) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun dialogConfirmed() {
        dialog?.dismiss()
        callback()
    }
}

