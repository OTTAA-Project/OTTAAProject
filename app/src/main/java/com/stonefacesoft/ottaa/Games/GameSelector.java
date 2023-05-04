package com.stonefacesoft.ottaa.Games;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.RecyclerViews.Game_Recyler_View;
import com.stonefacesoft.ottaa.Viewpagers.ViewPager_Game_Grupo;
import com.stonefacesoft.ottaa.Views.MatchPictograms;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * @author Gonzalo Juarez
 *
 * <p>Choose the level game</p>
 *
 *  @see ViewPager_Game_Grupo
 * @see Game_Recyler_View
 *
 *
 * */
public class GameSelector extends AppCompatActivity implements View.OnClickListener , Make_Click_At_Time,View.OnTouchListener {

    private static final String TAG = "GameSelector";
    private Json json;
    private int boton;
    private JSONArray mJsonArrayTodosLosGrupos;
    private boolean isCargando;
    private int mState;
    private FirebaseAuth mAuth;
    private CargarGruposJson cargarGruposJson;
    private ProgressBar mProgressBar;
    private TextView mTextViewCargandoGrupos;
    private String juego;
    private ImageButton up_button,down_button,backpress_button;
    private boolean showViewPager;
    private ViewPager_Game_Grupo grupo_viewPager;
    private Game_Recyler_View game_recycler_view;
    private SharedPreferences sharedPrefsDefault;
    private BarridoPantalla barridoPantalla;
    private Button btnBarrido;
    private ScrollFuntionGames function_scroll;
    private GameControl gameControl;
    private FloatingActionButton btnSelector;


    @AddTrace(name = "GameSelector", enabled = true /* optional */)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_grupos2);
        mAuth=FirebaseAuth.getInstance();
        juego=intento.getStringExtra("name_game");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.select_category));
        mProgressBar = findViewById(R.id.progressBar);
        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(this);
        showViewPager=true;
        mTextViewCargandoGrupos = findViewById(R.id.textoCargandoGrupos);
        btnSelector=findViewById(R.id.btnTalk);
        btnSelector.setOnClickListener(this);
        up_button=findViewById(R.id.up_button);
        down_button=findViewById(R.id.down_button);
        backpress_button=findViewById(R.id.back_button);
        btnBarrido=findViewById(R.id.btnBarrido);
        btnBarrido.setOnTouchListener(this);
        btnBarrido.setOnClickListener(this);
        up_button.setOnClickListener(this);
        down_button.setOnClickListener(this);
        backpress_button.setOnClickListener(this);
        textToSpeech mytts = textToSpeech.getInstance(this) ;
        grupo_viewPager=new ViewPager_Game_Grupo(this,mytts,devolverPosicion(juego));
        game_recycler_view=new Game_Recyler_View(this,mAuth,devolverPosicion(juego));
        cargarGruposJson = new GameSelector.CargarGruposJson(mProgressBar, mTextViewCargandoGrupos, GameSelector.this);
        cargarGruposJson.execute();
        grupo_viewPager.showViewPager(showViewPager);
        game_recycler_view.showRecyclerView(showViewPager);
        iniciarBarrido();
        function_scroll=new ScrollFuntionGames(this);
        gameControl=new GameControl(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent databack = new Intent();
        databack.putExtra("ID", 0);
        databack.putExtra("Boton", boton);
        setResult(3, databack);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        grupo_viewPager.updateData();
        grupo_viewPager.refreshView();
    }


    // adapter

    public int devolverCantidadColumnas() {
        Display display = this.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = this.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / 250);
//        return mActivity.getResources().getConfiguration().screenWidthDp / convertToDp(150);
    }

    private void initComponents(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.up_button:
                    if(!showViewPager)
                        game_recycler_view.scrollTo(false);
                    else
                        grupo_viewPager.scrollPosition(false);
                break;
            case R.id.down_button:
                    if(!showViewPager)
                        game_recycler_view.scrollTo(true);
                    else
                        grupo_viewPager.scrollPosition(true);
                break;
            case R.id.back_button:
                    onBackPressed();
                break;
            case R.id.btnTalk:
                grupo_viewPager.OnClickItem();
                break;
            case R.id.btnBarrido:
                    onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
                break;
        }
    }

    @Override
    public void OnClickBarrido() {
        if(function_scroll.isClickEnabled()&&barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId()==R.id.btnTodosLosPictos)
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        else if(!function_scroll.isClickEnabled()){
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }

    public class CargarGruposJson extends AsyncTask<Void, Void, Void> {

        private final ProgressBar mProgressBarCargandoGrupos;
        private final TextView mTextViewCargandoGrupos;
        private final Context mContext;

        public CargarGruposJson(ProgressBar mProgressBarCargandoGrupos, TextView mTextViewCargandoGrupos, Context context) {
            this.mProgressBarCargandoGrupos = mProgressBarCargandoGrupos;
            this.mTextViewCargandoGrupos = mTextViewCargandoGrupos;
            this.mContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarCargandoGrupos.setVisibility(View.VISIBLE);
            mTextViewCargandoGrupos.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Json.getInstance().setmContext(mContext);
            json = Json.getInstance();

            try {
                mJsonArrayTodosLosGrupos = json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
            } catch (JSONException | FiveMbException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mState = 0;
            isCargando = false;
            invalidateOptionsMenu();
            mProgressBarCargandoGrupos.setVisibility(View.GONE);
            mTextViewCargandoGrupos.setVisibility(View.GONE);
            game_recycler_view.cargarGrupo();


            /*Se encarga de abrir los grupos y manejar el drag-n-drop*/

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mProgressBarCargandoGrupos.setVisibility(View.GONE);
            mTextViewCargandoGrupos.setVisibility(View.GONE);
            isCargando = false;

        }
    }

    private RecyclerItemClickListener RecyclerItemClickListener() {
        return new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                if (v != null) {

                    if (position != -1) {
                        Intent intent=null;
                        switch (juego){
                            case "notigames":
                                intent = new Intent(GameSelector.this, WhichIsThePicto.class);
                                break;
                            case "seleccionar_palabras":
                                intent = new Intent(GameSelector.this, MatchPictograms.class);
                                break;
                            case "descripciones":
                                intent=new Intent(GameSelector.this,MemoryGame.class);
                                break;
                            case "history":
                                 intent = new Intent(GameSelector.this,TellAStory.class);
                                break;
                        }

                        try {
                            intent.putExtra("PictoID", mJsonArrayTodosLosGrupos.getJSONObject(position).getInt("id"));
                            intent.putExtra("PositionPadre", position);
                            startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
                        } catch (Exception e) {
                            Log.e(TAG, "onItemClick: "+e.getMessage() );
                        }


                    }
                }
            }


            @Override
            public void onDoubleTap(View view, final int position) {

            }

            @Override
            public void onLongClickListener(View view, int position) {

            }


        });
    }

    private int devolverPosicion(String title){
        switch (title){
            case "notigames":
                return 0;
            case "seleccionar_palabras":
                return 1;
            case "descripciones":
                return 2;
            case "history":
                return 3;
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        grupo_viewPager.updateData();
    }

    private void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(up_button);
        listadoObjetosBarrido.add(backpress_button);
        listadoObjetosBarrido.add(btnSelector);
        listadoObjetosBarrido.add(down_button);


        //  listadoObjetosBarrido.add(editButton);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    showViewPager=true;
                    btnBarrido.setVisibility(View.VISIBLE);
                }
            });
        }else{
            btnBarrido.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:

                    if(barridoPantalla.isScrollMode()||barridoPantalla.isScrollModeClicker()){
                        if(event.getAxisValue(MotionEvent.AXIS_VSCROLL)<0.0f){
                            if(barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.avanzarBarrido();
                        }
                        else{
                            if(barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.volverAtrasBarrido();

                        }
                    }
                    return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gameControl.makeClick(event);
    }
    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (barridoPantalla.isBarridoActivado()) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                event.startTracking();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                event.startTracking();
                return true;
            }
            if(keyCode == KeyEvent.KEYCODE_BACK){
                if(event.getSource() == InputDevice.SOURCE_MOUSE)
                    barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                else
                    onBackPressed();
                return true;
            }
        }
        return false;
    }
}




