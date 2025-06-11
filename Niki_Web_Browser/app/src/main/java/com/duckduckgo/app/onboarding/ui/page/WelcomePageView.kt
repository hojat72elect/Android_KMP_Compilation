

package com.duckduckgo.app.onboarding.ui.page

import android.content.Intent

object WelcomePageView {
    sealed class Event {
        object OnPrimaryCtaClicked : Event()
        object OnDefaultBrowserSet : Event()
        object OnDefaultBrowserNotSet : Event()
    }

    sealed class State {
        object Idle : State()
        data class ShowDefaultBrowserDialog(val intent: Intent) : State()
        object Finish : State()
    }
}
