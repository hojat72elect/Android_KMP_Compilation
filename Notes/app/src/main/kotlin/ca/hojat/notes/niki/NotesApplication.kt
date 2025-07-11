package ca.hojat.notes.niki

import android.app.Application
import ca.hojat.notes.niki.shared.extensions.baseConfig
import ca.hojat.notes.niki.shared.helpers.isNougatPlus
import java.util.Locale

/**
 * The main entry to our application.
 */
class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }

    private fun checkUseEnglish(){
        if (baseConfig.useEnglish && !isNougatPlus()) {
            val conf = resources.configuration
            conf.locale = Locale.ENGLISH
            resources.updateConfiguration(conf, resources.displayMetrics)
        }
    }
}
