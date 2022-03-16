package com.stonefacesoft.ottaa.JSONutils;

import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json0Recover {
    private JSONObject parent;
    public JSONObject createJson(){
        parent = new JSONObject();
        JSONArray array = new JSONArray();
        loadRelationShip(array);
        parent = JSONutils.crearJson(0, ConfigurarIdioma.getLanguaje(),"","",array,"ic_perro",0);
        return parent;
    }
    private void preparePictograms(JSONArray array,int id,int frec){
        JSONObject object = new JSONObject();
        try {
            object.put("id",id);
            object.put("frec",frec);
        } catch (JSONException e) {
        }
        array.put(object);
    }
    private void loadRelationShip(JSONArray array){
        preparePictograms(array,44,51);
        preparePictograms(array,377,73);
        preparePictograms(array,382,2);
        preparePictograms(array,384,1);
        preparePictograms(array,388,11);
        preparePictograms(array,389,13);
        preparePictograms(array,614,14);
        preparePictograms(array,623,10);
        preparePictograms(array,628,35);
        preparePictograms(array,632,16);
        preparePictograms(array,643,37);
    }
}
