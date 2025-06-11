

package com.duckduckgo.app.browser.state

import android.app.Activity
import android.os.Bundle
import com.duckduckgo.app.browser.BrowserActivity
import com.duckduckgo.browser.api.ActivityLifecycleCallbacks
import com.duckduckgo.browser.api.BrowserLifecycleObserver
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class BrowserApplicationStateInfo @Inject constructor(
    private val observers: DaggerSet<BrowserLifecycleObserver>,
) : ActivityLifecycleCallbacks {
    private var created = 0
    private var started = 0
    private var resumed = 0

    private var isFreshLaunch: Boolean = false
    private var overrideIsFreshLaunch: Boolean = false

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        if (created++ == 0 && !overrideIsFreshLaunch) isFreshLaunch = true
    }

    override fun onActivityStarted(activity: Activity) {
        if (started++ == 0) {
            observers.forEach { it.onOpen(isFreshLaunch) }
            isFreshLaunch = false
        }
    }

    override fun onActivityResumed(activity: Activity) {
        (++resumed)
        observers.forEach { it.onForeground() }
    }

    override fun onActivityPaused(activity: Activity) {
        if (resumed > 0) (--resumed)
        observers.forEach { it.onBackground() }
    }

    override fun onActivityStopped(activity: Activity) {
        if (started > 0) (--started)
        if (started == 0) observers.forEach { it.onClose() }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (created > 0) (--created)
        if (created == 0 && (activity is BrowserActivity)) {
            if (activity.destroyedByBackPress || activity.isChangingConfigurations) {
                overrideIsFreshLaunch = true
            } else {
                observers.forEach { it.onExit() }
            }
        }
    }
}
