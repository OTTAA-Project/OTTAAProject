package com.stonefacesoft.ottaa.customComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by Hector on 23/02/2016.
 */
public class Custom_Grupo extends ConstraintLayout {

    private String Custom_Texto;
    private Drawable Custom_Imagen;
    private Integer Custom_Color;



    private TextView tv;
    private ImageView Img,tagHora,tagUbicacion,tagSexo,tagEdad;
    private ImageView Color;

    public Custom_Grupo(Context context) {
        super(context);
        init();
    }


    public Custom_Grupo(Context context, AttributeSet attrs) {
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Custom_Picto,
                0, 0);
        try {
            Custom_Texto = a.getString(R.styleable.Custom_Picto_Texto);
            Custom_Color = a.getColor(R.styleable.Custom_Picto_Color,getResources().getColor(R.color.Black));
//            Custom_Imagen = a.getInteger(R.styleable.Custom_Picto_Imagen, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    public Custom_Grupo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Custom_Picto,
                0, 0);
        try {
            Custom_Texto = a.getString(R.styleable.Custom_Picto_Texto);
            Custom_Color = a.getColor(R.styleable.Custom_Picto_Color, getResources().getColor(R.color.Black));
        //    Custom_Imagen = a.getInteger(R.styleable.Custom_Picto_Imagen, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.grid_grupo_4, this);
        this.tv = findViewById(R.id.grid_text);
        this.Img = findViewById(R.id.grid_image);
        this.Color = findViewById(R.id.color_Picto);
        this.tagEdad=findViewById(R.id.tagCalendario);
        this.tagHora=findViewById(R.id.tagHora);
        this.tagUbicacion=findViewById(R.id.tagUbicacion);
        this.tagSexo=findViewById(R.id.tagClima);
    }


    public void setCustom_Color(Integer color) {
        this.Custom_Color = color;
        //Img.setBackgroundColor(color);

        Color.setColorFilter(color);

        invalidate();
        requestLayout();
    }


    public void setCustom_Img(Drawable imagen) {
        this.Custom_Imagen = imagen;
        Img.setImageDrawable(imagen);
        invalidate();
        requestLayout();
    }
    public void setCustom_Texto(String t) {
        this.Custom_Texto = t;
        tv.setText(t);
        invalidate();
        requestLayout();
    }

    public String getCustom_Texto()  {return Custom_Texto;}

    public int getCustom_Color()  {return Custom_Color;}

    public Drawable getCustom_Imagen()  {return Custom_Imagen;}

    public void goneCustomTexto(){
        tv.setVisibility(GONE);
    }
    public void setInvisibleCustomTexto(){
        tv.setVisibility(INVISIBLE);
    }
    public void setVisibleText(){
        tv.setVisibility(VISIBLE);
    }

    public void setTagDrawable(int position,int drawable){
        switch (position){
            case 0:

                tagHora.setImageResource(drawable);
                break;
            case 1:
                tagUbicacion.setImageResource(drawable);
                break;
            case 2:
                tagSexo.setImageResource(drawable);
                break;
            case 3:
                tagEdad.setImageResource(drawable);
                break;
        }
    }


    public ImageView getImg() {
        return Img;
    }
}
