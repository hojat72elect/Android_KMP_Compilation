package ca.hojat.notes.niki.shared.ui.compose.extensions

import android.app.Activity
import android.content.Context
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.extensions.baseConfig
import ca.hojat.notes.niki.shared.extensions.redirectToRateUs
import ca.hojat.notes.niki.shared.extensions.toast
import ca.hojat.notes.niki.shared.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Activity.rateStarsRedirectAndThankYou(stars: Int) {
    if (stars == 5) {
        redirectToRateUs()
    }
    toast(R.string.thank_you)
    baseConfig.wasAppRated = true
}
