package ca.hojat.notes.niki.shared.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.extensions.checkAndBackupNotesOnBoot

@SuppressLint("UnsafeProtectedBroadcastReceiver")
class BootCompletedReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            context.apply {
                checkAndBackupNotesOnBoot()
            }
        }
    }
}
