package com.stonefacesoft.ottaa.JSONutils;

import static org.junit.Assert.assertTrue;

import com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils.SortPictograms;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Random;

public class SortPictogramsTest  {
    private final int size = 10000;
    @Test
    public void quickSortMinValue() {
        JSONArray array = null;
        JSONObject object = createPictogramJSONArray();
        try {
            array = object.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
           new SortPictograms().quickSort(array,0,array.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int result =JSONutils.getId(array.getJSONObject(0));
            assertTrue(result == object.getInt("min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void quickSortMaxValue() {
        JSONArray array = null;
        JSONObject object = createPictogramJSONArray();
        try {
            array = object.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            new SortPictograms().quickSort(array,0,array.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int result =JSONutils.getId(array.getJSONObject(array.length()-1));
            assertTrue(result == object.getInt("max"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void heapsortMaxValue(){

        JSONArray array = null;
        JSONObject object = createPictogramJSONArray();
        try {
            array = object.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
           new SortPictograms().heapSort(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int result =JSONutils.getId(array.getJSONObject(array.length()-1));
            assertTrue(result == object.getInt("max"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void heapsortMinValue(){

        JSONArray array = null;
        JSONObject object = createPictogramJSONArray();
        try {
            array = object.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
          new  SortPictograms().heapSort(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int result =JSONutils.getId(array.getJSONObject(0));
            assertTrue(result == object.getInt("min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void heapsortNullValue(){

        JSONArray array = null;
        JSONObject object = createPictogramJSONArray();
        try {
            array = object.getJSONArray("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
          new  SortPictograms().heapSort(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            int result =JSONutils.getId(null);
            assertTrue(result == -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private JSONObject createPictogramJSONArray() {
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        int max = 0;
        int min = -1;
        Random random = new Random();

        for (int i = 0; i <size ; i++) {
            int aux = random.nextInt(size);
            int tipoRandom = random.nextInt(6);
            if(min == -1)
                min = aux;
            if(aux>max)
                max = aux;
            if(aux < min)
                min = aux;
            jsonArray.put(createPictograms(aux, "es", "Yo", "I",tipoRandom ));
        }
        try {
            object.put("array",jsonArray);
            object.put("max",max);
            object.put("min",min);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject createPictograms(int id, String locale, String localeName, String englishName, int tipo) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("tipo", tipo);
            JSONObject texto = new JSONObject();
            texto.put("en", englishName);
            texto.put(locale, localeName);
            jsonObject.put("texto", texto);
            JSONObject imagen = new JSONObject();
            imagen.put("picto", "ic_action_previous");
            jsonObject.put("imagen", imagen);
            JSONArray jsonArray = new JSONArray();
            JSONObject relatedId1 = new JSONObject();
            relatedId1.put("id", getRandomValue(size)).put("frec", 10);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id",  getRandomValue(size));
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id",  getRandomValue(size));
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private int getRandomValue(int max){
        Random random = new Random();
        return random.nextInt(max);
    }
}