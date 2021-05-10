package com.stonefacesoft.ottaa.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONutils {

    /**
     * @param nombreEnIngles name of the pictogram or group
     * @param jsonArray Jsonarray where searched  a group or a pictogram
     * @return id from a pictogram or a group
     * */
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

    //TODO falta el testing
    public static void crearRelacion(JSONArray relacion, int id) throws JSONException {
        JSONObject aux = new JSONObject();
        aux.put("id", id);
        aux.put("frec", 1);
        relacion.put(aux);
    }

    //TODO falta el testing
    public void aumentarFrec(JSONObject padre, JSONObject opcion) {
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

}
