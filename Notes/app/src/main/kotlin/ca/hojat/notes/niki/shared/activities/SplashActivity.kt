package ca.hojat.notes.niki.shared.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.extensions.baseConfig
import ca.hojat.notes.niki.shared.extensions.checkAppIconColor
import ca.hojat.notes.niki.shared.extensions.checkAppSideloading
import ca.hojat.notes.niki.shared.extensions.getSharedTheme
import ca.hojat.notes.niki.shared.extensions.isThankYouInstalled
import ca.hojat.notes.niki.shared.extensions.isUsingSystemDarkTheme
import ca.hojat.notes.niki.shared.extensions.showSideloadingDialog
import ca.hojat.notes.niki.shared.helpers.OPEN_NOTE_ID
import ca.hojat.notes.niki.shared.helpers.SIDELOADING_TRUE
import ca.hojat.notes.niki.shared.helpers.SIDELOADING_UNCHECKED

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private fun initActivity() {
        if (intent.extras?.containsKey(OPEN_NOTE_ID) == true) {
            Intent(this, MainActivity::class.java).apply {
                putExtra(OPEN_NOTE_ID, intent.getLongExtra(OPEN_NOTE_ID, -1L))
                startActivity(this)
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (baseConfig.appSideloadingStatus == SIDELOADING_UNCHECKED) {
            if (checkAppSideloading()) {
                return
            }
        } else if (baseConfig.appSideloadingStatus == SIDELOADING_TRUE) {
            showSideloadingDialog()
            return
        }

        baseConfig.apply {
            if (isUsingAutoTheme) {
                val isUsingSystemDarkTheme = isUsingSystemDarkTheme()
                isUsingSharedTheme = false
                textColor =
                    resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_text_color else R.color.theme_light_text_color)
                backgroundColor =
                    resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_background_color else R.color.theme_light_background_color)
            }
        }

        if (!baseConfig.isUsingAutoTheme && !baseConfig.isUsingSystemTheme && isThankYouInstalled()) {
            getSharedTheme {
                if (it != null) {
                    baseConfig.apply {
                        wasSharedThemeForced = true
                        isUsingSharedTheme = true
                        wasSharedThemeEverActivated = true

                        textColor = it.textColor
                        backgroundColor = it.backgroundColor
                        primaryColor = it.primaryColor
                        accentColor = it.accentColor
                    }

                    if (baseConfig.appIconColor != it.appIconColor) {
                        baseConfig.appIconColor = it.appIconColor
                        checkAppIconColor()
                    }
                }
                initActivity()
            }
        } else {
            initActivity()
        }
    }
}
