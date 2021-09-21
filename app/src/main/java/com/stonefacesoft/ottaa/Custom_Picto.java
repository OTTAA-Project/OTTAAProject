package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Hector on 23/02/2016.
 * <h3>How to declare</h3>
 * <code>Custom_picto picto=new Custom_picto(Context);</code>
 * <h3>Examples of implementation</h3>
 * <h4>How to set up the pictogram name</h4>
 * <code>String name="Hello";</code>
 *<code> picto.setCustom_Texto(name);</code>
 * <h4>How to set up the custom color </h4>
 * <code>int color=getResources().getColor(R.color.Black)</code>
 * <code>picto.setCustom_Color(color);</code>
 * <h4>How to set up the icon</h4>
 * <code>picto.setCustom_Img(Drawable imagen)</code>
 */
public class Custom_Picto extends LinearLayout {

    private final static String TAG = "Custom_Picto";
    private String Custom_Texto;
    private String Custom_description;
    private Drawable Custom_Imagen;
    private Integer Custom_Color;
    private boolean isClicked;
    private int id;

    TextView tv;
    ImageView Img;
    ImageView Color;

    public Custom_Picto(Context context) {
        super(context);
        init();
    }

    public Custom_Picto(Context context, AttributeSet attrs) {
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

    public Custom_Picto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Custom_Picto,
                0, 0);
        try {
            Custom_Texto = a.getString(R.styleable.Custom_Picto_Texto);
            Custom_Color = a.getColor(R.styleable.Custom_Picto_Color, getResources().getColor(R.color.Black));
//            Custom_Imagen = a.getInteger(R.styleable.Custom_Picto_Imagen, 0);
        } finally {
            a.recycle();
        }
        init();
    }



    private void init() {
        inflate(getContext(), R.layout.compound_picto_3, this);
        this.tv = findViewById(R.id.grid_text);
        this.Img = findViewById(R.id.Imagen_Picto);
        this.Color = findViewById(R.id.color_Picto);
    }

    /**
     * Set the color of the pictogram
     */
    public void setCustom_Color(Integer color) {
        this.Custom_Color = color;
        if(getContext()!=null){
            Color.setColorFilter(color);
            invalidate();
            requestLayout();
        }
    }

    /**
     * set the icon of the pictogram
     */
    public void setCustom_Img(Drawable imagen) {
        this.Custom_Imagen = imagen;
        Img.setImageDrawable(imagen);
        invalidate();
        requestLayout();
    }

    /**
     * Set up the name of the pictogram
     */
    public void setCustom_Texto(String t) {
        this.Custom_Texto = t;
        if(getContext()!=null){
        tv.setText(t);
        invalidate();
        requestLayout();
        }
    }

    public ImageView getImg() {
        return Img;
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

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isClicked() {
        return isClicked;
    }

    /**
     * Set up the id of the pictogram
     */
    public void setIdPictogram(int id) {
        this.id = id;
    }

    public int getIdPictogram() {
        return id;
    }

    /**
     * Set up the description about the pictogram
     */
    public void setCustom_description(String custom_description) {
        Custom_description = custom_description;
    }

    public String getCustom_description() {
        return Custom_description;
    }
}
