

package com.duckduckgo.sync.impl.ui.setup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.duckduckgo.sync.impl.ui.EnterCodeActivity
import com.duckduckgo.sync.impl.ui.EnterCodeActivity.Companion.Code
import com.duckduckgo.sync.impl.ui.EnterCodeActivity.Companion.EXTRA_USER_SWITCHED_ACCOUNT
import com.duckduckgo.sync.impl.ui.setup.EnterCodeContract.EnterCodeContractOutput

class EnterCodeContract : ActivityResultContract<Code, EnterCodeContractOutput>() {
    override fun createIntent(
        context: Context,
        codeType: Code,
    ): Intent {
        return EnterCodeActivity.intent(context, codeType)
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): EnterCodeContractOutput {
        when {
            resultCode == Activity.RESULT_OK -> {
                val userSwitchedAccount = intent?.getBooleanExtra(EXTRA_USER_SWITCHED_ACCOUNT, false) ?: false
                return if (userSwitchedAccount) {
                    EnterCodeContractOutput.SwitchAccountSuccess
                } else {
                    EnterCodeContractOutput.LoginSuccess
                }
            }

            else -> return EnterCodeContractOutput.Error
        }
    }

    sealed class EnterCodeContractOutput {
        data object LoginSuccess : EnterCodeContractOutput()
        data object SwitchAccountSuccess : EnterCodeContractOutput()
        data object Error : EnterCodeContractOutput()
    }
}
