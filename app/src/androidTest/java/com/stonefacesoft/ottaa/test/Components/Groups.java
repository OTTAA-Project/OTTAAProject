package com.stonefacesoft.ottaa.test.Components;

import com.stonefacesoft.ottaa.JSONutils.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Groups {
    private final Json json;

    public Groups(Json json){
        this.json=json;

    }


    public void deleteRelationship(JSONObject father,int idChild){
        //json.desvincularJson(father,idChild);
    }

    public JSONObject createGroup(int id, String locale, String localeName, String englisName, int tipo) {
        JSONObject dataObject=new JSONObject();
        try {
            dataObject.put("id",id);
            dataObject.put("tipo",tipo);
            JSONObject texto=new JSONObject();
            texto.put("en",englisName);
            texto.put(locale,localeName);
            dataObject.put("texto",texto);
            JSONObject imagen=new JSONObject();
            imagen.put("picto","ic_action_previous");
            dataObject.put("imagen",imagen);
            dataObject.put("relacion",new JSONArray());
            dataObject.put("frecuencia",0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataObject;
    }


}
