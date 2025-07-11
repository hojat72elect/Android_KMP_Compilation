

package com.duckduckgo.app.trackerdetection.model

interface Entity {
    val name: String
    val displayName: String
    val prevalence: Double

    val isMajor get() = prevalence > MAJOR_NETWORK_PREVALENCE

    companion object {
        const val MAJOR_NETWORK_PREVALENCE = 25.0
    }
}
