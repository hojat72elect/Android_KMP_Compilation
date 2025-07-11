

package com.duckduckgo.sync.impl.ui.setup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.duckduckgo.sync.impl.ui.SyncWithAnotherDeviceActivity
import com.duckduckgo.sync.impl.ui.SyncWithAnotherDeviceActivity.Companion.EXTRA_USER_SWITCHED_ACCOUNT
import com.duckduckgo.sync.impl.ui.setup.SyncWithAnotherDeviceContract.SyncWithAnotherDeviceContractOutput

internal class SyncWithAnotherDeviceContract : ActivityResultContract<Void?, SyncWithAnotherDeviceContractOutput>() {
    override fun createIntent(
        context: Context,
        input: Void?,
    ): Intent {
        return SyncWithAnotherDeviceActivity.intent(context)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): SyncWithAnotherDeviceContractOutput {
        when {
            resultCode == Activity.RESULT_OK -> {
                val userSwitchedAccount = intent?.getBooleanExtra(EXTRA_USER_SWITCHED_ACCOUNT, false) ?: false
                return if (userSwitchedAccount) {
                    SyncWithAnotherDeviceContractOutput.SwitchAccountSuccess
                } else {
                    SyncWithAnotherDeviceContractOutput.DeviceConnected
                }
            }
            else -> return SyncWithAnotherDeviceContractOutput.Error
        }
    }

    sealed class SyncWithAnotherDeviceContractOutput {
        data object DeviceConnected : SyncWithAnotherDeviceContractOutput()
        data object SwitchAccountSuccess : SyncWithAnotherDeviceContractOutput()
        data object Error : SyncWithAnotherDeviceContractOutput()
    }
}
