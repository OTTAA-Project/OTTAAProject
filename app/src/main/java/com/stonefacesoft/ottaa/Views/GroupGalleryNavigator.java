package com.stonefacesoft.ottaa.Views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Helper.OnStartDragListener;
import com.stonefacesoft.ottaa.Helper.RecyclerViewItemClickInterface;
import com.stonefacesoft.ottaa.Interfaces.FallanDatosDelUsuario;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaGruposControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionGaleriaGrupos;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.ArrayList;

public class GroupGalleryNavigator extends AppCompatActivity implements OnStartDragListener,
        RecyclerViewItemClickInterface, FallanDatosDelUsuario, View.OnClickListener, View.OnTouchListener, Make_Click_At_Time {

    protected BarridoPantalla barridoPantalla;
    protected ImageButton previous, foward,editButton, exit;
    protected Button btnBarrido;
    protected FloatingActionButton btnTalk;
    protected ScrollFunctionGaleriaGrupos function_scroll;
    protected boolean showViewPager;
    protected ProgressBar mProgressBar;
    protected FirebaseAuth mAuth;

    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected static String uid;
    protected textToSpeech myTTS;

    protected GaleriaGruposControls deviceControl;
    protected SharedPreferences sharedPrefsDefault;
    protected MenuItem item;
    protected Toolbar toolbar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_grupos2);
        initComponents();
        mAuth = FirebaseAuth.getInstance();
        myTTS = textToSpeech.getInstance(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnClickBarrido() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void isDroped(boolean isDroped) {

    }

    @Override
    public void onItemClicked(String name) {

    }

    @Override
    public void falloAlLeerArchivo(boolean fallo, String texto) {

    }

    private void initComponents(){
        btnBarrido = findViewById(R.id.btnBarrido);
        previous = findViewById(R.id.up_button);
        foward = findViewById(R.id.down_button);
        exit = findViewById(R.id.back_button);
        editButton = findViewById(R.id.edit_button);
        btnTalk = findViewById(R.id.btnTalk);
        btnBarrido.setOnClickListener(this);
        btnBarrido.setOnTouchListener(this);
        editButton.setOnClickListener(this);
        previous.setOnClickListener(this);
        foward.setOnClickListener(this);
        exit.setOnClickListener(this);
        btnTalk.setOnClickListener(this);
        function_scroll = new ScrollFunctionGaleriaGrupos(this,this );
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

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    public ScrollFunctionGaleriaGrupos getFunction_scroll(){
        return function_scroll;
    }

    protected void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(previous);
        listadoObjetosBarrido.add(exit);
        listadoObjetosBarrido.add(btnTalk);
        listadoObjetosBarrido.add(foward);
        //  listadoObjetosBarrido.add(editButton);
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    showViewPager=true;
                    btnBarrido.setVisibility(View.VISIBLE);
                    if(barridoPantalla.isAvanzarYAceptar())
                        barridoPantalla.changeButtonVisibility();
                }
            });
        }else{
            btnBarrido.setVisibility(View.GONE);
        }
    }

    protected void initViewPager(){

    }

    protected void crearRecyclerView(){


    }

    protected final void returnData(Intent data,IntentCode code){
        if(data != null){
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                int Picto = extras.getInt("ID");
                if (Picto != 0 && Picto != -1) {
                    setResult(code.getCode(), new Intent().putExtra("ID",Picto));
                    finish();
                }
            }
        }
    }


}
