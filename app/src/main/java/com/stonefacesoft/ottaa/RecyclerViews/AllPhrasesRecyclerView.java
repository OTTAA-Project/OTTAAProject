package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.PhrasesAdapter;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
import org.json.JSONException;

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
                createReturnPositionItem();
                try {
                    myTTS.hablar(adapter.getUserPhrases().getJSONObject(getPositionItem.getPosition()).getString("frase"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPictosFiltrados() {
        adapter.setUserPhrases(arrayAux);
        createReturnPositionItem();
        sincronizeData();
    }

    @Override
    public void onPictosNoFiltrados() {
        adapter.setUserPhrases(array);
        sincronizeData();
    }

    @Override
    public void scrollTo(boolean add) {
        createReturnPositionItem();
        sincronizeData();
        if(add){
            getPositionItem.add();
        }
        else{
            getPositionItem.subtract();
        }
        mRecyclerView.scrollToPosition(getPositionItem.getPosition());
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

}
