package com.simplemobiletools.smsmessenger

import android.app.Application
import com.simplemobiletools.smsmessenger.extensions.checkUseEnglish

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }
}
