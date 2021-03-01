package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.SelectFavoritePhrasesAdapter;

public class PhrasesRecyclerView extends Custom_recyclerView{
    private SelectFavoritePhrasesAdapter adapter;


    public PhrasesRecyclerView(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        cantColumnas=1;
        setArray();
    }

    public void setArray(){
        array=json.getmJSONArrayTodasLasFrases();
        createRecyclerLayoutManager();
        adapter=new SelectFavoritePhrasesAdapter(mActivity);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    public  void savePhrases(){
        adapter.saveList();
    }

    @Override
    protected void createRecyclerLayoutManager() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
    }

}
