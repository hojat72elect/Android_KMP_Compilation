

package com.duckduckgo.autofill.impl.importing.gpm.webflow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.autofill.impl.R
import com.duckduckgo.autofill.impl.databinding.ActivityImportGooglePasswordsWebflowBinding
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePassword.AutofillImportViaGooglePasswordManagerScreen
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePasswordResult.Companion.RESULT_KEY
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePasswordResult.Companion.RESULT_KEY_DETAILS
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePasswordResult.UserCancelled
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(AutofillImportViaGooglePasswordManagerScreen::class)
class ImportGooglePasswordsWebFlowActivity : DuckDuckGoActivity() {

    val binding: ActivityImportGooglePasswordsWebflowBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureResultListeners()
        launchImportFragment()
    }

    private fun launchImportFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, ImportGooglePasswordsWebFlowFragment())
        }
    }

    private fun configureResultListeners() {
        supportFragmentManager.setFragmentResultListener(RESULT_KEY, this) { _, result ->
            exitWithResult(result)
        }
    }

    private fun exitWithResult(resultBundle: Bundle) {
        setResult(RESULT_OK, Intent().putExtras(resultBundle))
        finish()
    }

    fun exitUserCancelled(stage: String) {
        val result = Bundle().apply {
            putParcelable(RESULT_KEY_DETAILS, UserCancelled(stage))
        }
        exitWithResult(result)
    }
}

object ImportGooglePassword {
    data object AutofillImportViaGooglePasswordManagerScreen : ActivityParams {
        private fun readResolve(): Any = AutofillImportViaGooglePasswordManagerScreen
    }
}
