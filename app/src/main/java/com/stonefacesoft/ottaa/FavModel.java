package com.stonefacesoft.ottaa;

import android.graphics.Bitmap;

public class FavModel {

    private Bitmap imagen;
    private String texto;

    public FavModel() {
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
