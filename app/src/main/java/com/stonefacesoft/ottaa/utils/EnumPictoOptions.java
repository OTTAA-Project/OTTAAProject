package com.stonefacesoft.ottaa.utils;

import org.json.JSONObject;

public class EnumPictoOptions {
    public static JSONObject pictoPadre, opcion1, opcion2, opcion3, opcion4, onLongOpcion;

     private JSONObject object;

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public JSONObject getObject() {
        return object;
    }
}
