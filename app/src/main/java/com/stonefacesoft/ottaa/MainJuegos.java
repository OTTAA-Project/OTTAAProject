package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.stonefacesoft.ottaa.Games.GameCard;
import com.stonefacesoft.ottaa.Games.GameSelector;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_galeria_juegos;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GameControl;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFuntionGames;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

import java.util.ArrayList;

//import com.stonefacesoft.ottaa.Games.ArmarFrases;

/**
 * @author Hector Costa
 * <h3>Objective</h3>
 * Select the game to play
 * */
public class MainJuegos extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Make_Click_At_Time , View.OnTouchListener {

    private GameCard card1, card2, card3, card4;
    private Json json;
    private ImageButton down_button,up_button;
    private viewpager_galeria_juegos view_game;
    private AnalyticsFirebase analyticsFirebase;
    private FloatingActionButton btnAction;
    private FloatingActionButton backpress_button;
    private ScrollFuntionGames function_scroll;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private InmersiveMode inmersiveMode;
    private SharedPreferences sharedPrefsDefault;
    private BarridoPantalla barridoPantalla;
    private Button btnBarrido;
    private GameControl gameControl;



    private static final String TAG = "MainJuegos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_juegos);
        navigationView=findViewById(R.id.nav_view);
        sharedPrefsDefault= androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.setOnClickListener(this);
        drawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return windowInsets;
            }
        });
        toolbar = findViewById(R.id.toolbar);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        analyticsFirebase=new AnalyticsFirebase(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.str_game));
        setSupportActionBar(toolbar);
        json=Json.getInstance();
        json.setmContext(this);
        inmersiveMode=new InmersiveMode(this);
        initComponents();
        int value=sharedPrefsDefault.getInt("showMenuGames",4);
        if(value>0) {
            drawerLayout.open();
            value--;
            sharedPrefsDefault.edit().putInt("showMenuGames", value).apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.close();
                }
            }, 5000);
        }
        view_game.setUpPositionItem(3);
    }

    @Override
    public void onClick(View view) {

        //TODO hace el click dentro de la clase GameCard

        switch (view.getId()){
            case R.id.up_button:
                view_game.scrollPosition(false);
                analyticsFirebase.customEvents("Touch","Juegos","Previous Button");
                break;
            case R.id.down_button:
                view_game.scrollPosition(true);
                analyticsFirebase.customEvents("Touch","Juegos","Next Button");
                break;
            case R.id.btnTalk:
                analyticsFirebase.customEvents("Touch","Juegos","Select Game");
                view_game.actionClick();
                break;
            case R.id.btnBarrido:
                onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
                break;
            case R.id.back_button:
                    onBackPressed();
                break;


        }
    }

    private void initComponents(){
        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card1.prepareCardView( R.string.whichpictogram, R.string.which_description_name, R.drawable.whats_picto, createOnClickListener(this, GameSelector.class, "notigames"));
        card2.prepareCardView( R.string.join_pictograms, R.string.join_pictograms_description, R.drawable.match_picto, createOnClickListener(this, GameSelector.class, "seleccionar_palabras"));
        card3.prepareCardView( R.string.memory_game, R.string.memory_game_string, R.drawable.match_picto, createOnClickListener(this, GameSelector.class, "descripciones"));
        card1.setmTxtScore(json.devolverCantidadGruposUsados(0)+"/"+json.getmJSONArrayTodosLosGrupos().length());
        card2.setmTxtScore(json.devolverCantidadGruposUsados(1)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
        card3.setmTxtScore(json.devolverCantidadGruposUsados(1)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
        view_game=new viewpager_galeria_juegos(this);
        up_button=findViewById(R.id.up_button);
        down_button=findViewById(R.id.down_button);
        btnAction=findViewById(R.id.btnTalk);
        up_button.setOnClickListener(this);
        down_button.setOnClickListener(this);
        btnAction.setOnClickListener(this);
        backpress_button=findViewById(R.id.back_button);
        backpress_button.setOnClickListener(this);
        btnAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        btnBarrido=findViewById(R.id.btnBarrido);
        btnBarrido.setOnClickListener(this);
        btnBarrido.setOnTouchListener(this);
        iniciarBarrido();
        gameControl=new GameControl(this);
        function_scroll=new ScrollFuntionGames(this);

    }

    private void setTextoJuego(GameCard game1,int id){
        Log.d(TAG, "setTextoJuego: " + json.devolverCantidadGruposUsados(1));
        game1.setmTxtScore(json.devolverCantidadGruposUsados(id) + "/" + json.getmJSONArrayTodosLosGrupos().length());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        view_game.updateAdapter();
        card1.setmTxtScore(json.devolverCantidadGruposUsados(0)+"/"+json.getmJSONArrayTodosLosGrupos().length());
        card2.setmTxtScore(json.devolverCantidadGruposUsados(1)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
        card3.setmTxtScore(json.devolverCantidadGruposUsados(2)+"/"+json.getmJSONArrayTodosLosGrupos().length());//todo in recycler fill with the position
    }


    private View.OnClickListener createOnClickListener(Context context,Class clase,String value){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,clase);
                intent.putExtra("name_game",value);

                MainJuegos.this.startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                    onBackPressed();
                break;

        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //defino la configuracion al principio
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        boolean preferencesm = preferences.getBoolean("firstrun", true);

        Log.d(TAG, "attachBaseContext: " + newBase.getString(R.string.str_idioma));
        //configuro el idioma por defecto
        new ConfigurarIdioma(newBase, preferences.getString(newBase.getString(R.string.str_idioma), "en"));
        //adjunto el contexto base de la aplicacion
        super.attachBaseContext(myContextWrapper.wrap(newBase, preferences.getString(newBase.getString(R.string.str_idioma), "en")));


    }

    private void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(findViewById(R.id.up_button));
        listadoObjetosBarrido.add(findViewById(R.id.btnTalk));
        listadoObjetosBarrido.add(findViewById(R.id.down_button));
        listadoObjetosBarrido.add(findViewById(R.id.back_button));
        //  listadoObjetosBarrido.add(editButton);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido, this);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.VISIBLE);
                }
            });
        }else{
            btnBarrido.setVisibility(View.GONE);
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

    public ScrollFuntionGames getFunction_scroll() {
        return function_scroll;
    }
}
