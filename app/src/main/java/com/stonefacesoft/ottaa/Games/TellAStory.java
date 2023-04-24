package com.stonefacesoft.ottaa.Games;

import com.stonefacesoft.ottaa.Games.Story.GalleryGroupsChooser;
import com.stonefacesoft.ottaa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
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
        hideAllViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Option1:
                showIcon();
                Intent intent = new Intent(this, GalleryGroupsChooser.class);
                startActivity(intent);
                break;
            case R.id.Option2:
                showIcon();
                break;
            case R.id.Option3:
                showIcon();
                break;
            case R.id.Option4:
                showIcon();
                break;
        }
        super.onClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return super.onTouch(v, event);
    }

    private void showIcon(){
        flag++;
        showViews(flag);
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
