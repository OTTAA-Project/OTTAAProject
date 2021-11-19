package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.VincularPictosAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;

public class Picto_Vincular_Recycler_View extends Custom_recyclerView  {
    private JSONArray arrayVincular;
    private VincularPictosAdapter galeriaPictos2;
    private final String TAG="Picto_Vincular_R_V";
    public Picto_Vincular_Recycler_View(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
    }


    public void setArray(){

            array= Json.getInstance().getmJSONArrayTodosLosPictos();
            arrayVincular=new JSONArray();
        createRecyclerLayoutManager();
        galeriaPictos2=new VincularPictosAdapter(mActivity, R.layout.grid_item_layout,array,false).initGlideAttatcher();
        mRecyclerView.setAdapter(galeriaPictos2);
    }

    public void changeData(){
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void filtrarPictogramas(){
        galeriaPictos2.setmVincularArray(arrayAux);
        galeriaPictos2.setEsFiltrado(true);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
    public void pictogramasNoFiltrados(){
        galeriaPictos2.setEsFiltrado(false);
        try {
            array=JSONutils.getHijosGrupo2(json.getmJSONArrayTodosLosPictos(),json.getGrupoFromId(24));
            arrayAux=new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        galeriaPictos2.setmVincularArray(array);
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    public JSONArray getArray() {
        return array;
    }



    public JSONArray getmArrayFiltrados() {
        return arrayAux;
    }


    @Override
    public void onPictosFiltrados() {
        filtrarPictogramas();
    }

    @Override
    public void onPictosNoFiltrados() {
        pictogramasNoFiltrados();
    }

    public VincularPictosAdapter getGaleriaPictos2() {
        return galeriaPictos2;
    }


}
