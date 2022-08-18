package com.stonefacesoft.ottaa.utils;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.view.ReportActivityInterface;

public enum EnumImageView {
    ImageView1,ImageView2,ImageView3,ImageView4,ImageView5,ImageView6,ImageView7,ImageView8,ImageView9,ImageView10;
    private ImageView imageview;
    public ImageView getImageview() {
        return imageview;
    }
    public void setImageview(ImageView imageview) {
        this.imageview =imageview ;
    }
}
