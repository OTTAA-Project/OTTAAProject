package com.stonefacesoft.ottaa.customComponents;

import android.content.Context;

public class Custom_ImageDescription {
    private Context mContext;
    private String description;
    private String title;
    private int idImage;
    private int color;
    private int textColor;
    public Custom_ImageDescription(Context mContext,String title,String description,int idImage,int idColor,int textColor){
        this.mContext=mContext;
        this.description=description;
        this.idImage=idImage;
        this.title=title;
        this.color=idColor;
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

    public int getColor() {
        return color;
    }

    public int getTextColor() {
        return textColor;
    }
}
