package com.stonefacesoft.ottaa.Activities.Phrases;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.MostUsedPhrases_Recycler_View;
import com.stonefacesoft.ottaa.Views.Phrases.PhrasesView;
import com.stonefacesoft.ottaa.utils.IntentCode;

import java.util.ArrayList;

public class FavoritePhrases extends PhrasesView {

    private MostUsedPhrases_Recycler_View favorite_phrases_recycler_view;
    private ArrayList<FavModel> favoritePhrases;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void initComponents() {
        super.initComponents();
        favorite_phrases_recycler_view = new MostUsedPhrases_Recycler_View(this,firebaseUser.getmAuth());
        ImageButton foward=findViewById(R.id.down_button);
        ImageButton previous=findViewById(R.id.up_button);
        ImageButton exit=findViewById(R.id.back_button);
        ImageButton btnEditar=findViewById(R.id.edit_button);
        btnEditar.setVisibility(View.VISIBLE);
        btnEditar.setOnClickListener(FavoritePhrases.this);
        foward.setOnClickListener(FavoritePhrases.this);
        previous.setOnClickListener(FavoritePhrases.this);
        exit.setOnClickListener(FavoritePhrases.this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.up_button:
                favorite_phrases_recycler_view.scrollTo(false);
                break;
            case R.id.down_button:
                favorite_phrases_recycler_view.scrollTo(true);
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.edit_button:
                Intent intent=new Intent(this, VincularFrases.class);
                startActivityForResult(intent, IntentCode.CUSTOMPHRASES.getCode());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
