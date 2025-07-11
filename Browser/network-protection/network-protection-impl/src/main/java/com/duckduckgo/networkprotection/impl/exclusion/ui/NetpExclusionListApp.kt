

package com.duckduckgo.networkprotection.impl.exclusion.ui

import com.duckduckgo.networkprotection.impl.exclusion.systemapps.SystemAppsExclusionRepository.SystemAppCategory

data class NetpExclusionListApp(
    val packageName: String,
    val name: String,
    val isProtected: Boolean = false,
    val isNotCompatibleWithVPN: Boolean = false,
)

data class NetpExclusionListSystemAppCategory(
    val category: SystemAppCategory,
    val text: String,
    val isEnabled: Boolean = false,
)
