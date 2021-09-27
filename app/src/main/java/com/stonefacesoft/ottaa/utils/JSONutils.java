package com.stonefacesoft.ottaa.utils;

import android.util.Log;

import com.stonefacesoft.ottaa.JSONutils.SearchObjects;
import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class JSONutils {

    /**
     * @param nombreEnIngles name of the pictogram or group
     * @param jsonArray Jsonarray where searched  a group or a pictogram
     * @return id from a pictogram or a group
     * */
    private final static String TAG = "JSONUtils";
    private static JSONutils _JsoNutils;

    public static synchronized JSONutils getInstance(){
        if(_JsoNutils == null)
            _JsoNutils = new JSONutils();
        return _JsoNutils;
    }


    //TODO ver porque tiene q ser static
    public static int getIDfromNombre(String nombreEnIngles, JSONArray jsonArray) throws JSONException {
        int id = -1;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getJSONObject("texto").getString("en").equals(nombreEnIngles)) {
                id = object.getInt("id");
                break;
            }
        }
        return id;
    }

    /**
     * Convert the JsonArray String to an ArrayList
     * */
    public static ArrayList<JSONObject> stringToArrayList(String contenido) {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        if (contenido != null) {
            try {
                JSONArray array = new JSONArray(contenido);
                for (int i = 0; i < array.length(); i++) {
                    arrayList.add(array.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Json_stringToArray", "contenido = null");
        }
        return arrayList;
    }

    /**
     * @param object pictogram or group
     * @return  The name of a pictogram or group
     * */
    public static String getNombre(JSONObject object, String lang) {
        try {
            return object.getJSONObject("texto").getString(lang);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * @param object Pictogram
     * @return the kind of pictogram in the Fitzgerald Key
     * */
    public static int getTipo(JSONObject object) {
        try {
            return object.getInt("tipo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getWordType(JSONObject object) {
        try {
            return object.getInt("wordTYPE");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return 0;
    }

    public static void setHijosGrupo2(JSONArray mJsonArrayGrupoPadre, JSONArray mJsonArrayHijosGrupo, int boton) {
        try {
            mJsonArrayGrupoPadre.getJSONObject(boton).put("relacion", mJsonArrayHijosGrupo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setPosicion(JSONObject ob, Posicion posicion) {
        JSONArray arrayPosicion = new JSONArray();
        try {
            ob.getJSONArray(Constants.UBICACION);
            arrayPosicion = ob.getJSONArray(Constants.UBICACION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayPosicion.put(arrayPosicion.length(), posicion);
            ob.put(Constants.UBICACION, arrayPosicion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setNombre(JSONObject object, String textoOriginal, String textoTraducido, String idiomaOriginal, String idomaTraducion) {
        try {
            if (!idiomaOriginal.equals(idomaTraducion)) {
                object.getJSONObject("texto").put(idomaTraducion, textoTraducido);
                object.getJSONObject("texto").put(idiomaOriginal, textoOriginal);
            } else {
                object.getJSONObject("texto").put(idiomaOriginal, textoOriginal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setTipo(JSONObject object, int tipo) {
        try {
            object.put("tipo", tipo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setImagen(JSONObject object, String img, String url, String pushKey) {
        try {
            object.getJSONObject("imagen").put("pictoEditado", img);
            object.getJSONObject("imagen").put("urlFoto", url);
            object.getJSONObject("imagen").put("pushKey", pushKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getImagen(JSONObject object) throws JSONException {
        if (object != null) {
            String picto = null;
            if (object.has("imagen")) {
                try {
                    JSONObject icon = object.getJSONObject("imagen");
                    if (icon.has("pictoEditado") && !icon.getString("pictoEditado").isEmpty()) {
                        picto = icon.getString("pictoEditado");
                        return new JSONObject().put("type", 1).put("picto", picto);
                    } else if (!icon.has("pictoEditado") || (icon.has("pictoEditado") && icon.getString("pictoEditado").isEmpty())) {
                        picto = icon.getString("picto");
                        return new JSONObject().put("type", 2).put("picto", picto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return new JSONObject().put("type", 3).put("picto", null);
                }
            } else
                return new JSONObject().put("type", 3).put("picto", null);
        }
        return new JSONObject().put("type", 3).put("picto", null);
    }

    public static void desvincularJson(JSONObject padre, int id) {
        JSONArray jsonArray;
        JSONArray list = new JSONArray();
        try {
            jsonArray = padre.getJSONArray("relacion");
            for (int i = 0; i < jsonArray.length(); i++) {
                //Excluding the item at position
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.getInt("id") != id) {
                    list.put(jsonArray.get(i));
                }
            }
            padre.put("relacion", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setHorario(JSONObject ob, Horario horario) {
        JSONArray arrayHora = new JSONArray();
        try {
            if (ob.getJSONArray(Constants.HORA) != null) {
                arrayHora = ob.getJSONArray(Constants.HORA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            boolean tienehora = false;
            for (int i = 0; i < arrayHora.length(); i++) {
                if (arrayHora.getString(i).contentEquals(horario.name())) {
                    tienehora = true;
                }
            }
            if (!tienehora)
                arrayHora.put(arrayHora.length(), horario);
            ob.put(Constants.HORA, arrayHora);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setSexo(JSONObject ob, Sexo sexo) {
        JSONArray arraySexo = new JSONArray();
        try {
            if (ob.getJSONArray(Constants.SEXO) != null) {
                arraySexo = ob.getJSONArray(Constants.SEXO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arraySexo.put(arraySexo.length(), sexo);
            ob.put(Constants.SEXO, arraySexo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setEdad(JSONObject ob, Edad edad) {
        JSONArray arrayEdad = new JSONArray();
        try {
            if (ob.getJSONArray(Constants.EDAD) != null) {
                arrayEdad = ob.getJSONArray(Constants.EDAD);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayEdad.put(arrayEdad.length(), edad);
            ob.put(Constants.EDAD, arrayEdad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setClima(JSONObject ob, Clima clima) {
        JSONArray arrayClima = new JSONArray();
        try {
            if (ob.getJSONArray(Constants.CLIMA) != null) {
                arrayClima = ob.getJSONArray(Constants.CLIMA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayClima.put(arrayClima.length(), clima);
            ob.put(Constants.CLIMA, arrayClima);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void crearRelacion(JSONArray relacion, int id) throws JSONException {
        JSONObject aux = new JSONObject();
        aux.put("id", id);
        aux.put("frec", 1);
        relacion.put(aux);
    }

    public static void aumentarFrec(JSONObject padre, JSONObject opcion) {
        boolean nuevo = true;
        try {
            JSONArray array = padre.getJSONArray("relacion");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("id") == opcion.getInt("id")) {
                    int frec = object.getInt("frec") + 1;
                    object.put("frec", frec);
                    nuevo = false;
                    break;
                }
            }
            if (nuevo) {
                crearRelacion(array, opcion.getInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject crearJson(int id, String locale, String textoEsp, String textoEn, JSONArray relacion, String img, int tipo) {
        JSONObject picto = new JSONObject();
        JSONObject texto = new JSONObject();
        JSONObject imagen = new JSONObject();

        try {
            texto.put(locale, textoEsp);
            texto.put("en", textoEn);
            imagen.put("picto", img);
            picto.put("id", id);
            picto.put("texto", texto);
            picto.put("tipo", tipo);
            picto.put("imagen", imagen);
            picto.put("relacion", relacion);

        } catch (JSONException error) {
            error.getMessage();
        }
        return picto;
    }

    public static void relacionarConGrupo2(JSONArray jsonArrayAVincular, int padre, int picto) throws JSONException {
        JSONutils.crearRelacion(jsonArrayAVincular.getJSONObject(padre).getJSONArray("relacion"), picto);
    }

    public static int getId(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("id");
    }

    public static void addToAllRelacion2(JSONArray arrayListGrupos, int padre) {
        try {
            for (int i = 0; i < arrayListGrupos.length(); i++) {
                if (arrayListGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("ALL") || arrayListGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("EveryThing")) {
                    JSONArray relacion = arrayListGrupos.getJSONObject(i).getJSONArray("relacion");
                    relacion.put(arrayListGrupos.getJSONObject(padre).getJSONArray("relacion").get(arrayListGrupos.getJSONObject(padre).getJSONArray("relacion").length() - 1));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray crearPicto(JSONArray mArrayListGrupo, JSONArray mArrayListTodosLosPictos, String locale, int padre, String textoEsp, String textoEn, String img, int tipo, String url, String pushKey) {
        Date date = new Date(System.currentTimeMillis());
        long date1 = date.getTime();
        int id = (int) date1;

        mArrayListTodosLosPictos.put(JSONutils.crearJson(id, locale , textoEsp, textoEn, new JSONArray(), img, tipo));
        try {
            JSONutils.relacionarConGrupo2(mArrayListGrupo, padre, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONutils.setImagen(mArrayListTodosLosPictos.getJSONObject(mArrayListTodosLosPictos.length() - 1), img, url, pushKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addToAllRelacion2(mArrayListGrupo, padre);
        return mArrayListTodosLosPictos;
    }

    public static JSONArray crearGrupo(JSONArray jsonArrayGrupos, String locale, String textoEsp, String textoEn, String img, int tipo, String url, String pushKey) throws JSONException {
        Date date = new Date(System.currentTimeMillis());
        long date1=date.getTime();
        int id=(int) date1 ;
        jsonArrayGrupos.put(jsonArrayGrupos.length(), JSONutils.crearJson(id, locale, textoEsp, textoEn, new JSONArray(), img, tipo));
        JSONutils.setImagen(jsonArrayGrupos.getJSONObject(jsonArrayGrupos.length() - 1), img, url, pushKey);
        return jsonArrayGrupos;
    }

    public static JSONArray getHijosGrupo2(JSONArray jsonArrayTodosLosPictos,JSONObject jsonObject) throws JSONException {

        JSONArray relacion = jsonObject.getJSONArray("relacion");
        JSONArray hijo = new JSONArray();
        for (int i = 0; i < relacion.length(); i++) {
            try {
                if (!relacion.getJSONObject(i).isNull("id"))
                    hijo.put(hijo.length(), getPictoFromId2(jsonArrayTodosLosPictos,relacion.getJSONObject(i).getInt("id")));
            } catch (JSONException ex) {
                ex.getMessage();
            }
        }
        return hijo;
    }

    public static JSONObject getPictoFromId2(JSONArray jsonArray, int idABuscar) {
        try{
            int position = new SearchObjects().searchItemById(jsonArray, idABuscar);
            if(position!=-1)
                return jsonArray.getJSONObject(position);
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
        int position=new SearchObjects().searchItemById(jsonArray,1024);
        for (int i = position; i <jsonArray.length() ; i++) {
            try {
                JSONObject object=jsonArray.getJSONObject(i);
                if(getId(object)==idABuscar)
                    return jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONArray getHijosGrupo2(JSONObject object, JSONArray jsonArrayTodosLosPictos) {
        JSONArray array = null;
        JSONArray hijo = new JSONArray();
        try {
            if (object.has("relacion")) {
                array = object.getJSONArray("relacion");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject child=getPictoFromId2(jsonArrayTodosLosPictos, array.getJSONObject(i).getInt("id"));
                        if(child!=null)
                            hijo.put(child);
                    } catch (JSONException ex) {
                        ex.getMessage();
                    }
                }
            }
            return hijo;
        } catch (JSONException e) {
            e.printStackTrace();
            return hijo;
        }
    }

    /**
     * This method run the array an edit the group or the pictogram
     * @param arrayListAEditar JsonArray to modify the pictogram
     * @param jsonObjectAEditar pictogram or group to modify
     * */
    public static JSONArray setJsonEditado2(JSONArray arrayListAEditar, JSONObject jsonObjectAEditar) throws JSONException {
        for (int i = 0; i < arrayListAEditar.length(); i++) {
            JSONObject object = arrayListAEditar.getJSONObject(i);
            if (object.getInt("id") == jsonObjectAEditar.getInt("id")) {
                arrayListAEditar.put(i, jsonObjectAEditar);
                break;
            }
        }
        return arrayListAEditar;
    }

    public static double score(JSONObject json, boolean esSugerencia, JSONArray jsonArrayTodosLosPictos, String agenda, String sexo, String hora, String edad, String posicion) {
        int frec = 0, agendaUsuario = 0, gps = 0, horaDelDia = 0, id = 0, present = 0, sexoUsuario,
                edadUsuario = 0;
        final double pesoFrec = 2, pesoAgenda = 8, pesoGps = 12, pesoHora = 50, pesoPresent = 100,
                pesoSexo = 3, pesoEdad = 5;
        double score;
        JSONObject original = null;

        try {
            frec = 0;
            if(json.has("frec"))
                frec = json.getInt("frec");
            id = json.getInt("id");

            original = getPictoFromId2(jsonArrayTodosLosPictos,id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        agendaUsuario = tieneAgenda(original, agenda);
        horaDelDia = tieneHora(original, hora);
        gps = tienePosicion(original, posicion);
        sexoUsuario = tieneSexo(original, sexo);
        edadUsuario = tieneEdad(original, edad);

        Log.e(TAG, "score: Agenda:"+agendaUsuario+" horaDeldia:"+horaDelDia +" gps:"+ gps+" sexoUsuario" +sexoUsuario +" edad :"+ edadUsuario);


        if (esSugerencia) {
            gps = 0;
            agendaUsuario = 0;
            horaDelDia = 0;
        }

        score = (frec * pesoFrec) + (agendaUsuario * pesoAgenda) + (gps * pesoGps) + (horaDelDia *
                pesoHora) + (sexoUsuario * pesoSexo) + (edadUsuario * pesoEdad);
        return score;
    }

//    public void setAgenda(JSONObject ob) {
//        JSONArray arrayAgenda = new JSONArray();
//        try {
//            if (ob.getJSONArray(Constants.CALENDARIO) != null) {
//                arrayAgenda = ob.getJSONArray(Constants.CALENDARIO);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            arrayAgenda.put(arrayAgenda.length(), JSONutils.getNombre(ob,sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")).toUpperCase());
//            ob.put(Constants.CALENDARIO, arrayAgenda);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public static int tieneSexo(JSONObject json, String sexo) {
        try {
            JSONArray array = json.getJSONArray(Constants.SEXO);
            for (int i = 0; i < array.length(); i++) {
                if (sexo.equalsIgnoreCase(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int tieneAgenda(JSONObject json, String agenda) {
        try {
            JSONArray array = json.getJSONArray(Constants.CALENDARIO);
            for (int i = 0; i < array.length(); i++) {
                if (agenda.equals(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int tieneHora(JSONObject json, String hora) {
        try {
            JSONArray array = json.getJSONArray(Constants.HORA);
            for (int i = 0; i < array.length(); i++) {
                if (hora.toString().equals(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int tieneEdad(JSONObject json, String edad) {
        try {
            JSONArray array = json.getJSONArray(Constants.EDAD);
            for (int i = 0; i < array.length(); i++) {
                if (edad.equals(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    /**
     * Return 1 if the position exist.
     * Else return -1
     * Use that method to make a consult about the location
     * */
    public static int tienePosicion(JSONObject json, String posicion) {
        try {
            JSONArray array = json.getJSONArray(Constants.UBICACION);
            for (int i = 0; i < array.length(); i++) {
                if (posicion.equals(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }



}
