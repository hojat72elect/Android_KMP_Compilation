package ca.hojat.smart.musicplayer.home.splash

import android.annotation.SuppressLint
import android.content.Intent
import ca.hojat.smart.musicplayer.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
