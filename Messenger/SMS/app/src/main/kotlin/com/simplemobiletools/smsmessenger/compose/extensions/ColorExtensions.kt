package com.simplemobiletools.smsmessenger.compose.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.simplemobiletools.smsmessenger.compose.theme.LocalTheme
import com.simplemobiletools.smsmessenger.compose.theme.model.Theme
import com.simplemobiletools.smsmessenger.extensions.baseConfig
import com.simplemobiletools.smsmessenger.extensions.getProperPrimaryColor

@Composable
fun linkColor(): Color {
    val theme: Theme = LocalTheme.current
    val accentColor = LocalContext.current.baseConfig.accentColor
    val primaryColor = LocalContext.current.getProperPrimaryColor()
    return onStartEventValue(keys = arrayOf(accentColor, primaryColor)) {
        Color(
            when (theme) {
                is Theme.BlackAndWhite, is Theme.White -> accentColor
                else -> primaryColor
            }
        )
    }
}
