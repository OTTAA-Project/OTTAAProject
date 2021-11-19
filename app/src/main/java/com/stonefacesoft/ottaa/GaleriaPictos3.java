package com.stonefacesoft.ottaa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.RecyclerViews.Picto_Recycler_View_Sort;
import com.stonefacesoft.ottaa.RecyclerViews.Picto_Recycler_view;
import com.stonefacesoft.ottaa.RecyclerViews.Picto_Vincular_Recycler_View;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_galeria_pictos;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaPictosControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionGaleriaPictos;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GaleriaPictos3 extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, Make_Click_At_Time {
    private static final String TAG = "GaleriaPictos3";
    public int mState = 1;
    protected SearchView mSearchView;
    //Common Objects
    private Json json;
    private FirebaseAuth mAuth;
    private SubirArchivosFirebase uploadFile;
    private boolean esVincular;
    private boolean showViewPager;
    private boolean isSorter;
    private SharedPreferences sharedPrefsDefault;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private Menu menu;
    private MenuItem item;
    private FirebaseAnalytics mFirebaseAnalytics;

    //TextToSpeech
    private textToSpeech myTTS;
    //RecyclerViewComponents
    private Picto_Vincular_Recycler_View mVincularRecyclerView;
    private Picto_Recycler_view mPictoRecyclerView;
    private Picto_Recycler_View_Sort mPictoSortRecyclerView;
    private JSONArray pictosDelGrupoFiltrado;
    private JSONArray pictosDelGrupo;
    //ViewPager
    private viewpager_galeria_pictos viewpager_galeria_pictos;
    //ShowViewPager
    //Menu

    //Id Grupo Padre
    private int boton;
    private String nombre;
    //ImageButton
    private ImageButton previous, foward, exit, edit_button;
    private Button btnBarrido;
    private FloatingActionButton btnTalk;
    private BarridoPantalla barridoPantalla;
    private ScrollFunctionGaleriaPictos function_scroll;
    private AnalyticsFirebase analyticsFirebase;
    private GaleriaPictosControls navigationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        analyticsFirebase = new AnalyticsFirebase(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_grupos2);
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initComponents();
        navigationControl = new GaleriaPictosControls(this);
    }

    private void cargarAdapter() {
        isVincular(esVincular).isSorted(isSorter).isDefault();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);

        MenuItem item;
        item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        item = menu.findItem(R.id.tipe_view);
        if (!showViewPager)
            item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
        else
            item.setIcon(R.drawable.ic_baseline_apps_white_24);
        Log.d(TAG, "onCreateOptionsMenu: esVincular: " + esVincular);
        if (esVincular || isSorter) {
            item.setVisible(false);
            item = menu.findItem(R.id.vincular);
            item.setVisible(false);
            item = menu.findItem(R.id.nuevo);
            item.setVisible(false);
            item = menu.findItem(R.id.order_items);
            item.setVisible(false);

        } else {


            item = menu.findItem(R.id.action_search);
            item.setVisible(true);
        }
        if (esVincular) {
            item = menu.findItem(R.id.action_search);
            item.setVisible(true);
        } else if (isSorter) {
            item = menu.findItem(R.id.action_search);
            item.setVisible(false);

        }
        item = menu.findItem(R.id.bajarFotos);
        item.setVisible(false);
        if (menu.findItem(R.id.action_search).isVisible()) {
            menuItem = menu.findItem(R.id.action_search);
            mSearchView = (SearchView) menuItem.getActionView();
            mSearchView.setFocusable(true);
            mSearchView.setQueryHint("Search");
            mSearchView.onActionViewCollapsed();
            mSearchView.setSubmitButtonEnabled(true);
            setmSearchView();
        }
        return true;
    }

    public void setmSearchView() {
        if (mVincularRecyclerView != null)
            mVincularRecyclerView.setSearchView(mSearchView);
        if (mPictoRecyclerView != null)
            mPictoRecyclerView.setSearchView(mSearchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:
                return true;
            case R.id.nuevo:
                analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Add Pictogram");
                if (sharedPrefsDefault.getInt("premium", 0) == 1) {

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Nuevo Picto");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);

                    Intent intent = new Intent(GaleriaPictos3.this, Edit_Picto_Visual.class);
                    intent.putExtra("esNuevo", true);
                    intent.putExtra("Padre", boton);
                    intent.putExtra("esGrupo", false);
                    intent.putExtra("Texto","");
                    myTTS.hablar(getString(R.string.add_pictograma));
                    Log.d(TAG, "onOptionsItemSelected: Creando un nuevo picto");

                    startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
                    return true;
                } else {
                    Intent i = new Intent(GaleriaPictos3.this, LicenciaExpirada.class);
                    startActivity(i);
                }
                return true;

            case R.id.vincular:
                analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Vinculate Pictograms");
                if (sharedPrefsDefault.getInt("premium", 0) == 1) {

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Vincular");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);

                    Intent intent2 = new Intent(GaleriaPictos3.this, GaleriaPictos3.class);
                    intent2.putExtra("Boton", boton);
                    intent2.putExtra("esVincular", true);
                    intent2.putExtra("Nombre", nombre);


                    startActivityForResult(intent2, IntentCode.VINCULAR.getCode());
                } else {
                    Intent i = new Intent(GaleriaPictos3.this, LicenciaExpirada.class);
                    startActivity(i);
                }
                return true;
            case R.id.order_items:
                analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Sort Pictograms");
                if (sharedPrefsDefault.getInt("premium", 0) == 1) {

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Ordenar");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);

                    Intent intent2 = new Intent(GaleriaPictos3.this, GaleriaPictos3.class);
                    intent2.putExtra("Boton", boton);
                    intent2.putExtra("esOrdenar", true);
                    intent2.putExtra("Nombre", nombre);


                    startActivityForResult(intent2, IntentCode.ORDENAR.getCode());
                } else {
                    Intent i = new Intent(GaleriaPictos3.this, LicenciaExpirada.class);
                    startActivity(i);
                }
                return true;
            case R.id.tipe_view:
                analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Change View");
                showViewPager = !showViewPager;
                if (!showViewPager)
                    item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
                else
                    item.setIcon(R.drawable.ic_baseline_apps_white_24);
                sharedPrefsDefault.edit().putBoolean("showViewPager_pictos", showViewPager).apply();
                viewpager_galeria_pictos.showViewPager(showViewPager);
                if (mPictoRecyclerView != null)
                    mPictoRecyclerView.showRecyclerView(showViewPager);
                showView(edit_button, showViewPager);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.VINCULAR.getCode() || requestCode == IntentCode.ORDENAR.getCode() || requestCode == IntentCode.EDITARPICTO.getCode()) {
            mPictoRecyclerView.sincronizeData();
            mPictoRecyclerView.changeData();
            mPictoRecyclerView.guardarDatosGrupo();
            viewpager_galeria_pictos.setArray(mPictoRecyclerView.getArray());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                if (barridoPantalla.isBarridoActivado()) {
                    analyticsFirebase.customEvents("Accessibility", "Galeria Pictos", "Edit Pictogram");
                }
                if (isSorter) {
                    analyticsFirebase.customEvents("Touch", "Ordenar Pictogramas", "Save child sort");
                    mPictoSortRecyclerView.guardarOrden();
                    mPictoSortRecyclerView.subirGrupos();
                    Intent databack = new Intent();
                    setResult(IntentCode.ORDENAR.getCode(), databack);
                    finish();
                } else if (esVincular) {
                    analyticsFirebase.customEvents("Touch", "Vincular Pictogramas", "Vinculate child");
                    guardarVincular();
                } else {
                    if (showViewPager) {
                        viewpager_galeria_pictos.editItem(sharedPrefsDefault.getInt("premium", 0) == 1);
                    }
                }

                break;
            case R.id.up_button:
                scrollNextButton(false);
                break;
            case R.id.down_button:
                scrollNextButton(true);
                break;
            case R.id.back_button:
                if (barridoPantalla.isBarridoActivado())
                    analyticsFirebase.customEvents("Accessibility", "Galeria Pictos", "Backpress Button");
                else
                    analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Backpress Button");
                onBackPressed();
                break;
            case R.id.btnTalk:
                if (showViewPager) {
                    if (!barridoPantalla.isBarridoActivado())
                        analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Select Pictogram with button talk");
                    else
                        analyticsFirebase.customEvents("Accessibility", "Galeria Pictos", "Select Pictogram with button talk");

                    viewpager_galeria_pictos.OnClickItem();
                }
                break;
            case R.id.btnBarrido:
                if (barridoPantalla.isBarridoActivado() && barridoPantalla.isAvanzarYAceptar()) {
                    Log.d(TAG, "onClick() returned: Barrido Pantalla");

                } else if (barridoPantalla.isBarridoActivado() && !barridoPantalla.isAvanzarYAceptar()) {
                    int position = barridoPantalla.getPosicionBarrido();
                    if (position != -1)
                        barridoPantalla.getmListadoVistas().get(position).callOnClick();
                }
                break;
        }
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();
        uploadFile = new SubirArchivosFirebase(getApplicationContext());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        json = Json.getInstance();
        json.setmContext(this);
        Bundle extras = getIntent().getExtras();
        showViewPager = sharedPrefsDefault.getBoolean("showViewPager_pictos", false);
        if (extras != null) {
            esVincular = extras.getBoolean("esVincular", false);
            isSorter = extras.getBoolean("esOrdenar", false);
            boton = extras.getInt("Boton", 0);
            nombre = extras.getString("Nombre");
            if (esVincular || isSorter )
                showViewPager = false;
        }
        myTTS = textToSpeech.getInstance(this);
        viewpager_galeria_pictos = new viewpager_galeria_pictos(this, myTTS, boton);
        
        previous = findViewById(R.id.down_button);
        foward = findViewById(R.id.up_button);
        exit = findViewById(R.id.back_button);
        edit_button = findViewById(R.id.edit_button);
        btnTalk = findViewById(R.id.btnTalk);
        btnBarrido = findViewById(R.id.btnBarrido);
        btnBarrido.setVisibility(View.GONE);

        if (esVincular || isSorter) {
            edit_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_save_white_24));

        }
        btnBarrido.setOnClickListener(this);
        btnBarrido.setOnTouchListener(this);
        previous.setOnClickListener(this);
        exit.setOnClickListener(this);
        edit_button.setOnClickListener(this);
        btnTalk.setOnClickListener(this);
        foward.setOnClickListener(this);
        function_scroll = new ScrollFunctionGaleriaPictos(this, this);
        iniciarBarrido();
        viewpager_galeria_pictos.showViewPager(showViewPager);
        cargarAdapter();
        showView(edit_button, showViewPager);

    }

    private void guardarVincular() {
        JSONArray mSelectedPictos = mVincularRecyclerView.getGaleriaPictos2().getmSelectedPictos();
        JSONArray todosLosGrupos = json.getmJSONArrayTodosLosGrupos();
        if (mSelectedPictos != null) {
            for (int i = 0; i < mSelectedPictos.length(); i++) {
                try {
                    int id = mSelectedPictos.getJSONObject(i).getInt("id");
                    JSONutils.relacionarConGrupo2(todosLosGrupos, boton, id);
                    Log.d(TAG, "guardarVincular: " + mSelectedPictos);
                } catch (JSONException e) {
                    Log.e(TAG, "guardarVincular: Error: " + e.getMessage());
                }
            }
        }
        json.setmJSONArrayTodosLosGrupos(todosLosGrupos);
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
            Log.d(TAG, "guardarVincular: ");
        Intent databack = new Intent();
        databack.putExtra("nuevosPictos", mSelectedPictos.length());
        setResult(IntentCode.VINCULAR.getCode(), databack);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent databack = new Intent();
        databack.putExtra("ID", 0);
        databack.putExtra("Boton", -1);
        setResult(3, databack);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //esto soluciona el error que salta en el nokia 8 por que si no elimina la vista
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void iniciarBarrido() {
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        if (!esVincular) {
            listadoObjetosBarrido.add(previous);
            listadoObjetosBarrido.add(exit);
            listadoObjetosBarrido.add(btnTalk);
            listadoObjetosBarrido.add(foward);
        }
        barridoPantalla = new BarridoPantalla(this, listadoObjetosBarrido);
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    if (!esVincular) {
                        showViewPager = true;
                        btnBarrido.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            btnBarrido.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return navigationControl.makeClick(event);
    }

    @Override
    public void OnClickBarrido() {
        if (function_scroll.isClickEnabled() && barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId() == R.id.btnTodosLosPictos)
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        else if (!function_scroll.isClickEnabled()) {
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:

                    if (barridoPantalla.isScrollMode() || barridoPantalla.isScrollModeClicker()) {
                        if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                            if (barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.avanzarBarrido();
                        } else {
                            if (barridoPantalla.isScrollMode())
                                function_scroll.HacerClickEnTiempo();
                            barridoPantalla.volverAtrasBarrido();

                        }
                    }
                    return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public void showView(View view, boolean showItem) {
        if (isSorter || esVincular)
            showItem = !showItem;
        if (showItem)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.INVISIBLE);
    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    public ScrollFunctionGaleriaPictos getFunction_scroll() {
        return function_scroll;
    }

    /// load the adapter at this section
    public GaleriaPictos3 isVincular(boolean isVincular) {
        if (isVincular) {
            mVincularRecyclerView = new Picto_Vincular_Recycler_View(this, mAuth);
            mVincularRecyclerView.setSharedPrefsDefault(sharedPrefsDefault);
            mVincularRecyclerView.setArray();
            mVincularRecyclerView.setMyTTS(myTTS);
            mVincularRecyclerView.showRecyclerView(showViewPager);
        }
        return this;
    }

    public GaleriaPictos3 isSorted(boolean isSorter) {
        if (isSorter) {
            mPictoSortRecyclerView = new Picto_Recycler_View_Sort(this, mAuth);
            mPictoSortRecyclerView.setSharedPrefsDefault(sharedPrefsDefault);
            mPictoSortRecyclerView.setArray(boton);
            mPictoSortRecyclerView.setMyTTS(myTTS);
            mPictoSortRecyclerView.setUploadFirebaseFile(uploadFile);
            mPictoSortRecyclerView.showRecyclerView(showViewPager);
        }
        return this;
    }

    public GaleriaPictos3 isDefault() {
        if (!isSorter && !esVincular ) {
            mPictoRecyclerView = new Picto_Recycler_view(this, mAuth);
            mPictoRecyclerView.setSharedPrefsDefault(sharedPrefsDefault);
            mPictoRecyclerView.setArray(boton);
            mPictoRecyclerView.setMyTTS(myTTS);
            mPictoRecyclerView.setUploadFirebaseFile(uploadFile);
            mPictoRecyclerView.showRecyclerView(showViewPager);
        }
        return this;
    }


    // recyclerViewMethods
    public void scrollNextButton(boolean isNextButton) {
        isNextButton(isNextButton).scrollViewPager(isNextButton).scrollSorter(isNextButton).scrollVincular(isNextButton).scrollDefault(isNextButton);
    }

    public GaleriaPictos3 scrollViewPager(boolean nextButton) {
        if (showViewPager)
            viewpager_galeria_pictos.scrollPosition(nextButton);
        return this;
    }

    public GaleriaPictos3 scrollSorter(boolean nextButton) {
        if (isSorter) {
            mPictoSortRecyclerView.scrollTo(nextButton);
        }
        return this;
    }

    public GaleriaPictos3 scrollDefault(boolean nextButton) {
        if (!showViewPager && !isSorter && !esVincular ) {
            mPictoRecyclerView.scrollTo(nextButton);
        }
        return this;
    }

    public GaleriaPictos3 scrollVincular(boolean nextButton) {
        if (esVincular) {
            mVincularRecyclerView.scrollTo(nextButton);
        }
        return this;
    }


    public GaleriaPictos3 isNextButton(boolean nextButton) {
        if (barridoPantalla.isBarridoActivado()) {
            analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Next Button");
        } else {
            analyticsFirebase.customEvents("Touch", "Galeria Pictos", "Previous Button");
        }
        return this;
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
