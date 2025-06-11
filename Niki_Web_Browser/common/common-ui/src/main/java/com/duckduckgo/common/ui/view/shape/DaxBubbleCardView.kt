

package com.duckduckgo.common.ui.view.shape

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.duckduckgo.common.ui.view.getColorFromAttr
import com.duckduckgo.mobile.android.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel

class DaxBubbleCardView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cardViewStyle,
) : MaterialCardView(context, attrs, defStyleAttr) {

    init {
        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.DaxBubbleCardView, defStyleAttr, 0)
        val edgePosition = EdgePosition.from(attr.getInt(R.styleable.DaxBubbleCardView_edgePosition, 0))

        val cornderRadius = resources.getDimension(R.dimen.mediumShapeCornerRadius)
        val cornerSize = resources.getDimension(R.dimen.daxBubbleDialogEdge)
        val distanceFromEdge = resources.getDimension(R.dimen.daxBubbleDialogDistanceFromEdge)
        val edgeTreatment = DaxBubbleEdgeTreatment(cornerSize, distanceFromEdge, edgePosition)

        setCardBackgroundColor(ColorStateList.valueOf(context.getColorFromAttr(R.attr.daxColorSurface)))

        shapeAppearanceModel = when (edgePosition) {
            EdgePosition.TOP -> ShapeAppearanceModel.builder()
                .setAllCornerSizes(cornderRadius)
                .setTopEdge(edgeTreatment)
                .build()

            EdgePosition.LEFT -> ShapeAppearanceModel.builder()
                .setAllCornerSizes(cornderRadius)
                .setLeftEdge(edgeTreatment)
                .build()
        }
    }

    enum class EdgePosition {
        TOP,
        LEFT,
        ;

        companion object {
            fun from(value: Int): EdgePosition {
                // same order as attrs-dax-dialog.xml
                return when (value) {
                    1 -> LEFT
                    else -> TOP
                }
            }
        }
    }
}
