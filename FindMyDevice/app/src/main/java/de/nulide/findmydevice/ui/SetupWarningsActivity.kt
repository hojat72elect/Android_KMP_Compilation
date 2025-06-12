package de.nulide.findmydevice.ui

import android.os.Bundle
import de.nulide.findmydevice.databinding.ActivitySetupWarningsBinding
import de.nulide.findmydevice.permissions.globalAppPermissions
import de.nulide.findmydevice.ui.UiUtil.Companion.setupEdgeToEdgeAppBar
import de.nulide.findmydevice.ui.UiUtil.Companion.setupEdgeToEdgeScrollView


class SetupWarningsActivity : FmdActivity() {

    private lateinit var viewBinding: ActivitySetupWarningsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySetupWarningsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupEdgeToEdgeAppBar(viewBinding.appBar)
        setupEdgeToEdgeScrollView(viewBinding.scrollView)

        setupPermissionsList(
            this,
            viewBinding.permissionsRequiredTitle,
            viewBinding.permissionsRequiredList,
            globalAppPermissions()
        )
    }
}
