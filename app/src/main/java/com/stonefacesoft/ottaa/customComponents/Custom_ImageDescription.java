package com.stonefacesoft.ottaa.customComponents;

public class Custom_ImageDescription {

    private String description;
    private String title;
    private int idImage;
    private int backgroundColor;
    private int textColor;
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
