package ca.hojat.notes.niki.shared.extensions

import android.content.res.Resources
import android.graphics.drawable.Drawable
import ca.hojat.notes.niki.shared.extensions.applyColorFilter


fun Resources.getColoredDrawableWithColor(drawableId: Int, color: Int, alpha: Int = 255): Drawable {
    val drawable = getDrawable(drawableId)
    drawable.mutate().applyColorFilter(color)
    drawable.mutate().alpha = alpha
    return drawable
}
