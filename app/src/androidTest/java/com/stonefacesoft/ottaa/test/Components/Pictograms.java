package com.stonefacesoft.ottaa.test.Components;

import android.content.Context;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pictograms {
    private final Context mContext;
    private final Json json;
    private Custom_Picto picto;

    public Pictograms(Context mContext, Json json) {
        this.mContext = mContext;
        this.json = json;


    }

    public void relacionarObjeto(JSONObject father, JSONObject child) {
        json.aumentarFrec(father, child);
    }

    public JSONArray ordenarObjetos(JSONObject father) {
        try {
            try {
                return json.cargarOpciones(father, 0);
            } catch (FiveMbException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJson(int id) {
        return json.getPictoFromId2(id);
    }

    public void addPictogram(JSONObject object) {
        json.getmJSONArrayTodosLosPictos().put(object);
    }

    public JSONObject
    generatePictogram(int id, String locale, String localeName, String englisName, int tipo) {
        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("id", id);
            dataObject.put("tipo", tipo);
            JSONObject texto = new JSONObject();
            texto.put("en", englisName);
            texto.put(locale, localeName);
            dataObject.put("texto", texto);
            JSONObject imagen = new JSONObject();
            imagen.put("picto", "ic_action_previous");
            dataObject.put("imagen", imagen);
            dataObject.put("relacion", new JSONArray());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataObject;
    }

    public Json getJson() {
        return json;
    }
}
