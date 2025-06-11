

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.duckduckgo.common.ui.view.hide
import com.duckduckgo.common.ui.view.show
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.extensions.safeGetApplicationIcon
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.apps.ui.TrackingProtectionExclusionListActivity.Companion.AppsFilter
import com.duckduckgo.mobile.android.vpn.apps.ui.TrackingProtectionExclusionListActivity.Companion.AppsFilter.PROTECTED_ONLY
import com.duckduckgo.mobile.android.vpn.apps.ui.TrackingProtectionExclusionListActivity.Companion.AppsFilter.UNPROTECTED_ONLY
import com.duckduckgo.mobile.android.vpn.databinding.ViewDeviceShieldActivityAppsBinding
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.model.AppsData

class AppsProtectionStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding: ViewDeviceShieldActivityAppsBinding by viewBinding()

    fun bind(
        item: AppsData,
        onAppsFilterClick: (AppsFilter) -> Unit,
    ) {
        binding.activityApps.text = context.resources.getQuantityString(
            if (item.isProtected) R.plurals.atp_ActivityProtectedApps else R.plurals.atp_ActivityUnprotectedApps,
            item.appsCount,
            item.appsCount,
        )

        binding.activityAppsImage.setIcons(item.packageNames.mapNotNull { context.packageManager.safeGetApplicationIcon(it) })

        if (item.isProtected) {
            binding.activityAppsWarningImage.hide()
        } else {
            binding.activityAppsWarningImage.show()
        }

        setOnClickListener {
            val appsFilter = if (item.isProtected) PROTECTED_ONLY else UNPROTECTED_ONLY
            onAppsFilterClick(appsFilter)
        }
    }
}
