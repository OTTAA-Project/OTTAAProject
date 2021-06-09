package com.stonefacesoft.ottaa.Games;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.Dialogos.DialogGameProgressInform;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Games.AnimGameScore;
import com.stonefacesoft.ottaa.utils.Games.Juego;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsTTS;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//Merge realizado
public class DescribirPictograma extends AppCompatActivity implements View
        .OnClickListener, Toolbar.OnMenuItemClickListener {

    private SharedPreferences sharedPrefsDefault;
    //
    private CustomToast dialogo;
    //Declaracion de variables del TTS
    private TextToSpeech mTTS;
    private UtilsTTS mUtilsTTS;


    //Declaracion botones de preguntas
    private Custom_Picto Seleccion1;

    //creo imageview cuando gana
    private ImageView mAnimationWin;

    private Context context;
    //Declaracion botones de respuesatas
    private Custom_Picto Opcion1;
    private Custom_Picto Opcion2;
    private Custom_Picto Opcion3;
    private Custom_Picto Opcion4;
    private int[] valores=new int[]{-1,-1,-1,-1};
    private boolean primerUso;
    private int ganadorAnterior=-1;

    //RatingStar
    private RatingBar Puntaje;
    private AnalyticsFirebase analitycsFirebase;


    //Pistas


    //Declaramos el media player
    private MediaPlayerAudio mediaPlayer,music;
    //Handler para animar la respuesat correcta luego de un tiempo si no se presiona
    private final Handler handlerHablar = new Handler();
    private final Handler handlerGano = new Handler();
    private final Handler decirPicto=new Handler();

    //View para animar respuesta correcta en niveles
    private Custom_Picto viewGanador;
    private ImageButton imageButton;

    //Pictos en juego
    private int[] pictos;
    private int mPositionPadre;

    private boolean isChecked;

    //Jsons
    private Json json;
    private  JSONArray mDescripcion;


    // Datos que le paso por el intent!!!
    private int PictoID;                // picto actual
    private static final String TAG = "DescribirPictograma";
    //Handler para animar el Hablar cuando pasa cierto tiempo
    private int cantVecInc;
    private boolean mute;

    private Toolbar toolbar;

    private AnimGameScore animGameScore;

    private Menu mMenu;
    private MenuItem scoreItem;

    private Juego game;
    private ImageView imageView;
    private AnalyticsFirebase analyticsFirebase;


    private final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            viewGanador.startAnimation(AnimationUtils.loadAnimation(DescribirPictograma.this, R.anim.shake));
            if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
                handlerHablar.postDelayed(this, 4000);
        }
    };



    private final Runnable talkGanador=new Runnable() {
        @Override
        public void run() {
            imageButton.callOnClick();
        }
    };
    private boolean mTutorialFlag = true;


    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        music.stop();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Ocultamos la barra de notificaciones
        Intent intent = getIntent();
        analitycsFirebase=new AnalyticsFirebase(this);
        PictoID = intent.getIntExtra("PictoID", 0);
        mPositionPadre = intent.getIntExtra("PositionPadre", 0);
//        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
       analitycsFirebase=new AnalyticsFirebase(this);
        dialogo=new CustomToast(this);
        mediaPlayer=new MediaPlayerAudio(this);
        music=new MediaPlayerAudio(this);
        mediaPlayer.setVolumenAudio(0.15f);
        music.setVolumenAudio(0.05f);
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_games);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.whichpictogram));
        setSupportActionBar(toolbar);
        context = this;
        primerUso = true;
        //Implemento el manejador de preferencias
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mute=sharedPrefsDefault.getBoolean("muteSound",false);
        isChecked=sharedPrefsDefault.getBoolean(getString(R.string.str_pistas),true);
        music.setMuted(mute);
        if(mUtilsTTS==null) {
            mUtilsTTS=new UtilsTTS(this,mTTS,dialogo,sharedPrefsDefault);
        }
        music.playMusic();
        analitycsFirebase.levelNameGame(TAG);



        //Declaramos el boton para que reproduzca el tts con lo que tiene que decir
        imageButton = findViewById(R.id.ttsJuego);
        imageButton.setOnClickListener(this);

        mAnimationWin = findViewById(R.id.ganarImagen);
        mAnimationWin.setImageAlpha(230);
        mAnimationWin.setVisibility(View.INVISIBLE);
        TextView mSeleccion = findViewById(R.id.SeleccioneEste);

        //Implementacion de los botones de la pregunta
        Seleccion1 = findViewById(R.id.Seleccion1);
        Seleccion1.goneCustomTexto();
        Seleccion1.setOnClickListener(this);

        //Pistas



        //Implementacion de los botones con las respuestas
        Opcion1 = findViewById(R.id.Option1);
        Opcion1.setOnClickListener(this);
        Opcion2 = findViewById(R.id.Option2);
        Opcion2.setOnClickListener(this);
        Opcion3 = findViewById(R.id.Option3);
        Opcion3.setOnClickListener(this);
        Opcion4 = findViewById(R.id.Option4);
        Opcion4.setOnClickListener(this);
        //Json para todx el juego
        Json.getInstance().setmContext(this);
        json = Json.getInstance();

        try {
            mDescripcion = json.readJSONArrayFromFile(Constants.ARCHIVO_JUEGO_DESCRIPCION);
            game=new Juego(this,2,0);
            game.startUseTime();
            Bundle bundle = new Bundle();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {

        }



        mTutorialFlag = sharedPrefsDefault.getBoolean("PrimerUsoJuegos", true);
        PrimerNivel();

        animGameScore = new AnimGameScore(this, mAnimationWin);


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //la variable temporal galeria grupos se la usa para modificar puntaje

        game.endUseTime();
        game.guardarObjetoJson();


        //TODO Gonza corregir esto que tira error
        handlerHablar.removeCallbacks(animarGano);
        decirPicto.removeCallbacks(talkGanador);
        mediaPlayer.stop();
        music.stop();
        Intent databack = new Intent();
        databack.putExtra("Boton", mPositionPadre);
        setResult(3, databack);
        this.finish();
    }

    private void Alert() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setCancelable(false);
        dialogo1.setMessage(getResources().getString(R.string.pref_error_112));
        //dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }



    private void PrimerNivel() {
        liberaMemoria();

        if (sharedPrefsDefault.getBoolean(getString(R.string.str_pistas), false))
            handlerHablar.postDelayed(animarHablar, 10000);
        cargarPictos();

    }

    private void liberaMemoria() {
        this.onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.onTrimMemory(TRIM_MEMORY_MODERATE);
        Log.d("NotiGame", "Trimming memory");
    }

    private void elegirGanador(int... pictos) {
        int ganador=devolverOpcionGanadora((int)(Math.random()*4));
        switch (ganador) {
            case 0:
                viewGanador = Opcion1;
                break;
            case 1:
                viewGanador = Opcion2;
                break;
            case 2:
                viewGanador = Opcion3;
                break;
            case 3:
                viewGanador = Opcion4;
                break;
        }
        desbloquearPictos();
        handlerHablar.postDelayed(talkGanador,600);
        //return pictos[ganador];
    }

    private boolean esGanador(Custom_Picto valor, Custom_Picto ganadorLvl) {
        if(valor.getCustom_description().equals(ganadorLvl.getCustom_description())){
            bloquearPictos();
            return true;
        }
        return false;
    }

    private boolean resolveIntent() {
        PackageManager pm = getPackageManager();
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        ResolveInfo resolveInfo = pm.resolveActivity(installIntent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo != null;
    }

    //Carga el color correspondienta a cada picto
    private Integer cargarColor(int tipo) {
        switch (tipo) {
            case 1:
                return getResources().getColor(R.color.Yellow);
            case 2:
                return getResources().getColor(R.color.Orange);
            case 3:
                return getResources().getColor(R.color.YellowGreen);
            case 4:
                return getResources().getColor(R.color.DodgerBlue);
            case 5:
                return getResources().getColor(R.color.Magenta);
            case 6:
                return getResources().getColor(R.color.Black);
            default:
                return getResources().getColor(R.color.Black);
        }
    }

    //private void cargarPictos(int picto1,int picto2,int picto3,int picto4){
    private void cargarPictos() {
        //animarReset();
        if (mDescripcion != null) {
            Seleccion1.setCustom_Img(getResources().getDrawable(R.drawable.ic_help_outline_black_24dp));
            cargarDatosValors(0);
            cargarDatosValors(1);
            cargarDatosValors(2);
            cargarDatosValors(3);

            try {
                //Cargas los pictos desde el json
                //Seteamos el texto de las opciones
                cargarDatosOpcion(valores[0], Opcion1, 0);
                cargarDatosOpcion(valores[1], Opcion2, 1);
                cargarDatosOpcion(valores[2], Opcion3, 2);
                cargarDatosOpcion(valores[3], Opcion4, 3);


            } catch (Exception e) {
            }
        }
    }

    //Handler para animar el Hablar cuando pasa cierto tiempo
    private final Runnable animarGano = new Runnable() {
        @Override
        public void run() {
            PrimerNivel();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Option1:
                actionGanador(Opcion1);
                break;
            case R.id.Option2:
                actionGanador(Opcion2);
                break;
            case R.id.Option3:
                actionGanador(Opcion3);
                break;
            case R.id.Option4:
                actionGanador(Opcion4);
                break;
            case R.id.ttsJuego:
                if(viewGanador!=null){


                    mUtilsTTS.hablar(viewGanador.getCustom_description());
                    if(primerUso) {
                        viewGanador.startAnimation(AnimationUtils.loadAnimation(DescribirPictograma.this, R.anim.shake));
                        primerUso=false;
                    }

                }
                break;


        }





    }

    @Override
    protected void onPause() {

        Json.getInstance().setmContext(this);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(music!=null){
            music.stop();
            music.playMusic();
        }
        Json.getInstance().setmContext(this);
    }





    private void CalcularPuntaje(boolean Acerto) {
        if (Acerto) {
            game.incrementCorrect();
            Drawable drawable=game.devolverCarita();
            drawable.setTint(getResources().getColor(R.color.colorWhite));
            mMenu.getItem(0).setIcon(drawable).setVisible(true);

        } else{
            game.incrementWrong();
            Drawable drawable=game.devolverCarita();
            drawable.setTint(getResources().getColor(R.color.colorWhite));
            mMenu.getItem(0).setIcon(drawable).setVisible(true);

        }
        getPuntaje();
    }

    private double getPuntaje() {
        return game.getScoreClass().calcularValor();
    }


    private void actionGanador(Custom_Picto picto){
        if (esGanador(picto, viewGanador)) {
            if(cantVecInc==0)
                mediaPlayer.playYesSound();
            else
                mediaPlayer.playYupi2Sound();
            Seleccion1.setCustom_Img(picto.getCustom_Imagen());
            picto.setCustom_Img(getResources().getDrawable(R.drawable.ic_bien));
            picto.setCustom_Color(getResources().getColor(R.color.LightGreen));
            handlerHablar.removeCallbacks(animarHablar);
            //animarGanador(picto);
            decirPicto.postDelayed(animarGano, 3000);
            //Seteo el puntaje
            valores = new int[]{-1, -1, -1, -1};
            CalcularPuntaje(true);
            cantVecInc = 0;
            animGameScore.animateCorrect(picto, game.getSmiley(Juego.SATISFIED));
        } else {
            mediaPlayer.playOhOhSound();
            picto.setCustom_Img(getResources().getDrawable(R.drawable.ic_mal));
            picto.setCustom_Color(getResources().getColor(R.color.Red));
            cantVecInc++;
            //Seteo el puntaje
            CalcularPuntaje(false);
            animGameScore.animateCorrect(picto, game.getSmiley(Juego.DISSATISFIED));

        }
    }

    private int devolverOpcionGanadora ( int value){
        if (ganadorAnterior == -1) {
            ganadorAnterior = value;
            return value;
        }
        Log.d(TAG, "devolverOpcionGanadora: " + value);
        if (ganadorAnterior == value) {
            devolverValor((int) (Math.random() * 4));
        }
        ganadorAnterior = value;
        return value;
    }

    private int devolverValor ( int value){
        if (!tieneValor(value))
            return value;
        else
            devolverValor(Math.round((float) Math.random() * mDescripcion.length()));
        return -1;
    }

    private boolean tieneValor ( int valor){
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == valor)
                return true;
        }
        return false;
    }
    //Metodos tts we can implements them because the tts start later
    //Anima el picto correctamente seleccionado


    private void cargarDatosValors ( int position){
        int valor = (int) (Math.random() * mDescripcion.length());
        boolean tieneValor = false;
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == valor)
                tieneValor = true;
        }
        if (!tieneValor)
            valores[position] = valor;

    }


    private void cargarDatosOpcion(int position, Custom_Picto option, int pos) {
        try {
            if (mDescripcion.getJSONObject(position) != null) {
                JSONObject jsonObject=json.getPictoFromId2(mDescripcion.getJSONObject(position).getInt("id"));
                //json.getDescription(mDescripcion.getJSONObject(position)).getString("es")
                     option.setCustom_Texto(JSONutils.getNombre(jsonObject,sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                    option.setCustom_Img(json.getIcono(jsonObject));
                    option.setCustom_Color(cargarColor(JSONutils.getTipo(jsonObject)));
                    option.setCustom_description(json.getDescription(mDescripcion.getJSONObject(position)).getString("es"));
                    valores[pos]=position;
                /*}else{
                    position =devolverValor(Math.round((float) Math.random() * mJsonArrayTodosLosPictos.length()));
                    cargarDatosValors(pos);
                    cargarDatosOpcion(position, option, pos);
                }*/
            } else {
                position =devolverValor(Math.round((float) Math.random() * mDescripcion.length()));
                valores[pos]=position;
                cargarDatosOpcion(position, option, pos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            position =devolverValor(Math.round((float) Math.random() * mDescripcion.length()));
            valores[pos]=position;
            cargarDatosOpcion(position, option, pos);

        } finally {
            if (pos == 3) {
                elegirGanador();
                Seleccion1.setCustom_Img(getResources().getDrawable(R.drawable.agregar_picto_transp));
                Log.d(TAG, "cargarDatosOpcion: " + viewGanador.getCustom_Texto());
            }
        }
    }

    private void desbloquearPictos(){
        Opcion1.setEnabled(true);
        Opcion2.setEnabled(true);
        Opcion3.setEnabled(true);
        Opcion4.setEnabled(true);

    }
    private void bloquearPictos(){
        Opcion1.setEnabled(false);
        Opcion2.setEnabled(false);
        Opcion3.setEnabled(false);
        Opcion4.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        mMenu=menu;
        inflater.inflate(R.menu.action_bar_game, mMenu);
        // mMenu = menu;
        scoreItem=menu.findItem(R.id.score);
        // animGameScore.setMenuView(mMenu);
        Drawable drawable=game.devolverCarita();
        if(game.getScore()==0)
            drawable=getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_white_24dp);
        drawable.setTint(getResources().getColor(R.color.colorWhite));
        mMenu.getItem(0).setIcon(drawable);
        mMenu.getItem(0).setVisible(true);
        menu.getItem(2).setVisible(false);


        if(game!=null)
            scoreItem.setIcon(game.devolverCarita());
        mMenu.getItem(0).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(1).setOnMenuItemClickListener(this::onMenuItemClick);
        mMenu.getItem(3).setOnMenuItemClickListener(this::onMenuItemClick);

        setIcon(mMenu.getItem(3),mute,R.drawable.ic_volume_off_white_24dp,R.drawable.ic_volume_up_white_24dp);
        setIcon(mMenu.getItem(1),isChecked,R.drawable.ic_live_help_white_24dp,R.drawable.ic_unhelp);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_parar:
                analyticsFirebase.customEvents("Touch","Describir Pictogramas","Mute");
                mute=!mute;
                sharedPrefsDefault.edit().putBoolean("muteSound",mute).apply();
                music.setMuted(mute);
                setIcon(item,mute,R.drawable.ic_volume_off_white_24dp,R.drawable.ic_volume_up_white_24dp);

                return true;
            case R.id.score:
                analyticsFirebase.customEvents("Touch","Describir Pictogramas","Score Dialog");
                DialogGameProgressInform inform=new DialogGameProgressInform(this,R.layout.game_progress_score,game);
                inform.cargarDatosJuego();
                inform.showDialog();
//                Bundle bundle = new Bundle();
//                bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Dialogo_Score");
//                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);
                analitycsFirebase.setUnlockAchievement("Dialogo_Score");
                return true;
            case R.id.check:
                analyticsFirebase.customEvents("Touch","Describir Pictogramas","Help Action");
                isChecked=!isChecked;
                sharedPrefsDefault.edit().putBoolean(getString(R.string.str_pistas), isChecked).apply();
                setIcon(item,isChecked,R.drawable.ic_live_help_white_24dp,R.drawable.ic_unhelp);
                if (isChecked) {
                    handlerHablar.postDelayed(animarHablar, 4000);
                    dialogo.mostrarFrase(getString(R.string.help_function));
                } else {
                    handlerHablar.removeCallbacks(animarHablar);
                    dialogo.mostrarFrase(getString(R.string.help_function_disabled));
                }
                return true;
        }
        return false;
    }

    private void setIcon(MenuItem item,boolean status,int dEnabled,int dDisabled){

        if (status) {
            item.setIcon(getResources().getDrawable(dEnabled));
        } else {
            item.setIcon(getResources().getDrawable(dDisabled));
        }
    }
}