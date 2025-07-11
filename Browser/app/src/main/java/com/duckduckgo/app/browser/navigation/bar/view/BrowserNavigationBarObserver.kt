

package com.duckduckgo.app.browser.navigation.bar.view

import androidx.annotation.EmptySuper

interface BrowserNavigationBarObserver {
    @EmptySuper
    fun onFireButtonClicked() {
    }

    @EmptySuper
    fun onTabsButtonClicked() {
    }

    @EmptySuper
    fun onTabsButtonLongClicked() {
    }

    @EmptySuper
    fun onMenuButtonClicked() {
    }

    @EmptySuper
    fun onBackButtonClicked() {
    }

    @EmptySuper
    fun onBackButtonLongClicked() {
    }

    @EmptySuper
    fun onForwardButtonClicked() {
    }

    @EmptySuper
    fun onNewTabButtonClicked() {
    }

    @EmptySuper
    fun onAutofillButtonClicked() {
    }

    @EmptySuper
    fun onBookmarksButtonClicked() {
    }
}
