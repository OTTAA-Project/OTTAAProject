package com.stonefacesoft.ottaa.customComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.stonefacesoft.ottaa.R;

public class Custom_ImageDescription {
    private Context mContext;
    private String description;
    private String title;
    private int idImage;
    private int color;
    public Custom_ImageDescription(Context mContext,String title,String description,int idImage,int idColor){
        this.mContext=mContext;
        this.description=description;
        this.idImage=idImage;
        this.title=title;
        this.color=idColor;
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
