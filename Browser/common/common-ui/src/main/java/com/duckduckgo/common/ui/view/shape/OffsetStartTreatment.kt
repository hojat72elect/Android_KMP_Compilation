
package com.duckduckgo.common.ui.view.shape

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class OffsetStartTreatment(private val other: EdgeTreatment, private val offsetPx: Int) :
    EdgeTreatment() {

    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath,
    ) {
        other.getEdgePath(length, offsetPx.toFloat(), interpolation, shapePath)
    }
}
