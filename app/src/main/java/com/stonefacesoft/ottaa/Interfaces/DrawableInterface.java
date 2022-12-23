package com.stonefacesoft.ottaa.Interfaces;

import android.graphics.drawable.Drawable;

public interface DrawableInterface {
    public Drawable getDrawable(Drawable drawable);
    public void fetchDrawable(Drawable drawable);
    public void fetchDrawable(Drawable drawable,int position);
}
