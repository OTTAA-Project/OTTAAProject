package com.stonefacesoft.ottaa.Games;

import com.stonefacesoft.ottaa.Activities.Groups_TellStory;
import com.stonefacesoft.ottaa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;

/***
 *  Option one show : People,Animals , food,games
 *  Option two show : Colours,adjetives and feelings
 *  Option three show: Actions
 *  Option four show: Places and Stores
 * */
public class TellAStory extends GameViewSelectPictogramsFourOptions {
    private String promptChatGpt ="Necesito armar una historia a partir de la siguientes frases:";
    private int flag;
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
        super.onClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return super.onTouch(v, event);
    }

    private void showIcon(int position){
        TellAStoryUtils.getInstance().setPictoPosition(position);
        Intent intent = new Intent(this, Groups_TellStory.class);
        startActivity(intent);
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
}
