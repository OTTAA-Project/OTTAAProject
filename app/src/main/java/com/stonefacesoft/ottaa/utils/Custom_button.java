package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.stonefacesoft.ottaa.R;

public class Custom_button extends ConstraintLayout {
    private ImageView icono;
    private TextView textView;
    public Custom_button(Context context) {
        super(context);

    }

    public Custom_button(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout constraintLayout=(ConstraintLayout) inflater.inflate(R.layout.custom_imagebutton,this);
        icono=constraintLayout.findViewById(R.id.icono);
        textView=constraintLayout.findViewById(R.id.nombre);
    }

    public Custom_button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setIcono(ImageView icono) {
        this.icono = icono;
    }
    public void setTextView(){

    }

    public void setTexto(String texto){
        if(textView!=null)
        textView.setText(texto);
    }
    public void setImageView(Context c,int drawable){
        icono.setImageDrawable(c.getResources().getDrawable(drawable));
    }
}
