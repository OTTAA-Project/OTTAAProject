package com.stonefacesoft.ottaa.JSONutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import com.google.android.libraries.places.api.model.Place;
import com.stonefacesoft.ottaa.BuildConfig;
import com.stonefacesoft.ottaa.DrawableManager;
import com.stonefacesoft.ottaa.Interfaces.DrawableInterface;
import com.stonefacesoft.ottaa.Interfaces.SortPictogramsInterface;
import com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils.SortPictograms;
import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author morro
 * @author Hector Costa
 * @author Gonzalo Juarez
 * @version 2.0
 * Edited by Hector on 27/11/2018
 * Edited by Hector on 04/01/2019
 * <h3>Objectives</h3>
 * <p>Load,Read and process all Json Object</p>pictos
 * <h3>How to declare</h3>
 * <code>Json json=Json.getInstance();</code>
 * <br>
 * <code>json.setmContext(Context);</code>
 * <h3>Examples of Implementation</h3>
 * <h4>Get id</h4>
 * <code>{@link JSONObject object=getmJSONArrayTodosLosPictos.getJSONObject(position);}</code><br>
 * <code>int id=json.getid(object);</code>
 * <h4>How to add pictogram</h4>
 * <code>json.crearPicto(mArrayListGrupo, mArrayListTodosLosPictos,idPadre,textLanguage,textoEnglis,UrlImage,tipe,urlFirebase,pushKeyPictogram) </code>
 * <h4>How to edit pictogram</h4>
 * <p>
 * {@link #setmJSONArrayTodosLosPictos(JSONArray)}
 * @since 8/5/2016.
 */
public class Json  {


    private static final String TAG = "Json";
    //Declaro el manejador de preferencia
    protected static SharedPreferences sharedPrefsDefault;
    //Json singleton
    private static Json _instance;
    private static boolean fallaJson;
    private final int idGps = 0;
    // Arraylist de Json
    private ArrayList<JSONObject> mArrayListTodasLasFotosBackup;
    private JSONArray mJSONArrayPictosSugeridos;
    private JSONArray mJSONArrayTodosLosGrupos;
    private JSONArray mJSONArrayTodasLasFrases;
    private JSONArray mJSONArrayTodosLosPictos;
    private JSONArray mJSONArrayTodasLasFotosBackup;
    private JSONArray mJSonArrayJuegos;
    private JSONArray mJSonArrayFrasesFavoritas;
    private final String eventoActual = "none";
    private Context mContext;
    private String textoTags;
    private String mListPlaceName = "";
    private ArrayList<Place> placesNames;
    private int cantFallas;
    //JSONArray


    //JSON es ahora un singleton
    private Json() {

        this.mJSONArrayTodosLosPictos = new JSONArray();
        this.mJSONArrayPictosSugeridos = new JSONArray();
        this.mJSONArrayTodosLosGrupos = new JSONArray();
        this.mJSONArrayTodasLasFrases = new JSONArray();
        this.mJSONArrayTodasLasFotosBackup = new JSONArray();
        this.mJSonArrayJuegos = new JSONArray();
        this.mJSonArrayFrasesFavoritas = new JSONArray();
        //Implemento el manejador de preferencias
    }

    /**
     * Singleton Instance
     */
    public synchronized static Json getInstance() {
        if (_instance == null) {

            synchronized (Json.class) {
                if (_instance == null) {
                    _instance = new Json();

                }
            }
        }

        return _instance;
    }

    /**
     * set the context to the json object.
     *
     * @param context Activity or context where implements the object
     */
    public Json setmContext(Context context) {
        this.mContext = context;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
        return this;
    }

    /**
     * Load all the jsonArray objects
     */
    public void initJsonArrays() {
        //Cargo por unica vez los archivos al array
        try {
            mJSONArrayTodosLosPictos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        try {
            new SortPictograms().quickSort(mJSONArrayTodosLosPictos,0,mJSONArrayTodosLosPictos.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mJSONArrayTodosLosGrupos = readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
            GroupManagerClass.getInstance().setmGroup(mJSONArrayTodosLosGrupos);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        try {
            mJSONArrayTodasLasFrases = readJSONArrayFromFile(Constants.ARCHIVO_FRASES);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        try {
            mJSONArrayPictosSugeridos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        try {
            mJSonArrayFrasesFavoritas = readJSONArrayFromFile(Constants.ARCHIVO_FRASES_FAVORITAS);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
        try {
            mJSonArrayJuegos = readJSONArrayFromFile(Constants.ARCHIVO_JUEGO);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }

    }

    /**
     * Update the JsonArray
     */
    public void refreshJsonArrays(){
        initJsonArrays();
    }

    /**
     *
     */
    public JSONArray getmJSONArrayTodosLosPictos() {
        return mJSONArrayTodosLosPictos;
    }

    public void setmJSONArrayTodosLosPictos(JSONArray mJSONArrayTodosLosPictos) {
        this.mJSONArrayTodosLosPictos = mJSONArrayTodosLosPictos;
        try {
            if(mJSONArrayTodosLosPictos != null)
               new SortPictograms().quickSort(this.mJSONArrayTodosLosPictos,0,mJSONArrayTodosLosPictos.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getmJSONArrayPictosSugeridos() {
        return mJSONArrayPictosSugeridos;
    }

    public void setmJSONArrayPictosSugeridos(JSONArray mJSONArrayPictosSugeridos) {
        this.mJSONArrayPictosSugeridos = mJSONArrayPictosSugeridos;
        try {
            new SortPictograms().quickSort(mJSONArrayPictosSugeridos,0,mJSONArrayPictosSugeridos.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getmJSONArrayTodasLasFotosBackup() {
        return mJSONArrayTodasLasFotosBackup;
    }

    public void setmJSONArrayTodasLasFotosBackup(JSONArray mJSONArrayTodasLasFotosBackup) {
        this.mJSONArrayTodasLasFotosBackup = mJSONArrayTodasLasFotosBackup;
    }

    public JSONArray getmJSONArrayTodosLosGrupos() {
        return GroupManagerClass.getInstance().getmGroup();
    }

    public JSONArray getmJSonArrayGroupsGame(){
        return GroupManagerClass.getInstance().getmGroup();
    }

    public  void setmJSONArrayTodosLosGrupos(JSONArray mJSONArrayTodosLosGrupos) {
        this.mJSONArrayTodosLosGrupos = mJSONArrayTodosLosGrupos;
        GroupManagerClass.getInstance().setmGroup(this.mJSONArrayTodosLosGrupos);
    }

    public  JSONArray getmJSONArrayTodasLasFrases() {
        return mJSONArrayTodasLasFrases;
    }

    public JSONArray getPhrasesByLanguage(){
        JSONArray aux = mJSONArrayTodasLasFrases;
        JSONArray result = new JSONArray();
        for (int i = 0; i < aux.length(); i++) {
            try {
                if(aux.getJSONObject(i).has("locale")){
                    if(aux.getJSONObject(i).getString("locale").equalsIgnoreCase(ConfigurarIdioma.getLanguaje())) {
                        result.put(aux.getJSONObject(i));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setmJSONArrayTodasLasFrases(JSONArray mJSONArrayTodasLasFrases) {
        this.mJSONArrayTodasLasFrases = mJSONArrayTodasLasFrases;
    }


    public JSONArray getmJSonArrayJuegos() {
        return mJSonArrayJuegos;
    }

    public void setmJSonArrayJuegos(JSONArray mJSonArrayJuegos) {
        this.mJSonArrayJuegos = mJSonArrayJuegos;
    }

    public Json initSharedPrefs() {
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getInstance();
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

    public Drawable getIcono(JSONObject object) {
        try {
            JSONObject jsonObjectImage = JSONutils.getImagen(object);

            switch (jsonObjectImage.getInt("type")) {
                case 1:
                    Drawable draw = AbrirBitmap(jsonObjectImage.getString("picto"),0);
                    return draw;
                case 2:
                    return mContext.getResources().getDrawable(mContext.getResources().getIdentifier(jsonObjectImage.getString("picto"),
                            "drawable", mContext.getPackageName()));
                case 3:
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }



    public Drawable getIconWithNullOption(JSONObject object) {
        try {
            JSONObject jsonObjectImage = JSONutils.getImagen(object);

            switch (jsonObjectImage.getInt("type")) {
                case 1:
                    Drawable draw = AbrirBitmap(jsonObjectImage.getString("picto"),1);
                    return draw;
                case 2:
                    return mContext.getResources().getDrawable(mContext.getResources().getIdentifier(jsonObjectImage.getString("picto"),
                            "drawable", mContext.getPackageName()));
                case 3:
                    DrawableManager drawableManager = new DrawableManager();
                    return drawableManager.fetchDrawable(jsonObjectImage.getString("urlFoto"), new DrawableInterface() {
                        @Override
                        public Drawable getDrawable(Drawable drawable) {
                            return drawable;
                        }

                        @Override
                        public void fetchDrawable(Drawable drawable) {

                        }

                        @Override
                        public void fetchDrawable(Drawable drawable, int position) {

                        }
                    });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }catch (Exception ex){
            return null;
        }
        return null;
    }

    public Drawable getBitmap(String path) throws Exception {
        Drawable d = Drawable.createFromPath(path);
        if (d != null)
            return d;
        else {
            Log.d(TAG, "getBitmap: isEmpty");
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                bitmap = mContext.getContentResolver().loadThumbnail(Uri.parse(path), new Size(500, 500), null);
            }
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            d.draw(canvas);
        }
        return d;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public Drawable AbrirBitmap(String path,int opt) {
        Drawable d = null;
        if (!path.isEmpty()) {
            try {
                d = getBitmap(path);
            } catch (Exception ex) {
                ex.printStackTrace();
                if(opt ==0)
                    d = mContext.getResources().getDrawable(R.drawable.ic_baseline_cloud_download_24_big);
                else
                    d = null;
            }
        }
        return d;
    }

    private String getAgenda() {
        return eventoActual;
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
            arrayAgenda.put(arrayAgenda.length(), JSONutils.getNombre(ob, ConfigurarIdioma.getLanguaje()).toUpperCase());
            ob.put(Constants.CALENDARIO, arrayAgenda);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getPictoFromId2(int idABuscar) {
        return JSONutils.getPictoFromId2(getmJSONArrayTodosLosPictos(), idABuscar);
    }


    public JSONArray getHijosGrupo2(int pos) {

        try {
            return JSONutils.getHijosGrupo2(getmJSONArrayTodosLosPictos(), getmJSONArrayTodosLosGrupos().getJSONObject(pos));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private double score(JSONObject json, boolean esSugerencia) {

        return JSONutils.score(json, esSugerencia, getAgenda(), obtenerSexo(), calcularHora().toString(), obtenerEdad(), calcularPosicion().toString());
    }


    //TODO hasta aca revisado

    public String obtenerSexo() {
        return sharedPrefsDefault.getString("prefSexo", "NotDefined");
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
        this.mListPlaceName = "TYPE_" + name;
    }

    public String getCantidadDePlaces() {
        return mListPlaceName;
    }

    public Posicion calcularPosicion() {

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
        try{
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
        }catch (Exception ex){
            return Horario.ISEMPTY;
        }
    }

    public String obtenerEdad() {
        return sharedPrefsDefault.getString("prefEdad", "NINO");
    }

    public boolean useChatGPT() {
        return sharedPrefsDefault.getBoolean("key_chat_gpt", false);
    }

    public int compareTo(double frec1, double frec2) {
        // descending order
        if (frec1 > frec2){
            return -1;
        }
        if (frec2 > frec1){
            return 1;
        }
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
        if(listado == null)
            return -1;
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
    public int getPosPictoBinarySearch(JSONArray listado, int idABuscar) {
        if(listado == null)
            return -1;
        return JSONutils.getPositionPicto2(listado,idABuscar);
    }


    public synchronized boolean guardarJson(String archivo) {
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
            case Constants.ARCHIVO_JUEGO:
                jsonArrayAGuardar = mJSonArrayJuegos;
                break;
            case Constants.ARCHIVO_FRASES_FAVORITAS:
                jsonArrayAGuardar = mJSonArrayFrasesFavoritas;
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

    public JSONObject getJsonByPosition(JSONArray array,int position){
        try{
            return array.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Ya esta
    public JSONObject getPictoFromCustomArrayById(ArrayList<JSONObject> arrayList, int idABuscar) {
        int id = -1;
        for (JSONObject jsonObject : arrayList) {
            try {
                if (getId(jsonObject) == idABuscar) {
                    return jsonObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getId(JSONObject jsonObject) throws JSONException {
        return JSONutils.getId(jsonObject);
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



    private String readFromFile(String fileName) {
        File archivo = new File(mContext.getFilesDir(), fileName);
        if (archivo.length() > Constants.CINCO_MEGAS) {
            Log.d(TAG, "readFromFile:  Bigger than 5Mb");
        } else {
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            FileInputStream fis;
            try {
                if (fileName.equals(Constants.ARCHIVO_PICTOS) || fileName.equals(Constants.ARCHIVO_GRUPOS) || fileName.equals(Constants.ARCHIVO_FRASES) || fileName.equals(Constants.ARCHIVO_PICTOS_DATABASE) || fileName.equals(Constants.ARCHIVO_FRASES_JUEGOS) || fileName.equals(Constants.ARCHIVO_JUEGO) || fileName.equals(Constants.ARCHIVO_JUEGO_DESCRIPCION) || fileName.equals(Constants.ARCHIVO_FRASES_FAVORITAS))
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
                Log.e(TAG, "readFromFile: " + e);
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "readFromFile: " + e);

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


    private JSONArray elegirHijos2(JSONObject padre, boolean esSugerencia) throws JSONException {
            JSONArray array = padre.getJSONArray("relacion");
          //  new SortArraysByScore().quickSort(array,0,array.length()-1);
            ArrayList<JSONObject> relacion = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                relacion.add(array.getJSONObject(i));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                relacion.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject json1, JSONObject json2) {
                        return compareValues(json1,json2,esSugerencia);
                    }
                });
            } else {
                Collections.sort(relacion, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject json1, JSONObject json2) {
                      return compareValues(json1,json2,esSugerencia);
                    }
                });
            }
            Log.d(TAG, "elegirHijos2: Ordenado");
            return new JSONArray(relacion.toString());

        //return array;
    }



    public int compareValues(JSONObject json1,JSONObject json2,boolean esSugerencia){
        double frec1 = 0;
        double frec2 = 0;
        try {
            frec1 = getScore(json1, esSugerencia);
            frec2 = getScore(json2, esSugerencia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "compare 2 : frec 1: " + frec1 + ", frec2:" + frec2);
        return compareTo(frec1, frec2);
    }

    public JSONObject getPictoFromCustomArrayById2(JSONArray jsonArray, int idABuscar) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getInt("id") == idABuscar)
                return object;
        }
        return null;
    }

    public void loadPictogramsInsideArray(JSONObject father,JSONArray array,JSONArray relationShip,int lasPosition) throws JSONException {
        for (int i = 0; i <4 ; i++) {
            int position = i+lasPosition;
            if(isLessThanArray(relationShip,position)){
                addChildrenToArray(array,relationShip,position,false);
            }else{
                int lastLocation = position - array.length()+i;
                if(getValueBiggerOrEquals0(lastLocation)){
                    if(itIsASuggestedLanguage()){
                        if(mostrarSugerencias(father,lastLocation,array)){
                            int posPadre = getPosPictoBinarySearch(getmJSONArrayPictosSugeridos(),getId(father));
                            upgradeIndexOfLoadOptions((relationShip.length() + mJSONArrayPictosSugeridos.getJSONObject(posPadre).getJSONArray("relacion").length()) / 4);
                        }else{
                            array.put(i,createAnEmptyObject());
                            upgradeIndexOfLoadOptions(Constants.VUELTAS_CARRETE+1);
                        }
                    }else{
                        array.put(i,createAnEmptyObject());
                        upgradeIndexOfLoadOptions(Constants.VUELTAS_CARRETE+1);
                    }
                }else{
                   addChildrenToArray(array,relationShip,position,false);
                }
            }
        }
    }

    public void upgradeIndexOfLoadOptions(int value){
        if(Constants.VUELTAS_CARRETE != value)
            Constants.VUELTAS_CARRETE = value;
    }

    public boolean isLessThanArray(JSONArray array,int value){
        return value < array.length();
    }
    public boolean getValueBiggerOrEquals0(int value){
        return value >= 0;
    }

    public void addChildrenToArray(JSONArray array, JSONArray relationShip, int position,boolean isSuggested) throws JSONException {
        array.put(getPictoFromId2(relationShip.getJSONObject(position).getInt("id")));
        array.getJSONObject(array.length() - 1).put("esSugerencia", isSuggested);
    }

    public boolean itIsASuggestedLanguage(){
       return  (ConfigurarIdioma.getLanguaje().equals("es") || ConfigurarIdioma.getLanguaje().equals("ca") || ConfigurarIdioma.getLanguaje().equals("en")) && mJSONArrayPictosSugeridos.length() != 0 && sharedPrefsDefault.getBoolean("bool_sugerencias", false);
    }

    public JSONObject createAnEmptyObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", "-1");
        return object;
    }




    //TODO ver si se puede optimizar
    /*
       variable i : i es un desplazamiento que va de 0 a 3 , ultima posicion es el indicador desde donde comenzar
       variable ultimaposicion : indica la ultima posicion
     *
     * */
    public void cargarOpciones(JSONObject padre, int cuentaMasPictos, SortPictogramsInterface sortPictograms) {
        //mJSONArrayTodosLosPictos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);// leo los pictos
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
             try {
                 if (!consultarPago())
                     sharedPrefsDefault.edit().putBoolean("bool_sugerencias", consultarPago()).apply();
                 JSONArray relacion = elegirHijos2(padre, false); //selecciono el picto padre
                JSONArray jsonElegidos = new JSONArray();
                int ultimaPosicion = cuentaMasPictos * 4; //posicion del picto
                loadPictogramsInsideArray(padre,jsonElegidos,relacion,ultimaPosicion);
                sortPictograms.pictogramsAreSorted(jsonElegidos);
            }catch (JSONException ex){
                 Log.e(TAG, "exception: "+ ex.getMessage() );
             }
            }
        });
        thread.start();

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
                JSONutils.desvincularJson(mJSONArrayPictosSugeridos.getJSONObject(positionPadre), mJSONArrayPictosSugeridos.getJSONObject(positionPadre).getJSONArray("relacion").getJSONObject(pos).getInt("id"));
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



    public void cargarPictosSugeridosJson() {
        try {
            mJSONArrayPictosSugeridos = readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //pregunto por la posicion y el id
    }

    public void crearFrase(String frase, ArrayList<JSONObject> historial, long fecha) throws JSONException {
        if (!frase.isEmpty()) {
            int pos = fraseExist(frase);
            if (pos != -1) {
                mJSONArrayTodasLasFrases.getJSONObject(pos).put("frecuencia", mJSONArrayTodasLasFrases.getJSONObject(pos).getInt("frecuencia") + 1);
                mJSONArrayTodasLasFrases.getJSONObject(pos).accumulate("fecha", fecha);
                if (!mJSONArrayTodasLasFrases.getJSONObject(pos).has("id")) {
                    int id = getThePhraseLastId() + 1;
                    mJSONArrayTodasLasFrases.getJSONObject(pos).put("id", id);
                }
                if (!mJSONArrayTodasLasFrases.getJSONObject(pos).has("locale"))
                    mJSONArrayTodasLasFrases.getJSONObject(pos).put("locale", ConfigurarIdioma.getLanguaje());
                guardarJson(Constants.ARCHIVO_FRASES);
            } else {
                JSONObject nuevaFrase = new JSONObject();
                nuevaFrase.put("frase", frase);
                nuevaFrase.put("frecuencia", 1);
                nuevaFrase.put("complejidad", getComplejidad(historial));
                nuevaFrase.accumulate("fecha", fecha);
                nuevaFrase.put("locale", ConfigurarIdioma.getLanguaje());
                int id = getThePhraseLastId() + 1;
                nuevaFrase.put("id", id);
                mJSONArrayTodasLasFrases.put(nuevaFrase);
                guardarJson(Constants.ARCHIVO_FRASES);
            }
        }
    }

    public int getThePhraseLastId() {
        int id = -1;
        try {
            mJSONArrayTodasLasFrases.getJSONObject(0).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mJSONArrayTodasLasFrases.length(); i++) {
            try {
                JSONObject object = mJSONArrayTodasLasFrases.getJSONObject(i);
                if (mJSONArrayTodasLasFrases.getJSONObject(i).has("id")) {
                    int idFrase = mJSONArrayTodasLasFrases.getJSONObject(i).getInt("id");
                    if (id < idFrase)
                        id = idFrase;
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
            historial = JSONutils.stringToArrayList(arrayList.toString());
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
        cantFallas = 0;
    }

    public void sumarFallas() {
        cantFallas++;
    }

    public int getCantFallas() {
        return cantFallas;
    }



    public boolean estaEditado(JSONObject object) {
        try {
            if (object == null)
                return false;
            if(object.has("imagen")){
                if (object.getJSONObject("imagen").has("pictoEditado"))
                    return true;
            }
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public void agregarJuego(JSONObject object) {
        try {

            mJSonArrayJuegos.getJSONObject(0).getJSONObject(object.getInt("game") + "").put(object.getInt("levelId") + "", object);
        } catch (JSONException e) {
            e.printStackTrace();
            mJSonArrayJuegos.put(new JSONObject());
            try {
                JSONObject game = new JSONObject();
                game.accumulate(object.getInt("levelId") + "", object);
                mJSonArrayJuegos.getJSONObject(0).put(object.getInt("game") + "", game);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    public JSONObject getGame(int idGame, int leveId) {
        try {
            return mJSonArrayJuegos.getJSONObject(0).getJSONObject(idGame + "").getJSONObject(leveId + "");
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getObjectPuntaje(JSONObject object) {
        try {
            if(object.has("puntaje"))
                return object.getJSONObject("puntaje");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean consultarPago() {
        return sharedPrefsDefault.getInt("premium", 0) == 1;
    }

    public int devolverCantidadGruposUsados(int id) {
        int cant = 0;
        try {
            cant = mJSonArrayJuegos.getJSONObject(0).getJSONObject(id + "").length();

        } catch (JSONException e) {
            e.printStackTrace();
            cant = 0;
            Log.e(TAG, "devolverCantidadGruposUsados: " + e.getMessage());

        }
        return cant;
    }



    public int getScore(JSONObject object, boolean isSugerencia) {
        try {
            int score = (int) score(object, isSugerencia);
            Log.e(TAG, "getScore: " + score);
            return (int) score(object, isSugerencia);
        } catch (Exception ex) {
            Log.e(TAG, "getScore: " + ex.getMessage());
            return 0;
        }
    }



    public JSONArray getmJSonArrayFrasesFavoritas() {
        return mJSonArrayFrasesFavoritas;
    }

    public void setmJSonArrayFrasesFavoritas(JSONArray mJSonArrayFrasesFavoritas) {
        this.mJSonArrayFrasesFavoritas = mJSonArrayFrasesFavoritas;
    }

    public void addAraasacPictogramFromInternet(JSONObject pictogram) {
        mJSONArrayTodosLosPictos.put(pictogram);
    }

    public void addPictogramToAll(JSONObject object) {
        try {
            for (int i = 0; i < mJSONArrayTodosLosGrupos.length(); i++) {
                if (mJSONArrayTodosLosGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("ALL") || mJSONArrayTodosLosGrupos.getJSONObject(i).optJSONObject("texto").optString("en").equalsIgnoreCase("EveryThing")) {
                    JSONArray relacion = mJSONArrayTodosLosGrupos.getJSONObject(i).getJSONArray("relacion");
                    relacion.put(relacion.length(), object);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getmContext() {
        return mContext;
    }
}
