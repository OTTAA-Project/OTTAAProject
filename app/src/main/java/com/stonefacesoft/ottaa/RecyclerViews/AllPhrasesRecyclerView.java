package com.stonefacesoft.ottaa.RecyclerViews;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.PhrasesAdapter;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.CompartirArchivos;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllPhrasesRecyclerView extends Custom_recyclerView {
    private PhrasesAdapter adapter;
    private final String TAG ="AllPhrasesRecyclerView";


    public AllPhrasesRecyclerView(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray() {
        adapter = new PhrasesAdapter(mActivity);
        array = adapter.getUserPhrases();
        arrayAux = adapter.getUserPhrases();
        createRecyclerLayoutManager();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setOnClickListener() {
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(createReturnPositionItem())
                        talkAtPosition();
            }
        });
    }

    @Override
    public void onPictosFiltrados() {
        adapter.setUserPhrases(arrayAux);
        if(createReturnPositionItem())
            sincronizeData();
    }

    @Override
    public void onPictosNoFiltrados() {
        adapter.setUserPhrases(array);
        sincronizeData();
    }

    @Override
    public void scrollTo(boolean add) {
        if(createReturnPositionItem()) {
            sincronizeData();
            if (add) {
                getPositionItem.add();
            } else {
                getPositionItem.subtract();
            }
            mRecyclerView.scrollToPosition(getPositionItem.getPosition());
        }
    }

    @Override
    public void sincronizeData() {
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void recorrerListado(int k, int tam, String query) {
        for (int i = k; i < tam; i++) {
            try {
                if (array.getJSONObject(i).getString("frase").toLowerCase().
                        replace(mActivity.getString(R.string.chart_a), "a").
                        replace(mActivity.getString(R.string.chart_e), "e").
                        replace(mActivity.getString(R.string.chart_i), "i").
                        replace(mActivity.getString(R.string.chart_o), "o").
                        replace(mActivity.getString(R.string.chart_u), "u").
                        contains(query.toLowerCase())) {
                    arrayAux.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   Log.e("positionBusqueda",i+"");
        }
    }


    @Override
    protected void createRecyclerLayoutManager() {
        ScrollManager manager = new ScrollManager(mActivity,1,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void talkAtPosition(){
        try {
            if(createReturnPositionItem()){
              int value = getPositionItem.getPosition();
              if(validatePosition(value))
                myTTS.hablar(adapter.getUserPhrases().getJSONObject(value).getString("frase"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareAudio(CompartirArchivos compartirArchivos){
        try {
            if(createReturnPositionItem()){
                int value = getPositionItem.getPosition();
                if(validatePosition(value)) {
                    GestionarBitmap gestionarBitmap = new GestionarBitmap(mActivity);
                    JSONArray child =gestionarBitmap.getJsonArray(adapter.getUserPhrases().getJSONObject(value));
                    compartirArchivos.setHistorial(getJSonObjectList(child));
                    compartirArchivos.seleccionarFormato(adapter.getUserPhrases().getJSONObject(value).getString("frase"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<JSONObject> getJSonObjectList(JSONArray array){
        ArrayList<JSONObject> pictograms = new ArrayList<>();
        try {
            for (int i = 0; i <= array.length()-1; i++) {
                JSONObject picto = json.getPictoFromId2(array.getJSONObject(i).getInt("id"));
                if(picto!=null){
                    pictograms.add(picto);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pictograms;
    }




}
