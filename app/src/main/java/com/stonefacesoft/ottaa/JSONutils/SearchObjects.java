package com.stonefacesoft.ottaa.JSONutils;

import android.util.Log;

import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchObjects {

    private static final String TAG = "SearchObjects";

    public int searchItemById(JSONArray arreglo, int busqueda) {
        return busquedaBinariaRecursiva(arreglo, busqueda, 0, arreglo.length()-1);
    }

    public int busquedaBinariaRecursiva(JSONArray arreglo, int busqueda, int izquierda, int derecha) {

        // Si izquierda es mayor que derecha significa que no encontramos nada
        if (izquierda > derecha) {
            return -1;
        }

        // Calculamos las mitades...
        int indiceDelElementoDelMedio = (int) Math.floor((izquierda + derecha) / 2);
        int elementoDelMedio = -1;
        try {
            elementoDelMedio = JSONutils.getId(getObject(arreglo, indiceDelElementoDelMedio));
        } catch (JSONException e) {
            e.getMessage();
        }

        // Ver si est en la mitad
        if(elementoDelMedio == busqueda){
            return indiceDelElementoDelMedio;
        }
        // Si no, entonces vemos si est a la izquierda o derecha
             if(busqueda < elementoDelMedio){
                derecha = indiceDelElementoDelMedio - 1;
                return busquedaBinariaRecursiva(arreglo, busqueda, izquierda, derecha);
            }else{
                izquierda = indiceDelElementoDelMedio + 1;
                return busquedaBinariaRecursiva(arreglo, busqueda, izquierda, derecha);
            }

    }

    public JSONObject getObject(JSONArray array, int position) {
        try {
            return array.getJSONObject(position);
        } catch (JSONException e) {
            Log.e(TAG, "getObject: Error: " + e.getMessage());
            return null;
        }
    }
}
