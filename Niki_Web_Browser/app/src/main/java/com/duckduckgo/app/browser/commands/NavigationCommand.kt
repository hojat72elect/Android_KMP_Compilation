

package com.duckduckgo.app.browser.commands

sealed class NavigationCommand : Command() {
    class NavigateToHistory(val historyStackIndex: Int) : Command()
    object Refresh : NavigationCommand()
    class Navigate(
        val url: String,
        val headers: Map<String, String>,
    ) : NavigationCommand()

    class NavigateBack(val steps: Int) : NavigationCommand()
    object NavigateForward : NavigationCommand()
}
