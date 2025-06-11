package ca.hojat.gamehub.core.urlopeners

import android.content.Context

interface UrlOpener {

    /**
     * Tries to open a url in any possible way, and then returns the result about
     * whether it was successful or no.
     *
     * @param url the url to open
     * @param context the activity context
     *
     * @return true if the url was opened; false otherwise
     */
    fun openUrl(url: String, context: Context): Boolean
}
