package com.amaze.fileutilities.home_page.welcome_helper;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Wrapper for a color int. Used to distinguish between
 * color resource ids and color ints.
 */
public class BackgroundColor {

    private final int color;

    public BackgroundColor(@ColorInt int color) {
        this.color = color;
    }

    public BackgroundColor(@Nullable @ColorInt Integer color, @ColorInt int fallbackColor) {
        this.color = color != null ? color : fallbackColor;
    }

    public int value() {
        return this.color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BackgroundColor) {
            BackgroundColor otherColor = (BackgroundColor) obj;
            return otherColor.value() == this.value();
        }
        return false;
    }

}
