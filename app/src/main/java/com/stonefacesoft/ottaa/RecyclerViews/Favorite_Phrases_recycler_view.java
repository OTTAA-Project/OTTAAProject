package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.CustomFavoritePhrasesAdapter;

public class Favorite_Phrases_recycler_view extends Custom_recyclerView{
    private CustomFavoritePhrasesAdapter adapter;

    public Favorite_Phrases_recycler_view(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray(){
        array=json.getmJSONArrayTodasLasFrases();
        createRecyclerLayoutManager();
        adapter=new CustomFavoritePhrasesAdapter(mActivity);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    @Override
    protected void createRecyclerLayoutManager() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
    }

}
