package ca.hojat.notes.niki.shared.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.biometric.BiometricPrompt
import androidx.biometric.auth.AuthPromptCallback
import androidx.biometric.auth.AuthPromptHost
import androidx.biometric.auth.Class2BiometricAuthPrompt
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.databinding.DialogTitleBinding
import ca.hojat.notes.niki.shared.dialogs.AppSideLoadedDialog
import ca.hojat.notes.niki.shared.dialogs.DonateDialog
import ca.hojat.notes.niki.shared.dialogs.RateStarsDialog
import ca.hojat.notes.niki.shared.dialogs.SecurityDialog
import ca.hojat.notes.niki.shared.dialogs.UpgradeToProDialog
import ca.hojat.notes.niki.shared.helpers.DARK_GREY
import ca.hojat.notes.niki.shared.helpers.MyContentProvider
import ca.hojat.notes.niki.shared.helpers.PROTECTION_FINGERPRINT
import ca.hojat.notes.niki.shared.helpers.SIDELOADING_FALSE
import ca.hojat.notes.niki.shared.helpers.SIDELOADING_TRUE
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.helpers.isOnMainThread
import ca.hojat.notes.niki.shared.helpers.isRPlus
import ca.hojat.notes.niki.shared.data.models.SharedTheme
import ca.hojat.notes.niki.shared.extensions.*
import ca.hojat.notes.niki.shared.views.MyTextView
import java.io.File


fun Activity.appLaunched(appId: String) {
    baseConfig.internalStoragePath = getInternalStoragePath()
    updateSDCardPath()
    baseConfig.appId = appId
    if (baseConfig.appRunCount == 0) {
        baseConfig.wasOrangeIconChecked = true
        checkAppIconColor()
    } else if (!baseConfig.wasOrangeIconChecked) {
        baseConfig.wasOrangeIconChecked = true
        val primaryColor = resources.getColor(R.color.color_primary)
        if (baseConfig.appIconColor != primaryColor) {
            getAppIconColors().forEachIndexed { index, color ->
                toggleAppIconColor(appId, index, color, false)
            }

            val defaultClassName =
                "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, defaultClassName),
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            )

            val orangeClassName =
                "${baseConfig.appId.removeSuffix(".debug")}.activities.SplashActivity"
            packageManager.setComponentEnabledSetting(
                ComponentName(baseConfig.appId, orangeClassName),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            baseConfig.appIconColor = primaryColor
            baseConfig.lastIconColor = primaryColor
        }
    }

    baseConfig.appRunCount++
    if (baseConfig.appRunCount % 30 == 0 && !isAProApp()) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            showDonateOrUpgradeDialog()
        }
    }

    if (baseConfig.appRunCount % 40 == 0 && !baseConfig.wasAppRated) {
        if (!resources.getBoolean(R.bool.hide_google_relations)) {
            RateStarsDialog(this)
        }
    }
}

fun Activity.showDonateOrUpgradeDialog() {
    if (getCanAppBeUpgraded()) {
        UpgradeToProDialog(this)
    } else if (!isOrWasThankYouInstalled()) {
        DonateDialog(this)
    }
}

fun Activity.isAppInstalledOnSDCard(): Boolean = try {
    val applicationInfo = packageManager.getPackageInfo(packageName, 0).applicationInfo
    (applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE
} catch (e: Exception) {
    false
}

fun Activity.launchPurchaseThankYouIntent() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=com.simplemobiletools.thankyou")
    } catch (ignored: Exception) {
        launchViewIntent(getString(R.string.thank_you_url))
    }
}

fun Activity.launchUpgradeToProIntent() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=${baseConfig.appId.removeSuffix(".debug")}.pro")
    } catch (ignored: Exception) {
        launchViewIntent(getStoreUrl())
    }
}

fun Activity.launchMoreAppsFromUsIntent() {
    launchViewIntent("https://play.google.com/store/apps/dev?id=9070296388022589266")
}

fun Activity.launchViewIntent(id: Int) = launchViewIntent(getString(id))

fun Activity.launchViewIntent(url: String) {
    hideKeyboard()
    ensureBackgroundThread {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            try {
                startActivity(this)
            } catch (e: ActivityNotFoundException) {
                toast(R.string.no_browser_found)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        }
    }
}

fun Activity.redirectToRateUs() {
    hideKeyboard()
    try {
        launchViewIntent("market://details?id=${packageName.removeSuffix(".debug")}")
    } catch (ignored: ActivityNotFoundException) {
        launchViewIntent(getStoreUrl())
    }
}

inline fun <T : ViewBinding> Activity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun Activity.performSecurityCheck(
    protectionType: Int,
    requiredHash: String,
    successCallback: ((String, Int) -> Unit)? = null,
    failureCallback: (() -> Unit)? = null
) {
    if (protectionType == PROTECTION_FINGERPRINT && isRPlus()) {
        showBiometricPrompt(successCallback, failureCallback)
    } else {
        SecurityDialog(
            activity = this,
            requiredHash = requiredHash,
            showTabIndex = protectionType,
            callback = { hash, type, success ->
                if (success) {
                    successCallback?.invoke(hash, type)
                } else {
                    failureCallback?.invoke()
                }
            }
        )
    }
}

fun Activity.scanPathRecursively(path: String, callback: (() -> Unit)? = null) {
    applicationContext.scanPathRecursively(path, callback)
}

fun Activity.scanPathsRecursively(paths: List<String>, callback: (() -> Unit)? = null) {
    applicationContext.scanPathsRecursively(paths, callback)
}

fun Activity.rescanPath(path: String, callback: (() -> Unit)? = null) {
    applicationContext.rescanPath(path, callback)
}

fun Activity.rescanPaths(paths: List<String>, callback: (() -> Unit)? = null) {
    applicationContext.rescanPaths(paths, callback)
}


fun Activity.showBiometricPrompt(
    successCallback: ((String, Int) -> Unit)? = null,
    failureCallback: (() -> Unit)? = null
) {
    Class2BiometricAuthPrompt.Builder(
        getText(R.string.authenticate),
        getText(R.string.cancel)
    )
        .build()
        .startAuthentication(
            AuthPromptHost(this as FragmentActivity),
            object : AuthPromptCallback() {
                override fun onAuthenticationSucceeded(
                    activity: FragmentActivity?,
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    successCallback?.invoke("", PROTECTION_FINGERPRINT)
                }

                override fun onAuthenticationError(
                    activity: FragmentActivity?,
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    val isCanceledByUser =
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED
                    if (!isCanceledByUser) {
                        toast(errString.toString())
                    }
                    failureCallback?.invoke()
                }

                override fun onAuthenticationFailed(activity: FragmentActivity?) {
                    toast(R.string.authentication_failed)
                    failureCallback?.invoke()
                }
            }
        )
}

fun createTempFile(file: File): File? {
    return if (file.isDirectory) {
        createTempDir("temp", "${System.currentTimeMillis()}", file.parentFile)
    } else {
        if (isRPlus()) {
            // this can throw FileSystemException, lets catch and handle it at the place calling this function
            kotlin.io.path.createTempFile(
                file.parentFile?.toPath(),
                "temp",
                "${System.currentTimeMillis()}"
            ).toFile()
        } else {
            createTempFile("temp", "${System.currentTimeMillis()}", file.parentFile)
        }
    }
}

fun Activity.hideKeyboard() {
    if (isOnMainThread()) {
        hideKeyboardSync()
    } else {
        Handler(Looper.getMainLooper()).post {
            hideKeyboardSync()
        }
    }
}

fun Activity.hideKeyboardSync() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun Activity.showKeyboard(et: EditText) {
    et.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.handleHiddenFolderPasswordProtection(callback: () -> Unit) {
    if (baseConfig.isHiddenPasswordProtectionOn) {
        SecurityDialog(
            this,
            baseConfig.hiddenPasswordHash,
            baseConfig.hiddenProtectionType
        ) { _, _, success ->
            if (success) {
                callback()
            }
        }
    } else {
        callback()
    }
}

fun Activity.handleLockedFolderOpening(path: String, callback: (success: Boolean) -> Unit) {
    if (baseConfig.isFolderProtected(path)) {
        SecurityDialog(
            this,
            baseConfig.getFolderProtectionHash(path),
            baseConfig.getFolderProtectionType(path)
        ) { _, _, success ->
            callback(success)
        }
    } else {
        callback(true)
    }
}

fun Activity.updateSharedTheme(sharedTheme: SharedTheme) {
    try {
        val contentValues = MyContentProvider.fillThemeContentValues(sharedTheme)
        applicationContext.contentResolver.update(
            MyContentProvider.MY_CONTENT_URI,
            contentValues,
            null,
            null
        )
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Activity.setupDialogStuff(
    view: View,
    dialog: AlertDialog.Builder,
    titleId: Int = 0,
    titleText: String = "",
    cancelOnTouchOutside: Boolean = true,
    callback: ((alertDialog: AlertDialog) -> Unit)? = null
) {
    if (isDestroyed || isFinishing) {
        return
    }

    val textColor = getProperTextColor()
    val primaryColor = getProperPrimaryColor()
    if (view is ViewGroup) {
        updateTextColors(view)
    } else if (view is MyTextView) {
        view.setColors(textColor, primaryColor)
    }

    if (dialog is MaterialAlertDialogBuilder) {
        dialog.create().apply {
            if (titleId != 0) {
                setTitle(titleId)
            } else if (titleText.isNotEmpty()) {
                setTitle(titleText)
            }

            setView(view)
            setCancelable(cancelOnTouchOutside)
            if (!isFinishing) {
                show()
            }
            getButton(Dialog.BUTTON_POSITIVE)?.setTextColor(primaryColor)
            getButton(Dialog.BUTTON_NEGATIVE)?.setTextColor(primaryColor)
            getButton(Dialog.BUTTON_NEUTRAL)?.setTextColor(primaryColor)
            callback?.invoke(this)
        }
    } else {
        var title: DialogTitleBinding? = null
        if (titleId != 0 || titleText.isNotEmpty()) {
            title = DialogTitleBinding.inflate(layoutInflater, null, false)
            title.dialogTitleTextview.apply {
                if (titleText.isNotEmpty()) {
                    text = titleText
                } else {
                    setText(titleId)
                }
                setTextColor(textColor)
            }
        }

        // if we use the same primary and background color, use the text color for dialog confirmation buttons
        val dialogButtonColor = if (primaryColor == baseConfig.backgroundColor) {
            textColor
        } else {
            primaryColor
        }

        dialog.create().apply {
            setView(view)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCustomTitle(title?.root)
            setCanceledOnTouchOutside(cancelOnTouchOutside)
            if (!isFinishing) {
                show()
            }
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(dialogButtonColor)
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(dialogButtonColor)
            getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(dialogButtonColor)

            val bgDrawable = when {
                isBlackAndWhiteTheme() -> resources.getDrawable(
                    R.drawable.black_dialog_background,
                    theme
                )

                baseConfig.isUsingSystemTheme -> resources.getDrawable(
                    R.drawable.dialog_you_background,
                    theme
                )

                else -> resources.getColoredDrawableWithColor(
                    R.drawable.dialog_bg,
                    baseConfig.backgroundColor
                )
            }

            window?.setBackgroundDrawable(bgDrawable)
            callback?.invoke(this)
        }
    }
}

fun Activity.getAlertDialogBuilder() = if (baseConfig.isUsingSystemTheme) {
    MaterialAlertDialogBuilder(this)
} else {
    AlertDialog.Builder(this)
}

fun Activity.checkAppSideloading(): Boolean {
    val isSideloaded = when (baseConfig.appSideloadingStatus) {
        SIDELOADING_TRUE -> true
        SIDELOADING_FALSE -> false
        else -> isAppSideLoaded()
    }

    baseConfig.appSideloadingStatus = if (isSideloaded) SIDELOADING_TRUE else SIDELOADING_FALSE
    if (isSideloaded) {
        showSideloadingDialog()
    }

    return isSideloaded
}

fun Activity.isAppSideLoaded(): Boolean {
    return try {
        AppCompatResources.getDrawable(this, R.drawable.ic_camera_vector)
        false
    } catch (e: Exception) {
        true
    }
}

fun Activity.showSideloadingDialog() {
    AppSideLoadedDialog(this) {
        finish()
    }
}

fun Activity.onApplyWindowInsets(callback: (WindowInsetsCompat) -> Unit) {
    window.decorView.setOnApplyWindowInsetsListener { view, insets ->
        callback(WindowInsetsCompat.toWindowInsetsCompat(insets))
        view.onApplyWindowInsets(insets)
        insets
    }
}

fun Activity.getThemeId(color: Int = baseConfig.primaryColor, showTransparentTop: Boolean = false) =
    when {
        baseConfig.isUsingSystemTheme -> if (isUsingSystemDarkTheme()) R.style.AppTheme_Base_System else R.style.AppTheme_Base_System_Light
        isBlackAndWhiteTheme() -> when {
            showTransparentTop -> R.style.AppTheme_BlackAndWhite_NoActionBar
            baseConfig.primaryColor.getContrastColor() == DARK_GREY -> R.style.AppTheme_BlackAndWhite_DarkTextColor
            else -> R.style.AppTheme_BlackAndWhite
        }

        isWhiteTheme() -> when {
            showTransparentTop -> R.style.AppTheme_White_NoActionBar
            baseConfig.primaryColor.getContrastColor() == Color.WHITE -> R.style.AppTheme_White_LightTextColor
            else -> R.style.AppTheme_White
        }

        showTransparentTop -> {
            when (color) {
                -12846 -> R.style.AppTheme_Red_100_core
                -1074534 -> R.style.AppTheme_Red_200_core
                -1739917 -> R.style.AppTheme_Red_300_core
                -1092784 -> R.style.AppTheme_Red_400_core
                -769226 -> R.style.AppTheme_Red_500_core
                -1754827 -> R.style.AppTheme_Red_600_core
                -2937041 -> R.style.AppTheme_Red_700_core
                -3790808 -> R.style.AppTheme_Red_800_core
                -4776932 -> R.style.AppTheme_Red_900_core

                -476208 -> R.style.AppTheme_Pink_100_core
                -749647 -> R.style.AppTheme_Pink_200_core
                -1023342 -> R.style.AppTheme_Pink_300_core
                -1294214 -> R.style.AppTheme_Pink_400_core
                -1499549 -> R.style.AppTheme_Pink_500_core
                -2614432 -> R.style.AppTheme_Pink_600_core
                -4056997 -> R.style.AppTheme_Pink_700_core
                -5434281 -> R.style.AppTheme_Pink_800_core
                -7860657 -> R.style.AppTheme_Pink_900_core
                -1982745 -> R.style.AppTheme_Purple_100_core
                -3238952 -> R.style.AppTheme_Purple_200_core
                -4560696 -> R.style.AppTheme_Purple_300_core
                -5552196 -> R.style.AppTheme_Purple_400_core
                -6543440 -> R.style.AppTheme_Purple_500_core
                -7461718 -> R.style.AppTheme_Purple_600_core
                -8708190 -> R.style.AppTheme_Purple_700_core
                -9823334 -> R.style.AppTheme_Purple_800_core
                -11922292 -> R.style.AppTheme_Purple_900_core
                -3029783 -> R.style.AppTheme_Deep_Purple_100_core
                -5005861 -> R.style.AppTheme_Deep_Purple_200_core
                -6982195 -> R.style.AppTheme_Deep_Purple_300_core
                -8497214 -> R.style.AppTheme_Deep_Purple_400_core
                -10011977 -> R.style.AppTheme_Deep_Purple_500_core
                -10603087 -> R.style.AppTheme_Deep_Purple_600_core
                -11457112 -> R.style.AppTheme_Deep_Purple_700_core
                -12245088 -> R.style.AppTheme_Deep_Purple_800_core
                -13558894 -> R.style.AppTheme_Deep_Purple_900_core
                -3814679 -> R.style.AppTheme_Indigo_100_core
                -6313766 -> R.style.AppTheme_Indigo_200_core
                -8812853 -> R.style.AppTheme_Indigo_300_core
                -10720320 -> R.style.AppTheme_Indigo_400_core
                -12627531 -> R.style.AppTheme_Indigo_500_core
                -13022805 -> R.style.AppTheme_Indigo_600_core
                -13615201 -> R.style.AppTheme_Indigo_700_core
                -14142061 -> R.style.AppTheme_Indigo_800_core
                -15064194 -> R.style.AppTheme_Indigo_900_core
                -4464901 -> R.style.AppTheme_Blue_100_core
                -7288071 -> R.style.AppTheme_Blue_200_core
                -10177034 -> R.style.AppTheme_Blue_300_core
                -12409355 -> R.style.AppTheme_Blue_400_core
                -14575885 -> R.style.AppTheme_Blue_500_core
                -14776091 -> R.style.AppTheme_Blue_600_core
                -15108398 -> R.style.AppTheme_Blue_700_core
                -15374912 -> R.style.AppTheme_Blue_800_core
                -15906911 -> R.style.AppTheme_Blue_900_core
                -4987396 -> R.style.AppTheme_Light_Blue_100_core
                -8268550 -> R.style.AppTheme_Light_Blue_200_core
                -11549705 -> R.style.AppTheme_Light_Blue_300_core
                -14043396 -> R.style.AppTheme_Light_Blue_400_core
                -16537100 -> R.style.AppTheme_Light_Blue_500_core
                -16540699 -> R.style.AppTheme_Light_Blue_600_core
                -16611119 -> R.style.AppTheme_Light_Blue_700_core
                -16615491 -> R.style.AppTheme_Light_Blue_800_core
                -16689253 -> R.style.AppTheme_Light_Blue_900_core

                -5051406 -> R.style.AppTheme_Cyan_100_core
                -8331542 -> R.style.AppTheme_Cyan_200_core
                -11677471 -> R.style.AppTheme_Cyan_300_core
                -14235942 -> R.style.AppTheme_Cyan_400_core
                -16728876 -> R.style.AppTheme_Cyan_500_core
                -16732991 -> R.style.AppTheme_Cyan_600_core
                -16738393 -> R.style.AppTheme_Cyan_700_core
                -16743537 -> R.style.AppTheme_Cyan_800_core
                -16752540 -> R.style.AppTheme_Cyan_900_core
                -5054501 -> R.style.AppTheme_Teal_100_core
                -8336444 -> R.style.AppTheme_Teal_200_core
                -11684180 -> R.style.AppTheme_Teal_300_core
                -14244198 -> R.style.AppTheme_Teal_400_core
                -16738680 -> R.style.AppTheme_Teal_500_core
                -16742021 -> R.style.AppTheme_Teal_600_core
                -16746133 -> R.style.AppTheme_Teal_700_core
                -16750244 -> R.style.AppTheme_Teal_800_core
                -16757440 -> R.style.AppTheme_Teal_900_core
                -3610935 -> R.style.AppTheme_Green_100_core
                -5908825 -> R.style.AppTheme_Green_200_core
                -8271996 -> R.style.AppTheme_Green_300_core
                -10044566 -> R.style.AppTheme_Green_400_core
                -11751600 -> R.style.AppTheme_Green_500_core
                -12345273 -> R.style.AppTheme_Green_600_core
                -13070788 -> R.style.AppTheme_Green_700_core
                -13730510 -> R.style.AppTheme_Green_800_core
                -14983648 -> R.style.AppTheme_Green_900_core
                -2298424 -> R.style.AppTheme_Light_Green_100_core
                -3808859 -> R.style.AppTheme_Light_Green_200_core
                -5319295 -> R.style.AppTheme_Light_Green_300_core
                -6501275 -> R.style.AppTheme_Light_Green_400_core
                -7617718 -> R.style.AppTheme_Light_Green_500_core
                -8604862 -> R.style.AppTheme_Light_Green_600_core
                -9920712 -> R.style.AppTheme_Light_Green_700_core
                -11171025 -> R.style.AppTheme_Light_Green_800_core
                -13407970 -> R.style.AppTheme_Light_Green_900_core

                -985917 -> R.style.AppTheme_Lime_100_core
                -1642852 -> R.style.AppTheme_Lime_200_core
                -2300043 -> R.style.AppTheme_Lime_300_core
                -2825897 -> R.style.AppTheme_Lime_400_core
                -3285959 -> R.style.AppTheme_Lime_500_core
                -4142541 -> R.style.AppTheme_Lime_600_core
                -5983189 -> R.style.AppTheme_Lime_700_core
                -6382300 -> R.style.AppTheme_Lime_800_core
                -8227049 -> R.style.AppTheme_Lime_900_core
                -1596 -> R.style.AppTheme_Yellow_100_core
                -2672 -> R.style.AppTheme_Yellow_200_core
                -3722 -> R.style.AppTheme_Yellow_300_core
                -4520 -> R.style.AppTheme_Yellow_400_core
                -5317 -> R.style.AppTheme_Yellow_500_core
                -141259 -> R.style.AppTheme_Yellow_600_core
                -278483 -> R.style.AppTheme_Yellow_700_core
                -415707 -> R.style.AppTheme_Yellow_800_core
                -688361 -> R.style.AppTheme_Yellow_900_core
                -4941 -> R.style.AppTheme_Amber_100_core
                -8062 -> R.style.AppTheme_Amber_200_core
                -10929 -> R.style.AppTheme_Amber_300_core
                -13784 -> R.style.AppTheme_Amber_400_core
                -16121 -> R.style.AppTheme_Amber_500_core
                -19712 -> R.style.AppTheme_Amber_600_core
                -24576 -> R.style.AppTheme_Amber_700_core
                -28928 -> R.style.AppTheme_Amber_800_core
                -37120 -> R.style.AppTheme_Amber_900_core
                -8014 -> R.style.AppTheme_Orange_100_core
                -13184 -> R.style.AppTheme_Orange_200_core
                -18611 -> R.style.AppTheme_Orange_300_core
                -22746 -> R.style.AppTheme_Orange_400_core
                -26624 -> R.style.AppTheme_Orange_500_core
                -291840 -> R.style.AppTheme_Orange_600_core
                -689152 -> R.style.AppTheme_Orange_700_core
                -1086464 -> R.style.AppTheme_Orange_800_core
                -1683200 -> R.style.AppTheme_Orange_900_core
                -13124 -> R.style.AppTheme_Deep_Orange_100_core
                -21615 -> R.style.AppTheme_Deep_Orange_200_core
                -30107 -> R.style.AppTheme_Deep_Orange_300_core
                -36797 -> R.style.AppTheme_Deep_Orange_400_core
                -43230 -> R.style.AppTheme_Deep_Orange_500_core
                -765666 -> R.style.AppTheme_Deep_Orange_600_core
                -1684967 -> R.style.AppTheme_Deep_Orange_700_core
                -2604267 -> R.style.AppTheme_Deep_Orange_800_core
                -4246004 -> R.style.AppTheme_Deep_Orange_900_core
                -2634552 -> R.style.AppTheme_Brown_100_core
                -4412764 -> R.style.AppTheme_Brown_200_core
                -6190977 -> R.style.AppTheme_Brown_300_core
                -7508381 -> R.style.AppTheme_Brown_400_core
                -8825528 -> R.style.AppTheme_Brown_500_core
                -9614271 -> R.style.AppTheme_Brown_600_core
                -10665929 -> R.style.AppTheme_Brown_700_core
                -11652050 -> R.style.AppTheme_Brown_800_core
                -12703965 -> R.style.AppTheme_Brown_900_core
                -3155748 -> R.style.AppTheme_Blue_Grey_100_core
                -5194811 -> R.style.AppTheme_Blue_Grey_200_core
                -7297874 -> R.style.AppTheme_Blue_Grey_300_core
                -8875876 -> R.style.AppTheme_Blue_Grey_400_core
                -10453621 -> R.style.AppTheme_Blue_Grey_500_core
                -11243910 -> R.style.AppTheme_Blue_Grey_600_core
                -12232092 -> R.style.AppTheme_Blue_Grey_700_core
                -13154481 -> R.style.AppTheme_Blue_Grey_800_core
                -14273992 -> R.style.AppTheme_Blue_Grey_900_core
                -1 -> R.style.AppTheme_Grey_100_core
                -1118482 -> R.style.AppTheme_Grey_200_core
                -2039584 -> R.style.AppTheme_Grey_300_core
                -4342339 -> R.style.AppTheme_Grey_400_core
                -6381922 -> R.style.AppTheme_Grey_500_core
                -9079435 -> R.style.AppTheme_Grey_600_core
                -10395295 -> R.style.AppTheme_Grey_700_core
                -12434878 -> R.style.AppTheme_Grey_800_core
                -16777216 -> R.style.AppTheme_Grey_900_core

                else -> R.style.AppTheme_Orange_700_core
            }
        }

        else -> {
            when (color) {
                -12846 -> R.style.AppTheme_Red_100
                -1074534 -> R.style.AppTheme_Red_200
                -1739917 -> R.style.AppTheme_Red_300
                -1092784 -> R.style.AppTheme_Red_400
                -769226 -> R.style.AppTheme_Red_500
                -1754827 -> R.style.AppTheme_Red_600
                -2937041 -> R.style.AppTheme_Red_700
                -3790808 -> R.style.AppTheme_Red_800
                -4776932 -> R.style.AppTheme_Red_900
                -476208 -> R.style.AppTheme_Pink_100
                -749647 -> R.style.AppTheme_Pink_200
                -1023342 -> R.style.AppTheme_Pink_300
                -1294214 -> R.style.AppTheme_Pink_400
                -1499549 -> R.style.AppTheme_Pink_500
                -2614432 -> R.style.AppTheme_Pink_600
                -4056997 -> R.style.AppTheme_Pink_700
                -5434281 -> R.style.AppTheme_Pink_800
                -7860657 -> R.style.AppTheme_Pink_900
                -1982745 -> R.style.AppTheme_Purple_100
                -3238952 -> R.style.AppTheme_Purple_200
                -4560696 -> R.style.AppTheme_Purple_300
                -5552196 -> R.style.AppTheme_Purple_400
                -6543440 -> R.style.AppTheme_Purple_500
                -7461718 -> R.style.AppTheme_Purple_600
                -8708190 -> R.style.AppTheme_Purple_700
                -9823334 -> R.style.AppTheme_Purple_800
                -11922292 -> R.style.AppTheme_Purple_900
                -3029783 -> R.style.AppTheme_Deep_Purple_100
                -5005861 -> R.style.AppTheme_Deep_Purple_200
                -6982195 -> R.style.AppTheme_Deep_Purple_300
                -8497214 -> R.style.AppTheme_Deep_Purple_400
                -10011977 -> R.style.AppTheme_Deep_Purple_500
                -10603087 -> R.style.AppTheme_Deep_Purple_600
                -11457112 -> R.style.AppTheme_Deep_Purple_700
                -12245088 -> R.style.AppTheme_Deep_Purple_800
                -13558894 -> R.style.AppTheme_Deep_Purple_900
                -3814679 -> R.style.AppTheme_Indigo_100
                -6313766 -> R.style.AppTheme_Indigo_200
                -8812853 -> R.style.AppTheme_Indigo_300
                -10720320 -> R.style.AppTheme_Indigo_400
                -12627531 -> R.style.AppTheme_Indigo_500
                -13022805 -> R.style.AppTheme_Indigo_600
                -13615201 -> R.style.AppTheme_Indigo_700
                -14142061 -> R.style.AppTheme_Indigo_800
                -15064194 -> R.style.AppTheme_Indigo_900
                -4464901 -> R.style.AppTheme_Blue_100
                -7288071 -> R.style.AppTheme_Blue_200
                -10177034 -> R.style.AppTheme_Blue_300
                -12409355 -> R.style.AppTheme_Blue_400
                -14575885 -> R.style.AppTheme_Blue_500
                -14776091 -> R.style.AppTheme_Blue_600
                -15108398 -> R.style.AppTheme_Blue_700
                -15374912 -> R.style.AppTheme_Blue_800
                -15906911 -> R.style.AppTheme_Blue_900
                -4987396 -> R.style.AppTheme_Light_Blue_100
                -8268550 -> R.style.AppTheme_Light_Blue_200
                -11549705 -> R.style.AppTheme_Light_Blue_300
                -14043396 -> R.style.AppTheme_Light_Blue_400
                -16537100 -> R.style.AppTheme_Light_Blue_500
                -16540699 -> R.style.AppTheme_Light_Blue_600
                -16611119 -> R.style.AppTheme_Light_Blue_700
                -16615491 -> R.style.AppTheme_Light_Blue_800
                -16689253 -> R.style.AppTheme_Light_Blue_900
                -5051406 -> R.style.AppTheme_Cyan_100
                -8331542 -> R.style.AppTheme_Cyan_200
                -11677471 -> R.style.AppTheme_Cyan_300
                -14235942 -> R.style.AppTheme_Cyan_400
                -16728876 -> R.style.AppTheme_Cyan_500
                -16732991 -> R.style.AppTheme_Cyan_600
                -16738393 -> R.style.AppTheme_Cyan_700
                -16743537 -> R.style.AppTheme_Cyan_800
                -16752540 -> R.style.AppTheme_Cyan_900
                -5054501 -> R.style.AppTheme_Teal_100
                -8336444 -> R.style.AppTheme_Teal_200
                -11684180 -> R.style.AppTheme_Teal_300
                -14244198 -> R.style.AppTheme_Teal_400
                -16738680 -> R.style.AppTheme_Teal_500
                -16742021 -> R.style.AppTheme_Teal_600
                -16746133 -> R.style.AppTheme_Teal_700
                -16750244 -> R.style.AppTheme_Teal_800
                -16757440 -> R.style.AppTheme_Teal_900
                -3610935 -> R.style.AppTheme_Green_100
                -5908825 -> R.style.AppTheme_Green_200
                -8271996 -> R.style.AppTheme_Green_300
                -10044566 -> R.style.AppTheme_Green_400
                -11751600 -> R.style.AppTheme_Green_500
                -12345273 -> R.style.AppTheme_Green_600
                -13070788 -> R.style.AppTheme_Green_700
                -13730510 -> R.style.AppTheme_Green_800
                -14983648 -> R.style.AppTheme_Green_900
                -2298424 -> R.style.AppTheme_Light_Green_100
                -3808859 -> R.style.AppTheme_Light_Green_200
                -5319295 -> R.style.AppTheme_Light_Green_300
                -6501275 -> R.style.AppTheme_Light_Green_400
                -7617718 -> R.style.AppTheme_Light_Green_500
                -8604862 -> R.style.AppTheme_Light_Green_600
                -9920712 -> R.style.AppTheme_Light_Green_700
                -11171025 -> R.style.AppTheme_Light_Green_800
                -13407970 -> R.style.AppTheme_Light_Green_900
                -985917 -> R.style.AppTheme_Lime_100
                -1642852 -> R.style.AppTheme_Lime_200
                -2300043 -> R.style.AppTheme_Lime_300
                -2825897 -> R.style.AppTheme_Lime_400
                -3285959 -> R.style.AppTheme_Lime_500
                -4142541 -> R.style.AppTheme_Lime_600
                -5983189 -> R.style.AppTheme_Lime_700
                -6382300 -> R.style.AppTheme_Lime_800
                -8227049 -> R.style.AppTheme_Lime_900
                -1596 -> R.style.AppTheme_Yellow_100
                -2672 -> R.style.AppTheme_Yellow_200
                -3722 -> R.style.AppTheme_Yellow_300
                -4520 -> R.style.AppTheme_Yellow_400
                -5317 -> R.style.AppTheme_Yellow_500
                -141259 -> R.style.AppTheme_Yellow_600
                -278483 -> R.style.AppTheme_Yellow_700
                -415707 -> R.style.AppTheme_Yellow_800
                -688361 -> R.style.AppTheme_Yellow_900

                -4941 -> R.style.AppTheme_Amber_100
                -8062 -> R.style.AppTheme_Amber_200
                -10929 -> R.style.AppTheme_Amber_300
                -13784 -> R.style.AppTheme_Amber_400
                -16121 -> R.style.AppTheme_Amber_500
                -19712 -> R.style.AppTheme_Amber_600
                -24576 -> R.style.AppTheme_Amber_700
                -28928 -> R.style.AppTheme_Amber_800
                -37120 -> R.style.AppTheme_Amber_900
                -8014 -> R.style.AppTheme_Orange_100
                -13184 -> R.style.AppTheme_Orange_200
                -18611 -> R.style.AppTheme_Orange_300
                -22746 -> R.style.AppTheme_Orange_400
                -26624 -> R.style.AppTheme_Orange_500
                -291840 -> R.style.AppTheme_Orange_600
                -689152 -> R.style.AppTheme_Orange_700
                -1086464 -> R.style.AppTheme_Orange_800
                -1683200 -> R.style.AppTheme_Orange_900
                -13124 -> R.style.AppTheme_Deep_Orange_100
                -21615 -> R.style.AppTheme_Deep_Orange_200
                -30107 -> R.style.AppTheme_Deep_Orange_300
                -36797 -> R.style.AppTheme_Deep_Orange_400
                -43230 -> R.style.AppTheme_Deep_Orange_500
                -765666 -> R.style.AppTheme_Deep_Orange_600
                -1684967 -> R.style.AppTheme_Deep_Orange_700
                -2604267 -> R.style.AppTheme_Deep_Orange_800
                -4246004 -> R.style.AppTheme_Deep_Orange_900
                -2634552 -> R.style.AppTheme_Brown_100
                -4412764 -> R.style.AppTheme_Brown_200
                -6190977 -> R.style.AppTheme_Brown_300
                -7508381 -> R.style.AppTheme_Brown_400
                -8825528 -> R.style.AppTheme_Brown_500
                -9614271 -> R.style.AppTheme_Brown_600
                -10665929 -> R.style.AppTheme_Brown_700
                -11652050 -> R.style.AppTheme_Brown_800
                -12703965 -> R.style.AppTheme_Brown_900
                -3155748 -> R.style.AppTheme_Blue_Grey_100
                -5194811 -> R.style.AppTheme_Blue_Grey_200
                -7297874 -> R.style.AppTheme_Blue_Grey_300
                -8875876 -> R.style.AppTheme_Blue_Grey_400
                -10453621 -> R.style.AppTheme_Blue_Grey_500
                -11243910 -> R.style.AppTheme_Blue_Grey_600
                -12232092 -> R.style.AppTheme_Blue_Grey_700
                -13154481 -> R.style.AppTheme_Blue_Grey_800
                -14273992 -> R.style.AppTheme_Blue_Grey_900
                -1 -> R.style.AppTheme_Grey_100
                -1118482 -> R.style.AppTheme_Grey_200
                -2039584 -> R.style.AppTheme_Grey_300
                -4342339 -> R.style.AppTheme_Grey_400
                -6381922 -> R.style.AppTheme_Grey_500
                -9079435 -> R.style.AppTheme_Grey_600
                -10395295 -> R.style.AppTheme_Grey_700
                -12434878 -> R.style.AppTheme_Grey_800
                -16777216 -> R.style.AppTheme_Grey_900

                else -> R.style.AppTheme_Orange_700
            }
        }
    }
