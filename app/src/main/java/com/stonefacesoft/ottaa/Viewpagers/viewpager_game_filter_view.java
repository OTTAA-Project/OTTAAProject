package com.stonefacesoft.ottaa.Viewpagers;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONArray;

public class viewpager_game_filter_view extends viewpager_galeria_grupo{

    public viewpager_game_filter_view(AppCompatActivity mActivity, textToSpeech myTTS) {
        super(mActivity, myTTS);
    }

    @Override
    protected void initArray() {
        array= new JSONArray();
        array.put(json.getGrupoFromId(2));
        array.put(json.getGrupoFromId(17));
        array.put(json.getGrupoFromId(3));
        array.put(json.getGrupoFromId(12));
    }
}
