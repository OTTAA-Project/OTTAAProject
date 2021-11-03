package com.stonefacesoft.ottaa.test;

import com.stonefacesoft.ottaa.JSONutils.SearchObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchObjectsTest {

    @Test
    public void searchItemById() {
        JSONArray jsonArray = createJSONArray();
        SearchObjects searchObjects = new SearchObjects();
        int search = searchObjects.searchItemById(jsonArray,118);

        assertEquals(2,search);
    }

    @Test
    public void busquedaBinariaRecursiva() {
        JSONArray jsonArray = createJSONArray();
        SearchObjects searchObjects = new SearchObjects();

        int search = searchObjects.busquedaBinariaRecursiva(jsonArray,118,0,jsonArray.length()-1);
        assertEquals(2,search);

        search = searchObjects.busquedaBinariaRecursiva(jsonArray,999,0,jsonArray.length()-1);
        assertEquals(-1,search);

        search = searchObjects.busquedaBinariaRecursiva(jsonArray,1,0,jsonArray.length()-1);
        assertEquals(-1,search);
    }

    @Test
    public void getObjectTest() {
        JSONArray jsonArray = createJSONArray();
        SearchObjects searchObjects = new SearchObjects();
        JSONObject actual = searchObjects.getObject(jsonArray,3);
        try {
            assertEquals(jsonArray.get(3),actual);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        actual = searchObjects.getObject(jsonArray,103);
        assertNull(actual);

    }


    public JSONObject createPictograms(int id, String locale, String localeName, String englishName, int tipo) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("tipo",tipo);
            JSONObject texto=new JSONObject();
            texto.put("en",englishName);
            texto.put(locale,localeName);
            jsonObject.put("texto",texto);
            JSONObject imagen=new JSONObject();
            imagen.put("picto","ic_action_previous");
            jsonObject.put("imagen",imagen);
            JSONArray jsonArray = new JSONArray();
            JSONObject relatedId1 = new JSONObject();
            relatedId1.put("id",22).put("frec",10);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id",118);
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id",474);
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray createJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643,"es","Yo","I",1));
        jsonArray.put(createPictograms(22,"es","quiero","want",3));
        jsonArray.put(createPictograms(118,"es","comer","eat",3));
        jsonArray.put(createPictograms(474,"es","manzana","apple",2));
        return jsonArray;
    }

}