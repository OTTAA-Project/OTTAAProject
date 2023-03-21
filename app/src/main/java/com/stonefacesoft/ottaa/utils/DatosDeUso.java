
package com.stonefacesoft.ottaa.utils;


import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Todos los datos de uso de la aplicacion, cuando se usa, como se usa, frases y pictos.
public class DatosDeUso {

    private JSONArray frasesOrdenadas;
    private final ArrayList<JSONObject> frasesOrdenSort = new ArrayList<>();
    private JSONArray gruposOrdenados;
    private final ArrayList<JSONObject> gruposOrdenSort = new ArrayList<>();

    private final Json json;
    private static final String TAG = "DatosDeUso";

    public DatosDeUso(Context mContext) throws FiveMbException {

        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();
        ordenarFrasesPorFrecuencia();
        ordenarGruposPorFrecuencia();
    }

    public void refrescarFrasesMasUsadas() {
        ordenarFrasesPorFrecuencia();
    }


    public List<JSONObject> getArrayListFrasesMasUsadas(int cantFrases) {

        if (cantFrases > frasesOrdenSort.size())
            cantFrases = frasesOrdenSort.size();

        return frasesOrdenSort.subList(0, cantFrases);
    }

    //metodo que se encarga de ordenar todas las frases por frecuencia y las carga en frasesOrdenadas ArrayList
    private void ordenarFrasesPorFrecuencia() {

        try {
            frasesOrdenadas = json.readJSONArrayFromFile(Constants.ARCHIVO_FRASES);

            Log.e(TAG, "ordenarFrasesPorFrecuencia: " + frasesOrdenadas.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
            Log.e(TAG, "ordenarFrasesPorFrecuencia: " + e.getMessage());
        }
        if (frasesOrdenadas != null) {
            for (int i = 0; i < frasesOrdenadas.length(); i++) {
                addPhrase(i);
            }

            Collections.sort(frasesOrdenSort, (json1, json2) -> {
                double frec1 = 0;
                double frec2 = 0;
                try {
                    frec1 = json1.getInt("frecuencia");
                    frec2 = json2.getInt("frecuencia");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error: " + json1.toString() + "\n" + json2.toString());
                }
                return json.compareTo(frec1, frec2);
            });
        }
    }

    private void addPhrase(int i){
        try {
            if(frasesOrdenadas.getJSONObject(i).has("locale")){
                if(frasesOrdenadas.getJSONObject(i).getString("locale").equalsIgnoreCase(ConfigurarIdioma.getLanguaje()))
                    frasesOrdenSort.add(frasesOrdenadas.getJSONObject(i));
            }else{
                frasesOrdenSort.add(frasesOrdenadas.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<JSONObject> getFrasesOrdenadas() {
        return frasesOrdenSort;
    }


    private void ordenarGruposPorFrecuencia() throws FiveMbException {

        try {
            gruposOrdenados = json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (gruposOrdenados != null) {
            for (int i = 0; i < gruposOrdenados.length(); i++) {
                try {
                    gruposOrdenSort.add(gruposOrdenados.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                gruposOrdenSort.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject json1, JSONObject json2) {
                        double frec1 = 0;
                        double frec2 = 0;
                        try {
                            frec1 = json1.getInt("frecuencia");
                            frec2 = json2.getInt("frecuencia");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        return json.compareTo(frec1, frec2);
                    }
                });
            }else{
                Collections.sort(gruposOrdenSort, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject json1, JSONObject json2) {
                        double frec1 = 0;
                        double frec2 = 0;
                        try {
                            frec1 = json1.getInt("frecuencia");
                            frec2 = json2.getInt("frecuencia");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        return json.compareTo(frec1, frec2);
                    }
                });
            }
        }
    }

    public ArrayList<JSONObject> getGruposOrdenados() {
        return gruposOrdenSort;
    }

    /**
     * @return the promedio
     * */
    public float getPromedioPictoPorFrase() {
        int sumPictosPorFrase = 0;
        int totalFrases = getCantidadDeFrasesTotal();
        try{
            for (int i = 0; i < frasesOrdenadas.length(); i++) {
                try {
                    JSONArray array = frasesOrdenadas.getJSONObject(i).getJSONObject("complejidad").getJSONArray("pictos componentes");
                    sumPictosPorFrase += array.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return (float) sumPictosPorFrase / (float) frasesOrdenadas.length();
        }catch (Exception ex){
            return 0;
        }
    }

    public ArrayList<JSONObject> getGrupoConPuntaje() {
        ArrayList<JSONObject> aux = new ArrayList<>();

        for (int i = 0; i < gruposOrdenados.length(); i++) {
            try {
                gruposOrdenados.getJSONObject(i).get("juegos");
                aux.add(gruposOrdenados.getJSONObject(i));
            } catch (JSONException e) {
                Log.d("DataInforme", "No tiene puntaje");

            }
        }
        return aux;
    }

    //este metodo devuelve la cantidad de frases hechas
    public int getCantidadDeFrasesTotal() {
        int CantDeFrases = 0;
        if(frasesOrdenadas!=null)
            for (int i = 0; i < frasesOrdenadas.length(); i++) {
                try {
                    CantDeFrases += frasesOrdenadas.getJSONObject(i).getInt("frecuencia");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return CantDeFrases;
    }

    //este metodo devuelve la cantidad de frases hechas en un mes particular

//    public int getCantidadDeFrasesGeneradasPorSemana(int semana) {
//        int CantDeFrases = 0;
//
//
//        for (int i = 0; i < frasesOrdenadas.size(); i++) {
//            try {
//                JSONArray array = frasesOrdenadas.get(i).getJSONArray("fecha");
//                for (int j = 0; j < array.length(); j++) {
//                    if (array.getLong(j)>timeStampMesAnterior && array.getLong(j)<timeStampMesSiguiente)
//                        CantDeFrases++;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return CantDeFrases;
//    }

    //este metodo devuelve la cantidad de frases hechas en un mes particular
    public int getCantidadDeFrasesGeneradasPorMes(int mes, int anio) {
        int CantDeFrases = 0;
        long timeStampMesAnterior, timeStampMesSiguiente;
        //Tiempo en
        // milis desde el primero de enero de ese anio
        if (mes - 1 < 0) {
            timeStampMesAnterior = (new GregorianCalendar(anio - 1, 12, 1).getTimeInMillis()) / 1000;
        } else
            timeStampMesAnterior = (new GregorianCalendar(anio, mes, 1).getTimeInMillis()) / 1000;

        if (mes + 1 >= 12) {
            timeStampMesSiguiente = (new GregorianCalendar(anio + 1, 1, 1).getTimeInMillis()) / 1000;
        } else
            timeStampMesSiguiente = (new GregorianCalendar(anio, mes + 1, 1).getTimeInMillis()) / 1000;

        for (int i = 0; i < frasesOrdenadas.length(); i++) {
            try {
                JSONArray array = frasesOrdenadas.getJSONObject(i).getJSONArray("fecha");
                for (int j = 0; j < array.length(); j++) {
                    if (array.getLong(j) > timeStampMesAnterior && array.getLong(j) < timeStampMesSiguiente)
                        CantDeFrases++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return CantDeFrases;
    }

    //este metodo devuelve la cantidad de frases hechas en un mes particular
    public int getCantidadDeFrasesGeneradasUltimoDias(int CantDeDias) {
        int CantDeFrases = 0;
        long timeStamp = (Calendar.getInstance().getTimeInMillis() - TimeUnit.DAYS.toMillis
                (CantDeDias)) / 1000;
        if(frasesOrdenadas!=null)
            for (int i = 0; i < frasesOrdenadas.length(); i++) {
                try {
                    JSONArray array = frasesOrdenadas.getJSONObject(i).getJSONArray("fecha");
                    for (int j = 0; j < array.length(); j++) {
                        if (array.getLong(j) >= timeStamp)
                            CantDeFrases++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return CantDeFrases;
    }

    public int getCantidadDeFrasesGeneradasPorAnio(int anio) {
        int CantDeFrases = 0;
        long timeStampComienzo, timeStampFin;

        timeStampComienzo = (new GregorianCalendar(anio, 0, 1).getTimeInMillis()) / 1000;

        timeStampFin = (new GregorianCalendar(anio, 11, 31).getTimeInMillis()) / 1000;

        for (int i = 0; i < frasesOrdenadas.length(); i++) {
            try {
                JSONArray array = frasesOrdenadas.getJSONObject(i).getJSONArray("fecha");
                for (int j = 0; j < array.length(); j++) {
                    if (array.getLong(j) > timeStampComienzo && array.getLong(j) < timeStampFin)
                        CantDeFrases++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return CantDeFrases;
    }


    //Array [dias de la semana] [Ma;ana, Tarde, Noche]
    public int[][] getCantidadFrasesPorDia() {
        int[][] matriz = new int[8][3];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                matriz[i][j] = 0;
            }
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int dia = 0;
        int hora = 0;
        if(frasesOrdenadas!=null){
            for (int i = 0; i < frasesOrdenadas.length(); i++) {
                try {
                    JSONArray array = frasesOrdenadas.getJSONObject(i).getJSONArray("fecha");
                    for (int j = 0; j < array.length(); j++) {
                        gregorianCalendar.setTime(new Date(array.getLong(j) * 1000));
                        dia = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        hora = getMomentoDelDia(gregorianCalendar);
                        matriz[dia][hora]++;
                        Log.d("FrasesPorDia", "Mes: " + gregorianCalendar.get(Calendar.MONTH) + "Dia: " + gregorianCalendar.get(Calendar.DAY_OF_WEEK) + "Hora: " + gregorianCalendar.get(Calendar.HOUR_OF_DAY));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "getCantidadFrasesPorDia_Error:"+e.getMessage() );
                    try {
                        long lonG=frasesOrdenadas.getJSONObject(i).getLong("fecha");
                        gregorianCalendar.setTime(new Date(lonG * 1000));
                        dia = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        hora = getMomentoDelDia(gregorianCalendar);
                        matriz[dia][hora]++;
                        Log.d("FrasesPorDia", "Mes: " + gregorianCalendar.get(Calendar.MONTH) + "Dia: " + gregorianCalendar.get(Calendar.DAY_OF_WEEK) + "Hora: " + gregorianCalendar.get(Calendar.HOUR_OF_DAY));

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return matriz;
    }



    private int getMomentoDelDia(GregorianCalendar gregorianCalendar) {
        if (gregorianCalendar.get(Calendar.HOUR_OF_DAY) > 5 && gregorianCalendar.get(Calendar.HOUR_OF_DAY) < 11)
            return 0;
        else if (gregorianCalendar.get(Calendar.HOUR_OF_DAY) > 11 && gregorianCalendar.get(Calendar.HOUR_OF_DAY) < 19)
            return 1;
        else return 2;
    }



}