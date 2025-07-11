

package com.duckduckgo.anvil.annotations

import dagger.MapKey

/**
 * Use [PriorityKey] in combination with @ContributesMultibinding when contributing a plugin and want
 * to assign a priority to the instance.
 * Lower priority values mean the associated plugin comes first in the list of plugins.
 * When two plugins have the same priority the priority is resolved sorting by class instance fully qualified name
 *
 * Note: Plugins that are not annotated with [PriorityKey] will always come last in the list of plugins and order
 * by class instance fully qualified name
 */
@MapKey
annotation class PriorityKey(val priority: Int)
