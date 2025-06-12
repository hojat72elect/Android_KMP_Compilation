package ca.hojat.smart.musicplayer.shared.data

import android.view.ActionMode

abstract class MyActionModeCallback : ActionMode.Callback {
    var isSelectable = false
}
