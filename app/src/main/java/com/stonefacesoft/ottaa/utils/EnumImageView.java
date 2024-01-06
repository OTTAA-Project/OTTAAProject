package com.stonefacesoft.ottaa.utils;

import android.widget.ImageButton;
import android.widget.ImageView;

public class EnumImageView {
    public  ImageButton[] imageView = new ImageButton[10];
    public static EnumImageView _EnumImageView;
    public static EnumImageView getInstance(){
        if(_EnumImageView == null)
            _EnumImageView = new EnumImageView();
        return _EnumImageView;
    }
    public ImageButton[] getImageview() {
        return imageView;
    }
    public void setImageview( ImageButton imgView,int child) {imageView[child] = imgView ;
    }
}
