package com.simplemobiletools.smsmessenger.compose.extensions

import android.app.Activity
import android.content.Context
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.extensions.baseConfig
import com.simplemobiletools.smsmessenger.extensions.redirectToRateUs
import com.simplemobiletools.smsmessenger.extensions.toast
import com.simplemobiletools.smsmessenger.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Activity.rateStarsRedirectAndThankYou(stars: Int) {
    if (stars == 5) {
        redirectToRateUs()
    }
    toast(R.string.thank_you)
    baseConfig.wasAppRated = true
}
