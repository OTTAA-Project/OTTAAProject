package com.stonefacesoft.ottaa.Views.Phrases;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.PhrasesViewControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunction;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionPhraseView;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;
import com.stonefacesoft.ottaa.utils.preferences.User;

import java.util.ArrayList;

public class PhrasesView extends AppCompatActivity implements View.OnClickListener, Make_Click_At_Time {
    protected ReturnPositionItem returnPositionItem;
    protected User firebaseUser;
    protected ViewPager2 viewPager2;
    protected ImageButton btnEditar;
    protected FloatingActionButton btnTalk;
    protected Toolbar toolbar;
    protected SearchView mSearchView;
    protected BarridoPantalla barridoPantalla;
    protected ImageButton foward;
    protected ImageButton previous;
    protected ImageButton exit;
    protected Button btnBarrido;
    protected ScrollFunction function_scroll;
    protected PhrasesViewControls controls;
    protected String TAG="PhraseView";
    protected AnalyticsFirebase mAnalyticsFirebase;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_galeria_grupos2);
        viewPager2 = findViewById(R.id.viewPager_groups);
        viewPager2.setVisibility(View.GONE);
        firebaseUser = new User(this);
        mAnalyticsFirebase = new AnalyticsFirebase(this);
    }


    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBarrido =findViewById(R.id.btnBarrido);
        foward = findViewById(R.id.down_button);
        previous = findViewById(R.id.up_button);
        exit = findViewById(R.id.back_button);
        btnEditar = findViewById(R.id.edit_button);
        btnEditar.setVisibility(View.VISIBLE);
        btnTalk = findViewById(R.id.btnTalk);
        btnEditar.setOnClickListener(this::onClick);
        foward.setOnClickListener(this::onClick);
        previous.setOnClickListener(this::onClick);
        exit.setOnClickListener(this::onClick);
        btnTalk.setOnClickListener(this::onClick);
        controls = new PhrasesViewControls(this);
        btnBarrido.setOnClickListener(this::onClick);
        btnBarrido.setOnTouchListener(this::onTouch);
        function_scroll = new ScrollFunctionPhraseView(this);
        iniciarBarrido();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBarrido){
            barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: " + keyCode);
        if (requestScreenScanningIsEnabled()) {

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
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getSource() == InputDevice.SOURCE_MOUSE)
                    barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                else
                    onBackPressed();
                return true;
            }

        }
        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);
        for (int i = 0; i <menu.size() ; i++) {
            menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Prepare the screen-Scanning
     * */
    protected void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(previous);
        listadoObjetosBarrido.add(exit);
        listadoObjetosBarrido.add(btnTalk);
        listadoObjetosBarrido.add(btnEditar);
        listadoObjetosBarrido.add(foward);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.VISIBLE);
                    if(barridoPantalla.isBarridoActivado())
                        barridoPantalla.changeButtonVisibility();
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
    public boolean onTouch(View v, MotionEvent event) {
        return controls.makeClick(event);
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

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    public ScrollFunction getFunction_scroll() {
        return function_scroll;
    }

    private boolean requestScreenScanningIsEnabled(){
        if(barridoPantalla != null)
            return barridoPantalla.isBarridoActivado();
        return false;
    }
}
