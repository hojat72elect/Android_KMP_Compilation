

package com.duckduckgo.app.fire

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.BrowserActivity
import com.duckduckgo.common.ui.view.fadeTransitionConfig
import com.duckduckgo.common.ui.view.noAnimationConfig
import com.duckduckgo.di.scopes.ActivityScope

/**
 * Activity which is responsible for killing the main process and restarting it. This Activity will automatically finish itself after a brief time.
 *
 * This Activity runs in a separate process so that it can seamlessly restart the main app process without much in the way of a jarring UX.
 *
 * The correct way to invoke this Activity is through its `triggerRestart(context)` method.
 *
 * This Activity was largely inspired by https://github.com/JakeWharton/ProcessPhoenix
 */
@InjectWith(ActivityScope::class)
class FireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent.getParcelableExtra<Intent>(KEY_RESTART_INTENTS)
        startActivity(intent, fadeTransitionConfig())
        overridePendingTransition(0, 0)
        finish()
        killProcess()
    }

    companion object {
        private const val KEY_RESTART_INTENTS = "KEY_RESTART_INTENTS"

        fun triggerRestart(
            context: Context,
            notifyDataCleared: Boolean,
            enableTransitionAnimation: Boolean = true,
        ) {
            val intent = Intent(context, FireActivity::class.java)
            val nextIntent = getRestartIntent(context, notifyDataCleared)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(KEY_RESTART_INTENTS, nextIntent)

            val transitionAnimationConfig = if (enableTransitionAnimation) {
                context.fadeTransitionConfig()
            } else {
                context.noAnimationConfig()
            }

            context.startActivity(intent, transitionAnimationConfig)
            if (context is Activity) {
                context.overridePendingTransition(0, 0)
                context.finish()
            }
            killProcess()
        }

        private fun getRestartIntent(
            context: Context,
            notifyDataCleared: Boolean = false,
        ): Intent {
            val intent = BrowserActivity.intent(context, notifyDataCleared = notifyDataCleared, isLaunchFromClearDataAction = true)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }

        private fun killProcess() {
            Runtime.getRuntime().exit(0)
        }
    }
}
