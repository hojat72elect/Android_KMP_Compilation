

package com.duckduckgo.common.utils.plugins

/** A PluginPoint provides a list of plugins of a particular type T */
interface PluginPoint<T> {
    /** @return the list of plugins of type <T> */
    fun getPlugins(): Collection<T>
}
