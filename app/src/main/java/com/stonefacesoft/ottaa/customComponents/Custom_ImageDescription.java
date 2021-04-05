package com.stonefacesoft.ottaa.customComponents;

import android.content.Context;

public class Custom_ImageDescription {
    private final Context mContext;
    private final String description;
    private final String title;
    private final int idImage;
    private final int color;

    public Custom_ImageDescription(Context mContext, String title, String description, int idImage, int idColor) {
        this.mContext = mContext;
        this.description = description;
        this.idImage = idImage;
        this.title = title;
        this.color = idColor;
    }

    public String getDescription() {
        return description;
    }

    public int getIdImage() {
        return idImage;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}
