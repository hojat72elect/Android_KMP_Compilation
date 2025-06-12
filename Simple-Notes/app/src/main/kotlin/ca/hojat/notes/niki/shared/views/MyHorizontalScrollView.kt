package ca.hojat.notes.niki.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

@SuppressLint("ClickableViewAccessibility")
class MyHorizontalScrollView : HorizontalScrollView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(false)
        return super.onTouchEvent(ev)
    }
}
