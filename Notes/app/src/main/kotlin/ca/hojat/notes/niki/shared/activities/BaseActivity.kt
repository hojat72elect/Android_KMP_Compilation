package ca.hojat.notes.niki.shared.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ScrollingView
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.feature_about.AboutActivity
import ca.hojat.notes.niki.feature_settings.CustomizationActivity
import ca.hojat.notes.niki.shared.data.models.Android30RenameFormat
import ca.hojat.notes.niki.shared.data.models.FAQItem
import ca.hojat.notes.niki.shared.data.models.FileDirItem
import ca.hojat.notes.niki.shared.data.models.Release
import ca.hojat.notes.niki.shared.dialogs.ConfirmationAdvancedDialog
import ca.hojat.notes.niki.shared.dialogs.ConfirmationDialog
import ca.hojat.notes.niki.shared.dialogs.WhatsNewDialog
import ca.hojat.notes.niki.shared.dialogs.WritePermissionDialog
import ca.hojat.notes.niki.shared.dialogs.WritePermissionDialog.WritePermissionDialogMode
import ca.hojat.notes.niki.shared.extensions.addBit
import ca.hojat.notes.niki.shared.extensions.adjustAlpha
import ca.hojat.notes.niki.shared.extensions.applyColorFilter
import ca.hojat.notes.niki.shared.extensions.baseConfig
import ca.hojat.notes.niki.shared.extensions.buildDocumentUriSdk30
import ca.hojat.notes.niki.shared.extensions.canManageMedia
import ca.hojat.notes.niki.shared.extensions.createAndroidDataOrObbPath
import ca.hojat.notes.niki.shared.extensions.createAndroidDataOrObbUri
import ca.hojat.notes.niki.shared.extensions.createDirectorySync
import ca.hojat.notes.niki.shared.extensions.createFirstParentTreeUri
import ca.hojat.notes.niki.shared.extensions.createFirstParentTreeUriUsingRootTree
import ca.hojat.notes.niki.shared.extensions.deleteAndroidSAFDirectory
import ca.hojat.notes.niki.shared.extensions.deleteDocumentWithSAFSdk30
import ca.hojat.notes.niki.shared.extensions.deleteFromMediaStore
import ca.hojat.notes.niki.shared.extensions.doesThisOrParentHaveNoMedia
import ca.hojat.notes.niki.shared.extensions.getAndroidTreeUri
import ca.hojat.notes.niki.shared.extensions.getAppIconColors
import ca.hojat.notes.niki.shared.extensions.getColoredDrawableWithColor
import ca.hojat.notes.niki.shared.extensions.getColoredMaterialStatusBarColor
import ca.hojat.notes.niki.shared.extensions.getContrastColor
import ca.hojat.notes.niki.shared.extensions.getDoesFilePathExist
import ca.hojat.notes.niki.shared.extensions.getFileInputStreamSync
import ca.hojat.notes.niki.shared.extensions.getFileOutputStreamSync
import ca.hojat.notes.niki.shared.extensions.getFileUrisFromFileDirItems
import ca.hojat.notes.niki.shared.extensions.getFilenameFromPath
import ca.hojat.notes.niki.shared.extensions.getFirstParentLevel
import ca.hojat.notes.niki.shared.extensions.getFirstParentPath
import ca.hojat.notes.niki.shared.extensions.getIntValue
import ca.hojat.notes.niki.shared.extensions.getIsPathDirectory
import ca.hojat.notes.niki.shared.extensions.getLongValue
import ca.hojat.notes.niki.shared.extensions.getMimeType
import ca.hojat.notes.niki.shared.extensions.getParentPath
import ca.hojat.notes.niki.shared.extensions.getPermissionString
import ca.hojat.notes.niki.shared.extensions.getProperBackgroundColor
import ca.hojat.notes.niki.shared.extensions.getProperStatusBarColor
import ca.hojat.notes.niki.shared.extensions.getSomeDocumentFile
import ca.hojat.notes.niki.shared.extensions.getThemeId
import ca.hojat.notes.niki.shared.extensions.hasPermission
import ca.hojat.notes.niki.shared.extensions.hasProperStoredAndroidTreeUri
import ca.hojat.notes.niki.shared.extensions.hasProperStoredDocumentUriSdk30
import ca.hojat.notes.niki.shared.extensions.hasProperStoredFirstParentUri
import ca.hojat.notes.niki.shared.extensions.hasProperStoredTreeUri
import ca.hojat.notes.niki.shared.extensions.hideKeyboard
import ca.hojat.notes.niki.shared.extensions.humanizePath
import ca.hojat.notes.niki.shared.extensions.internalStoragePath
import ca.hojat.notes.niki.shared.extensions.isAccessibleWithSAFSdk30
import ca.hojat.notes.niki.shared.extensions.isAppInstalledOnSDCard
import ca.hojat.notes.niki.shared.extensions.isPathOnInternalStorage
import ca.hojat.notes.niki.shared.extensions.isPathOnOTG
import ca.hojat.notes.niki.shared.extensions.isPathOnSD
import ca.hojat.notes.niki.shared.extensions.isRecycleBinPath
import ca.hojat.notes.niki.shared.extensions.isRestrictedSAFOnlyRoot
import ca.hojat.notes.niki.shared.extensions.isRestrictedWithSAFSdk30
import ca.hojat.notes.niki.shared.extensions.isSDCardSetAsDefaultStorage
import ca.hojat.notes.niki.shared.extensions.isUsingGestureNavigation
import ca.hojat.notes.niki.shared.extensions.navigationBarHeight
import ca.hojat.notes.niki.shared.extensions.needsStupidWritePermissions
import ca.hojat.notes.niki.shared.extensions.onApplyWindowInsets
import ca.hojat.notes.niki.shared.extensions.removeBit
import ca.hojat.notes.niki.shared.extensions.renameAndroidSAFDocument
import ca.hojat.notes.niki.shared.extensions.renameDocumentSdk30
import ca.hojat.notes.niki.shared.extensions.rescanAndDeletePath
import ca.hojat.notes.niki.shared.extensions.rescanPath
import ca.hojat.notes.niki.shared.extensions.rescanPaths
import ca.hojat.notes.niki.shared.extensions.scanPathRecursively
import ca.hojat.notes.niki.shared.extensions.scanPathsRecursively
import ca.hojat.notes.niki.shared.extensions.showErrorToast
import ca.hojat.notes.niki.shared.extensions.statusBarHeight
import ca.hojat.notes.niki.shared.extensions.storeAndroidTreeUri
import ca.hojat.notes.niki.shared.extensions.toFileDirItem
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.extensions.trySAFFileDelete
import ca.hojat.notes.niki.shared.extensions.updateInMediaStore
import ca.hojat.notes.niki.shared.extensions.updateLastModified
import ca.hojat.notes.niki.shared.extensions.updateOTGPathFromPartition
import ca.hojat.notes.niki.shared.extensions.writeLn
import ca.hojat.notes.niki.shared.helpers.APP_FAQ
import ca.hojat.notes.niki.shared.helpers.APP_ICON_IDS
import ca.hojat.notes.niki.shared.helpers.APP_LAUNCHER_NAME
import ca.hojat.notes.niki.shared.helpers.APP_LICENSES
import ca.hojat.notes.niki.shared.helpers.APP_NAME
import ca.hojat.notes.niki.shared.helpers.APP_VERSION_NAME
import ca.hojat.notes.niki.shared.helpers.CREATE_DOCUMENT_SDK_30
import ca.hojat.notes.niki.shared.helpers.DARK_GREY
import ca.hojat.notes.niki.shared.helpers.EXTERNAL_STORAGE_PROVIDER_AUTHORITY
import ca.hojat.notes.niki.shared.helpers.EXTRA_SHOW_ADVANCED
import ca.hojat.notes.niki.shared.helpers.HIGHER_ALPHA
import ca.hojat.notes.niki.shared.helpers.MEDIUM_ALPHA
import ca.hojat.notes.niki.shared.helpers.MyContextWrapper
import ca.hojat.notes.niki.shared.helpers.NavigationIcon
import ca.hojat.notes.niki.shared.helpers.OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
import ca.hojat.notes.niki.shared.helpers.OPEN_DOCUMENT_TREE_FOR_SDK_30
import ca.hojat.notes.niki.shared.helpers.OPEN_DOCUMENT_TREE_OTG
import ca.hojat.notes.niki.shared.helpers.OPEN_DOCUMENT_TREE_SD
import ca.hojat.notes.niki.shared.helpers.SD_OTG_SHORT
import ca.hojat.notes.niki.shared.helpers.SELECT_EXPORT_SETTINGS_FILE_INTENT
import ca.hojat.notes.niki.shared.helpers.SHOW_FAQ_BEFORE_MAIL
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.helpers.isOreoPlus
import ca.hojat.notes.niki.shared.helpers.isQPlus
import ca.hojat.notes.niki.shared.helpers.isRPlus
import ca.hojat.notes.niki.shared.helpers.isTiramisuPlus
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.regex.Pattern

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
open class BaseActivity : AppCompatActivity() {
    private var materialScrollColorAnimation: ValueAnimator? = null
    var copyMoveCallback: ((destinationPath: String) -> Unit)? = null
    private var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    private var isAskingPermissions = false
    var useDynamicTheme = true
    private var showTransparentTop = false
    var isMaterialActivity =
        false      // by material activity we mean translucent navigation bar and opaque status and action bars.
    private var checkedDocumentPath = ""
    private var currentScrollY = 0
    private var configItemsToExport = LinkedHashMap<String, Any>()

    private var mainCoordinatorLayout: CoordinatorLayout? = null
    private var nestedView: View? = null
    private var scrollingView: ScrollingView? = null
    private var toolbar: Toolbar? = null
    private var useTransparentNavigation = false
    private var useTopSearchMenu = false


    companion object {
        var funAfterSAFPermission: ((success: Boolean) -> Unit)? = null
        var funAfterSdk30Action: ((success: Boolean) -> Unit)? = null
        var funAfterUpdate30File: ((success: Boolean) -> Unit)? = null
        var funAfterTrash30File: ((success: Boolean) -> Unit)? = null
        var funRecoverableSecurity: ((success: Boolean) -> Unit)? = null
        var funAfterManageMediaPermission: (() -> Unit)? = null

        private const val GENERIC_PERM_HANDLER = 100
        private const val DELETE_FILE_SDK_30_HANDLER = 300
        private const val RECOVERABLE_SECURITY_HANDLER = 301
        private const val UPDATE_FILE_SDK_30_HANDLER = 302
        private const val MANAGE_MEDIA_RC = 303
        private const val TRASH_FILE_SDK_30_HANDLER = 304
    }

    open fun getAppIconIDs() = arrayListOf(R.mipmap.ic_launcher)

    open fun getAppLauncherName() = getString(R.string.app_launcher_name)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (useDynamicTheme) {
            setTheme(getThemeId(showTransparentTop = showTransparentTop))
        }

        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        if (useDynamicTheme) {
            setTheme(getThemeId(showTransparentTop = showTransparentTop))

            val backgroundColor = if (baseConfig.isUsingSystemTheme) {
                resources.getColor(R.color.you_background_color, theme)
            } else {
                baseConfig.backgroundColor
            }

            updateBackgroundColor(backgroundColor)
        }

        if (showTransparentTop) {
            window.statusBarColor = Color.TRANSPARENT
        } else if (!isMaterialActivity) {
            val color = if (baseConfig.isUsingSystemTheme) {
                resources.getColor(R.color.you_status_bar_color)
            } else {
                getProperStatusBarColor()
            }

            updateActionbarColor(color)
        }

        updateRecentsAppIcon()

        var navBarColor = getProperBackgroundColor()
        if (isMaterialActivity) {
            navBarColor = navBarColor.adjustAlpha(HIGHER_ALPHA)
        }

        updateNavigationBarColor(navBarColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        funAfterSAFPermission = null
        actionOnPermission = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleNavigationAndScrolling()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                finish()
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        if (newBase.baseConfig.useEnglish && !isTiramisuPlus()) {
            super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, "en"))
        } else {
            super.attachBaseContext(newBase)
        }
    }

    fun updateBackgroundColor(color: Int = baseConfig.backgroundColor) {
        window.decorView.setBackgroundColor(color)
    }

    private fun updateStatusBarColor(color: Int) {
        window.statusBarColor = color

        if (color.getContrastColor() == DARK_GREY) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

    fun updateActionbarColor(color: Int = getProperStatusBarColor()) {
        updateStatusBarColor(color)
        setTaskDescription(ActivityManager.TaskDescription(null, null, color))
    }

    private fun updateNavigationBarColor(color: Int) {
        window.navigationBarColor = color
        updateNavigationBarButtons(color)
    }

    private fun updateNavigationBarButtons(color: Int) {
        if (isOreoPlus()) {
            if (color.getContrastColor() == DARK_GREY) {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
            } else {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
            }
        }
    }

    // use translucent navigation bar, set the background color to action and status bars
    fun updateMaterialActivityViews(
        mainCoordinatorLayout: CoordinatorLayout?,
        nestedView: View?,
        useTransparentNavigation: Boolean,
        useTopSearchMenu: Boolean,
    ) {
        this.mainCoordinatorLayout = mainCoordinatorLayout
        this.nestedView = nestedView
        this.useTransparentNavigation = useTransparentNavigation
        this.useTopSearchMenu = useTopSearchMenu
        handleNavigationAndScrolling()

        val backgroundColor = getProperBackgroundColor()
        updateStatusBarColor(backgroundColor)
        updateActionbarColor(backgroundColor)
    }

    private fun handleNavigationAndScrolling() {
        if (useTransparentNavigation) {
            if (navigationBarHeight > 0 || isUsingGestureNavigation()) {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                updateTopBottomInsets(statusBarHeight, navigationBarHeight)
                // Don't touch this. Window Inset API often has a domino effect and things will most likely break.
                onApplyWindowInsets {
                    val insets =
                        it.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
                    updateTopBottomInsets(insets.top, insets.bottom)
                }
            } else {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                updateTopBottomInsets(0, 0)
            }
        }
    }

    private fun updateTopBottomInsets(top: Int, bottom: Int) {
        nestedView?.run {
            setPadding(paddingLeft, paddingTop, paddingRight, bottom)
        }
        (mainCoordinatorLayout?.layoutParams as? FrameLayout.LayoutParams)?.topMargin = top
    }

    // colorize the top toolbar and statusbar at scrolling down a bit
    fun setupMaterialScrollListener(scrollingView: ScrollingView?, toolbar: Toolbar) {
        this.scrollingView = scrollingView
        this.toolbar = toolbar
        if (scrollingView is RecyclerView) {
            scrollingView.setOnScrollChangeListener { _, _, _, _, _ ->
                val newScrollY = scrollingView.computeVerticalScrollOffset()
                scrollingChanged(newScrollY, currentScrollY)
                currentScrollY = newScrollY
            }
        } else if (scrollingView is NestedScrollView) {
            scrollingView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                scrollingChanged(scrollY, oldScrollY)
            }
        }
    }

    private fun scrollingChanged(newScrollY: Int, oldScrollY: Int) {
        if (newScrollY > 0 && oldScrollY == 0) {
            val colorFrom = window.statusBarColor
            val colorTo = getColoredMaterialStatusBarColor()
            animateTopBarColors(colorFrom, colorTo)
        } else if (newScrollY == 0 && oldScrollY > 0) {
            val colorFrom = window.statusBarColor
            val colorTo = getRequiredStatusBarColor()
            animateTopBarColors(colorFrom, colorTo)
        }
    }

    private fun animateTopBarColors(colorFrom: Int, colorTo: Int) {
        if (toolbar == null) {
            return
        }

        materialScrollColorAnimation?.end()
        materialScrollColorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        materialScrollColorAnimation!!.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            if (toolbar != null) {
                updateTopBarColors(toolbar!!, color)
            }
        }

        materialScrollColorAnimation!!.start()
    }

    private fun getRequiredStatusBarColor(): Int {
        return if ((scrollingView is RecyclerView || scrollingView is NestedScrollView) && scrollingView?.computeVerticalScrollOffset() == 0) {
            getProperBackgroundColor()
        } else {
            getColoredMaterialStatusBarColor()
        }
    }

    fun updateTopBarColors(toolbar: Toolbar, color: Int) {
        val contrastColor = if (useTopSearchMenu) {
            getProperBackgroundColor().getContrastColor()
        } else {
            color.getContrastColor()
        }

        if (!useTopSearchMenu) {
            updateStatusBarColor(color)
            toolbar.setBackgroundColor(color)
            toolbar.setTitleTextColor(contrastColor)
            toolbar.navigationIcon?.applyColorFilter(contrastColor)
            toolbar.collapseIcon = resources.getColoredDrawableWithColor(
                R.drawable.ic_arrow_left_vector,
                contrastColor
            )
        }

        toolbar.overflowIcon =
            resources.getColoredDrawableWithColor(R.drawable.ic_three_dots_vector, contrastColor)

        val menu = toolbar.menu
        for (i in 0 until menu.size()) {
            try {
                menu.getItem(i)?.icon?.setTint(contrastColor)
            } catch (ignored: Exception) {
            }
        }
    }

    fun setupToolbar(
        toolbar: Toolbar,
        toolbarNavigationIcon: NavigationIcon = NavigationIcon.None,
        statusBarColor: Int = getRequiredStatusBarColor(),
        searchMenuItem: MenuItem? = null
    ) {
        val contrastColor = statusBarColor.getContrastColor()
        if (toolbarNavigationIcon != NavigationIcon.None) {
            val drawableId =
                if (toolbarNavigationIcon == NavigationIcon.Cross) R.drawable.ic_cross_vector else R.drawable.ic_arrow_left_vector
            toolbar.navigationIcon =
                resources.getColoredDrawableWithColor(drawableId, contrastColor)
            toolbar.setNavigationContentDescription(toolbarNavigationIcon.accessibilityResId)
        }

        toolbar.setNavigationOnClickListener {
            hideKeyboard()
            finish()
        }

        updateTopBarColors(toolbar, statusBarColor)

        if (!useTopSearchMenu) {
            searchMenuItem?.actionView?.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
                ?.apply {
                    applyColorFilter(contrastColor)
                }

            searchMenuItem?.actionView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                ?.apply {
                    setTextColor(contrastColor)
                    setHintTextColor(contrastColor.adjustAlpha(MEDIUM_ALPHA))
                    hint = "${getString(R.string.search)}…"

                    if (isQPlus()) {
                        textCursorDrawable = null
                    }
                }

            // search underline
            searchMenuItem?.actionView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
                ?.apply {
                    background.setColorFilter(contrastColor, PorterDuff.Mode.MULTIPLY)
                }
        }
    }

    private fun updateRecentsAppIcon() {
        if (baseConfig.isUsingModifiedAppIcon) {
            val appIconIDs = getAppIconIDs()
            val currentAppIconColorIndex = getCurrentAppIconColorIndex()
            if (appIconIDs.size - 1 < currentAppIconColorIndex) {
                return
            }

            val recentsIcon =
                BitmapFactory.decodeResource(resources, appIconIDs[currentAppIconColorIndex])
            val title = getAppLauncherName()
            val color = baseConfig.primaryColor

            val description = ActivityManager.TaskDescription(title, recentsIcon, color)
            setTaskDescription(description)
        }
    }

    fun updateMenuItemColors(
        menu: Menu?,
        baseColor: Int = getProperStatusBarColor(),
        forceWhiteIcons: Boolean = false
    ) {
        if (menu == null) {
            return
        }

        var color = baseColor.getContrastColor()
        if (forceWhiteIcons) {
            color = Color.WHITE
        }

        for (i in 0 until menu.size()) {
            try {
                menu.getItem(i)?.icon?.setTint(color)
            } catch (ignored: Exception) {
            }
        }
    }

    private fun getCurrentAppIconColorIndex(): Int {
        val appIconColor = baseConfig.appIconColor
        getAppIconColors().forEachIndexed { index, color ->
            if (color == appIconColor) {
                return index
            }
        }
        return 0
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        val partition = try {
            checkedDocumentPath.substring(9, 18)
        } catch (e: Exception) {
            ""
        }

        val sdOtgPattern = Pattern.compile(SD_OTG_SHORT)
        if (requestCode == CREATE_DOCUMENT_SDK_30) {
            if (resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {

                val treeUri = resultData.data
                val checkedUri = buildDocumentUriSdk30(checkedDocumentPath)

                if (treeUri != checkedUri) {
                    toast(getString(R.string.wrong_folder_selected, checkedDocumentPath))
                    return
                }

                val takeFlags =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                applicationContext.contentResolver.takePersistableUriPermission(treeUri, takeFlags)
                val funAfter = funAfterSdk30Action
                funAfterSdk30Action = null
                funAfter?.invoke(true)
            } else {
                funAfterSdk30Action?.invoke(false)
            }

        } else if (requestCode == OPEN_DOCUMENT_TREE_FOR_SDK_30) {
            if (resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
                val treeUri = resultData.data
                val checkedUri = createFirstParentTreeUri(checkedDocumentPath)

                if (treeUri != checkedUri) {
                    val level = getFirstParentLevel(checkedDocumentPath)
                    val firstParentPath = checkedDocumentPath.getFirstParentPath(this, level)
                    toast(getString(R.string.wrong_folder_selected, humanizePath(firstParentPath)))
                    return
                }

                val takeFlags =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                applicationContext.contentResolver.takePersistableUriPermission(treeUri, takeFlags)
                val funAfter = funAfterSdk30Action
                funAfterSdk30Action = null
                funAfter?.invoke(true)
            } else {
                funAfterSdk30Action?.invoke(false)
            }

        } else if (requestCode == OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB) {
            if (resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
                if (isProperAndroidRoot(checkedDocumentPath, resultData.data!!)) {
                    if (resultData.dataString == baseConfig.otgTreeUri || resultData.dataString == baseConfig.sdTreeUri) {
                        val pathToSelect = createAndroidDataOrObbPath(checkedDocumentPath)
                        toast(getString(R.string.wrong_folder_selected, pathToSelect))
                        return
                    }

                    val treeUri = resultData.data
                    storeAndroidTreeUri(checkedDocumentPath, treeUri.toString())

                    val takeFlags =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    applicationContext.contentResolver.takePersistableUriPermission(
                        treeUri!!,
                        takeFlags
                    )
                    funAfterSAFPermission?.invoke(true)
                    funAfterSAFPermission = null
                } else {
                    toast(
                        getString(
                            R.string.wrong_folder_selected,
                            createAndroidDataOrObbPath(checkedDocumentPath)
                        )
                    )
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        if (isRPlus()) {
                            putExtra(
                                DocumentsContract.EXTRA_INITIAL_URI,
                                createAndroidDataOrObbUri(checkedDocumentPath)
                            )
                        }

                        try {
                            startActivityForResult(this, requestCode)
                        } catch (e: Exception) {
                            showErrorToast(e)
                        }
                    }
                }
            } else {
                funAfterSAFPermission?.invoke(false)
            }
        } else if (requestCode == OPEN_DOCUMENT_TREE_SD) {
            if (resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
                val isProperPartition = partition.isEmpty() || !sdOtgPattern.matcher(partition)
                    .matches() || (sdOtgPattern.matcher(partition)
                    .matches() && resultData.dataString!!.contains(partition))
                if (isProperSDRootFolder(resultData.data!!) && isProperPartition) {
                    if (resultData.dataString == baseConfig.otgTreeUri) {
                        toast(R.string.sd_card_usb_same)
                        return
                    }

                    saveTreeUri(resultData)
                    funAfterSAFPermission?.invoke(true)
                    funAfterSAFPermission = null
                } else {
                    toast(R.string.wrong_root_selected)
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

                    try {
                        startActivityForResult(intent, requestCode)
                    } catch (e: Exception) {
                        showErrorToast(e)
                    }
                }
            } else {
                funAfterSAFPermission?.invoke(false)
            }
        } else if (requestCode == OPEN_DOCUMENT_TREE_OTG) {
            if (resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
                val isProperPartition = partition.isEmpty() || !sdOtgPattern.matcher(partition)
                    .matches() || (sdOtgPattern.matcher(partition)
                    .matches() && resultData.dataString!!.contains(partition))
                if (isProperOTGRootFolder(resultData.data!!) && isProperPartition) {
                    if (resultData.dataString == baseConfig.sdTreeUri) {
                        funAfterSAFPermission?.invoke(false)
                        toast(R.string.sd_card_usb_same)
                        return
                    }
                    baseConfig.otgTreeUri = resultData.dataString!!
                    baseConfig.otgPartition =
                        baseConfig.otgTreeUri.removeSuffix("%3A").substringAfterLast('/')
                            .trimEnd('/')
                    updateOTGPathFromPartition()

                    val takeFlags =
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    applicationContext.contentResolver.takePersistableUriPermission(
                        resultData.data!!,
                        takeFlags
                    )

                    funAfterSAFPermission?.invoke(true)
                    funAfterSAFPermission = null
                } else {
                    toast(R.string.wrong_root_selected_usb)
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

                    try {
                        startActivityForResult(intent, requestCode)
                    } catch (e: Exception) {
                        showErrorToast(e)
                    }
                }
            } else {
                funAfterSAFPermission?.invoke(false)
            }
        } else if (requestCode == SELECT_EXPORT_SETTINGS_FILE_INTENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val outputStream = contentResolver.openOutputStream(resultData.data!!)
            exportSettingsTo(outputStream, configItemsToExport)
        } else if (requestCode == DELETE_FILE_SDK_30_HANDLER) {
            funAfterSdk30Action?.invoke(resultCode == Activity.RESULT_OK)
        } else if (requestCode == RECOVERABLE_SECURITY_HANDLER) {
            funRecoverableSecurity?.invoke(resultCode == Activity.RESULT_OK)
            funRecoverableSecurity = null
        } else if (requestCode == UPDATE_FILE_SDK_30_HANDLER) {
            funAfterUpdate30File?.invoke(resultCode == Activity.RESULT_OK)
        } else if (requestCode == MANAGE_MEDIA_RC) {
            funAfterManageMediaPermission?.invoke()
        } else if (requestCode == TRASH_FILE_SDK_30_HANDLER) {
            funAfterTrash30File?.invoke(resultCode == Activity.RESULT_OK)
        }
    }

    private fun saveTreeUri(resultData: Intent) {
        val treeUri = resultData.data
        baseConfig.sdTreeUri = treeUri.toString()

        val takeFlags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        applicationContext.contentResolver.takePersistableUriPermission(treeUri!!, takeFlags)
    }

    private fun isProperSDRootFolder(uri: Uri) =
        isExternalStorageDocument(uri) && isRootUri(uri) && !isInternalStorage(uri)

    private fun isProperSDFolder(uri: Uri) =
        isExternalStorageDocument(uri) && !isInternalStorage(uri)

    private fun isProperOTGRootFolder(uri: Uri) =
        isExternalStorageDocument(uri) && isRootUri(uri) && !isInternalStorage(uri)

    private fun isProperOTGFolder(uri: Uri) =
        isExternalStorageDocument(uri) && !isInternalStorage(uri)

    private fun isRootUri(uri: Uri) = uri.lastPathSegment?.endsWith(":") ?: false

    private fun isInternalStorage(uri: Uri) =
        isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri)
            .contains("primary")

    private fun isAndroidDir(uri: Uri) =
        isExternalStorageDocument(uri) && DocumentsContract.getTreeDocumentId(uri)
            .contains(":Android")

    private fun isInternalStorageAndroidDir(uri: Uri) = isInternalStorage(uri) && isAndroidDir(uri)
    private fun isOTGAndroidDir(uri: Uri) = isProperOTGFolder(uri) && isAndroidDir(uri)
    private fun isSDAndroidDir(uri: Uri) = isProperSDFolder(uri) && isAndroidDir(uri)
    private fun isExternalStorageDocument(uri: Uri) =
        EXTERNAL_STORAGE_PROVIDER_AUTHORITY == uri.authority

    private fun isProperAndroidRoot(path: String, uri: Uri): Boolean {
        return when {
            isPathOnOTG(path) -> isOTGAndroidDir(uri)
            isPathOnSD(path) -> isSDAndroidDir(uri)
            else -> isInternalStorageAndroidDir(uri)
        }
    }

    fun startAboutActivity(
        appNameId: Int,
        licenseMask: Long,
        versionName: String,
        faqItems: ArrayList<FAQItem>,
        showFAQBeforeMail: Boolean
    ) {
        hideKeyboard()
        Intent(applicationContext, AboutActivity::class.java).apply {
            putExtra(APP_ICON_IDS, getAppIconIDs())
            putExtra(APP_LAUNCHER_NAME, getAppLauncherName())
            putExtra(APP_NAME, getString(appNameId))
            putExtra(APP_LICENSES, licenseMask)
            putExtra(APP_VERSION_NAME, versionName)
            putExtra(APP_FAQ, faqItems)
            putExtra(SHOW_FAQ_BEFORE_MAIL, showFAQBeforeMail)
            startActivity(this)
        }
    }

    fun startCustomizationActivity() {

        Intent(applicationContext, CustomizationActivity::class.java).apply {
            putExtra(APP_ICON_IDS, getAppIconIDs())
            putExtra(APP_LAUNCHER_NAME, getAppLauncherName())
            startActivity(this)
        }
    }

    // synchronous return value determines only if we are showing the SAF dialog, callback result tells if the SD or OTG permission has been granted
    fun handleSAFDialog(path: String, callback: (success: Boolean) -> Unit): Boolean {
        hideKeyboard()
        return if (!packageName.startsWith("com.simplemobiletools")) {
            callback(true)
            false
        } else if (isShowingSAFDialog(path) || isShowingOTGDialog(path)) {
            funAfterSAFPermission = callback
            true
        } else {
            callback(true)
            false
        }
    }

    fun handleSAFDialogSdk30(path: String, callback: (success: Boolean) -> Unit): Boolean {
        hideKeyboard()
        return if (!packageName.startsWith("com.simplemobiletools")) {
            callback(true)
            false
        } else if (isShowingSAFDialogSdk30(path)) {
            funAfterSdk30Action = callback
            true
        } else {
            callback(true)
            false
        }
    }

    private fun checkManageMediaOrHandleSAFDialogSdk30(
        path: String,
        callback: (success: Boolean) -> Unit
    ): Boolean {
        hideKeyboard()
        return if (canManageMedia()) {
            callback(true)
            false
        } else {
            handleSAFDialogSdk30(path, callback)
        }
    }

    fun handleSAFCreateDocumentDialogSdk30(
        path: String,
        callback: (success: Boolean) -> Unit
    ): Boolean {
        hideKeyboard()
        return if (!packageName.startsWith("com.simplemobiletools")) {
            callback(true)
            false
        } else if (isShowingSAFCreateDocumentDialogSdk30(path)) {
            funAfterSdk30Action = callback
            true
        } else {
            callback(true)
            false
        }
    }

    fun handleAndroidSAFDialog(path: String, callback: (success: Boolean) -> Unit): Boolean {
        hideKeyboard()
        return if (!packageName.startsWith("com.simplemobiletools")) {
            callback(true)
            false
        } else if (isShowingAndroidSAFDialog(path)) {
            funAfterSAFPermission = callback
            true
        } else {
            callback(true)
            false
        }
    }

    fun handleOTGPermission(callback: (success: Boolean) -> Unit) {
        hideKeyboard()
        if (baseConfig.otgTreeUri.isNotEmpty()) {
            callback(true)
            return
        }

        funAfterSAFPermission = callback
        WritePermissionDialog(this, WritePermissionDialogMode.Otg) {
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                try {
                    startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                    return@apply
                } catch (e: Exception) {
                    type = "*/*"
                }

                try {
                    startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                } catch (e: ActivityNotFoundException) {
                    toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                } catch (e: Exception) {
                    toast(R.string.unknown_error_occurred)
                }
            }
        }
    }

    @SuppressLint("NewApi")
    fun deleteSDK30Uris(uris: List<Uri>, callback: (success: Boolean) -> Unit) {
        hideKeyboard()
        if (isRPlus()) {
            funAfterSdk30Action = callback
            try {
                val deleteRequest =
                    MediaStore.createDeleteRequest(contentResolver, uris).intentSender
                startIntentSenderForResult(deleteRequest, DELETE_FILE_SDK_30_HANDLER, null, 0, 0, 0)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        } else {
            callback(false)
        }
    }

    @SuppressLint("NewApi")
    fun updateSDK30Uris(uris: List<Uri>, callback: (success: Boolean) -> Unit) {
        hideKeyboard()
        if (isRPlus()) {
            funAfterUpdate30File = callback
            try {
                val writeRequest = MediaStore.createWriteRequest(contentResolver, uris).intentSender
                startIntentSenderForResult(writeRequest, UPDATE_FILE_SDK_30_HANDLER, null, 0, 0, 0)
            } catch (e: Exception) {
                showErrorToast(e)
            }
        } else {
            callback(false)
        }
    }

    @SuppressLint("DefaultLocale")
    fun getAlternativeFile(file: File): File {
        var fileIndex = 1
        var newFile: File?
        do {
            val newName =
                String.format("%s(%d).%s", file.nameWithoutExtension, fileIndex, file.extension)
            newFile = File(file.parent, newName)
            fileIndex++
        } while (getDoesFilePathExist(newFile!!.absolutePath))
        return newFile
    }

    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermission = null
        if (hasPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissions = true
            actionOnPermission = callback
            ActivityCompat.requestPermissions(
                this,
                arrayOf(getPermissionString(permissionId)),
                GENERIC_PERM_HANDLER
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissions = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermission?.invoke(grantResults[0] == 0)
        }
    }

    fun checkAppOnSDCard() {
        if (!baseConfig.wasAppOnSDShown && isAppInstalledOnSDCard()) {
            baseConfig.wasAppOnSDShown = true
            ConfirmationDialog(this, "", R.string.app_on_sd_card, R.string.ok, 0) {}
        }
    }

    private fun exportSettingsTo(
        outputStream: OutputStream?,
        configItems: LinkedHashMap<String, Any>
    ) {
        if (outputStream == null) {
            toast(R.string.unknown_error_occurred)
            return
        }

        ensureBackgroundThread {
            outputStream.bufferedWriter().use { out ->
                for ((key, value) in configItems) {
                    out.writeLn("$key=$value")
                }
            }

            toast(R.string.settings_exported_successfully)
        }
    }

    private fun isShowingSAFDialog(path: String): Boolean {
        return if ((!isRPlus() && isPathOnSD(path) && !isSDCardSetAsDefaultStorage() && (baseConfig.sdTreeUri.isEmpty() || !hasProperStoredTreeUri(
                false
            )))
        ) {
            runOnUiThread {
                if (!isDestroyed && !isFinishing) {
                    WritePermissionDialog(
                        this,
                        WritePermissionDialogMode.SdCard
                    ) {
                        Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                            putExtra(EXTRA_SHOW_ADVANCED, true)
                            try {
                                startActivityForResult(this, OPEN_DOCUMENT_TREE_SD)
                                checkedDocumentPath = path
                                return@apply
                            } catch (e: Exception) {
                                type = "*/*"
                            }

                            try {
                                startActivityForResult(this, OPEN_DOCUMENT_TREE_SD)
                                checkedDocumentPath = path
                            } catch (e: ActivityNotFoundException) {
                                toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                            } catch (e: Exception) {
                                toast(R.string.unknown_error_occurred)
                            }
                        }
                    }
                }
            }
            true
        } else {
            false
        }
    }

    @SuppressLint("InlinedApi")
    fun isShowingSAFDialogSdk30(path: String): Boolean {
        return if (isAccessibleWithSAFSdk30(path) && !hasProperStoredFirstParentUri(path)) {
            runOnUiThread {
                if (!isDestroyed && !isFinishing) {
                    val level = getFirstParentLevel(path)
                    WritePermissionDialog(
                        this,
                        WritePermissionDialogMode.OpenDocumentTreeSDK30(
                            path.getFirstParentPath(
                                this,
                                level
                            )
                        )
                    ) {
                        Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                            putExtra(EXTRA_SHOW_ADVANCED, true)
                            putExtra(
                                DocumentsContract.EXTRA_INITIAL_URI,
                                createFirstParentTreeUriUsingRootTree(path)
                            )
                            try {
                                startActivityForResult(this, OPEN_DOCUMENT_TREE_FOR_SDK_30)
                                checkedDocumentPath = path
                                return@apply
                            } catch (e: Exception) {
                                type = "*/*"
                            }

                            try {
                                startActivityForResult(this, OPEN_DOCUMENT_TREE_FOR_SDK_30)
                                checkedDocumentPath = path
                            } catch (e: ActivityNotFoundException) {
                                toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                            } catch (e: Exception) {
                                toast(R.string.unknown_error_occurred)
                            }
                        }
                    }
                }
            }
            true
        } else {
            false
        }
    }

    @SuppressLint("InlinedApi")
    fun isShowingSAFCreateDocumentDialogSdk30(path: String): Boolean {
        return if (!hasProperStoredDocumentUriSdk30(path)) {
            runOnUiThread {
                if (!isDestroyed && !isFinishing) {
                    WritePermissionDialog(
                        this,
                        WritePermissionDialogMode.CreateDocumentSDK30
                    ) {
                        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            type = DocumentsContract.Document.MIME_TYPE_DIR
                            putExtra(EXTRA_SHOW_ADVANCED, true)
                            addCategory(Intent.CATEGORY_OPENABLE)
                            putExtra(
                                DocumentsContract.EXTRA_INITIAL_URI,
                                buildDocumentUriSdk30(path.getParentPath())
                            )
                            putExtra(Intent.EXTRA_TITLE, path.getFilenameFromPath())
                            try {
                                startActivityForResult(this, CREATE_DOCUMENT_SDK_30)
                                checkedDocumentPath = path
                                return@apply
                            } catch (e: Exception) {
                                type = "*/*"
                            }

                            try {
                                startActivityForResult(this, CREATE_DOCUMENT_SDK_30)
                                checkedDocumentPath = path
                            } catch (e: ActivityNotFoundException) {
                                toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                            } catch (e: Exception) {
                                toast(R.string.unknown_error_occurred)
                            }
                        }
                    }
                }
            }
            true
        } else {
            false
        }
    }

    private fun isShowingAndroidSAFDialog(path: String): Boolean {
        return if (isRestrictedSAFOnlyRoot(path) && (getAndroidTreeUri(path).isEmpty() || !hasProperStoredAndroidTreeUri(
                path
            ))
        ) {
            runOnUiThread {
                if (!isDestroyed && !isFinishing) {
                    ConfirmationAdvancedDialog(
                        this,
                        "",
                        R.string.confirm_storage_access_android_text,
                        R.string.ok,
                        R.string.cancel
                    ) { success ->
                        if (success) {
                            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                                putExtra(EXTRA_SHOW_ADVANCED, true)
                                putExtra(
                                    DocumentsContract.EXTRA_INITIAL_URI,
                                    createAndroidDataOrObbUri(path)
                                )
                                try {
                                    startActivityForResult(
                                        this,
                                        OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
                                    )
                                    checkedDocumentPath = path
                                    return@apply
                                } catch (e: Exception) {
                                    type = "*/*"
                                }

                                try {
                                    startActivityForResult(
                                        this,
                                        OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB
                                    )
                                    checkedDocumentPath = path
                                } catch (e: ActivityNotFoundException) {
                                    toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                                } catch (e: Exception) {
                                    toast(R.string.unknown_error_occurred)
                                }
                            }
                        }
                    }
                }
            }
            true
        } else {
            false
        }
    }


    private fun isShowingOTGDialog(path: String): Boolean {
        return if (!isRPlus() && isPathOnOTG(path) && (baseConfig.otgTreeUri.isEmpty() || !hasProperStoredTreeUri(
                true
            ))
        ) {
            showOTGPermissionDialog(path)
            true
        } else {
            false
        }
    }


    private fun showOTGPermissionDialog(path: String) {
        runOnUiThread {
            if (!isDestroyed && !isFinishing) {
                WritePermissionDialog(this, WritePermissionDialogMode.Otg) {
                    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                            checkedDocumentPath = path
                            return@apply
                        } catch (e: Exception) {
                            type = "*/*"
                        }

                        try {
                            startActivityForResult(this, OPEN_DOCUMENT_TREE_OTG)
                            checkedDocumentPath = path
                        } catch (e: ActivityNotFoundException) {
                            toast(R.string.system_service_disabled, Toast.LENGTH_LONG)
                        } catch (e: Exception) {
                            toast(R.string.unknown_error_occurred)
                        }
                    }
                }
            }
        }
    }


    fun deleteFile(
        file: FileDirItem,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        deleteFiles(arrayListOf(file), allowDeleteFolder, callback)
    }

    private fun copySingleFileSdk30(source: FileDirItem, destination: FileDirItem): Boolean {
        val directory = destination.getParentPath()
        if (!createDirectorySync(directory)) {
            val error = String.format(getString(R.string.could_not_create_folder), directory)
            showErrorToast(error)
            return false
        }

        var inputStream: InputStream? = null
        var out: OutputStream? = null
        try {

            out = getFileOutputStreamSync(destination.path, source.path.getMimeType())
            inputStream = getFileInputStreamSync(source.path)!!

            var copiedSize = 0L
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytes = inputStream.read(buffer)
            while (bytes >= 0) {
                out!!.write(buffer, 0, bytes)
                copiedSize += bytes
                bytes = inputStream.read(buffer)
            }

            out?.flush()

            return if (source.size == copiedSize && getDoesFilePathExist(destination.path)) {
                if (baseConfig.keepLastModified) {
                    copyOldLastModified(source.path, destination.path)
                    val lastModified = File(source.path).lastModified()
                    if (lastModified != 0L) {
                        File(destination.path).setLastModified(lastModified)
                    }
                }
                true
            } else {
                false
            }
        } finally {
            inputStream?.close()
            out?.close()
        }
    }

    private fun copyOldLastModified(sourcePath: String, destinationPath: String) {
        val projection =
            arrayOf(MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_MODIFIED)
        val uri = MediaStore.Files.getContentUri("external")
        val selection = "${MediaStore.MediaColumns.DATA} = ?"
        var selectionArgs = arrayOf(sourcePath)
        val cursor =
            applicationContext.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )

        cursor?.use {
            if (cursor.moveToFirst()) {
                val dateTaken = cursor.getLongValue(MediaStore.Images.Media.DATE_TAKEN)
                val dateModified = cursor.getIntValue(MediaStore.Images.Media.DATE_MODIFIED)

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DATE_TAKEN, dateTaken)
                    put(MediaStore.Images.Media.DATE_MODIFIED, dateModified)
                }

                selectionArgs = arrayOf(destinationPath)
                applicationContext.contentResolver.update(uri, values, selection, selectionArgs)
            }
        }
    }


    private fun deleteFiles(
        files: List<FileDirItem>,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        ensureBackgroundThread {
            deleteFilesBg(files, allowDeleteFolder, callback)
        }
    }


    private fun deleteFilesBg(
        files: List<FileDirItem>,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        if (files.isEmpty()) {
            runOnUiThread {
                callback?.invoke(true)
            }
            return
        }

        val firstFile = files.first()
        val firstFilePath = firstFile.path
        handleSAFDialog(firstFilePath) {
            if (!it) {
                return@handleSAFDialog
            }

            checkManageMediaOrHandleSAFDialogSdk30(firstFilePath) {
                if (!it) {
                    return@checkManageMediaOrHandleSAFDialogSdk30
                }

                val recycleBinPath = firstFile.isRecycleBinPath(this)
                if (canManageMedia() && !recycleBinPath && !firstFilePath.doesThisOrParentHaveNoMedia(
                        HashMap(), null
                    )
                ) {
                    val fileUris = getFileUrisFromFileDirItems(files)

                    deleteSDK30Uris(fileUris) { success ->
                        runOnUiThread {
                            callback?.invoke(success)
                        }
                    }
                } else {
                    deleteFilesCasual(files, allowDeleteFolder, callback)
                }
            }
        }
    }

    private fun deleteFilesCasual(
        files: List<FileDirItem>,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        var wasSuccess = false
        val failedFileDirItems = ArrayList<FileDirItem>()
        files.forEachIndexed { index, file ->
            deleteFileBg(file, allowDeleteFolder, true) {
                if (it) {
                    wasSuccess = true
                } else {
                    failedFileDirItems.add(file)
                }

                if (index == files.lastIndex) {
                    if (isRPlus() && failedFileDirItems.isNotEmpty()) {
                        val fileUris = getFileUrisFromFileDirItems(failedFileDirItems)
                        deleteSDK30Uris(fileUris) { success ->
                            runOnUiThread {
                                callback?.invoke(success)
                            }
                        }
                    } else {
                        runOnUiThread {
                            callback?.invoke(wasSuccess)
                        }
                    }
                }
            }
        }
    }

    fun deleteFileBg(
        fileDirItem: FileDirItem,
        allowDeleteFolder: Boolean = false,
        isDeletingMultipleFiles: Boolean,
        callback: ((wasSuccess: Boolean) -> Unit)? = null,
    ) {
        val path = fileDirItem.path
        if (isRestrictedSAFOnlyRoot(path)) {
            deleteAndroidSAFDirectory(path, allowDeleteFolder, callback)
        } else {
            val file = File(path)
            if (!isRPlus() && file.absolutePath.startsWith(internalStoragePath) && !file.canWrite()) {
                callback?.invoke(false)
                return
            }

            var fileDeleted =
                !isPathOnOTG(path) && ((!file.exists() && file.length() == 0L) || file.delete())
            if (fileDeleted) {
                deleteFromMediaStore(path) { needsRescan ->
                    if (needsRescan) {
                        rescanAndDeletePath(path) {
                            runOnUiThread {
                                callback?.invoke(true)
                            }
                        }
                    } else {
                        runOnUiThread {
                            callback?.invoke(true)
                        }
                    }
                }
            } else {
                if (getIsPathDirectory(file.absolutePath) && allowDeleteFolder) {
                    fileDeleted = deleteRecursively(file, this)
                }

                if (!fileDeleted) {
                    if (needsStupidWritePermissions(path)) {
                        handleSAFDialog(path) {
                            if (it) {
                                trySAFFileDelete(fileDirItem, allowDeleteFolder, callback)
                            }
                        }
                    } else if (isAccessibleWithSAFSdk30(path)) {
                        if (canManageMedia()) {
                            deleteSdk30(fileDirItem, callback)
                        } else {
                            handleSAFDialogSdk30(path) {
                                if (it) {
                                    deleteDocumentWithSAFSdk30(
                                        fileDirItem,
                                        allowDeleteFolder,
                                        callback
                                    )
                                }
                            }
                        }
                    } else if (isRPlus() && !isDeletingMultipleFiles) {
                        deleteSdk30(fileDirItem, callback)
                    } else {
                        callback?.invoke(false)
                    }
                }
            }
        }
    }

    private fun deleteSdk30(
        fileDirItem: FileDirItem,
        callback: ((wasSuccess: Boolean) -> Unit)?
    ) {
        val fileUris = getFileUrisFromFileDirItems(arrayListOf(fileDirItem))
        deleteSDK30Uris(fileUris) { success ->
            runOnUiThread {
                callback?.invoke(success)
            }
        }
    }

    fun checkWhatsNew(releases: List<Release>, currVersion: Int) {
        if (baseConfig.lastVersion == 0) {
            baseConfig.lastVersion = currVersion
            return
        }

        val newReleases = arrayListOf<Release>()
        releases.filterTo(newReleases) { it.id > baseConfig.lastVersion }

        if (newReleases.isNotEmpty()) {
            WhatsNewDialog(this, newReleases)
        }

        baseConfig.lastVersion = currVersion
    }

    fun renameFile(
        oldPath: String,
        newPath: String,
        isRenamingMultipleFiles: Boolean,
        callback: ((success: Boolean, android30RenameFormat: Android30RenameFormat) -> Unit)? = null
    ) {
        if (isRestrictedSAFOnlyRoot(oldPath)) {
            handleAndroidSAFDialog(oldPath) {
                if (!it) {
                    runOnUiThread {
                        callback?.invoke(false, Android30RenameFormat.NONE)
                    }
                    return@handleAndroidSAFDialog
                }

                try {
                    ensureBackgroundThread {
                        val success = renameAndroidSAFDocument(oldPath, newPath)
                        runOnUiThread {
                            callback?.invoke(success, Android30RenameFormat.NONE)
                        }
                    }
                } catch (e: Exception) {
                    showErrorToast(e)
                    runOnUiThread {
                        callback?.invoke(false, Android30RenameFormat.NONE)
                    }
                }
            }
        } else if (isAccessibleWithSAFSdk30(oldPath)) {
            if (canManageMedia() && !File(oldPath).isDirectory && isPathOnInternalStorage(oldPath)) {
                renameCasually(oldPath, newPath, isRenamingMultipleFiles, callback)
            } else {
                handleSAFDialogSdk30(oldPath) {
                    if (!it) {
                        return@handleSAFDialogSdk30
                    }

                    try {
                        ensureBackgroundThread {
                            val success = renameDocumentSdk30(oldPath, newPath)
                            if (success) {
                                updateInMediaStore(oldPath, newPath)
                                rescanPath(newPath) {
                                    runOnUiThread {
                                        callback?.invoke(true, Android30RenameFormat.NONE)
                                    }
                                    if (!oldPath.equals(newPath, true)) {
                                        deleteFromMediaStore(oldPath)
                                    }
                                    scanPathRecursively(newPath)
                                }
                            } else {
                                runOnUiThread {
                                    callback?.invoke(false, Android30RenameFormat.NONE)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        showErrorToast(e)
                        runOnUiThread {
                            callback?.invoke(false, Android30RenameFormat.NONE)
                        }
                    }
                }
            }
        } else if (needsStupidWritePermissions(newPath)) {
            handleSAFDialog(newPath) {
                if (!it) {
                    return@handleSAFDialog
                }

                val document = getSomeDocumentFile(oldPath)
                if (document == null || (File(oldPath).isDirectory != document.isDirectory)) {
                    runOnUiThread {
                        toast(R.string.unknown_error_occurred)
                        callback?.invoke(false, Android30RenameFormat.NONE)
                    }
                    return@handleSAFDialog
                }

                try {
                    ensureBackgroundThread {
                        try {
                            DocumentsContract.renameDocument(
                                applicationContext.contentResolver,
                                document.uri,
                                newPath.getFilenameFromPath()
                            )
                        } catch (ignored: FileNotFoundException) {
                            // FileNotFoundException is thrown in some weird cases, but renaming works just fine
                        } catch (e: Exception) {
                            showErrorToast(e)
                            callback?.invoke(false, Android30RenameFormat.NONE)
                            return@ensureBackgroundThread
                        }

                        updateInMediaStore(oldPath, newPath)
                        rescanPaths(arrayListOf(oldPath, newPath)) {
                            if (!baseConfig.keepLastModified) {
                                updateLastModified(newPath, System.currentTimeMillis())
                            }
                            deleteFromMediaStore(oldPath)
                            runOnUiThread {
                                callback?.invoke(true, Android30RenameFormat.NONE)
                            }
                        }
                    }
                } catch (e: Exception) {
                    showErrorToast(e)
                    runOnUiThread {
                        callback?.invoke(false, Android30RenameFormat.NONE)
                    }
                }
            }
        } else renameCasually(oldPath, newPath, isRenamingMultipleFiles, callback)
    }

    private fun renameCasually(
        oldPath: String,
        newPath: String,
        isRenamingMultipleFiles: Boolean,
        callback: ((success: Boolean, android30RenameFormat: Android30RenameFormat) -> Unit)?
    ) {
        val oldFile = File(oldPath)
        val newFile = File(newPath)
        val tempFile = try {
            ca.hojat.notes.niki.shared.extensions.createTempFile(oldFile) ?: return
        } catch (exception: Exception) {
            if (isRPlus() && exception is java.nio.file.FileSystemException) {
                // if we are renaming multiple files at once, we should give the Android 30+ permission dialog all uris together, not one by one
                if (isRenamingMultipleFiles) {
                    callback?.invoke(false, Android30RenameFormat.CONTENT_RESOLVER)
                } else {
                    val fileUris =
                        getFileUrisFromFileDirItems(arrayListOf(File(oldPath).toFileDirItem(this)))
                    updateSDK30Uris(fileUris) { success ->
                        if (success) {
                            val values = ContentValues().apply {
                                put(
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    newPath.getFilenameFromPath()
                                )
                            }

                            try {
                                contentResolver.update(fileUris.first(), values, null, null)
                                callback?.invoke(true, Android30RenameFormat.NONE)
                            } catch (e: Exception) {
                                showErrorToast(e)
                                callback?.invoke(false, Android30RenameFormat.NONE)
                            }
                        } else {
                            callback?.invoke(false, Android30RenameFormat.NONE)
                        }
                    }
                }
            } else {
                if (exception is IOException && File(oldPath).isDirectory && isRestrictedWithSAFSdk30(
                        oldPath
                    )
                ) {
                    toast(R.string.cannot_rename_folder)
                } else {
                    showErrorToast(exception)
                }
                callback?.invoke(false, Android30RenameFormat.NONE)
            }
            return
        }

        val oldToTempSucceeds = oldFile.renameTo(tempFile)
        val tempToNewSucceeds = tempFile.renameTo(newFile)
        if (oldToTempSucceeds && tempToNewSucceeds) {
            if (newFile.isDirectory) {
                updateInMediaStore(oldPath, newPath)
                rescanPath(newPath) {
                    runOnUiThread {
                        callback?.invoke(true, Android30RenameFormat.NONE)
                    }
                    if (!oldPath.equals(newPath, true)) {
                        deleteFromMediaStore(oldPath)
                    }
                    scanPathRecursively(newPath)
                }
            } else {
                if (!baseConfig.keepLastModified) {
                    newFile.setLastModified(System.currentTimeMillis())
                }
                updateInMediaStore(oldPath, newPath)
                scanPathsRecursively(arrayListOf(newPath)) {
                    if (!oldPath.equals(newPath, true)) {
                        deleteFromMediaStore(oldPath)
                    }
                    runOnUiThread {
                        callback?.invoke(true, Android30RenameFormat.NONE)
                    }
                }
            }
        } else {
            tempFile.delete()
            newFile.delete()
            if (isRPlus()) {
                // if we are renaming multiple files at once, we should give the Android 30+ permission dialog all uris together, not one by one
                if (isRenamingMultipleFiles) {
                    callback?.invoke(false, Android30RenameFormat.SAF)
                } else {
                    val fileUris =
                        getFileUrisFromFileDirItems(arrayListOf(File(oldPath).toFileDirItem(this)))
                    updateSDK30Uris(fileUris) { success ->
                        if (!success) {
                            return@updateSDK30Uris
                        }
                        try {
                            val sourceUri = fileUris.first()
                            val sourceFile = File(oldPath).toFileDirItem(this)

                            if (oldPath.equals(newPath, true)) {
                                val tempDestination = try {
                                    ca.hojat.notes.niki.shared.extensions.createTempFile(
                                        File(
                                            sourceFile.path
                                        )
                                    ) ?: return@updateSDK30Uris
                                } catch (exception: Exception) {
                                    showErrorToast(exception)
                                    callback?.invoke(false, Android30RenameFormat.NONE)
                                    return@updateSDK30Uris
                                }

                                val copyTempSuccess =
                                    copySingleFileSdk30(
                                        sourceFile,
                                        tempDestination.toFileDirItem(this)
                                    )
                                if (copyTempSuccess) {
                                    contentResolver.delete(sourceUri, null)
                                    tempDestination.renameTo(File(newPath))
                                    if (!baseConfig.keepLastModified) {
                                        newFile.setLastModified(System.currentTimeMillis())
                                    }
                                    updateInMediaStore(oldPath, newPath)
                                    scanPathsRecursively(arrayListOf(newPath)) {
                                        runOnUiThread {
                                            callback?.invoke(true, Android30RenameFormat.NONE)
                                        }
                                    }
                                } else {
                                    callback?.invoke(false, Android30RenameFormat.NONE)
                                }
                            } else {
                                val destinationFile = FileDirItem(
                                    newPath,
                                    newPath.getFilenameFromPath(),
                                    sourceFile.isDirectory,
                                    sourceFile.children,
                                    sourceFile.size,
                                    sourceFile.modified
                                )
                                val copySuccessful =
                                    copySingleFileSdk30(sourceFile, destinationFile)
                                if (copySuccessful) {
                                    if (!baseConfig.keepLastModified) {
                                        newFile.setLastModified(System.currentTimeMillis())
                                    }
                                    contentResolver.delete(sourceUri, null)
                                    updateInMediaStore(oldPath, newPath)
                                    scanPathsRecursively(arrayListOf(newPath)) {
                                        runOnUiThread {
                                            callback?.invoke(true, Android30RenameFormat.NONE)
                                        }
                                    }
                                } else {
                                    toast(R.string.unknown_error_occurred)
                                    callback?.invoke(false, Android30RenameFormat.NONE)
                                }
                            }

                        } catch (e: Exception) {
                            showErrorToast(e)
                            callback?.invoke(false, Android30RenameFormat.NONE)
                        }
                    }
                }
            } else {
                toast(R.string.unknown_error_occurred)
                callback?.invoke(false, Android30RenameFormat.NONE)
            }
        }
    }

    private fun deleteRecursively(file: File, context: Context): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles() ?: return file.delete()
            for (child in files) {
                deleteRecursively(child, context)
            }
        }

        val deleted = file.delete()
        if (deleted) {
            context.deleteFromMediaStore(file.absolutePath)
        }
        return deleted
    }

}
