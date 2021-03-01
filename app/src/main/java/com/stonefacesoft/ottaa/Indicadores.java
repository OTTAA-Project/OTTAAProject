package com.stonefacesoft.ottaa;

import android.content.Context;
import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Hector on 31/08/2015.
 */
/*
Modificaciones realizadas por gonzalo 29/11/2017
* se agrego un metodo para contar la cantidad de frases que se hacen contarfrases()
* se modifico el metodo crearFrase() , ahora permite crear un json que registra las frases realizadas  que permite medir complejidad,frecuencia, dia , fecha y hora de la frase
* se agrego el metodo devolverCantidadDeFrases(), se encarga de devolver el contador con la cantidad de frases realizadas
* se agrego el metodo resetear(), el metodo es llamado por el metodo guardar mes
* se agrego el metodo guardarMes() , este metodo es llamado por el crearfrase, de esta manera lo que hago es verificar si cambio el mes o no en ese momento
* se agrego el metodo devolverFrasesAleatorias(), devuelve una frase de manera aleatoria
* se agrego el metodo ordenarFrases(), ordena las frases de acuerdo a la frecuencia
* se agrego el metodo cantidadFrasesCreadas(), devuelve la cantidad de elementos que tiene el json
*
* hay que tener en cuenta que el mes va a estar relacionado con la fecha de inscripcion, por ahora el reseteo lo hace cuando cambia el mes en el dispositivo
* */
public class Indicadores {

    Context mContext;
    Json json;
    private JSONArray mJSONArrayTodasLasFrases;
    private int cantidadFrases, tam = 2;

    private Calendar SystemTime = Calendar.getInstance();
    private String ultimaFrase = "";


    public Indicadores(Context context) throws FiveMbException {
        this.mContext = context;

        Json.getInstance().setmContext(mContext);
        json = Json.getInstance();


        try {
            this.mJSONArrayTodasLasFrases = json.readJSONArrayFromFile(Constants.ARCHIVO_FRASES);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private JSONArray getJsonArray(ArrayList<JSONObject> mArrayListTodasLasFrasesJson) {
        return new JSONArray(mArrayListTodasLasFrasesJson);
    }

    private boolean existeLaFrase(JSONArray jsonArray, String FraseABuscar) {
        return jsonArray.toString().contains("\"frase\":\"" + FraseABuscar + "\"");
    }

    //Crea el objeto Frase si es nuevo lo agrega, si ya existe suma la frecuencia de uso y fecha
    public ArrayList<JSONObject> crearFrase(ArrayList<JSONObject> mArrayListTodasLasFrases, String Frase,
                                            long fecha, ArrayList<JSONObject> historial) {
        JSONArray jsonArray = getJsonArray(mArrayListTodasLasFrases);
        if (existeLaFrase(jsonArray, Frase)) {
            //La frase existe, tengo q modificar la frecuencia
            Log.d("Indicadores_crearFrase", "La frase existe");
            try {
                for (JSONObject jsonObject : mArrayListTodasLasFrases) {
                    if (jsonObject.getString("frase").equals(Frase)) {
                        jsonObject.put("frecuencia", jsonObject.getInt("frecuencia") + 1);
                        jsonObject.accumulate("fecha", fecha);
                    }
                }
                return mArrayListTodasLasFrases;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Indicadores_crearFrase", "Error al modificar el JSON");
            }
        } else {
            //La frase no existe, tengo q agregarla
            Log.d("Indicadores_crearFrase", "La NO frase existe");
            JSONObject nuevaFrase = new JSONObject();
            try {
                nuevaFrase.put("frase", Frase);
                nuevaFrase.put("frecuencia", 1);
                nuevaFrase.put("complejidad", getComplejidad(historial));
                nuevaFrase.put("fecha", new JSONArray().put(fecha));
                mArrayListTodasLasFrases.add(nuevaFrase);
                return mArrayListTodasLasFrases;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Indicadores_crearFrase", "Error al crear el JSON nuevo" + e.toString());
            }
        }
        return null;
    }

    //Devuelve la complejidad de una frase
    private JSONObject getComplejidad(ArrayList<JSONObject> historial) {
        JSONObject complejidad = new JSONObject();
        try {
            complejidad.put("valor", 0);
            complejidad.put("pictos componentes", new JSONArray(getIdFromHistorial(historial)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return complejidad;
    }

    //Este metodo saca los Id de los pictos que componen Historial
    private ArrayList<JSONObject> getIdFromHistorial(ArrayList<JSONObject> arrayList) {
        if (arrayList != null) {
            ArrayList<JSONObject> historial;
            historial = json.stringToArrayList(arrayList.toString());
            historial.remove(0);
            for (JSONObject jsonObject : historial) {
                jsonObject.remove("texto");
                jsonObject.remove("tipo");
                jsonObject.remove("imagen");
                jsonObject.remove("relacion");
                jsonObject.remove("agenda");
                jsonObject.remove("gps");
            }
            return historial;
        } else {
            Log.e("Json_getIdFromHist", "arrayList == null");
            return null;
        }
    }

    //Devuelve UNA frase aleatoria
    public JSONObject devolverFraseAleatorias() throws JSONException {
        int n = (int) (Math.random() * mJSONArrayTodasLasFrases.length());
        return mJSONArrayTodasLasFrases.getJSONObject(n);
    }

    //Ordena las frases por uso asecndente o descendente
    public ArrayList<JSONObject> ordenarFrasesDescendente(ArrayList<JSONObject> mArrayListFrases, boolean orden) {
        List<JSONObject> frasesOrdenadas = mArrayListFrases;

        Collections.sort(frasesOrdenadas, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject t1, JSONObject t2) {
                try {
                    String s1 = t1.getString("frecuencia");
                    String s2 = t2.getString("frecuencia");
                    return s1.compareTo(s2);
                } catch (JSONException e) {
                    Log.e("indicadores_ordenarFra", "ERROR al ordenar las frases");
                }
                return 0;
            }
        });
        if (orden)
            return new ArrayList<>(frasesOrdenadas);
        else {
            Collections.reverse(frasesOrdenadas);
            return new ArrayList<>(frasesOrdenadas);
        }
    }

    //Devuelve la cantidad de frases creadas
    public int cantidadFrasesCreadas() {
        return mJSONArrayTodasLasFrases.length();
    }

    //define el tama&ntildeo para devolver la cantidad de frases  mas usadas
    public void definirSize(int n) {
        this.tam = n;
    }


}
