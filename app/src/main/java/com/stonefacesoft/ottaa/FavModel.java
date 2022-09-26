package com.stonefacesoft.ottaa;

import android.graphics.Bitmap;

import org.json.JSONObject;

public class FavModel {

    private Bitmap imagen;
    private String texto;
    private JSONObject pictogram;
    private int position;

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

    public void setPictogram(JSONObject pictogram) {
        this.pictogram = pictogram;
    }

    public JSONObject getPictogram() {
        return pictogram;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
