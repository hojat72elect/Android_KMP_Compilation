package com.amaze.fileutilities.home_page.welcome_helper;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

class ColorHelper {


    static int getColor(Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }


    static int resolveColorAttribute(Context context, @AttrRes int resId, int fallback) {
        android.util.TypedValue value = new android.util.TypedValue();
        boolean colorFound = context.getTheme().resolveAttribute(resId, value, true);
        return colorFound ? value.data : fallback;
    }

}
