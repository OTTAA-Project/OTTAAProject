package com.stonefacesoft.ottaa.JSONutils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchObjects {

    private static final String TAG = "SearchObjects";

    public int searchItemById(JSONArray arreglo, int busqueda, Json json) {
        return busquedaBinariaRecursiva(arreglo, busqueda, 0, arreglo.length() - 1, json);
    }

    public int busquedaBinariaRecursiva(JSONArray arreglo, int busqueda, int izquierda, int derecha, Json json) {

        // Si izquierda es mayor que derecha significa que no encontramos nada
        if (izquierda > derecha) {
            return -1;
        }

        // Calculamos las mitades...
        int indiceDelElementoDelMedio = (int) Math.floor((izquierda + derecha) / 2);
        int elementoDelMedio = -1;
        try {
            elementoDelMedio = json.getId(getObject(arreglo, indiceDelElementoDelMedio));
            Log.d(TAG, "busquedaBinariaRecursiva() returned: valor:" + elementoDelMedio + " id:" + busqueda + " posicion:" + indiceDelElementoDelMedio);
        } catch (JSONException e) {
            Log.e(TAG, "busquedaBinariaRecursiva: Error: " + e.getMessage());
        }

        // Ver si est en la mitad
        if(elementoDelMedio == busqueda){
            return indiceDelElementoDelMedio;
        }
        // Si no, entonces vemos si est a la izquierda o derecha
             if(busqueda < elementoDelMedio){
                derecha = indiceDelElementoDelMedio - 1;
                return busquedaBinariaRecursiva(arreglo, busqueda, izquierda, derecha,json);
            }else{
                izquierda = indiceDelElementoDelMedio + 1;
                return busquedaBinariaRecursiva(arreglo, busqueda, izquierda, derecha,json);
            }

    }

    public JSONObject getObject(JSONArray array, int position) {
        try {
            return array.getJSONObject(position);
        } catch (JSONException e) {
            Log.e(TAG, "getObject: Error: " + e.getMessage());
        }
        return null;
    }
}
