

package com.duckduckgo.app.trackerdetection

import android.net.Uri

interface Client {

    enum class ClientType {
        BLOCKING,
        ALLOWLIST,
    }

    enum class ClientName(val type: ClientType) {
        // current clients
        TDS(ClientType.BLOCKING),

        // legacy clients
        EASYLIST(ClientType.BLOCKING),
        EASYPRIVACY(ClientType.BLOCKING),
        TRACKERSALLOWLIST(ClientType.ALLOWLIST),
    }

    data class Result(
        val matches: Boolean,
        val entityName: String? = null,
        val categories: List<String>? = null,
        val surrogate: String? = null,
        val isATracker: Boolean,
    )

    val name: ClientName

    fun matches(
        url: String,
        documentUrl: Uri,
        requestHeaders: Map<String, String>,
    ): Result

    fun matches(
        url: Uri,
        documentUrl: Uri,
        requestHeaders: Map<String, String>,
    ): Result
}
