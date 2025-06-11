package ca.hojat.gamehub

import android.app.Application
import ca.hojat.gamehub.initializers.Initializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@HiltAndroidApp
class GameHubApplication : Application() {

    @Inject
    lateinit var initializer: Initializer

    override fun onCreate() {
        super.onCreate()

        initializer.init()
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}
