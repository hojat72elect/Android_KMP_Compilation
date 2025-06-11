

package com.duckduckgo.app

import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.di.AppComponent
import com.duckduckgo.app.global.DuckDuckGoApplication

fun getApp(): DuckDuckGoApplication {
    return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as DuckDuckGoApplication
}

fun getDaggerComponent(): AppComponent {
    return getApp().daggerAppComponent as AppComponent
}
