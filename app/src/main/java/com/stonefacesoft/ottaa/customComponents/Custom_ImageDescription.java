package com.stonefacesoft.ottaa.customComponents;

import android.content.Context;

public class Custom_ImageDescription {
    private Context mContext;
    private final String description;
    private final String title;
    private final int idImage;
    private final int backgroundColor;
    private final int textColor;
    public Custom_ImageDescription(Context mContext,String title,String description,int idImage,int backgroundColor,int textColor){
        this.mContext=mContext;
        this.description=description;
        this.idImage=idImage;
        this.title=title;
        this.backgroundColor=backgroundColor;
        this.textColor=textColor;
    }
    public Custom_ImageDescription(String title,String description,int idImage,int backgroundColor,int textColor){
        this.description=description;
        this.idImage=idImage;
        this.title=title;
        this.backgroundColor=backgroundColor;
        this.textColor=textColor;
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
