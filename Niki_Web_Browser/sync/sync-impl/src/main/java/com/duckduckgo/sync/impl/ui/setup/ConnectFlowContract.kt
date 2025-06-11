

package com.duckduckgo.sync.impl.ui.setup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.duckduckgo.sync.impl.ui.SyncConnectActivity

internal class ConnectFlowContract : ActivityResultContract<ConnectFlowContractInput, Boolean>() {
    override fun createIntent(
        context: Context,
        input: ConnectFlowContractInput,
    ): Intent {
        return SyncConnectActivity.intent(context, input.launchSource)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}

internal data class ConnectFlowContractInput(val launchSource: String? = null)
