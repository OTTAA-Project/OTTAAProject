package com.stonefacesoft.ottaa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Adapters.TagsAdapter;
import com.stonefacesoft.ottaa.Interfaces.DialogInterfaceTags;
import com.stonefacesoft.ottaa.Interfaces.TagInterfazJson;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
// Todo modularizar esta clase en partes
public class AsignTags {

    private static final String TAG = "AsignTags";
    private Json json;
    private JSONObject picto;
    private boolean esGrupo = false;
    private final boolean esGrupo2 = false;
    private Context mContext;
    private TagsAdapter adapter;
    private String pictoGrupoJson;
    private ArrayList<JSONObject> mArrayListSelectedTAGS;
    private ArrayList<JSONObject> mArrayListTagsPorTipo;
    private DialogInterfaceTags mDialogInterface;
    private TagInterfazJson mTagInterface;
    private ArrayList<JSONObject> arrayListTodosLosTags;
    private ArrayList<JSONObject> noselectedTags;

    public AsignTags() {
    }

    public AsignTags(Context mContext) {
        this.mContext = mContext;

        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();

        this.mArrayListSelectedTAGS = new ArrayList<>();
        this.mArrayListTagsPorTipo = new ArrayList<>();
        this.arrayListTodosLosTags = new ArrayList<>();

        String tags = "[{\"id\":379,\"texto\":{\"en\":\"Morning\",\"es\":\"" + Horario.MANANA + "\"},\"tipo\":\"" + Constants.HORA + "\",\"imagen\":{\"picto\":\"ic_buenos_dias\"}}," +
                "{\"id\":818,\"texto\":{\"en\":\"Noon\",\"es\":\"" + Horario.MEDIODIA + "\"},\"tipo\":\"" + Constants.HORA + "\",\"imagen\":{\"picto\":\"verano\"}}," +
                "{\"id\":380,\"texto\":{\"en\":\"Afternoon\",\"es\":\"" + Horario.TARDE + "\"},\"tipo\":\"" + Constants.HORA + "\",\"imagen\":{\"picto\":\"ic_buenas_tardes\"}}," +
                "{\"id\":381,\"texto\":{\"en\":\"Night\",\"es\":\"" + Horario.NOCHE + "\"},\"tipo\":\"" + Constants.HORA + "\",\"imagen\":{\"picto\":\"ic_buenas_noches\"}}," +
                "{\"id\":614,\"texto\":{\"en\":\"Grandpa\",\"es\":\"" + Edad.ADULTO + "\"},\"tipo\":\"" + Constants.EDAD + "\",\"imagen\":{\"picto\":\"ic_abuelo\"}}," +
                "{\"id\":195,\"texto\":{\"en\":\"Young\",\"es\":\"" + Edad.JOVEN + "\"},\"tipo\":\"" + Constants.EDAD + "\",\"imagen\":{\"picto\":\"joven\"}}," +
                "{\"id\":630,\"texto\":{\"en\":\"Boy\",\"es\":\"" + Edad.NINO + "\"},\"tipo\":\"" + Constants.EDAD + "\",\"imagen\":{\"picto\":\"nino\"}}," +
                "{\"id\":651,\"texto\":{\"en\":\"Field\",\"es\":\"" + Posicion.ESTADIO + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"cancha\"}}," +
                "{\"id\":653,\"texto\":{\"en\":\"Square\",\"es\":\"" + Posicion.PARQUE + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"plaza\"}}," +
                "{\"id\":1013,\"texto\":{\"en\":\"Movie theater\",\"es\":\"" + Posicion.CINE + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"cine\"}}," +
                "{\"id\":654,\"texto\":{\"en\":\"Computer store\",\"es\":\"" + Posicion.TIENDA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"tienda_de_informatica\"}}," +
                "{\"id\":687,\"texto\":{\"en\":\"Bar\",\"es\":\"" + Posicion.BAR + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"bar\"}}," +
                "{\"id\":496,\"texto\":{\"en\":\"Coffee\",\"es\":\"" + Posicion.CAFE + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"cafe\"}}," +
                "{\"id\":685,\"texto\":{\"en\":\"Restaurant\",\"es\":\"" + Posicion.RESTAURANT + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"restaurante\"}}," +
                "{\"id\":672,\"texto\":{\"en\":\"Bakery\",\"es\":\"" + Posicion.PANADERIA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"pan_blanco\"}}," +
                "{\"id\":652,\"texto\":{\"en\":\"Butchery\",\"es\":\"" + Posicion.CARNICERIA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"carniceria\"}}," +
                "{\"id\":698,\"texto\":{\"en\":\"Greengrocery\",\"es\":\"" + Posicion.VERDULERIA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"verduleria\"}}," +
                "{\"id\":655,\"texto\":{\"en\":\"Farmacy\",\"es\":\"" + Posicion.FARMACIA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"ic_farmacia\"}}," +
                "{\"id\":668,\"texto\":{\"en\":\"Hospital\",\"es\":\"" + Posicion.HOSPITAL + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"hospital\"}}," +
                "{\"id\":662,\"texto\":{\"en\":\"School\",\"es\":\"" + Posicion.ESCUELA + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"ic_escuela\"}}," +
                "{\"id\":611,\"texto\":{\"en\":\"Transportation\",\"es\":\"" + Posicion.ESTACIONDEBUS + "\"},\"tipo\":\"" + Constants.UBICACION + "\",\"imagen\":{\"picto\":\"transportes\"}}," +
                "{\"id\":1033,\"texto\":{\"en\":\"Woman\",\"es\":\"" + Sexo.FEMENINO + "\"},\"tipo\":\"" + Constants.SEXO + "\",\"imagen\":{\"picto\":\"mujer\"}}," +
                 "{\"id\":1035,\"texto\":{\"en\":\"Man\",\"es\":\"" + Sexo.MASCULINO + "\"},\"tipo\":\"" + Constants.SEXO + "\",\"imagen\":{\"picto\":\"hombre\"}}," +
                 "{\"id\":1036,\"texto\":{\"en\":\"Binary\",\"es\":\"" + Sexo.BINARIO + "\"},\"tipo\":\"" + Constants.SEXO + "\",\"imagen\":{\"picto\":\"hombre\"}}," +
                 "{\"id\":1037,\"texto\":{\"en\":\"Fluid\",\"es\":\"" + Sexo.FLUIDO + "\"},\"tipo\":\"" + Constants.SEXO + "\",\"imagen\":{\"picto\":\"hombre\"}}," +
                "{\"id\":1038,\"texto\":{\"en\":\"Others\",\"es\":\"" + Sexo.OTRO + "\"},\"tipo\":\"" + Constants.SEXO + "\",\"imagen\":{\"picto\":\"hombre\"}}]\n";

        arrayListTodosLosTags = JSONutils.stringToArrayList(tags);


    }


    // Setter para el listener de la clase AsignTags
    public void setInterfaz(DialogInterfaceTags interfaz) {
        this.mDialogInterface = interfaz;
    }

    public DialogInterfaceTags getmDialogInterface() {
        return mDialogInterface;
    }

    public TagInterfazJson getmTagInterface() {
        return mTagInterface;
    }

    public void setInterfazTag(TagInterfazJson interfaz) {
        this.mTagInterface = interfaz;
    }

    public void setAdapter(RecyclerView recyclerView) {

        adapter = new TagsAdapter(mArrayListTagsPorTipo, R.layout.grid_item_layout, mArrayListSelectedTAGS, mContext);
        recyclerView.setAdapter(adapter);

    }

    public void setExtras(JSONObject pictoExtra, boolean esGrupoExtra) {
        this.picto = pictoExtra;
        this.esGrupo = esGrupoExtra;

    }

    public JSONObject getPicto() {
        return picto;
    }

    public TagsAdapter getTagsAdapter() {
        return adapter = new TagsAdapter(mArrayListTagsPorTipo, R.layout.grid_item_layout, mArrayListSelectedTAGS, mContext);
    }

    public void getTagsYaAsignados() {
        if (esGrupo) {

            try {
                JSONArray tagDeGrupo = picto.getJSONArray("tags");
                for (int i = 0; i < tagDeGrupo.length(); i++) {
                    //agrego todos los tags guardados en grupo
                    mArrayListSelectedTAGS.add(json.getJsonObjectFromTexto(arrayListTodosLosTags, tagDeGrupo.getString(i)));

                }
                cargarTagsPictos();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            cargarTagsPictos();
        }

    }


    public void cargarTags(String tipoTag) {
        try {
            mArrayListTagsPorTipo = json.getArrayListFromTipo(tipoTag, arrayListTodosLosTags);
            getTagsYaAsignados();
        } catch (Exception ex) {

        }
    }

    //Funciona para que cuando apreto el boton ok del dialog asigne a los grupos o pictos los tags
    public void asignarTags(Dialog dialog) {
        if (esGrupo) {
            //Set a todos los hijos del grupo
            ModificarTags modificarTags = new ModificarTags(mContext, false, dialog);
            modificarTags.execute();
        } else {
            //set a un solo picto
            try {
                setTagsToPicto(picto);
                dialog.dismiss();
                //((DialogInterfaceTags) mContext).onTagAsignado(picto.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }





    public void setTagToGrupo(JSONObject grupo, Context context, boolean esAgenda) {

        ModificarTags modificarTags = new ModificarTags(context, esAgenda);
        modificarTags.execute();
    }

    private void setTagsToGrupo(JSONObject grupo, boolean esAgenda) throws FiveMbException {
        if(grupo!=null&&grupo.has("tags"))
            grupo.remove("tags");
        JSONArray todosLosPictos = null;
        try {
            todosLosPictos = json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

                JSONArray array = grupo.getJSONArray("relacion");//relacion de los grupos
                for (int i = 0; i < array.length(); i++) {
                    //Cargo el json que apunta al picto a modificar
                    JSONObject jsonGrupo = array.getJSONObject(i);// picto obtenido
                    Log.d(TAG, "setTagsToGrupo: " + jsonGrupo.getInt("id"));
                    //Modifico el json desde pictos

                    JSONObject jsonPicto = json.getPictoFromId2(jsonGrupo.getInt("id"));
                    if (jsonPicto != null) {
                        setTagsToPictogram(jsonPicto);
                        JSONutils.setJsonEditado2(json.getmJSONArrayTodosLosPictos(), jsonPicto);
                    } else {
                        array.remove(i);
                        int pos = json.getPosPicto(json.getmJSONArrayTodosLosGrupos(), jsonGrupo.getInt("id"));
                        json.getmJSONArrayTodosLosGrupos().getJSONObject(pos).put("relacion", array);
                    }
                }

            if (!json.guardarJson(Constants.ARCHIVO_PICTOS))
                Log.e(TAG, "Error al guardar el json");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void guardarTags() {

        if (mArrayListSelectedTAGS.size() > 0) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < mArrayListSelectedTAGS.size(); i++) {
                    jsonArray.put(mArrayListSelectedTAGS.get(i).getJSONObject("texto").getString("en"));
                    Log.d(TAG, "guardarTags: " + jsonArray);
                }
                picto.put("tags", jsonArray);
                //((TagInterfazJson) mContext).refrescarJsonTags(picto);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "guardarTags: Error");
            }
        }

    }



    public class ModificarTags extends AsyncTask<Void, Void, Boolean> {

        private final String TAG = "LongTask.java";
        private final ProgressDialog p;
        private final boolean esAgenda;
        private Dialog dialog;

        /*
             Constructor
         */
        public ModificarTags(Context context, boolean esAgenda, Dialog dialogDismiss) {
            this.p = new ProgressDialog(context);
            this.esAgenda = esAgenda;
            this.dialog = dialogDismiss;
        }

        public ModificarTags(Context context, boolean esAgenda) {
            this.p = new ProgressDialog(context);
            this.esAgenda = esAgenda;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p.setMessage("Agregando TAGs");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                setTagsToGrupo(picto, esAgenda);
            } catch (FiveMbException e) {
                e.printStackTrace();
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (p.isShowing() && p != null) {
                p.dismiss();
            }
            guardarTags();
            ((DialogInterfaceTags) mContext).onTagAsignado(picto.toString());

            if (dialog.isShowing() && dialog != null) {
                dialog.dismiss();
            }

        }
    }
/*
*The system verify if the tag exist
*The system run into the array an question which is the selected tag and mark them
* */
    private void cargarTagsPictos() {
        try {
            JSONArray pictosTags;
            ArrayList<String> tagNames = new ArrayList<>();
            tagNames.add(Constants.HORA);
            tagNames.add(Constants.EDAD);
            tagNames.add(Constants.SEXO);
            tagNames.add(Constants.UBICACION);
            for (int i = 0; i < tagNames.size(); i++) {
                String tagPorPos = tagNames.get(i);
                if (picto.has(tagPorPos)) {
                    pictosTags = picto.getJSONArray(tagPorPos);
                    for (int j = 0; j < pictosTags.length(); j++) {
                        //agrego todos los tags guardados en grupo
                        mArrayListSelectedTAGS.add(json.getJsonObjectFromTexto(arrayListTodosLosTags, pictosTags.getString(j)));
                        Log.d(TAG, "cargarTagsPictos: " + pictosTags.getString(j));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "cargarTagsPictos: Error" + e.getMessage());
        }

    }


    public void setTagsToPicto(JSONObject picto) throws JSONException {
        if(picto!=null) {
            //Borrar los array de tags
            if (tieneTag(Constants.HORA, picto))
                picto.remove(Constants.HORA);
            if (tieneTag(Constants.EDAD, picto))
                picto.remove(Constants.EDAD);
            if (tieneTag(Constants.UBICACION, picto))
                picto.remove(Constants.UBICACION);
            if (tieneTag(Constants.SEXO, picto))
                picto.remove(Constants.SEXO);
          setTagsToPictogram(picto);
        }

    }

    private Horario getHorario(int id) {
        switch (id) {
            case 379:
                return Horario.MANANA;
            case 818:
                return Horario.MEDIODIA;
            case 380:
                return Horario.TARDE;
            case 381:
                return Horario.NOCHE;
        }
        return null;
    }

    private Edad getEdad(int edad) {
        switch (edad) {
            case 614:
                return Edad.ADULTO;
            case 195:
                return Edad.JOVEN;
            case 630:
                return Edad.NINO;
        }
        return null;
    }

    private Sexo getSexo(int sexo) {
        switch (sexo) {
            case 1035:
                return Sexo.MASCULINO;
            case 1033:
                return Sexo.FEMENINO;
            case 1036:
                return Sexo.BINARIO;
            case 1037:
                return Sexo.FLUIDO;
            case 1038:
                return Sexo.BINARIO;
        }
        return null;
    }

    private Posicion getPosicion(int posicion) {
        switch (posicion) {
            case 668:
                return Posicion.HOSPITAL;
            case 611:
                return Posicion.ESTACIONDEBUS;
            case 654:
                return Posicion.TIENDA;
            case 662:
                return Posicion.ESCUELA;
            case 685:
                return Posicion.RESTAURANT;
            case 652:
                return Posicion.CARNICERIA;
            case 698:
                return Posicion.VERDULERIA;
            case 1013:
                return Posicion.CINE;
            case 651:
                return Posicion.ESTADIO;
            case 496:
                return Posicion.CAFE;
            case 687:
                return Posicion.BAR;
            case 653:
                return Posicion.PARQUE;
            case 655:
                return Posicion.FARMACIA;
            case 672:
                return Posicion.PANADERIA;
        }
        return null;
    }

    private boolean tieneTag(String  texto,JSONObject object){
        return object.has(texto);
    }

    public ArrayList<JSONObject> getArrayListTodosLosTags() {
        return arrayListTodosLosTags;
    }

    public void setmArrayListSelectedTAGS(ArrayList<JSONObject> mArrayListSelectedTAGS) {
        this.mArrayListSelectedTAGS = mArrayListSelectedTAGS;
    }

    public ArrayList<JSONObject> getmArrayListSelectedTAGS() {
        return mArrayListSelectedTAGS;
    }

    public ArrayList<JSONObject> getmArrayListTagsPorTipo() {
        return mArrayListTagsPorTipo;
    }

    public void setTagsToPictogram(JSONObject picto) {
        if (picto != null) {
            ArrayList<Horario> tagHora = new ArrayList<>();
            ArrayList<Edad> tagEdad = new ArrayList<>();
            ArrayList<Sexo> tagSexo = new ArrayList<>();
            ArrayList<Posicion> tagPosicion = new ArrayList<>();
            getTags(tagHora,tagEdad,tagSexo,tagPosicion);
            for (int i = 0; i < tagHora.size(); i++) {
                JSONutils.setHorario(picto, tagHora.get(i));
            }
            for (int i = 0; i < tagEdad.size(); i++) {
                JSONutils.setEdad(picto, tagEdad.get(i));
            }
            for (int i = 0; i < tagSexo.size(); i++) {
                JSONutils.setSexo(picto, tagSexo.get(i));
            }
            for (int i = 0; i < tagPosicion.size(); i++) {
                JSONutils.setPosicion(picto, tagPosicion.get(i));
            }

        }
    }

    private void getTags(ArrayList<Horario> tagHora,ArrayList<Edad> tagEdad,ArrayList<Sexo> tagSexo,ArrayList<Posicion> tagPosicion){
        for (int i = 0; i < mArrayListSelectedTAGS.size(); i++) {
            try {
                if (getHorario(mArrayListSelectedTAGS.get(i).getInt("id")) != null) {
                    tagHora.add(getHorario(mArrayListSelectedTAGS.get(i).getInt("id")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (getEdad(mArrayListSelectedTAGS.get(i).getInt("id")) != null) {
                    tagEdad.add(getEdad(mArrayListSelectedTAGS.get(i).getInt("id")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (getSexo(mArrayListSelectedTAGS.get(i).getInt("id")) != null) {
                    tagSexo.add(getSexo(mArrayListSelectedTAGS.get(i).getInt("id")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (getPosicion(mArrayListSelectedTAGS.get(i).getInt("id")) != null) {
                    tagPosicion.add(getPosicion(mArrayListSelectedTAGS.get(i).getInt("id")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
