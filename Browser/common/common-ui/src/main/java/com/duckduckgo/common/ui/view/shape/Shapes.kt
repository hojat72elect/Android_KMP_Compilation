

package com.duckduckgo.common.ui.view.shape

import com.duckduckgo.common.ui.view.shape.DaxBubbleCardView.EdgePosition
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath

class DaxBubbleEdgeTreatment
/**
 * Instantiates a triangle treatment of the given size, which faces inward or outward relative to
 * the shape.
 *
 * @param size the length in pixels that the triangle extends into or out of the shape. The length
 * of the side of the triangle coincident with the rest of the edge is 2 * size.
 * @param inside true if the triangle should be "cut out" of the shape (i.e. inward-facing); false
 * if the triangle should extend out of the shape.
 * @param edgePosition TOP for positioning triangle on the top side of the dialog; LEFT for
 * positioning triangle on the left side of the dialog
 */(
    private val size: Float,
    private val distanceFromEdge: Float,
    private val edgePosition: EdgePosition = EdgePosition.TOP,
) : EdgeTreatment() {
    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath,
    ) {
        when (edgePosition) {
            EdgePosition.TOP -> {
                shapePath.lineTo(distanceFromEdge - size * interpolation, 0f)
                shapePath.lineTo(distanceFromEdge, -size * interpolation)
                shapePath.lineTo(distanceFromEdge + size * interpolation, 0f)
                shapePath.lineTo(length, 0f)
            }

            EdgePosition.LEFT -> {
                val d = length - distanceFromEdge
                shapePath.lineTo(d - size * interpolation, 0f)
                shapePath.lineTo(d, -size * interpolation)
                shapePath.lineTo(d + size * interpolation, 0f)
                shapePath.lineTo(length, 0f)
            }
        }
    }
}

class TicketEdgeTreatment(
    private val size: Float,
) : EdgeTreatment() {
    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath,
    ) {
        val circleRadius = size * interpolation
        shapePath.lineTo(center - circleRadius, 0f)
        shapePath.addArc(
            center - circleRadius,
            -circleRadius,
            center + circleRadius,
            circleRadius,
            180f,
            -180f,
        )
        shapePath.lineTo(length, 0f)
    }
}

val ticketShapePathModel = ShapeAppearanceModel
    .Builder()
    .setAllCorners(CornerFamily.ROUNDED, 36f)
    .setLeftEdge(TicketEdgeTreatment(36f))
    .setRightEdge(TicketEdgeTreatment(36f))
    .build()

class TicketDrawable : MaterialShapeDrawable(ticketShapePathModel)
