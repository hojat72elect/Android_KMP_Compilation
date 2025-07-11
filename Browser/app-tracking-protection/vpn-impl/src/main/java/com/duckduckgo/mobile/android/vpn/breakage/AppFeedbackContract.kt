

package com.duckduckgo.mobile.android.vpn.breakage

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.duckduckgo.browser.api.ui.BrowserScreens.FeedbackActivityWithEmptyParams
import com.duckduckgo.navigation.api.GlobalActivityStarter
import javax.inject.Inject

class AppFeedbackContract @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
) : ActivityResultContract<Void?, Boolean>() {
    override fun createIntent(
        context: Context,
        input: Void?,
    ): Intent {
        return globalActivityStarter.startIntent(context, FeedbackActivityWithEmptyParams)!!
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Boolean {
        return resultCode == RESULT_OK
    }
}
