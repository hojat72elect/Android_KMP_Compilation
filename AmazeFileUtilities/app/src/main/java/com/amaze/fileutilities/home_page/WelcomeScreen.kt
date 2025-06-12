package com.amaze.fileutilities.home_page

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.welcome_helper.BasicPage
import com.amaze.fileutilities.home_page.welcome_helper.FragmentWelcomePage
import com.amaze.fileutilities.home_page.welcome_helper.WelcomeConfiguration

class WelcomeScreen : WelcomePermissionScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun configuration(): WelcomeConfiguration? {
        return WelcomeConfiguration.Builder(this)
            .defaultBackgroundColor(R.color.navy_blue)
            .page(
                BasicPage(
                    R.drawable.banner_app,
                    getString(R.string.welcome_media_title),
                    getString(R.string.welcome_media_summary)
                )
                    .background(R.color.purple)
            )
            .page(
                BasicPage(
                    R.drawable.ic_outline_analytics_32,
                    getString(R.string.title_analyse),
                    getString(R.string.welcome_analyse_summary)
                )
                    .background(R.color.orange_70)
            )
            .page(
                BasicPage(
                    R.drawable.ic_outline_connect_without_contact_32,
                    getString(R.string.title_transfer),
                    getString(R.string.welcome_transfer_summary)
                )
                    .background(R.color.peach_70)
            )
            .page(
                object : FragmentWelcomePage() {
                    override fun fragment(): Fragment {
                        return PermissionFragmentWelcome()
                    }
                }.background(R.color.navy_blue)
            )
            .canSkip(false)
            .swipeToDismiss(false)
            .useCustomDoneButton(true)
            .build()
    }
}
