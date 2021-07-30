package com.stonefacesoft.ottaa.Games;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stonefacesoft.ottaa.Custom_Picto;
import com.stonefacesoft.ottaa.Dialogos.Dialog_options_level_game;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Audio.MediaPlayerAudio;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Games.CalculaPuntos;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.Ttsutils.UtilsTTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArmarFrases extends AppCompatActivity implements View.OnClickListener {
    private final String TAG="ArmarFrases";
    private ImageButton seleccion1;
    private ImageButton seleccion2;
    private ImageButton seleccion3;
    private ImageButton seleccion4;
    private ImageButton seleccion5;
    private ImageButton seleccion6;
    private ImageButton seleccion7;
    private ImageButton seleccion8;
    private ImageButton seleccion9;
    private ImageButton seleccion10;
    private ImageButton button;

    private ImageButton deleteButton;
    private ImageButton levelOption;

    private ArrayList<View> seleccionN;
    private Custom_Picto Opcion1;
    private Custom_Picto Opcion2;
    private Custom_Picto Opcion3;
    private Custom_Picto Opcion4;

    private int level;

    private Json json;
    private JSONObject frase;
    private JSONArray frases;
    private ArrayList numeros;
    private JSONArray pictogramas;

    private int posicion;



    private final String[] fraseCompleta=new String[]{"","","",""};

    private SharedPreferences mDefaultSharedPreferences;
    private ArrayList<JSONObject> listadoPictos;
    private UtilsTTS mUtilsTTS;


    private CustomToast toast;
    private FloatingActionButton hablar;

    private Dialog_options_level_game dialog_options;
    private Custom_Picto Agregar;

    private int[] idPictos=new int[]{-1,-1,-1,-1};

    private MediaPlayerAudio player;

    private CalculaPuntos puntaje;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_v4);
        initComponents();
        puntaje=new CalculaPuntos();
        json=Json.getInstance();
        json.setmContext(this);
        inicializar_seleccion();
        seleccionarFrase();
    }

    private void initComponents(){
        player=new MediaPlayerAudio(this);
        Agregar = new Custom_Picto(this);
        Agregar.setCustom_Color(R.color.Black);
        Agregar.setCustom_Texto("");
        Agregar.setCustom_Img(getDrawable(R.drawable.agregar_picto_transp));
        Agregar.setIdPictogram(0);

        numeros=new ArrayList();
        mDefaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        toast=CustomToast.getInstance(this);
        mUtilsTTS=new UtilsTTS(this,toast,mDefaultSharedPreferences);
        listadoPictos=new ArrayList<>();
        seleccion1=findViewById(R.id.Seleccion1);
        seleccion2=findViewById(R.id.Seleccion2);
        seleccion3=findViewById(R.id.Seleccion3);
        seleccion4=findViewById(R.id.Seleccion4);
        seleccion5=findViewById(R.id.Seleccion5);
        seleccion6=findViewById(R.id.Seleccion6);
        seleccion7=findViewById(R.id.Seleccion7);
        seleccion8=findViewById(R.id.Seleccion8);
        seleccion9=findViewById(R.id.Seleccion9);
        seleccion10=findViewById(R.id.Seleccion10);

        levelOption=findViewById(R.id.action_reiniciar);
        levelOption.setOnClickListener(this);

        hablar=findViewById(R.id.btnTalk);
        AjustarAncho(R.id.Seleccion1);
        AjustarAncho(R.id.Seleccion2);
        AjustarAncho(R.id.Seleccion3);
        AjustarAncho(R.id.Seleccion4);
        AjustarAncho(R.id.Seleccion5);
        AjustarAncho(R.id.Seleccion6);
        AjustarAncho(R.id.Seleccion7);
        AjustarAncho(R.id.Seleccion8);

        Opcion1 = findViewById(R.id.Option1);
        Opcion2 = findViewById(R.id.Option2);
        Opcion3 = findViewById(R.id.Option3);
        Opcion4 = findViewById(R.id.Option4);

        hablar.setOnClickListener(this);
        Opcion1.setOnClickListener(this);
        Opcion2.setOnClickListener(this);
        Opcion3.setOnClickListener(this);
        Opcion4.setOnClickListener(this);

        deleteButton=findViewById(R.id.btn_borrar);
        deleteButton.setOnClickListener(this);

        dialog_options=new Dialog_options_level_game(this);


    }

    private void seleccionarFrase(){
        frases=json.getmJSONArrayTodasLasFrases();
          int value= (int)Math.round(Math.random()*frases.length());
        try {
            frase=frases.getJSONObject(value);
            Log.e(TAG, "seleccionarFrase: "+frase.toString() );
            pictogramas=json.devolverComplejidad(frase).getJSONArray("pictos componentes");
            if(pictogramas.length()>1&&pictogramas.length()<=4){
                for (int i = 0; i <pictogramas.length() ; i++) {
                    ordenarNumerosAleatorios(i,pictogramas.length());
                }
                for (int i = 0; i <numeros.size() ; i++) {
                    Log.e(TAG, "seleccionarFrase: "+numeros.get(i));
                    JSONObject pictograma=json.getPictoFromId2(json.getId(pictogramas.getJSONObject((int)numeros.get(i))));
                    listadoPictos.add(pictograma);
                    cargarOpcion(pictograma,i);
                }
            }else {
                seleccionarFrase();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            numeros.clear();
            listadoPictos.clear();
            seleccionarFrase();
        }
    }

    private void ordenarNumerosAleatorios(int pos,int size){
        int numero=(int)Math.round(Math.random()*size);
        Log.e(TAG, " numero"+numero );
        if(numeros.lastIndexOf(numero)==-1){
                numeros.add(numero);
            }else{
               ordenarNumerosAleatorios(pos,size);
            }
    }

    private void cargarOpcion(JSONObject picto,int i){
        switch (i){
            case 0:
                Opcion1.setCustom_Texto(JSONutils.getNombre(picto,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en")));
                Opcion1.setCustom_Img(json.getIcono(picto));

                break;
            case 1:
                Opcion2.setCustom_Texto(JSONutils.getNombre(picto,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en")));
                Opcion2.setCustom_Img(json.getIcono(picto));
                break;
            case 2:
                Opcion3.setCustom_Texto(JSONutils.getNombre(picto,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en")));
                Opcion3.setCustom_Img(json.getIcono(picto));
                break;
            case 3:
                Opcion4.setCustom_Texto(JSONutils.getNombre(picto,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en")));
                Opcion4.setCustom_Img(json.getIcono(picto));
                break;
        }
    }


    @Override
    public void onClick(View view) {
        level=dialog_options.getLevel();
        switch (view.getId()){
            case R.id.Option1:
                if(Opcion1!=null) {
                    consultarPictograma(0);
                }
                break;
            case R.id.Option2:
                if(Opcion2!=null){
                    consultarPictograma(1);
                }
                break;
            case R.id.Option3:
                if(Opcion3!=null){
                    consultarPictograma(2);
                }
                break;
            case R.id.Option4:
                if(Opcion4!=null){
                    consultarPictograma(3);
                }
                break;
            case R.id.btnTalk:
                try {
                    Log.e(TAG, "onClick: "+devolverFraseCompleta() );
                    Log.e(TAG, "onClick: "+frase.getString("frase") );
                    if(verificarFraseCorrecta()){
                    mUtilsTTS.hablar(frase.getString("frase"));
                    puntaje.sumarCantidadVecesCorrectas();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.playYesSound();
                                resetPhrase();
                            }
                        },1000);
                        puntaje.calcularValor();
                    }
                    else{
                        puntaje.sumarCantidVecesIncorretas();
                        player.playNoSound();
                        puntaje.calcularValor();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_borrar:
                borrar();
                break;
            case R.id.action_reiniciar:
                dialog_options.mostrarDialogo();
        }
    }

    private void consultarPictograma(int pos){
        try {
            int position=consultarPosicionPictograma(json.getId(listadoPictos.get(pos)));
            switch (level){
                case 0:
                    cargarPictogramaSeleccion(position);
                    break;
                case 1:
                    cargarPictogramaSeleccion(posicion,position);
                    posicion++;
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception ex){

        }
    }



    private int consultarPosicionPictograma(int id){
        for (int i = 0; i <pictogramas.length() ; i++) {
            try {
                if(json.getId(pictogramas.getJSONObject(i))==id){
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void cargarPictogramaSeleccion(int pos) {
        try {
            JSONObject object = json.getPictoFromId2(json.getId(pictogramas.getJSONObject(pos)));
            Log.e(TAG, "pos :" + pos);
            setDrawable(pos,json.getIcono(object));
            fraseCompleta[pos] = JSONutils.getNombre(object,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en"));
            idPictos[pos]=json.getId(object);
        } catch (JSONException ex) {

        }
     }

    private void cargarPictogramaSeleccion(int positionFinal,int location) {
        try {
            JSONObject object = json.getPictoFromId2(json.getId(pictogramas.getJSONObject(location)));
            Log.e(TAG, "pos :" + location);
            setDrawable(positionFinal,json.getIcono(object));
            fraseCompleta[positionFinal] = JSONutils.getNombre(object,mDefaultSharedPreferences.getString(getString(R.string.str_idioma), "en"));
            idPictos[positionFinal]=json.getId(object);
        } catch (JSONException ex) {

        }
    }
        private String devolverFraseCompleta(){
            String frase="";
            for (int i = 0; i <fraseCompleta.length ; i++) {
                frase+=" "+fraseCompleta[i];
            }
            return frase;
        }

        private void resetearFrase(){
            for (int i=0;i<fraseCompleta.length;i++){
                fraseCompleta[i]="";
            }

        }

        private void setDrawable(int pos, Drawable drawable){
         switch (pos){
             case 0:
                 seleccion1.setImageDrawable(drawable);
                 break;
             case 1:
                 seleccion2.setImageDrawable(drawable);
                 break;
             case 2:
                 seleccion3.setImageDrawable(drawable);
                 break;
             case 3:
                 seleccion4.setImageDrawable(drawable);
                 break;
         }
        }


    private void AjustarAncho(int Rid) {
        ImageButton view_instance = findViewById(Rid);
        android.view.ViewGroup.LayoutParams params = view_instance.getLayoutParams();

        params.width = view_instance.getLayoutParams().height;
        Log.e("Ancho", "Ancho " + params.width + " Alto " + params.height);
        view_instance.setLayoutParams(params);
        button = new ImageButton(getApplicationContext());
        button.setLayoutParams(params);

    }

    private void resetPhrase(){
        posicion=0;
        resetearPictogramas();
        inicializar_seleccion();
        listadoPictos.clear();
        numeros.clear();
        resetearFrase();
        seleccionarFrase();
    }

    private void inicializar_seleccion() {
        seleccion1.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion2.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion3.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion4.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion5.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion5.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion6.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion7.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion7.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion8.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion8.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion9.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion9.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        seleccion10.setImageDrawable(Agregar.getCustom_Imagen());
        seleccion10.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        idPictos=new int[]{-1,-1,-1,-1};
    }

    private  void resetearPictogramas(){
        Opcion1.setCustom_Img(getResources().getDrawable(R.drawable.ic_agregar_nuevo));
        Opcion2.setCustom_Img(getResources().getDrawable(R.drawable.ic_agregar_nuevo));
        Opcion3.setCustom_Img(getResources().getDrawable(R.drawable.ic_agregar_nuevo));
        Opcion4.setCustom_Img(getResources().getDrawable(R.drawable.ic_agregar_nuevo));
    }

    private void borrar(){
        switch (level){
            case 0:
                int pos=borrarPorPosicion();
                Log.e(TAG, "borrarPorPosicion: "+pos);
                if(pos!=-1)
                idPictos[pos]=-1;
                cambiarDrawable(pos);
                break;
         case 1:
                posicion--;
            if(posicion<0)
                posicion=0;
            cambiarDrawable(posicion);
            fraseCompleta[posicion]="";
        }
    }

    private void cambiarDrawable(int position){
        switch (position){
            case 0:
                seleccion1.setImageDrawable(Agregar.getCustom_Imagen());
                break;
            case 1:
                seleccion2.setImageDrawable(Agregar.getCustom_Imagen());
                break;
            case 2:
                seleccion3.setImageDrawable(Agregar.getCustom_Imagen());
                break;
            case 3:
                seleccion4.setImageDrawable(Agregar.getCustom_Imagen());
                break;
            default:

        }
    }

    private boolean verificarFraseCorrecta(){
        boolean estaCorrecto=false;
        for (int i = 0; i <pictogramas.length(); i++) {
            try {
                estaCorrecto = idPictos[i] == json.getId(pictogramas.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return estaCorrecto;
    }

    private int borrarPorPosicion(){
        for (int i = (idPictos.length-1); i >=0 ; i--) {

            if(idPictos[i]!=-1){
                Log.w(TAG, "borrarPorPosicion:e"+i );
                return i;
            }
        }
        return -1;
    }


    public static class memoryGame {
    }
}
