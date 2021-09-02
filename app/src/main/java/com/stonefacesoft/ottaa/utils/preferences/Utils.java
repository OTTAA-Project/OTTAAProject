package com.stonefacesoft.ottaa.utils.preferences;

import android.content.res.ColorStateList;
import androidx.annotation.ColorInt;

public class Utils {
    public static ColorStateList colorToStateList(@ColorInt int color, @ColorInt int disabledColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_checked},
                        new int[]{},
                },
                new int[]{
                        disabledColor,
                        disabledColor,
                        color,
                });
    }
}