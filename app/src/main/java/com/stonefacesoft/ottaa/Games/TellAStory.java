package com.stonefacesoft.ottaa.Games;

import com.stonefacesoft.ottaa.Activities.Groups_TellStory;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONObject;

/***
 *  Option one show : People,Animals , food,games
 *  Option two show : Colours,adjetives and feelings
 *  Option three show: Actions
 *  Option four show: Places and Stores
 * */
public class TellAStory extends GameViewSelectPictogramsFourOptions {
    private String promptChatGpt ="Necesito armar una historia a partir de la siguientes frases:";
    private int flag;
    private Json json;
    private GlideAttatcher glideAttatcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        Seleccion1.setVisibility(View.INVISIBLE);
        mAnimationWin.setVisibility(View.GONE);
        Opcion1.setCustom_Texto("Personaje");
        Opcion2.setCustom_Texto("Color");
        Opcion3.setCustom_Texto("Accion");
        Opcion4.setCustom_Texto("Lugar");
        //  hideAllViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Option1:
                showIcon(0);
                break;
            case R.id.Option2:
                showIcon(1);
                break;
            case R.id.Option3:
                showIcon(2);
                break;
            case R.id.Option4:
                showIcon(3);
                break;
        }
    }



    private void showIcon(int position){
        TellAStoryUtils.getInstance().setPictoPosition(position);
        Intent intent = new Intent(this, Groups_TellStory.class);
        startActivityForResult(intent, IntentCode.TELL_A_STORY.getCode());
    }

    public void showViews(int flag){
        switch (flag){
            case 1:
                Opcion2.setVisibility(View.VISIBLE);
            break;
            case 2:
                Opcion3.setVisibility(View.VISIBLE);
            break;
            case 3:
                Opcion4.setVisibility(View.VISIBLE);
            break;
            default:
                Opcion1.setVisibility(View.VISIBLE);
            break;
        }
    }

    public void hideAllViews(){
        Opcion2.setVisibility(View.INVISIBLE);
        Opcion3.setVisibility(View.INVISIBLE);
        Opcion4.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.TELL_A_STORY:
                galeriaGruposResult(data);
            break;
        }

    }

    private void galeriaGruposResult(Intent data) {
        if (data != null) {
            if(json==null) {
                json = Json.getInstance();
            }
            json.setmJSONArrayTodosLosPictos(Json.getInstance().getmJSONArrayTodosLosPictos());
            Bundle extras = data.getExtras();
            if (extras != null) {
                int Picto = extras.getInt("ID");
                if (Picto != 0) {
                    loadPictogram(Picto);
                }
            }
        }
    }

    private void loadPictogram(int picto){
        if(glideAttatcher == null)
            glideAttatcher = new GlideAttatcher(getApplicationContext());
        switch (TellAStoryUtils.getInstance().getPictoPosition()){
            case 0:
                loadDataPictoView(picto,Opcion1);

                break;
            case 1:
                loadDataPictoView(picto,Opcion2);
                break;
            case 2:
                loadDataPictoView(picto,Opcion3);
                break;
            case 3:
                loadDataPictoView(picto,Opcion4);
                break;
        }
    }

    private void loadDataPictoView(int id, PictoView option){
        option.setUpContext(this);
        option.setUpGlideAttatcher(this);
        option.setPictogramsLibraryPictogram(new Pictogram(json.getPictoFromId2(id), ConfigurarIdioma.getLanguaje()));
    }


}
