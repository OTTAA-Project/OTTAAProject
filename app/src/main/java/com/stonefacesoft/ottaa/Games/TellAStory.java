package com.stonefacesoft.ottaa.Games;

import com.airbnb.lottie.LottieAnimationView;
import com.stonefacesoft.ottaa.Activities.Groups_TellStory;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.TalkActions.ProcessPhrase;
import com.stonefacesoft.ottaa.utils.TalkActions.TellStoryPhrase;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

/***
 *  Option one show : People,Animals , food,games
 *  Option two show : Colours,adjetives and feelings
 *  Option three show: Actions
 *  Option four show: Places and Stores
 * */
public class TellAStory extends GameViewSelectPictogramsFourOptions {
    private String promptChatGpt ="Act as a kindergarten teacher and tell me a story in "+ConfigurarIdioma.getNormalLanguage()+" for kids using the following words. The story should be short, one paragraph, and funny.{option1},{option2},{option3},{option4}:";
    private String promptChatGptAux ="Act as a kindergarten teacher and tell me a story in "+ConfigurarIdioma.getNormalLanguage()+" for kids using the following words. The story should be short, one paragraph, and funny.{option1},{option2},{option3},{option4}:";
    private int flag;
    private Json json;
    private GlideAttatcher glideAttatcher;

    private String story="";
    private TellStoryPhrase tellStoryPhrase;
    private boolean updatePrompt;



    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        mSeleccion.setText(R.string.TellStory_Description);
        myTTS = textToSpeech.getInstance(this);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        Seleccion1.setVisibility(View.INVISIBLE);
        mAnimationWin.setVisibility(View.GONE);
        Opcion1.setCustom_Texto("Personaje");
        Opcion2.setCustom_Texto("Color");
        Opcion3.setCustom_Texto("Accion");
        Opcion4.setCustom_Texto("Lugar");
        tellStoryPhrase = new TellStoryPhrase(this,lottieAnimationView,this,promptChatGpt, HandlerUtils.TRANSLATEDPHRASE);

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
            case R.id.ttsJuego:
                executeChatGpt();
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

    public void executeChatGpt(){
        promptChatGptAux = promptChatGpt.replace("{option1}",Opcion1.getCustom_Texto()).replace("{option2}",Opcion2.getCustom_Texto()).replace("{option3}",Opcion3.getCustom_Texto()).replace("{option4}",Opcion4.getCustom_Texto());
        tellStoryPhrase.executeChatGpt(promptChatGptAux);
    }

    public void talkAction(){
        if(!story.isEmpty())
            myTTS.hablar(story);
    }

    public void setStory(String story) {
        this.story = story;
    }
}
