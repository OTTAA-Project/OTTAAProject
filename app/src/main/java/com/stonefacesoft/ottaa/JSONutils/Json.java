package com.stonefacesoft.ottaa.JSONutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Size;

import com.google.gson.JsonObject;
import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.Interfaces.FindPictogram;
import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.JsonUtils.JSONObjectManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * @author morro
 * @author Hector Costa
 * @author  Gonzalo Juarez
 * @since 8/5/2016.
 * @version 2.0
 * Edited by Hector on 27/11/2018
 * Edited by Hector on 04/01/2019
 *<h3>Objectives</h3>
 * <p>Load,Read and process all Json Object</p>
 * <h3>How to declare</h3>
 *<code>Json json=Json.getInstance();</code>
 * <br>
 * <code>json.setmContext(Context);</code>
 * <h3>Examples of Implementation</h3>
 *  <h4>Get id</h4>
 *  <code>{@link JSONObject object=getmJSONArrayTodosLosPictos.getJSONObject(position);}</code><br>
 *  <code>int id=json.getid(object);</code>
 *  <h4>How to add pictogram</h4>
 *  <code>json.crearPicto(mArrayListGrupo, mArrayListTodosLosPictos,idPadre,textLanguage,textoEnglis,UrlImage,tipe,urlFirebase,pushKeyPictogram) </code>
 *  <h4>How to edit pictogram</h4>
 *
 *  <code> json.setmJSONArrayTodosLosPictos(  {@link #setJsonEditado2(JSONArray, JSONObject)});</code><br>
 *  {@link #setmJSONArrayTodosLosPictos(JSONArray)}
 *
 */
public class Json implements FindPictogram {


    private static final String TAG = "Json";


    // Arraylist de Json
    private ArrayList<JSONObject> mArrayListTodasLasFotosBackup;
    //Json singleton
    private static volatile Json _instance;

    private JSONArray mJSONArrayPictosSugeridos;
    private JSONArray mJSONArrayTodosLosGrupos;
    private JSONArray mJSONArrayTodasLasFrases;
    private JSONArray mJSONArrayTodasLasFrasesJuegos;
    private JSONArray mJSONArrayTodosLosPictos;
    private JSONArray mJSONArrayTodasLasFotosBackup;
    private JSONArray mJSonArrayJuegos;
    private JSONArray mJSonArrayDescripciones;
    private JSONArray mJSonArrayFrasesFavoritas;


    private String eventoActual = "none";
    private final int idGps = 0;
    private Context mContext;
    private String textoTags;
    private static boolean fallaJson;
    private boolean noTieneRelacionHijo = false; //bandera que indica si tiene o no hijos

    //Declaro el manejador de preferencia
    protected static SharedPreferences sharedPrefsDefault;
    private String mListPlaceName;

    private int cantFallas;
    //JSONArray


    //JSON es ahora un singleton
    private Json() {

        this.mJSONArrayTodosLosPictos = new JSONArray();
        this.mJSONArrayPictosSugeridos = new JSONArray();
        this.mJSONArrayTodosLosGrupos = new JSONArray();
        this.mJSONArrayTodasLasFrases = new JSONArray();
        this.mJSONArrayTodasLasFotosBackup = new JSONArray();
        this.mJSONArrayTodasLasFrasesJuegos=new JSONArray();
        this.mJSonArrayJuegos=new JSONArray();
        this.mJSonArrayFrasesFavoritas=new JSONArray();

        //Implemento el manejador de preferencias

    }
    /**
     * Singleton Instance
     * */
    public synchronized static Json getInstance() {
        if (_instance == null) {

            synchronized (Json.class) {
                //chequeamos por segunda vez si la instancia no es nula
                //Si no existe una instancia disponible  , creamos una
                if (_instance == null) {
                    _instance = new Json();

                }
            }
        }

        return _instance;
    }

    /**
     * set the context to the json object.
     * @param context Activity or context where implements the object
     * */
    public void setmContext(Context context) {
        this.mContext = context;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    /**
     * Load all the jsonArray objects
     * */
    public void initJsonArrays() throws JSONException, FiveMbException {
        //Cargo por unica vez los archivos al array

        mJSONArrayTodosLosPictos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
        mJSONArrayTodosLosGrupos = readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
        GroupManagerClass.getInstance().setmGroup(mJSONArrayTodosLosGrupos);
        mJSONArrayTodasLasFrases = readJSONArrayFromFile(Constants.ARCHIVO_FRASES);
        mJSONArrayPictosSugeridos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE);
        mJSonArrayFrasesFavoritas=readJSONArrayFromFile(Constants.ARCHIVO_FRASES_FAVORITAS);
        mJSonArrayJuegos=readJSONArrayFromFile(Constants.ARCHIVO_JUEGO);
        mJSonArrayDescripciones=readJSONArrayFromFile(Constants.ARCHIVO_JUEGO_DESCRIPCION);
        mJSONArrayTodasLasFrasesJuegos = readJSONArrayFromFile(Constants.ARCHIVO_FRASES_JUEGOS);
//        readFile(mJSONArrayTodosLosPictos,Constants.ARCHIVO_PICTOS);
//        readFile(mJSONArrayTodosLosGrupos,Constants.ARCHIVO_GRUPOS);
//        readFile(mJSONArrayTodasLasFrases,Constants.ARCHIVO_FRASES);
//        readFile(mJSONArrayPictosSugeridos,Constants.ARCHIVO_PICTOS_DATABASE);
//        readFile(mJSonArrayJuegos,Constants.ARCHIVO_JUEGO);
//        readFile(mJSONArrayTodasLasFrasesJuegos,Constants.ARCHIVO_FRASES_JUEGOS);

        //cambiarIdiomaPorIdioma();

    }

    private void readFile(String fileName) {
        try {
            readJSONArrayFromFile(fileName);
        } catch (JSONException | FiveMbException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the JsonArray
     */
    public void refreshJsonArrays() throws JSONException, FiveMbException {
        initJsonArrays();
    }
    /**
     *
     * */
    public JSONArray getmJSONArrayTodosLosPictos() {
        return mJSONArrayTodosLosPictos;
    }

    public void setmJSONArrayTodosLosPictos(JSONArray mJSONArrayTodosLosPictos) {
        this.mJSONArrayTodosLosPictos = mJSONArrayTodosLosPictos;
    }

    public JSONArray getmJSONArrayPictosSugeridos() {
        return mJSONArrayPictosSugeridos;
    }

    public void setmJSONArrayPictosSugeridos(JSONArray mJSONArrayPictosSugeridos) {
        this.mJSONArrayPictosSugeridos = mJSONArrayPictosSugeridos;
    }

    public JSONArray getmJSONArrayTodasLasFotosBackup() {
        return mJSONArrayTodasLasFotosBackup;
    }

    public void setmJSONArrayTodasLasFotosBackup(JSONArray mJSONArrayTodasLasFotosBackup) {
        this.mJSONArrayTodasLasFotosBackup = mJSONArrayTodasLasFotosBackup;
    }

    public synchronized JSONArray getmJSONArrayTodosLosGrupos() {
        return GroupManagerClass.getInstance().getmGroup();
    }

    public synchronized void setmJSONArrayTodosLosGrupos(JSONArray mJSONArrayTodosLosGrupos) {
        this.mJSONArrayTodosLosGrupos = mJSONArrayTodosLosGrupos;
        GroupManagerClass.getInstance().setmGroup(this.mJSONArrayTodosLosGrupos);
    }

    public JSONArray getmJSONArrayTodasLasFrases() {
        return mJSONArrayTodasLasFrases;
    }

    public JSONArray getmJSONArrayTodasLasFrasesJuegos() {
        return mJSONArrayTodasLasFrasesJuegos;
    }

    public JSONArray getmJSonArrayJuegos() {
        return mJSonArrayJuegos;
    }

    public void setmJSONArrayTodasLasFrases(JSONArray mJSONArrayTodasLasFrases) {
        this.mJSONArrayTodasLasFrases = mJSONArrayTodasLasFrases;
    }

    public void setmJSONArrayTodasLasFrasesJuegos(JSONArray mJSONArrayTodasLasFrases) {
        this.mJSONArrayTodasLasFrasesJuegos = mJSONArrayTodasLasFrases;
    }

    public void setmJSonArrayJuegos(JSONArray mJSonArrayJuegos) {
        this.mJSonArrayJuegos = mJSonArrayJuegos;
    }

    public void setmJSonArrayFrasesFavoritas(JSONArray mJSonArrayFrasesFavoritas) {
        this.mJSonArrayFrasesFavoritas = mJSonArrayFrasesFavoritas;
    }

    /**
     * @param nombre name of the pictogram or group
     * @param jsonArray Jsonarray where searched  a group or a pictogram
     * @return id from a pictogram or a group
     * */
    public static int getIDfromNombre(String nombre, JSONArray jsonArray) throws JSONException {
        int id = -1;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getJSONObject("texto").getString("en").equals(nombre)) {
                id = object.getInt("id");
                break;
            }

        }
        return id;
    }

    /**
     * Convert the JsonArray String to an ArrayList
     * */
    public ArrayList<JSONObject> stringToArrayList(String contenido) {
        ArrayList<JSONObject> jsonADevolver = new ArrayList<>();
        if (contenido != null) {
            try {
                JSONArray array = new JSONArray(contenido);
                for (int i = 0; i < array.length(); i++) {
                    jsonADevolver.add(array.getJSONObject(i));
                }
                Log.d("Json_stringToArray", "tenemos el array");
            } catch (JSONException e) {
                Log.e("Json_stringToArray", "ERROR NO tenemos el array");
                e.printStackTrace();
            }
        } else {
            Log.d("Json_stringToArray", "contenido = null");
        }

        return jsonADevolver;
    }


    public boolean tieneNombre(JSONObject object){
        try {
            if(object.getJSONObject("texto").has(sharedPrefsDefault.getString("idioma","en"))){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return false;
    }
    /**
     * @param object pictogram or group
     * @return  The name of a pictogram or group
     * */
    public String getNombre(JSONObject object) {
        try {
            return object.getJSONObject("texto").getString(sharedPrefsDefault.getString(mContext
                    .getString(R.string.str_idioma), "en"));


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error_Json_getNombre", "1 :" + e.toString());
           fallaJson = true;
        }
        return "error";
    }

    public String getIdioma(){
        return sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en");
    }

    public String getNombre(JSONObject object, String idioma) {
        try {
            return object.getJSONObject("texto").getString(idioma);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getNombre: Error: 2" + e.toString());
            fallaJson = true;
        }
        return null;
    }
    /**
     * @param object Pictogram
     * @return the kind of pictogram in the Fitzgerald Key
     * */
    public int getTipo(JSONObject object) {
        try {
            return object.getInt("tipo");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getTipo: " +e.getMessage());
         //
            fallaJson = true;
        }
        return 0;
    }

    public int getWorlType(JSONObject object) {
        try {
            return object.getInt("wordTYPE");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getWorlType: " + e.getMessage());
            //
            fallaJson = true;
        }
        return 0;
    }



    /**
     * @deprecated  this  has been replaced by the class {@link com.stonefacesoft.ottaa.utils.Games.Juego}
     * @return  the score in the whichIsThePicto game
     * */
    public double getPuntaje(JSONObject object,int id) {
        JSONArray aux;
        try {
                aux = object.getJSONArray("juegos"+id);
            return aux.getJSONObject(0).getDouble("puntaje");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAciertos(JSONObject object,int id) {
        JSONArray aux;
        try {
            aux = object.getJSONArray("juegos"+id);
            return aux.getJSONObject(0).getInt("aciertos");
        } catch (JSONException e) {
            Log.e(TAG, "getAciertos: " + e.getMessage());

        }
        return 0;
    }

    public int getIntentos(JSONObject object,int id) {
        JSONArray aux;
        try {
            aux = object.getJSONArray("juegos"+id);
            return aux.getJSONObject(0).getInt("intentos");
        } catch (JSONException e) {
            Log.e(TAG, "getIntentos: " + e.getMessage());
            e.printStackTrace();

        }
        return 0;
    }


    public ArrayList<JSONObject> setHijosGrupo(ArrayList<JSONObject> mArrayListGrupoPadre, ArrayList<JSONObject> mArrayListHijos, int boton) {
        try {
            JSONArray jsonArray = new JSONArray(mArrayListHijos);
            mArrayListGrupoPadre.get(boton).put("relacion", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }


    public JSONArray setHijosGrupo2(JSONArray mJsonArrayGrupoPadre, JSONArray mJsonArrayHijosGrupo, int boton) {

        try {
            mJsonArrayGrupoPadre.getJSONObject(boton).put("relacion", mJsonArrayHijosGrupo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addAraasacPictogramFromInternet(JSONObject pictogram){
        mJSONArrayTodosLosPictos.put(pictogram);
    }


    private static JsonObject padre(JsonObject padre) {
        return padre;
    }

    public ArrayList<JSONObject> getmArrayListTodasLasFotosBackup() throws FiveMbException {
        mArrayListTodasLasFotosBackup.clear();
        String textoFotosBackup = readFromFile(Constants.ARCHIVO_FOTO_BACKUP);

        if (textoFotosBackup != null) {
            try {
                JSONArray array = new JSONArray(textoFotosBackup);
                for (int i = 0; i < array.length(); i++) {
                    mArrayListTodasLasFotosBackup.add(array.getJSONObject(i));
                }
                Log.d(TAG, "getmArrayListTodasLasFotosBackup: Tenemos el array");
            } catch (JSONException e) {
                Log.e(TAG, "getmArrayListTodasLasFotosBackup: No tenemos el array");
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "getmArrayListTodasLasFotosBackup: Backup Error");
        }

        return mArrayListTodasLasFotosBackup;
    }

    //TODO borrar este metodo
    private void cargarPriordad(JSONObject ob, int id) {
        try {
            ob.put("agenda", id);
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    //TODO borrar este metodo
    public void inicializarAgenda2() {
        for (int i = 0; i < mJSONArrayTodosLosPictos.length(); i++) {
            try {
                cargarPriordad(mJSONArrayTodosLosPictos.getJSONObject(i), 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPosicion(JSONObject ob, Posicion posicion) {
        JSONArray arrayPosicion = new JSONArray();
        Log.d(TAG, "setPosicion: " + posicion);
        try {
            if (ob.getJSONArray(Constants.UBICACION) != null) {
                arrayPosicion = ob.getJSONArray(Constants.UBICACION);
            }
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


    public void cargarAgenda(String texto) {
        //La nueva agenda setea el valor en this y lo usa de aca.
        this.eventoActual = texto;
//        texto = texto.toUpperCase();
//        int grupo = getID(texto, mJSONArrayTodosLosGrupos);
//        if (grupo > -1) {
//            JSONObject jsonObject = mJSONArrayTodosLosGrupos.get(grupo);
//            ArrayList<JSONObject> pictosDeGrupo = getHijosGrupo(jsonObject);
//            for (JSONObject object : pictosDeGrupo) {
//                cargarPriordad(object, agenda, 1);
//            }
//        }
    }


    public void setNombre(JSONObject object, String texto) {
        try {
            object.getJSONObject("texto").put(sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en"), texto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setNombre(JSONObject object, String textoOriginal, String textoTraducido, String idiomaOriginal, String idomaTraducion) {
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

    public void setPuntaje(JSONObject jsonObject,int id, double puntaje, int intentos, int aciertos) {
        JSONArray relacion = new JSONArray();
        JSONObject aux = new JSONObject();
        try {

            aux.put("puntaje", puntaje);
            aux.put("intentos", intentos);
            aux.put("aciertos", aciertos);
            relacion.put(aux);
            jsonObject.put("juegos"+id, relacion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cargarPosicion(JSONObject ob, Posicion posicion) {
        JSONArray pos = new JSONArray();
        //json_cp_cargar:json_cargarPosicion
        Log.d(TAG, "cargarPosicion: " + posicion);
        try {
            if (ob.getJSONArray("posicion") != null) {
                pos = ob.getJSONArray("posicion");
                Log.d(TAG, "cargarPosicion: Not null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pos.put(pos.length(), posicion);
            ob.put("posicion", pos);
            Log.d(TAG, "cargarPosicion: Json pos: " + ob.getJSONArray("posicion").get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTipo(JSONObject object, int tipo) {
        try {
            object.put("tipo", tipo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setImagen(JSONObject object, String img, String url, String pushKey) {
        try {
            object.getJSONObject("imagen").put("pictoEditado", img);
            object.getJSONObject("imagen").put("urlFoto", url);
            object.getJSONObject("imagen").put("pushKey", pushKey);
            Log.d(TAG, "setImagen: " + object.getJSONObject("imagen").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUriEditedPictogram(JSONObject object){
        try {
            String picto = object.getJSONObject("imagen").getString("pictoEditado");
            return picto;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Drawable getIcono(JSONObject object) {
        if (object != null) {
            String picto = null;
            if (object.has("imagen")) {
                try {
                    JSONObject icon = object.getJSONObject("imagen");
                    if (icon.has("pictoEditado") && !icon.getString("pictoEditado").isEmpty()) {
                            return AbrirBitmap(icon);
                    } else if (!icon.has("pictoEditado")||(icon.has("pictoEditado")&&icon.getString("pictoEditado").isEmpty())) {
                        try {
                            picto =icon.getString("picto");
                            Log.d("Json_getIcono: ", "Devuelvo picto standar");
                            return mContext.getResources().getDrawable(mContext.getResources().getIdentifier(picto,
                                    "drawable", mContext.getPackageName()));
                        } catch (JSONException e) {
                            Log.e(TAG, "getIcono JsonException: ERROR al devolver picto standar " + e.toString());
                            e.printStackTrace();
                        } catch (Exception e) {
                            Log.e(TAG, "getIcono: Exception ERROR al devolver picto standar " + e.toString());

                        }
                        Log.e(TAG, "getIcono: Devuelvo NULL");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } else
                return null;
        }
        return null;
    }

    public Drawable getBitmap(String path)throws Exception{
      Drawable  d = Drawable.createFromPath(path);
        if(d!=null)
            return d;
        else{
            Log.d(TAG, "getBitmap: isEmpty");
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                bitmap = mContext.getContentResolver().loadThumbnail(Uri.parse(path),new Size(500,500),null);
            }
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            d.draw(canvas);
        }
        return d;
    }

    public Drawable getUrlBitmap(String uri)throws Exception{
        Drawable  d = null;
        Bitmap bitmap = null;
            Log.d(TAG, "getBitmap : isEmpty");
            URL url = new URL( uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode()!=200){
                Canvas canvas = new Canvas(bitmap);
                d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                d.draw(canvas);
                return d;
            }
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            try{
                bitmap = BitmapFactory.decodeStream(bis);
            }catch (OutOfMemoryError ex){
                bitmap = null;
            }
            bis.close();
            is.close();

        return d;
    }

    public Drawable AbrirBitmap(JSONObject icon) {
        UriFiles auxPathFiles=new UriFiles(mContext);
        JSONObjectManager jsonObjectManager = new JSONObjectManager();
        String path = jsonObjectManager.JsonObjectGetString(icon,"pictoEditado","JsonPictoEditado");
        String url = jsonObjectManager.JsonObjectGetString(icon,"urlFoto","JsonURLFoto");

        Drawable d = mContext.getResources().getDrawable(R.drawable.ic_agregar);
        if(!path.isEmpty()){
            try {
                d=getBitmap(path);
            } catch (Exception ex) {
                ex.printStackTrace();
                d = mContext.getResources().getDrawable(R.drawable.ic_baseline_cloud_download_24_big);
        }
        }
        return d;
    }

    public void setImagen(JSONObject object, String img) {
        try {
            object.getJSONObject("imagen").put("pictoEditado", img);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void desvincularJson(JSONObject padre, int id) {
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

    public void setHorario(JSONObject ob, Horario horario) {
        JSONArray arrayHora = new JSONArray();
        Log.d(TAG, "setHorario: Horario" + horario);
        try {
            if (ob.getJSONArray(Constants.HORA) != null) {
                arrayHora = ob.getJSONArray(Constants.HORA);
                Log.d(TAG, "setHorario: Not null");
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

    public void setSexo(JSONObject ob, Sexo sexo) {
        JSONArray arraySexo = new JSONArray();
        Log.d(TAG, "setSexo: " + sexo);
        try {
            if (ob.getJSONArray(Constants.SEXO) != null) {
                arraySexo = ob.getJSONArray(Constants.SEXO);
                Log.d(TAG, "setSexo: Not null");
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

    public void setEdad(JSONObject ob, Edad edad) {
        JSONArray arrayEdad = new JSONArray();
        Log.d(TAG, "setEdad: " + edad);
        try {
            if (ob.getJSONArray(Constants.EDAD) != null) {
                arrayEdad = ob.getJSONArray(Constants.EDAD);
                Log.d(TAG, "setEdad: Not null");
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

    public void setClima(JSONObject ob, Clima clima) {
        //TODO arreglar lo de arrayHora
        JSONArray arrayHora = new JSONArray();
        Log.d(TAG, "setClima: " + clima);
        try {
            if (ob.getJSONArray(Constants.CLIMA) != null) {
                arrayHora = ob.getJSONArray(Constants.CLIMA);
                Log.d(TAG, "setClima: Not null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setClima: " + e.getMessage());
        }
        try {
            arrayHora.put(arrayHora.length(), clima);
            ob.put(Constants.CLIMA, arrayHora);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "setClima: " + e.getMessage());
        }
    }

    private String getAgenda() {
        return eventoActual;
    }
    public void aumentarFrecGrupo(JSONObject padre){
        try {
            int pos= getPosPicto(GroupManagerClass.getInstance().getmGroup(),getId(padre));
            int frec=padre.getInt("frec");
            frec++;
            padre.put("frec",frec);
            GroupManagerClass.getInstance().getmGroup().put(pos,padre);
        } catch (JSONException e) {
            e.printStackTrace();
            int pos;
            try {
                pos = getPosPicto(GroupManagerClass.getInstance().getmGroup(),getId(padre));
                padre.put("frec",0);
                GroupManagerClass.getInstance().getmGroup().put(pos,padre);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }
        guardarJson(Constants.ARCHIVO_GRUPOS);
    }

    public void aumentarFrec(JSONObject padre, JSONObject opcion) {
        boolean nuevo = true;
        try {
            Log.d(TAG, "aumentarFrec: " + "pictoPadre " + padre.getJSONObject("texto").getString
                    (sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")));
            Log.d(TAG, "aumentarFrec: " + "opcion " + opcion.getJSONObject("texto").getString(sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")));
            JSONArray array = padre.getJSONArray("relacion");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("id") == opcion.getInt("id")) {
                    int frec=object.getInt("frec")+1;
                    Log.d(TAG, "aumentarFrec: "+frec);
                    object.put("frec",  frec);
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


    public JSONArray crearPicto(JSONArray mArrayListGrupo, JSONArray mArrayListTodosLosPictos, int padre, String textoEsp, String textoEn, String img, int tipo, String url, String pushKey) {
        Date date = new Date(System.currentTimeMillis());
        long date1 = date.getTime();
        int id = (int) date1;
        Log.d(TAG, "crearPicto: " + id);
        mArrayListTodosLosPictos.put(crearJson(id, textoEsp, textoEn, new JSONArray(), img, tipo));
        try {
            relacionarConGrupo2(mArrayListGrupo, padre, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setImagen(mArrayListTodosLosPictos.getJSONObject(mArrayListTodosLosPictos.length() - 1), img, url, pushKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addToAllRelacion2(mArrayListGrupo, padre);
        return mArrayListTodosLosPictos;
    }

    public JSONArray crearGrupo(JSONArray jsonArrayGrupos, String textoEsp, String textoEn, String img, int tipo, String url, String pushKey) throws JSONException {
        Date date = new Date(System.currentTimeMillis());
        long date1=date.getTime();
        int id=(int) date1 ;
        jsonArrayGrupos.put(jsonArrayGrupos.length(), crearJson(id, textoEsp, textoEn, new JSONArray(), img, tipo));
        setImagen(jsonArrayGrupos.getJSONObject(jsonArrayGrupos.length() - 1), img, url, pushKey);
        return jsonArrayGrupos;
    }

    public void crearGrupo(String textoEsp, String textoEn, String img, int tipo) {
        Date date = new Date(System.currentTimeMillis());
        long date1=date.getTime();
        int id=(int) date1 ;
        try {
            GroupManagerClass.getInstance().getmGroup().put(GroupManagerClass.getInstance().getmGroup().length(), crearJson(id, textoEsp, textoEn, new JSONArray(), img, tipo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(!img.isEmpty())
            setImagen(GroupManagerClass.getInstance().getmGroup().getJSONObject(GroupManagerClass.getInstance().getmGroup().length() - 1), img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONArray getHijosGrupo2(JSONObject jsonObject) throws JSONException, FiveMbException {

        mJSONArrayTodosLosPictos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
        JSONArray array = jsonObject.getJSONArray("relacion");
        JSONArray hijo = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            try {
                if (!array.getJSONObject(i).isNull("id"))
                    hijo.put(hijo.length(), getPictoFromId2(array.getJSONObject(i).getInt("id")));
            } catch (JSONException ex) {
                Log.e(TAG, "getHijosGrupo2: No se encontro el hijo en la posicion "+i );
            }

        }
        return hijo;
    }

    public JSONArray getHijosGrupo2(int pos) {

        JSONArray array = null;
        JSONArray hijo = new JSONArray();
        try {
            Log.d(TAG, "getHijosGrupo2: " + pos);
            JSONObject object = GroupManagerClass.getInstance().getmGroup().getJSONObject(pos);

            if (object.has("relacion")) {
                Log.d(TAG, "getHijosGrupo2: Hay relacion");
                array = object.getJSONArray("relacion");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject child=getPictoFromId2(array.getJSONObject(i).getInt("id"));
                        if(child!=null)
                        hijo.put(child);
                    } catch (JSONException ex) {
                        Log.e(TAG, "getHijosGrupo2: " + ex.getMessage());
                    }

                }
            }
            return hijo;
        } catch (JSONException e) {
            e.printStackTrace();
            return hijo;
        }
    }


    //Ya esta
    public ArrayList<JSONObject> crearGrupo(ArrayList<JSONObject> todosLosGrupos, String textoEsp, String textoEn, String img, int tipo, String url, String pushKey) {
        Log.d(TAG, "crearGrupo: " + " textoEsp " + textoEsp + " textoEn " + textoEn + " img " + img + " tipo " + tipo);
        Date date = new Date(System.currentTimeMillis());
        long date1=date.getTime();
        int id=(int) date1 ;
        todosLosGrupos.add(todosLosGrupos.size(), crearJson(id, textoEsp, textoEn, new JSONArray(), img, tipo));
        setImagen(todosLosGrupos.get(todosLosGrupos.size() - 1), img, url, pushKey);

        return todosLosGrupos;
    }

    protected void crearRelacion(JSONArray relacion, int id) throws JSONException {
        JSONObject aux = new JSONObject();
        aux.put("id", id);
        aux.put("frec", 1);
        relacion.put(aux);
        Log.d(TAG, "crearRelacion: " + relacion.length());

    }

    private void addToAllRelacion2(JSONArray arrayListGrupos, int padre) {
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

    public void addPictogramToAll(JSONObject object) {
        try {

            for (int i = 0; i < mJSONArrayTodosLosGrupos.length(); i++) {
                if (mJSONArrayTodosLosGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("ALL") || mJSONArrayTodosLosGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("EveryThing")) {
                    JSONArray relacion = mJSONArrayTodosLosGrupos.getJSONObject(i).getJSONArray("relacion");
                    relacion.put(relacion.length(),object);
                    break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject crearJson(int id, String textoEsp, String textoEn, JSONArray relacion, String img, int tipo) {
        JSONObject picto = new JSONObject();
//        JSONObject nombre = new JSONObject();
        JSONObject texto = new JSONObject();
        JSONObject imagen = new JSONObject();
//        JSONObject jsonAux = null;

        try {
//            nombre.put("esp", "HOLA");
//            nombre.put("en", "HELLO");

            texto.put(sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en"),
                    textoEsp);
            texto.put("en", textoEn);

            imagen.put("picto", img);
//            imagen.put("pictoEditado", "/c/Users/etc");

            picto.put("id", id);
//            picto.put("nombre",nombre);
            picto.put("texto", texto);
            picto.put("tipo", tipo);
            picto.put("imagen", imagen);
            picto.put("relacion", relacion);

        } catch (JSONException error) {
            Log.e(TAG, "crearJson: Nos se pudo crear el objeto");
        }

        return picto;
    }
    /**
     * This method run the array an edit the group or the pictogram
     * @param arrayListAEditar JsonArray to modify the pictogram
     * @param jsonObjectAEditar pictogram or group to modify
     * */
    public JSONArray setJsonEditado2(JSONArray arrayListAEditar, JSONObject jsonObjectAEditar) throws JSONException {
        for (int i = 0; i < arrayListAEditar.length(); i++) {
            JSONObject object = arrayListAEditar.getJSONObject(i);
            if (object.getInt("id") == jsonObjectAEditar.getInt("id")) {
                arrayListAEditar.put(i, jsonObjectAEditar);
                break;
            }
        }
        return arrayListAEditar;
    }

    public void relacionarConGrupo2(JSONArray jsonArrayAVincular, int padre, int picto) throws JSONException {
        crearRelacion(jsonArrayAVincular.getJSONObject(padre).getJSONArray("relacion"), picto);
    }

    private double score(JSONObject json, boolean esSugerencia) {
        int frec = 0, agenda = 0, gps = 0, horaDelDia = 0, id = 0, present = 0, sexoUsuario,
                edadUsuario = 0;
        final double pesoFrec = 2, pesoAgenda = 8, pesoGps = 12, pesoHora = 50, pesoPresent = 100,
                pesoSexo = 3, pesoEdad = 5;
        double score;
        JSONObject original = null;
        try {
            frec = json.getInt("frec");
            id = json.getInt("id");
            original = getPictoFromId2(id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO chequear que gps tenga algun valor, antes asignaba 1
        agenda = tieneAgenda(original);
        horaDelDia = tieneHora(original);
        gps = tienePosicion(original);
        sexoUsuario = tieneSexo(original);
        edadUsuario = tieneEdad(original);
        if (esSugerencia) {
            gps = 0;
            agenda = 0;
            horaDelDia = 0;
        }
        score = (frec * pesoFrec) + (agenda * pesoAgenda) + (gps * pesoGps) + (horaDelDia *
                pesoHora) + (sexoUsuario * pesoSexo) + (edadUsuario * pesoEdad);

        return score;
    }


    public void setAgenda(JSONObject ob) {
        JSONArray arrayAgenda = new JSONArray();
        try {
            if (ob.getJSONArray(Constants.CALENDARIO) != null) {
                arrayAgenda = ob.getJSONArray(Constants.CALENDARIO);
                Log.d(TAG, "setAgenda: Not null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayAgenda.put(arrayAgenda.length(), getNombre(ob).toUpperCase());
            ob.put(Constants.CALENDARIO, arrayAgenda);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int tieneSexo(JSONObject json) {
        try {
            JSONArray array = json.getJSONArray(Constants.SEXO);
            for (int i = 0; i < array.length(); i++) {
                Log.d(TAG, "tieneSexo: " + array.get(i));
                if (obtenerSexo().equalsIgnoreCase(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int tieneAgenda(JSONObject json) {
        try {
            JSONArray array = json.getJSONArray(Constants.CALENDARIO);
            for (int i = 0; i < array.length(); i++) {
                Log.d(TAG, "tieneAgenda: " + array.get(i) + " getAgenda: " + getAgenda());
                if (getAgenda().equals(array.get(i).toString())) {
                    Log.d(TAG, "tieneAgenda: Si");
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int tieneHora(JSONObject json) {
        try {
            JSONArray array = json.getJSONArray(Constants.HORA);
            for (int i = 0; i < array.length(); i++) {
                Log.d(TAG, "tieneHora: " + array.get(i) + " calcularHora: " + calcularHora());
                if (calcularHora().toString().equals(array.get(i).toString())) {
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean tieneTag(JSONObject jsonObject, String tag) {

        String enumTag;

        switch (tag) {

            case Constants.HORA:
                enumTag = Arrays.toString(Horario.values());
                break;

            case Constants.UBICACION:
                enumTag = Arrays.toString(Posicion.values());
                break;

            case Constants.EDAD:
                enumTag = Arrays.toString(Edad.values());
                break;

            case Constants.CLIMA:
                enumTag = Arrays.toString(Clima.values());
                break;
            case Constants.SEXO:
                enumTag = Arrays.toString(Sexo.values());
                break;

            default:
                enumTag = "error";
                break;
        }

        try {
            JSONArray array = jsonObject.getJSONArray("tags");
            for (int i = 0; i < array.length(); i++) {
                if (enumTag.contains(array.get(i).toString()))
                    return true;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }




    public void setPlaceName(String name) {

        this.mListPlaceName = "TYPE_"+name;
    }

    public String getCantidadDePlaces() {
        return mListPlaceName;
    }


    private Posicion calcularPosicion() {


        switch (mListPlaceName) {
            case "TYPE_RESTAURANT":
                return Posicion.RESTAURANT;

            case "TYPE_BAKERY":
                return Posicion.PANADERIA;

            case "TYPE_PARK":
                return Posicion.PARQUE;

            case "TYPE_STORE":
                return Posicion.TIENDA;

            case "TYPE_SHOPPING_MALL":
                return Posicion.SHOPPING;

            case "TYPE_AIRPORT":
                return Posicion.AEROPUERTO;

            case "TYPE_ATM":
                return Posicion.CAJERO;

            case "TYPE_BANK":
                return Posicion.BANCO;

            case "TYPE_BAR":
                return Posicion.BAR;

            case "TYPE_INSURANCE_AGENCY":
                return Posicion.AGENCIADESEGUROS;

            case "TYPE_SUBWAY_STATION":
                return Posicion.SUBTE;

            case "TYPE_JEWELRY_STORE":
                return Posicion.JOLLERIA;

            case "TYPE_BUS_STATION":
                return Posicion.ESTACIONDEBUS;

            case "TYPE_TAXI_STAND":
                return Posicion.PARADATAXI;

            case "TYPE_STADIUM":
                return Posicion.ESTADIO;

            case "TYPE_HOSPITAL":
                return Posicion.HOSPITAL;

            case "TYPE_MEAL_DELIVERY":
                return Posicion.DELIVERY;

            case "TYPE_HAIR_CARE":
                return Posicion.PELUQUERIA;

            case "TYPE_MUSEUM":
                return Posicion.MUSEO;

            case "TYPE_TRAIN_STATION":
                return Posicion.ESTACIONDETREN;

            case "TYPE_MOVIE_THEATER":
                return Posicion.CINE;

            case "TYPE_CAFE":
                return Posicion.CAFE;

            case "TYPE_SCHOOL":
                return Posicion.ESCUELA;

            case "TYPE_ZOO":
                return Posicion.ZOOLOGICO;

            case "TYPE_LAUNDRY":
                return Posicion.LAVANDERIA;

            case "TYPE_LODGING":
                return Posicion.ALOJAMIENTO;

            case "TYPE_PET_STORE":
                return Posicion.VETERINARIA;

            case "TYPE_PHARMACY":
                return Posicion.FARMACIA;

            case "TYPE_DENTIST":
                return Posicion.DENTISTA;

            case "TYPE_NIGHT_CLUB":
                return Posicion.BOLICHE;

            case "TYPE_PHYSIOTHERAPIST":
                return Posicion.FISIOTERAPIA;

            case "TYPE_AMUSEMENT_PARK":
                return Posicion.PARQUE;

            case "TYPE_CLOTHING_STORE":
                return Posicion.TIENDADEROPA;

            case "TYPE_CHURCH":
                return Posicion.IGLESIA;

            case "TYPE_CONVENIENCE_STORE":
                return Posicion.TIENDADECONVENIENCIA;

            default:
                return Posicion.NADA;

        }
    }


    private Horario calcularHora() {
        SimpleDateFormat df = new SimpleDateFormat("H");
        Calendar SystemTime = Calendar.getInstance();
        int time = Integer.parseInt(df.format(SystemTime.getTime()));

        if (time >= 5 && time <= 11) {
            return Horario.MANANA;
        } else if (time > 11 && time <= 14) {
            return Horario.MEDIODIA;
        } else if (time > 14 && time < 20) {
            return Horario.TARDE;
        } else {
            return Horario.NOCHE;
        }
    }


    private String obtenerSexo() {
        return sharedPrefsDefault.getString("prefSexo", "NotDefined");
    }

    private int tieneEdad(JSONObject json) {
        try {
            JSONArray array = json.getJSONArray(Constants.EDAD);
            for (int i = 0; i < array.length(); i++) {
                Log.d(TAG, "tieneEdad: " + array.get(i));
                if (obtenerEdad().equals(array.get(i).toString())) {
                    Log.d(TAG, "tieneEdad: es igual");
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    private String obtenerEdad() {
        return sharedPrefsDefault.getString("prefEdad", "NotDefined");
    }


    /**
     * Return 1 if the position exist.
     * Else return -1
     * Use that method to make a consult about the location
     * */
    private int tienePosicion(JSONObject json) {
        try {
            Log.d(TAG, "tienePosicion: ");
            JSONArray array = json.getJSONArray(Constants.UBICACION);
            for (int i = 0; i < array.length(); i++) {
                Log.d(TAG, "tienePosicion: " + array.get(i) + " calcularPos: " +
                        calcularPosicion());
                if (calcularPosicion().toString().equals(array.get(i).toString())) {
                    Log.d(TAG, "tienePosicion: Coincide");
                    return 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public int compareTo(double frec1, double frec2) {
        // descending order
        if(frec1>frec2)
            return -1;
        if(frec2>frec1)
            return 1;

        return (int) (frec2 - frec1);
    }

    public JSONObject getGrupoFromId(int idABuscar) {
        for (int i = 0; i < GroupManagerClass.getInstance().getmGroup().length(); i++) {
            try {

                if (GroupManagerClass.getInstance().getmGroup().getJSONObject(i).getInt("id") == idABuscar) {
                    return GroupManagerClass.getInstance().getmGroup().getJSONObject(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public int getPosPicto(JSONArray listado, int idABuscar) {
        for (int i = 0; i < listado.length(); i++) {
            try {
                if (listado.getJSONObject(i).getInt("id") == idABuscar) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "getPosPicto: Error 1");
                return -1;
            }
        }
        return -1;
    }


    public JSONObject getPictoFromId2(int idABuscar) {
        try{
            int position = new SearchObjects().searchItemById(mJSONArrayTodosLosPictos, idABuscar, this);
            if(position!=-1)
                return mJSONArrayTodosLosPictos.getJSONObject(position);
            }catch (JSONException ex) {
            ex.printStackTrace();
        }
        int position=new SearchObjects().searchItemById(mJSONArrayTodosLosPictos,1024,this);
        for (int i = position; i <mJSONArrayTodosLosPictos.length() ; i++) {
            try {
                JSONObject object=mJSONArrayTodosLosPictos.getJSONObject(i);
                if(getId(object)==idABuscar)
                    return mJSONArrayTodosLosPictos.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public boolean guardarJson(String archivo) {
        JSONArray jsonArrayAGuardar = new JSONArray();
        switch (archivo) {
            case Constants.ARCHIVO_PICTOS:
                jsonArrayAGuardar = mJSONArrayTodosLosPictos;
                break;
            case Constants.ARCHIVO_GRUPOS:
                jsonArrayAGuardar = GroupManagerClass.getInstance().getmGroup();
                break;
            case Constants.ARCHIVO_FRASES:
                jsonArrayAGuardar = mJSONArrayTodasLasFrases;
                break;
            case Constants.ARCHIVO_PICTOS_DATABASE:
                jsonArrayAGuardar = mJSONArrayPictosSugeridos;
                break;
            case Constants.ARCHIVO_FRASES_JUEGOS:
                jsonArrayAGuardar=mJSONArrayTodasLasFrasesJuegos;
                break;
            case Constants.ARCHIVO_JUEGO:
                jsonArrayAGuardar=mJSonArrayJuegos;
                break;
            case Constants.ARCHIVO_JUEGO_DESCRIPCION:
                jsonArrayAGuardar=mJSonArrayDescripciones;
                break;
            case Constants.ARCHIVO_FRASES_FAVORITAS:
                jsonArrayAGuardar=mJSonArrayFrasesFavoritas;
                break;

        }

        try {
            if (isFileOk(jsonArrayAGuardar)) {
                if (jsonArrayAGuardar.length() > 0) {
                    FileOutputStream outputStream;
                    try {
                        outputStream = mContext.openFileOutput(archivo, Context.MODE_PRIVATE);
                        outputStream.write(jsonArrayAGuardar.toString().getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isFileOk(JSONArray jsonArray) throws JSONException, OutOfMemoryError {
        if (jsonArray != null) {
            int tamArray = jsonArray.toString().getBytes().length;
            if (tamArray > Constants.CINCO_MEGAS) {
                int randomObj = (int) Math.floor(Math.random() * jsonArray.length());
                for (int j = 0; j <= 1; j++) {
                    JSONObject randomJsonObj = jsonArray.getJSONObject(randomObj);
                    for (int i = randomObj + 1; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).equals(randomJsonObj)) {
                            return false;
                        }
                    }
                }

                return true;
            } else {
                return true;
            }
        } else
            return false;
    }


    //Ya esta
    public ArrayList<JSONObject> getArrayListFromTipo(String tipoABuscar, ArrayList<JSONObject> arrayListABuscar) {
        ArrayList<JSONObject> arrayListADevolver = new ArrayList<>();
        for (JSONObject jsonObject : arrayListABuscar) {
            try {
                if (jsonObject.getString("tipo").equals(tipoABuscar)) {
                    arrayListADevolver.add(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayListADevolver;
    }

    //Ya esta
    public JSONObject getPictoFromCustomArrayById(ArrayList<JSONObject> arrayList, int idABuscar) {
        int id = -1;
        for (JSONObject jsonObject : arrayList) {
            try {
                if (jsonObject.getInt("id") == idABuscar) {
                    return jsonObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getId(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("id");
    }


    public long generarCheckSum(ArrayList<JSONObject> listado) {
        return listado.hashCode();
    }

    //metodo encargado de devolver la existencia de un picto dentro de un grupo por medio de la relacion
    private boolean tienePicto(JSONArray arreglo, int id) {
        //primero recorro el arreglo
        for (int i = 0; i < arreglo.length(); i++) {
            try {
                //si tiene el id devuelvo verdadero
                if (arreglo.getJSONObject(i).getInt("id") == id) {
                    return true;
                }
                // en caso de no tenerlo o que salte alguna excepcion devuelve falso

            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }


    public boolean getFallJson() {
        return fallaJson;
    }



    private String readFromFile(String fileName){
        File archivo = new File(mContext.getFilesDir(), fileName);
        if (archivo.length() > Constants.CINCO_MEGAS) {
            Log.d(TAG, "readFromFile:  Bigger than 5Mb");
        } else {
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            FileInputStream fis;
            try {
                if (fileName.equals(Constants.ARCHIVO_PICTOS) || fileName.equals(Constants.ARCHIVO_GRUPOS) || fileName.equals(Constants.ARCHIVO_FRASES) || fileName.equals(Constants.ARCHIVO_PICTOS_DATABASE) || fileName.equals(Constants.ARCHIVO_FRASES_JUEGOS) || fileName.equals(Constants.ARCHIVO_JUEGO) || fileName.equals(Constants.ARCHIVO_JUEGO_DESCRIPCION)||fileName.equals(Constants.ARCHIVO_FRASES_FAVORITAS))
                    fis = mContext.openFileInput(fileName);
                else
                    fis = new FileInputStream(new File(fileName));

                reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                fis.close();
            } catch (IOException e) {
                Log.e(TAG, "readFromFile: " + e.toString());
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "readFromFile: " + e.toString());

                    }
                }
            }
            return builder.toString();
        }
        return null;
    }


    //Ya esta hecho
    public JSONObject getJsonObjectFromTexto(ArrayList<JSONObject> arrayList, String stringABuscar) {
        int id = -1;
        for (JSONObject jsonObject : arrayList) {
            try {
                if (jsonObject.getJSONObject("texto").getString("es").equals(stringABuscar)) {
                    return jsonObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //TODO return JSON error
        return null;
    }

    //Este metodo reemplaza a los getmArraylist
    public JSONArray readJSONArrayFromFile(String constantFileName) throws JSONException, FiveMbException {
        return new JSONArray(readFromFile(constantFileName));
    }

    public JSONObject getJsonObjectFromTextoEnIngles(String stringABuscarEnIngles, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getJSONObject("texto").getString("en").equals(stringABuscarEnIngles)) {
                return object;
            }

        }
        return null;
    }

    private JSONArray elegirHijos2(JSONObject padre, boolean esSugerencia) throws JSONException {
        JSONArray array = padre.getJSONArray("relacion");
        ArrayList<JSONObject> relacion = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            relacion.add(array.getJSONObject(i));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            relacion.sort(new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject json1, JSONObject json2) {
                    double frec1 = 0;
                    double frec2 = 0;

                        frec1 = getScore(json1, esSugerencia);
                        frec2 = getScore(json2, esSugerencia);
                    return compareTo(frec1, frec2);
                }
            });
        }else{

            Collections.sort(relacion, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject json1, JSONObject json2) {
                 double frec1 = 0;
                 double frec2 = 0;
                    try {
                        frec1 = getScore(json1, esSugerencia);
                        frec2 = getScore(json2, esSugerencia);
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                        return compareTo(frec1, frec2);
                }
            });
        }
        Log.d(TAG, "elegirHijos2: Ordenado");
        return new JSONArray(relacion.toString());
    }


    public JSONObject getPictoFromCustomArrayById2(JSONArray jsonArray, int idABuscar) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getInt("id") == idABuscar)
                return object;
        }
        return null;
    }

    public JSONObject getPictoFromId(ArrayList<Integer> integers, int idAbuscar) {
        int pos = integers.lastIndexOf(idAbuscar);
        if (pos != -1) {
            try {
                return mJSONArrayTodosLosPictos.getJSONObject(pos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Antes era Tipo pero se confunde con el tipo de picto
    public JSONArray getArrayListFromTag(String tagABuscar, JSONArray jsonArrayABuscar) throws JSONException {
        JSONArray jsonArrayADevolver = new JSONArray();
        for (int i = 0; i < jsonArrayABuscar.length(); i++) {
            JSONObject object = jsonArrayABuscar.getJSONObject(i);
            if (object.getString("tipo").equals(tagABuscar)) {
                jsonArrayADevolver.put(object);
            }
        }
        return jsonArrayADevolver;
    }


    //TODO ver si se puede optimizar
    /*
       variable i : i es un desplazamiento que va de 0 a 3 , ultima posicion es el indicador desde donde comenzar
       variable ultimaposicion : indica la ultima posicion
     *
     * */
    public JSONArray cargarOpciones(JSONObject padre, int cuentaMasPictos) throws JSONException, FiveMbException {
        //mJSONArrayTodosLosPictos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);// leo los pictos

            if(!consultarPago())
             sharedPrefsDefault.edit().putBoolean("bool_sugerencias",consultarPago()).apply();
        JSONArray relacion =elegirHijos2(padre, false); //selecciono el picto padre
       //   JSONArray relacion =new SortJsonObject().SortArray(padre.getJSONArray("relacion"),this).getArray(this);;
        //cargo la primera relacion
        if (noTieneRelacionHijo) {
            int pos = getPosPicto(mJSONArrayTodosLosPictos, padre.getInt("id"));
            relacion = elegirHijos2(mJSONArrayTodosLosPictos.getJSONObject(pos), false);
        }
        //listado de pictos elegidos
        JSONArray jsonElegidos = new JSONArray();
        int ultimaPosicion = cuentaMasPictos * 4; //posicion del picto
        for (int i = 0; i < 4; i++) {

            int position = i + ultimaPosicion;
            Log.d(TAG, "cargarOpciones 0: " + ultimaPosicion);
            if (position < relacion.length()) {

                jsonElegidos.put(getPictoFromId2(relacion.getJSONObject(i + ultimaPosicion).getInt("id")));
                jsonElegidos.getJSONObject(jsonElegidos.length() - 1).put("esSugerencia", false);

            } else if (position >= relacion.length()) {
                int ultimaUbic = ultimaPosicion - relacion.length() + i;
                Log.d(TAG, "cargarOpciones 0: " + ultimaUbic + "");
                Log.d(TAG, "cargarOpciones 1: " + (ultimaUbic) + "");
                if (ultimaUbic < 0) {
                    jsonElegidos.put(getPictoFromId2(relacion.getJSONObject(relacion.length() + ultimaUbic).getInt("id")));
                    jsonElegidos.getJSONObject(jsonElegidos.length() - 1).put("esSugerencia", false);

                } else if (ultimaUbic >= 0) {
                    if ((sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en").equals("es") || sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en").equals("ca") || sharedPrefsDefault.getString(mContext.getResources().getString(R.string.str_idioma), "en").equals("en")) && mJSONArrayPictosSugeridos.length() != 0 && sharedPrefsDefault.getBoolean("bool_sugerencias", false)) {
                        if (mostrarSugerencias(padre, ultimaUbic, jsonElegidos)) {
                            int posPadre = getPosPicto(mJSONArrayPictosSugeridos, padre.getInt("id"));
                            Constants.VUELTAS_CARRETE = ((relacion.length() + mJSONArrayPictosSugeridos.getJSONObject(posPadre).getJSONArray("relacion").length()) / 4);
                        } else {
                            JSONObject object = new JSONObject();
                            object.put("id", "-1");
                            jsonElegidos.put(i, object);
                            Constants.VUELTAS_CARRETE = Constants.VUELTAS_CARRETE + 1;
                            noTieneRelacionHijo = false;
                        }
                    } else {

                        JSONObject object = new JSONObject();
                        object.put("id", "-1");
                        jsonElegidos.put(i, object);
                        Constants.VUELTAS_CARRETE = Constants.VUELTAS_CARRETE + 1;
                        noTieneRelacionHijo = false;
                    }
                }
            }
        }
        return jsonElegidos;
    }

    /**
     * Este metodo se encarga de buscar los pictos relacionados en sugerencia con un picto determinado
     *
     * @param padre             objeto json padre
     * @param pos               picto en una posicion determinada
     * @param listadoDeElegidos json array a devolver en una posicion determinada
     *                          capturamos las excepciones para ver que hacer
     */
    private boolean mostrarSugerencias(JSONObject padre, int pos, JSONArray listadoDeElegidos) {

        try {
            int positionPadre = getPosPicto(mJSONArrayPictosSugeridos, padre.getInt("id"));
            while (tienePicto(padre.getJSONArray("relacion"), mJSONArrayPictosSugeridos.getJSONObject(positionPadre).getJSONArray("relacion").getJSONObject(pos).getInt("id"))) {
                desvincularJson(mJSONArrayPictosSugeridos.getJSONObject(positionPadre), mJSONArrayPictosSugeridos.getJSONObject(positionPadre).getJSONArray("relacion").getJSONObject(pos).getInt("id"));
            }
            JSONArray opciones = mJSONArrayPictosSugeridos.getJSONObject(positionPadre).getJSONArray("relacion");
            listadoDeElegidos.put(getPictoFromId2(opciones.getJSONObject(pos).getInt("id")));
            listadoDeElegidos.getJSONObject(listadoDeElegidos.length() - 1).put("esSugerencia", true);
            return true;
        } catch (IndexOutOfBoundsException | JSONException ex) {
            Log.e(TAG, "mostrarSugerencias: " + ex.getMessage());
            return false;
        }


    }


    private void addPictoToGroupAll(JSONArray arrayGrupos, int padre) throws JSONException {
        String strAll = "ALL";
        JSONObject jsonObjectGrupoAll = getJsonObjectFromTextoEnIngles(strAll.toLowerCase(), arrayGrupos);
        jsonObjectGrupoAll.getJSONArray("relacion").put(arrayGrupos.getJSONObject(padre).getJSONArray("relacion").length() - 1);

    }

    public JSONArray addFoto2BackUp(JSONArray jsonArrayBackUp, JSONObject imagen) {
        return jsonArrayBackUp.put(imagen);
    }


    public boolean arrayContains(JSONArray jsonArray, String myElementToSearch) throws JSONException {

        boolean found = false;
        for (int i = 0; i < jsonArray.length(); i++)
            if (jsonArray.getString(i).equals(myElementToSearch))
                found = true;

        return found;
    }

    public void cargarPictosSugeridosJson() {
        try {
            mJSONArrayPictosSugeridos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //pregunto por la posicion y el id
    }

    public void crearFrase(String frase, ArrayList<JSONObject> historial, long fecha) throws JSONException {
         if(!frase.isEmpty()){
            int pos = fraseExist(frase);
            if (pos != -1) {
                mJSONArrayTodasLasFrases.getJSONObject(pos).put("frecuencia", mJSONArrayTodasLasFrases.getJSONObject(pos).getInt("frecuencia") + 1);
                mJSONArrayTodasLasFrases.getJSONObject(pos).accumulate("fecha", fecha);
                if(!mJSONArrayTodasLasFrases.getJSONObject(pos).has("id")) {
                    int id=getThePhraseLastId()+1;
                    mJSONArrayTodasLasFrases.getJSONObject(pos).put("id", id);
                }
                if(!mJSONArrayTodasLasFrases.getJSONObject(pos).has("locale"))
                    mJSONArrayTodasLasFrases.getJSONObject(pos).put("locale",getIdioma());
                guardarJson(Constants.ARCHIVO_FRASES);
            } else {
                JSONObject nuevaFrase = new JSONObject();
                nuevaFrase.put("frase", frase);
                nuevaFrase.put("frecuencia", 1);
                nuevaFrase.put("complejidad", getComplejidad(historial));
                nuevaFrase.accumulate("fecha", fecha);
                nuevaFrase.put("locale",getIdioma());
                int id=getThePhraseLastId()+1;
                nuevaFrase.put("id",id);
                mJSONArrayTodasLasFrases.put(nuevaFrase);
                guardarJson(Constants.ARCHIVO_FRASES);
            }
         }
    }

    public int getThePhraseLastId(){
        int id=-1;
        try {
            mJSONArrayTodasLasFrases.getJSONObject(0).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <mJSONArrayTodasLasFrases.length() ; i++) {
            try {
                JSONObject object=mJSONArrayTodasLasFrases.getJSONObject(i);
                if(mJSONArrayTodasLasFrases.getJSONObject(i).has("id")){
                    int idFrase=mJSONArrayTodasLasFrases.getJSONObject(i).getInt("id");
                    if(id<idFrase)
                        id=idFrase;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    private int fraseExist(String frase) throws JSONException {

        if (mJSONArrayTodasLasFrases.toString().contains(frase)) {
            for (int i = 0; i < mJSONArrayTodasLasFrases.length(); i++) {
                if (mJSONArrayTodasLasFrases.getJSONObject(i).getString("frase").equals(frase)) {
                    return i;
                }
            }
        }

        return -1;
    }

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

    private ArrayList<JSONObject> getIdFromHistorial(ArrayList<JSONObject> arrayList) {
        if (arrayList != null) {
            ArrayList<JSONObject> historial;
            historial = stringToArrayList(arrayList.toString());
            if (historial.size() > 0) {

                for (JSONObject jsonObject : historial) {
                    jsonObject.remove("texto");
                    jsonObject.remove("tipo");
                    jsonObject.remove("imagen");
                    jsonObject.remove("relacion");
                    jsonObject.remove("agenda");
                    jsonObject.remove("gps");
                }
            }
            return historial;
        } else {
            Log.d(TAG, "getIdFromHistorial: arrayList == null");
            return null;
        }
    }

    public void resetearError() {
        fallaJson = false;
        sumarFallas();
    }

    public void sumarFallas() {
        cantFallas++;
    }

    public int getCantFallas() {
        return cantFallas;
    }

    public void ordenarSugerenciasPorTipo(int idPadre) {

        try {
            int pos = getPosPicto(getmJSONArrayPictosSugeridos(), idPadre);
            JSONObject object = getmJSONArrayPictosSugeridos().getJSONObject(pos);
            getmJSONArrayPictosSugeridos().getJSONObject(pos).put("relacion", elegirHijos2(object, true));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public JSONArray borrarSugerenciasPictosPadres(int idPictoPadre) {
        int posPadreSug = getPosPicto(mJSONArrayPictosSugeridos, idPictoPadre);
        int posPicto = getPosPicto(mJSONArrayTodosLosPictos, idPictoPadre);
        try {
            JSONArray listadoHijosSug = mJSONArrayPictosSugeridos.getJSONObject(posPadreSug).getJSONArray("relacion");
            JSONArray listadoHijosPad = mJSONArrayTodosLosPictos.getJSONObject(posPicto).getJSONArray("relacion");
            for (int i = 0; i < listadoHijosPad.length(); i++) {
                if (tienePicto(listadoHijosSug, listadoHijosPad.getJSONObject(i).getInt("id"))) {
                    desvincularJson(mJSONArrayPictosSugeridos.getJSONObject(posPadreSug), listadoHijosPad.getJSONObject(i).getInt("id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public JSONObject findPictogram(JSONArray array,int idABuscar) {

        try {
            if(idABuscar>array.getJSONObject(array.length()/2).getInt("id")){
            for (int i = array.length()/2; i <array.length() ; i++) {

                    try {
                         if (array.getJSONObject(i).getInt("id") == idABuscar) {
                            return mJSONArrayTodosLosPictos.getJSONObject(i);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
            }
            else if(idABuscar<array.getJSONObject(array.length()/2).getInt("id")&&idABuscar>=0){
            for (int i = 0; i <array.length() /2; i++) {

                    try {
                         if (array.getJSONObject(i).getInt("id") == idABuscar) {
                            return mJSONArrayTodosLosPictos.getJSONObject(i);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
            }else{
                for (int i = array.length()/2; i <array.length(); i++) {

                    try {
                        if (array.getJSONObject(i).getInt("id") == idABuscar) {
                            return mJSONArrayTodosLosPictos.getJSONObject(i);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean estaEditado(JSONObject object) {
        try {
            if(object==null)
                return false;
            if(object.getJSONObject("imagen").has("pictoEditado"))
                return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }
        return false;
    }

    public JSONObject devolverComplejidad(JSONObject object){
        if(object.has("complejidad")) {
            try {
                return object.getJSONObject("complejidad");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    public float calcularPuntajeGral(int id){
        float puntaje=0;
        int cantInt=0;
        int cantAciertos=0;
        for (int i = 0; i <GroupManagerClass.getInstance().getmGroup().length() ; i++) {
            try {
                cantAciertos+=getAciertos(GroupManagerClass.getInstance().getmGroup().getJSONObject(i),id);
                cantInt+=getIntentos(GroupManagerClass.getInstance().getmGroup().getJSONObject(i),id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Aciertos "+cantAciertos );
        Log.d(TAG, "Intentos "+cantInt );
        try {

           return puntaje=cantAciertos/(cantInt+cantAciertos);
        }catch (Exception ex){
            return 0;
        }
    }

    public void agregarJuego(JSONObject object) {
        try {

            mJSonArrayJuegos.getJSONObject(0).getJSONObject(object.getInt("game")+"").put(object.getInt("levelId")+"",object);
        } catch (JSONException e) {
            e.printStackTrace();
            mJSonArrayJuegos.put(new JSONObject());
            try {
                JSONObject game=new JSONObject();
                game.accumulate(object.getInt("levelId")+"",object);
                mJSonArrayJuegos.getJSONObject(0).put(object.getInt("game")+"",game);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    public JSONObject getGame(int idGame, int leveId){
        try {
            return mJSonArrayJuegos.getJSONObject(0).getJSONObject(idGame+"").getJSONObject(leveId+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getObjectPuntaje(JSONObject object){
        JSONObject jsonObject;
        try {
            return object.getJSONObject("puntaje");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean consultarPago() {
        return sharedPrefsDefault.getInt("premium", 0) == 1;
    }

    public int devolverCantidadGruposUsados(int id){
        int cant=0;
        try {
            cant=mJSonArrayJuegos.getJSONObject(0).getJSONObject(id+"").length();

        } catch (JSONException e) {
            e.printStackTrace();
            cant = 0;
            Log.e(TAG, "devolverCantidadGruposUsados: " + e.getMessage());

        }
        return cant;
    }

    public JSONObject devolverObjetoDescripcion(int position){
        try {
            return mJSonArrayDescripciones.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getDescription(JSONObject object){
        try {
            return object.getJSONObject("descripcion");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setmJSonArrayDescripciones(JSONArray mJSonArrayDescripciones) {
        this.mJSonArrayDescripciones = mJSonArrayDescripciones;
    }

    public JSONArray getmJSonArrayDescripciones() {
        return mJSonArrayDescripciones;
    }

    public int getScore(JSONObject object, boolean isSugerencia){
        try {
            Log.d(TAG, "getScore: " + object.get("id").toString());
            return (int) score(object, isSugerencia);
        }catch (Exception ex){
            Log.e(TAG, "getScore: " + ex.getMessage());
            return 0;
        }
    }

    public JSONArray getmJSonArrayFrasesFavoritas() {
        return mJSonArrayFrasesFavoritas;
    }
}
