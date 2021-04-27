package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.FindAllPictogramsAdapter;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;

class FindAllPictograms_Recycler_View extends Custom_recyclerView {

    private Json json;
    private JSONArray childs;
    private FindAllPictogramsAdapter findAllPictogramsAdapter;


    public FindAllPictograms_Recycler_View(AppCompatActivity appCompatActivity, FirebaseAuth mAuth){
        super(appCompatActivity,mAuth);
        try {
            childs=json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException mbException) {
            mbException.printStackTrace();
        }
        findAllPictogramsAdapter=new FindAllPictogramsAdapter(mActivity, R.layout.grid_item_layout,childs,true);
        mRecyclerView.setAdapter(findAllPictogramsAdapter);
    }


    @Override
    public void onPictosFiltrados() {
        filtrarPictogramas();
    }

    @Override
    public void onPictosNoFiltrados() {
        findAllPictogramsAdapter.setmVincularArray(childs);
        findAllPictogramsAdapter.setEsFiltrado(false);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void filtrarPictogramas(){
        findAllPictogramsAdapter.setmVincularArray(arrayAux);
        findAllPictogramsAdapter.setEsFiltrado(true);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
