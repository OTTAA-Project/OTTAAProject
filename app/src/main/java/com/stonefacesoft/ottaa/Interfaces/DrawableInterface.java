package com.stonefacesoft.ottaa.Interfaces;

import android.graphics.drawable.Drawable;

public interface DrawableInterface {
    Drawable getDrawable(Drawable drawable);
    void fetchDrawable(Drawable drawable);
    void fetchDrawable(Drawable drawable,int position);
}
