

package com.duckduckgo.mobile.android.vpn.breakage

data class InstalledApp(
    val packageName: String,
    val name: String,
    val isSelected: Boolean = false,
)

object ReportBreakageAppListView {
    data class State(
        val installedApps: List<InstalledApp>,
        val canSubmit: Boolean,
    )

    sealed class Command {
        data class LaunchBreakageForm(val selectedApp: InstalledApp) : Command()
        data class SendBreakageInfo(val issueReport: IssueReport) : Command()
    }
}
