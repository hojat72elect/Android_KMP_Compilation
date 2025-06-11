package ca.hojat.gamehub.core.extensions

import android.content.res.Configuration

val Configuration.isDarkThemeEnabled: Boolean
    get() = ((uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
