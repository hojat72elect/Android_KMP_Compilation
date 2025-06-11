@file:JvmName("IntentUtils")

package ca.hojat.gamehub.core.extensions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun Intent.attachNewTaskFlagIfNeeded(context: Context) {
    if (context !is AppCompatActivity) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
