package com.stonefacesoft.ottaa.RecyclerViews;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.CustomFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Phrases.CustomFavoritePhrases;

public class Favorite_Phrases_recycler_view extends Custom_recyclerView {
    private CustomFavoritePhrasesAdapter adapter;
    private CustomFavoritePhrases customFavoritePhrases;
    private final String TAG ="Favorite_phrase";

    public Favorite_Phrases_recycler_view(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray() {
        customFavoritePhrases = new CustomFavoritePhrases(mActivity);
        array = customFavoritePhrases.getPhrases();
        arrayAux = customFavoritePhrases.getPhrases();
        createRecyclerLayoutManager();
        adapter = new CustomFavoritePhrasesAdapter(mActivity);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talkAtPosition();
            }
        });
    }

    public void updateData(){
        array = new CustomFavoritePhrases(mActivity).getPhrases();
        adapter.updateData();
    }

    @Override
    public void sincronizeData() {
        getPositionItem.updateSize(adapter.getItemCount());
    }

    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    @Override
    public void scrollTo(boolean add) {
        if (createReturnPositionItem()) {
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
    protected void createRecyclerLayoutManager() {
        ScrollManager manager = new ScrollManager(mActivity, 1, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void talkAtPosition() {
        try {
            if(createReturnPositionItem())
                adapter.getMyTTs().hablar(adapter.getPhrases().getPhrases().getJSONObject(getPositionItem.getPosition()).getString("frase"));
        } catch (Exception e) {
            e.printStackTrace();
            myTTS.mostrarAlerta(mActivity.getResources().getString(R.string.str_favorite_phrases_empty));
        }

    }



}
