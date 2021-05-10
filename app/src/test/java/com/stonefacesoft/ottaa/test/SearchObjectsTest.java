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
    }

    @Test
    public void busquedaBinariaRecursiva() {
        //TODO hacer este metodo cuando saquemos el conext de JSON
    }

    @Test
    public void getObject() {
        JSONArray jsonArray = createJSONArray();
        SearchObjects searchObjects = new SearchObjects();
        JSONObject actual = searchObjects.getObject(jsonArray,3);
        try {
            assertEquals(jsonArray.get(3),actual);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray createJSONArray(){
        String string = "[{\"id\":0,\"tipo\":111},{\"id\":1,\"tipo\":222},{\"id\":2,\"tipo\":333},{\"id\":3,\"tipo\":444},{\"id\":4,\"tipo\":555},{\"id\":5,\"tipo\":666}]";
        try {
            return new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}