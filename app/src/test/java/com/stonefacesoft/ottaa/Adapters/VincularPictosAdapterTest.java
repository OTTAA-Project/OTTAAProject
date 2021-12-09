package com.stonefacesoft.ottaa.Adapters;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils.SortPictograms;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class VincularPictosAdapterTest {

    private final VincularPictosAdapter vincularPictosAdapter = new VincularPictosAdapter( R.layout.item_grid, createPictogramJSONArray(), false);

    @Test
    public void getItemCount() {

        assertEquals(4, vincularPictosAdapter.getItemCount());
    }


    @Test
    public void getmVincularArray() {
        JSONArray aux = createPictogramJSONArray();

        try {
            new SortPictograms().quickSort(aux,0,aux.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        aux.remove(0);
        assertEquals(aux.toString(), vincularPictosAdapter.getmVincularArray().toString());
    }

    @Test
    public void setmVincularArray() {
        JSONArray array = new JSONArray();
        vincularPictosAdapter.setmVincularArray(array);
        assertEquals(0, vincularPictosAdapter.getmVincularArray().length());
    }

    @Test
    public void isEsFiltrado() {
        vincularPictosAdapter.setEsFiltrado(true);
        assertTrue(vincularPictosAdapter.isEsFiltrado());
    }

    @Test
    public void setEsFiltrado() {
        vincularPictosAdapter.setEsFiltrado(false);
        assertFalse(vincularPictosAdapter.isEsFiltrado());
    }


    private JSONArray createPictogramJSONArray() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643, "es", "Yo", "I", 1));
        jsonArray.put(createPictograms(22, "es", "quiero", "want", 3));
        jsonArray.put(createPictograms(118, "es", "comer", "eat", 3));
        jsonArray.put(createPictograms(474, "es", "manzana", "apple", 2));
        jsonArray.put(createPictograms(0, "es", "manzanas", "apple", 2));
        return jsonArray;
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
            relatedId1.put("id", 22).put("frec", 10);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id", 118);
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id", 474);
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}