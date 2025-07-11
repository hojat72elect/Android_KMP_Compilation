package ca.hojat.smart.musicplayer.shared.extensions

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import ca.hojat.smart.musicplayer.R



@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getColoredDrawableWithColor(drawableId: Int, color: Int, alpha: Int = 255): Drawable {
    val drawable = getDrawable(drawableId)
    drawable.mutate().applyColorFilter(color)
    drawable.mutate().alpha = alpha
    return drawable
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getSmallPlaceholder(color: Int): Drawable {
    val placeholder = getDrawable(R.drawable.ic_headset_padded)
    val resized = resizeDrawable(placeholder, getDimension(R.dimen.song_image_size).toInt())
    resized.applyColorFilter(color)
    return resized
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getBiggerPlaceholder(color: Int): Drawable {
    val placeholder = getDrawable(R.drawable.ic_headset)
    val resized = resizeDrawable(placeholder, getDimension(R.dimen.artist_image_size).toInt())
    resized.applyColorFilter(color)
    return resized
}

fun Resources.resizeDrawable(drawable: Drawable, size: Int): Drawable {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val bitmapResized = Bitmap.createScaledBitmap(bitmap, size, size, false)
    return BitmapDrawable(this, bitmapResized)
}

private var coverArtHeight: Int = 0
fun Resources.getCoverArtHeight(): Int {
    return if (coverArtHeight == 0) {
        getDimension(R.dimen.top_art_height).toInt()
    } else {
        coverArtHeight
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Resources.getColoredBitmap(resourceId: Int, newColor: Int): Bitmap {
    val drawable = getDrawable(resourceId)
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
    drawable.draw(canvas)
    return bitmap
}
