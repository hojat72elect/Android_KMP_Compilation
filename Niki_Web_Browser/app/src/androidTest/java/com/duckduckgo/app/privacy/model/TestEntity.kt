

package com.duckduckgo.app.privacy.model

import com.duckduckgo.app.trackerdetection.model.Entity

data class TestEntity(
    override val name: String,
    override val displayName: String,
    override val prevalence: Double,
) : Entity
