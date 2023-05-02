package com.stonefacesoft.ottaa.Games;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.Activities.Groups_TellStory;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictogramsFourOptions;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
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
    private String promptChatGpt ="Act as a kindergarten teacher and tell me a story in {language} for kids using the following words :{option1},{option2},{option3},{option4}. The story should be short, one paragraph, and funny";
    private String promptChatGptAux ="";
    private int flag;
    private Json json;
    private GlideAttatcher glideAttatcher;

    private String story="";
    private TellStoryPhrase tellStoryPhrase;

    private boolean executeChatGPT = true;

    private ImageView textView;
    private TextView textViewStory;

    private ConstraintLayout menu_game;
    private ConstraintLayout gallery_navigator;

    private boolean tablet;

    private Menu mMenu;

    private MenuItem scoreItem;


    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbarName(toolbar,R.string.TellStory);
        initComponents();
        tablet = isTablet();
        menu_game = findViewById(R.id.container1);
        gallery_navigator = findViewById(R.id.container);
        textView = findViewById(R.id.story);
        textViewStory = findViewById(R.id.storyText);
        textView.setVisibility(View.VISIBLE);
        textView.setOnClickListener(this);
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSeleccion.setText(R.string.TellStory_Description);
        myTTS = textToSpeech.getInstance(this);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        Seleccion1.setVisibility(View.GONE);
        mAnimationWin.setVisibility(View.GONE);
        tellStoryPhrase = new TellStoryPhrase(this,lottieAnimationView,this,promptChatGpt, HandlerUtils.TRANSLATEDPHRASE);
        if(json==null) {
            json = Json.getInstance();
        }
       // initUtilsTTS(sharedPrefsDefault);
        json.setmJSONArrayTodosLosPictos(Json.getInstance().getmJSONArrayTodosLosPictos());
        downloadPromt();
        initPictograms();
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
            case R.id.story:
                    showOrHidePictograms();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Json.getInstance().setmContext(this);
    }

    private void showIcon(int position){
        TellAStoryUtils.getInstance().setPictoPosition(position);
        Intent intent = new Intent(this, Groups_TellStory.class);
        startActivityForResult(intent, IntentCode.TELL_A_STORY.getCode());
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
        executeChatGPT = true;
        if(glideAttatcher == null)
            glideAttatcher = new GlideAttatcher(getApplicationContext());
        switch (TellAStoryUtils.getInstance().getPictoPosition()){
            case 0:
                loadDataPictoView(picto,Opcion1,false);
                break;
            case 1:
                loadDataPictoView(picto,Opcion2,false);
                break;
            case 2:
                loadDataPictoView(picto,Opcion3,false);
                break;
            case 3:
                loadDataPictoView(picto,Opcion4,false);
                break;
        }
    }

    private void loadDataPictoView(int id, PictoView option,boolean group){
        option.setUpContext(this);
        option.setUpGlideAttatcher(this);
        if(group) {
            Pictogram pictogram = new Pictogram(Json.getInstance().getGrupoFromId(id), ConfigurarIdioma.getLanguaje());
            option.setCustom_Texto(pictogram.getObjectName());
        }
        else
            option.setPictogramsLibraryPictogram(new Pictogram(Json.getInstance().getPictoFromId2(id), ConfigurarIdioma.getLanguaje()));
    }

    public void executeChatGpt(){
        if(executeChatGPT){
            executeChatGPT = false;
            promptChatGptAux = promptChatGpt.replace("{language}",ConfigurarIdioma.getNormalLanguage()).replace("{option1}",Opcion1.getCustom_Texto()).replace("{option2}",Opcion2.getCustom_Texto()).replace("{option3}",Opcion3.getCustom_Texto()).replace("{option4}",Opcion4.getCustom_Texto());
            tellStoryPhrase.executeChatGpt(promptChatGptAux);
        }else{
            talkAction();
        }
    }

    public void talkAction(){
        if(!story.isEmpty())
            myTTS.hablar(story);
    }

    public void setStory(String story) {
        this.story = story;
    }



    private void initPictograms(){
        loadDataPictoView(17,Opcion1,true);
        loadDataPictoView(1,Opcion2,true);
        loadDataPictoView(0,Opcion3,true);
        loadDataPictoView(21,Opcion4,true);
    }

    public void downloadPromt(){
        promptChatGpt = RemoteConfigUtils.getInstance().getmFirebaseRemoteConfig().getString("tellStoryChatGPT");
        Log.d("ChatGPT", "downloadPromt: "+ promptChatGpt);
    /*    DatabaseReference ref = FirebaseUtils.getInstance().getmDatabase();
        ref.child("ChatGPTPrompt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(ConfigurarIdioma.getLanguaje())){
                    snapshot.child(ConfigurarIdioma.getLanguaje()).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            promptChatGpt = snapshot.getValue().toString();
                            Log.e(TAG, "Download Prompt: "+ chatGPTPrompt );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    protected void onStop() {
        super.onStop();
      myTTS.getTTS().stop();
    }

    public void setText(){
        textViewStory.setText(story);

    }

    private boolean isTablet() {
        // Get the screen size of the device
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));

        // Check if the screen size is greater than or equal to 7 inches
        return diagonalInches >= 7;
    }

    private void showOrHidePictograms(){
        int drawable= R.drawable.baseline_visibility_off_24;
        int view =  menu_game.getVisibility();
            if(view==View.GONE){
                menu_game.setVisibility(View.VISIBLE);
                drawable = R.drawable.baseline_visibility_off_24;
                if(!isTablet())
                    gallery_navigator.setVisibility(View.GONE);
            }
            else if(view== View.VISIBLE){
                menu_game.setVisibility(View.GONE);
                if(!isTablet())
                    gallery_navigator.setVisibility(View.VISIBLE);
                drawable = R.drawable.baseline_visibility_24;

            }

        textView.setImageDrawable(getResources().getDrawable(drawable));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mMenu = menu;
        inflater.inflate(R.menu.action_bar_game, mMenu);
        // mMenu = menu;
        scoreItem = menu.findItem(R.id.score);
        // animGameScore.setMenuView(mMenu);
        mMenu.getItem(0).setVisible(true);
        menu.getItem(2).setVisible(false);
        mMenu.getItem(0).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(1).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(3).setOnMenuItemClickListener(this::onMenuItemClick);

        setIcon(mMenu.getItem(3),false, R.drawable.ic_volume_off_white_24dp, R.drawable.ic_volume_up_white_24dp);
        setIcon(mMenu.getItem(1),false, R.drawable.ic_live_help_white_24dp, R.drawable.ic_unhelp);

        return super.onCreateOptionsMenu(menu);
    }

}
