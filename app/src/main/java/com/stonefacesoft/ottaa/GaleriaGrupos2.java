package com.stonefacesoft.ottaa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Activities.FindAllPictograms;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Progress_dialog_options;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Helper.OnStartDragListener;
import com.stonefacesoft.ottaa.Helper.RecyclerViewItemClickInterface;
import com.stonefacesoft.ottaa.Interfaces.FallanDatosDelUsuario;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.RecyclerViews.Grupo_Recycler_View;
import com.stonefacesoft.ottaa.RecyclerViews.Grupo_Recycler_View_Sort;
import com.stonefacesoft.ottaa.Viewpagers.viewpager_galeria_grupo;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.GaleriaGruposControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionGaleriaGrupos;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.Pictures.DownloadFirebasePictures;
import com.stonefacesoft.ottaa.utils.constants.ConstantsGroupGalery;
import com.stonefacesoft.ottaa.utils.constants.ConstantsMainActivity;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

;

public class GaleriaGrupos2 extends AppCompatActivity implements OnStartDragListener,
        RecyclerViewItemClickInterface,
        FirebaseSuccessListener, FallanDatosDelUsuario, View.OnClickListener, View.OnTouchListener, Make_Click_At_Time {

    private static final String TAG = "GaleriaGrupos2";
    //   private Menu menu;
    private final String textoPicto = "";
    int boton;
    public static ShowDismissDialog showDismissDialog;


    private StorageReference mStorageRef;

    private FirebaseAuth mAuth;
    private static String uid;

    private PopupMenu popupMenu;
    private static Context mContext;
    SharedPreferences sharedPrefsDefault;
    private MenuItem item;
    int mPermission = 0; //variable para saber si el permiso esta activado o no
    private static boolean updateAdapter;
    private static SubirArchivosFirebase uploadFirebaseFile;
    private Json json;
    private JSONArray mJSONArrayBackupFotos;
    private ProgressBar mProgressBar;
    private TextView mTextViewCargandoGrupos;
    private boolean isCargando = true;
    private FloatingActionButton btnTalk;

    private textToSpeech myTTS;


    private int progresoDeDescarga = 0;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public int mState = 1;
    private Toolbar toolbar;
    private CargarGruposJson cargarGruposJson;
    private FallanDatosDelUsuario fallanDatosDelUsuario;
    public static Progress_dialog_options downloadDialog;
    private ImageButton previous, foward,editButton, exit;
    private Button btnBarrido;


    private BarridoPantalla barridoPantalla;
    private viewpager_galeria_grupo viewpager;
    private Grupo_Recycler_View recycler_view_grupo;
    private Grupo_Recycler_View_Sort recycler_view_sort_grupo;
    private boolean showViewPager;
    private boolean isOrdenar;
    private ScrollFunctionGaleriaGrupos function_scroll;
    private AnalyticsFirebase analyticsFirebase;
    private FirebaseUtils firebaseUtils;
    private GaleriaGruposControls deviceControl;
    private DownloadFirebasePictures downloadFirebasePictures;
    private boolean editarPicto;


    @AddTrace(name = "GaleriaGrupos2", enabled = true /* optional */)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Obtenemos extras del intent*/
        Intent intento = getIntent();
        /*Quitamos la barra de navegacion y la ponemos en naranja y la default de ottaa*/
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        setContentView(R.layout.activity_galeria_grupos2);
        mContext = getApplicationContext();
        myTTS = textToSpeech.getInstance(this);
        analyticsFirebase=new AnalyticsFirebase(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressBar = findViewById(R.id.progressBar);
        mTextViewCargandoGrupos = findViewById(R.id.textoCargandoGrupos);
        fallanDatosDelUsuario = this;
        cargarGruposJson = new CargarGruposJson(mProgressBar, mTextViewCargandoGrupos, GaleriaGrupos2.this);
        cargarGruposJson.execute();
        function_scroll=new ScrollFunctionGaleriaGrupos(this,this);
        showDismissDialog=new ShowDismissDialog();
        editarPicto = sharedPrefsDefault.getBoolean(getString(R.string.str_editar_picto), true);

        /*Manejo de preferencias de cada usuario**/
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sharedPrefs = getSharedPreferences(sharedPrefsDefault.getString(getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);
        showViewPager=sharedPrefsDefault.getBoolean("showViewPager",false);

        //Referencias principales a la database
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        setTitle(R.string.title_activity_galeria_grupos2);

        //**Recycler View*//*

        Bundle extras = getIntent().getExtras();
        showViewPager=sharedPrefsDefault.getBoolean("showViewPager",false);
        if (extras!= null) {
            isOrdenar=extras.getBoolean("esOrdenar",false);
            if(isOrdenar)
                showViewPager=false;
        }
        //Sacamos el de bajarJsonFirebase que tardaba para entrar a grupo y no se estaba usando en ningun lado
        uploadFirebaseFile = new SubirArchivosFirebase(mContext);
        downloadDialog =new Progress_dialog_options(this);
        downloadDialog.setTitle(getApplicationContext().getResources().getString(R.string.downloadingFoto));
        downloadDialog.setMessage(getApplicationContext().getResources().getString(R.string.downLoadFotos));
        downloadDialog.setCancelable(false);
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Json.getInstance().setmContext(this);
        json = Json.getInstance();

        /*Si el usuario no entro nunca a la galeria pictos se hace un prompt del tutorial*/



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent mainIntent = new Intent().setClass(
                        GaleriaGrupos2.this, LoginActivity2.class);
                    startActivity(mainIntent);
                    Toast.makeText(GaleriaGrupos2.this, R.string.expired_sesions, Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    //UID Usuario
                    uid = mAuth.getCurrentUser().getUid();
                }

            }
        };
        btnBarrido =findViewById(R.id.btnBarrido);
        previous =findViewById(R.id.up_button);
        foward =findViewById(R.id.down_button);
        exit =findViewById(R.id.back_button);
         editButton=findViewById(R.id.edit_button);
        btnTalk=findViewById(R.id.btnTalk);
        btnBarrido.setOnClickListener(this);
        btnBarrido.setOnTouchListener(this);
        editButton.setOnClickListener(this);
        previous.setOnClickListener(this);
        foward.setOnClickListener(this);
        exit.setOnClickListener(this);
        btnTalk.setOnClickListener(this);


        iniciarBarrido();
        crearRecyclerView();
        changeEditButtonIcon();
        viewpager=new viewpager_galeria_grupo(this,myTTS);

        viewpager.showViewPager(showViewPager);
        //showView(editButton,showViewPager);
        deviceControl=new GaleriaGruposControls(this);
    }

    private void crearRecyclerView(){
        if(!isOrdenar){
            recycler_view_grupo=new Grupo_Recycler_View(this,mAuth);
            recycler_view_grupo.setUploadFirebaseFile(uploadFirebaseFile);
            recycler_view_grupo.setPopupMenu(popupMenu);
            recycler_view_grupo.setMyTTS(myTTS);
            recycler_view_grupo.setSharedPrefsDefault(sharedPrefsDefault);
            recycler_view_grupo.showRecyclerView(showViewPager);
        }else{
            recycler_view_sort_grupo=new Grupo_Recycler_View_Sort(this,mAuth);
            recycler_view_sort_grupo.setUploadFirebaseFile(uploadFirebaseFile);
            recycler_view_sort_grupo.setMyTTS(myTTS);
            recycler_view_sort_grupo.setSharedPrefsDefault(sharedPrefsDefault);
            recycler_view_sort_grupo.showRecyclerView(showViewPager);
        }

    }

    /**
     * Prepare the screen-Scanning
     * */
    private void iniciarBarrido() {
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

    @Override
    public void onBackPressed() {

        //Uso este boolean para prevenir que se haga atras antes que se carguen los pictos, de esta forma prevenimos que se reemplaze
        //el grupo por ningun valor y borre to do;

        if (!isCargando) {
            Intent databack = new Intent();
            databack.putExtra("ID", 0);
            databack.putExtra("Boton", boton);
            setResult(3, databack);
            finish();
        } else {
            myTTS.mostrarAlerta(getString(R.string.backpress_grupos));
        }

    }

    /**
     * FIN Chequear y bajar datos firebase con child listener
     **/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: Entering ActivityResult");
        switch (requestCode){
            case ConstantsGroupGalery
                    .GALERIAPICTOS:
                if(resultCode  != ConstantsGroupGalery.RETURNGALERIAPICTOSDATA){
                    viewpager.updateData();
                    if(recycler_view_grupo != null)
                        recycler_view_grupo.setGrupos();
                }
                returnData(data);
                break;
            case ConstantsMainActivity
                    .EDITARPICTO :
                loadData();
               break;
            case ConstantsGroupGalery.SORTACTION:
                loadData();
                break;
            case ConstantsGroupGalery.SEARCH_ALL_PICTOGRAMS:
                loadData();
                returnData(data);
            break;
            case ConstantsGroupGalery.RETURNGALERIAPICTOSDATA:
                returnData(data);
                break;
        }


/*        if (requestCode == IntentCode.EDITARPICTO.getCode()||requestCode==IntentCode.ORDENAR.getCode()|| resultCode == IntentCode.SEARCH_ALL_PICTOGRAMS.getCode()) {
            if(recycler_view_grupo!=null){
                recycler_view_grupo.sincronizeData();
                recycler_view_grupo.changeData();
                recycler_view_grupo.guardarDatosGrupo();
            }
            viewpager.updateData();
            if(resultCode == IntentCode.SEARCH_ALL_PICTOGRAMS.getCode()){
                if (data != null) {
                    if (data.getExtras() != null) {
                        Bundle extras = data.getExtras();
                        int Picto = extras.getInt("ID");
                        Log.e("GaleriaGrupo ", " PictoID: " + Picto);
                        if (Picto != 0 && Picto != -1) {
                            Intent databack = new Intent();
                            databack.putExtra("ID", Picto);
                            setResult(IntentCode.GALERIA_GRUPOS.getCode(), databack);
                            finish();
                        }
                    }

                }
            }

        }*/

        super.onActivityResult(requestCode, resultCode, data);
    }

    private final void returnData(Intent data){
        if(data != null){
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                int Picto = extras.getInt("ID");
                if (Picto != 0 && Picto != -1) {
                    setResult(IntentCode.GALERIA_GRUPOS.getCode(), new Intent().putExtra("ID",Picto));
                    finish();
                }
            }
        }
    }

    private void loadData(){
        if(recycler_view_grupo!=null){
            recycler_view_grupo.sincronizeData();
            recycler_view_grupo.changeData();
            recycler_view_grupo.guardarDatosGrupo();
        }
        viewpager.updateData();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void isDroped(boolean isDroped) {

    }


    @Override
    public void onItemClicked(String name) {
        Toast.makeText(getApplicationContext(), String.valueOf(name), Toast.LENGTH_LONG).show();
    }

    //------- ------- metodos relacionados al menu -------- ------------



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);
        if (mState == 1) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        } else {
            item = menu.findItem(R.id.vincular);
            item.setVisible(false);
         /*   item = menu.findItem(R.id.listo);
            item.setVisible(false);*/
            item = menu.findItem(R.id.action_search);
            item.setVisible(false);
            item = menu.findItem(R.id.action_settings);
            item.setVisible(false);
            item = menu.findItem(R.id.bajarFotos);
            item.setVisible(true);

            item=menu.findItem(R.id.order_items);
            item.setVisible(true);

            item=menu.findItem(R.id.tipe_view);
            item.setVisible(true);
            if(isOrdenar){
                menu.findItem(R.id.order_items).setVisible(false);
                menu.findItem(R.id.tipe_view).setVisible(false);
                menu.findItem(R.id.nuevo).setVisible(false);
                menu.findItem(R.id.bajarFotos).setVisible(false);
            }
            if(!showViewPager)
                item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
            else
                item.setIcon(R.drawable.ic_baseline_apps_white_24);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*Obtenemos el id del item del menu para luego usarlo para hacer alguna accion*/
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: id: " + id);

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.bajarFotos:

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Bajar pictogramas");
                analyticsFirebase.customEvents("Touch", "Galeria Grupos", "Download Pictograms");
                accionBajarFoto();
                // subirTodasLasFotos();
                return true;

            case R.id.nuevo:
                analyticsFirebase.customEvents("Touch", "Galeria Grupos", "add Group");
                if(editarPicto){
                  if (sharedPrefsDefault.getInt("premium", 0) == 1) {
                      /*Aca vamos a editar picto visual que maneja tanto para grupos como para pictos*/
                      Intent intent = new Intent(GaleriaGrupos2.this, Edit_Picto_Visual.class);
                      intent.putExtra("esNuevo", true);
                      intent.putExtra("Padre", boton);
                      intent.putExtra("esGrupo", true);
                      intent.putExtra("Texto","");
                      myTTS.hablar(getString(R.string.add_grupo));
                      Log.d(TAG, "onOptionsItemSelected: Creating new Group");
                      startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
                  } else {
                      Intent i = new Intent(GaleriaGrupos2.this, LicenciaExpirada.class);
                      startActivity(i);
                  }
                }
                return true;

            case R.id.tipe_view:
                analyticsFirebase.customEvents("Touch","Galeria Grupos","Change view");
                showViewPager=!showViewPager;
                if(!showViewPager)
                    item.setIcon(R.drawable.ic_baseline_view_carousel_white_24);
                else
                    item.setIcon(R.drawable.ic_baseline_apps_white_24);
                sharedPrefsDefault.edit().putBoolean("showViewPager",showViewPager).apply();
                viewpager.showViewPager(showViewPager);
                recycler_view_grupo.showRecyclerView(showViewPager);
                changeEditButtonIcon();
               // showView(editButton,showViewPager);
                break;
            case R.id.order_items:
                analyticsFirebase.customEvents("Touch","Galeria Grupos","Sort Groups");
                Intent intent=new Intent(GaleriaGrupos2.this,GaleriaGrupos2.class);
                intent.putExtra("esOrdenar",true);
                startActivityForResult(intent,IntentCode.ORDENAR.getCode());
                break;

        }

        return true;
    }


    @Override
    protected void onPause() {

        Json.getInstance().setmContext(this);

        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       if(downloadDialog !=null)
           downloadDialog.destruirDialogo();
        // subirTodasLasFotos();
    }

    //------ metodos relacionados con las fotos --------
    private void accionBajarFoto() {
        if(downloadFirebasePictures ==null)
            downloadFirebasePictures =new DownloadFirebasePictures(this,showDismissDialog,this);
        downloadFirebasePictures.setUpDirectory();

    }



    @Override
    public void onDescargaCompleta(int descargaCompleta) {

    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {

    }


    @Override
    public void onFotoDescargada(int fotosDescargadas) {
        progresoDeDescarga++;
        switch (fotosDescargadas) {
            case Constants.FOTO_DESCARGADA:
//                pd.incrementProgressBy(1);
                    downloadDialog.setProgress(progresoDeDescarga);
                break;
            case Constants.FOTOS_YA_DESCARGADAS:
                    downloadDialog.setProgress(progresoDeDescarga);
                    myTTS.mostrarAlerta(getApplicationContext().getResources().getString(R.string.fotosBajadas));

                break;
        }
    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {

    }



  /*  //-------- -------- ----------- metodos relacionados con los permisos ------------------ -----------------
    private void preguntarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
            .checkSelfPermission(getApplicationContext(), android.Manifest
                .permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
            (getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                    //Ver a donde hay q pasarlo, posiblemente Galeria Pictos
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, Constants.EXTERNAL_STORAGE);
            }
        }

    }
*/
    //permisos para poder crear los archivos, se necesitan cuando se bajan fotos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.EXTERNAL_STORAGE) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //permission not granted
                    mPermission = PackageManager.PERMISSION_DENIED;

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    mPermission = PackageManager.PERMISSION_GRANTED;
                    accionBajarFoto();
                }
            }
            if (mPermission == PackageManager.PERMISSION_GRANTED) {
               accionBajarFoto();
            } else if (mPermission == PackageManager.PERMISSION_DENIED) {
                myTTS.mostrarAlerta(getString(R.string.permisos_fotos_denegados));

            }


        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void falloAlLeerArchivo(boolean fallo, String titulo) {
        //when the aplication is failed, I implements thats interface to call the weeklyBackup
        if (fallo) {

            //     cargarGruposJson.cancel(true);
            cargarGruposJson.cancel(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.up_button:
               if(showViewPager)
               viewpager.scrollPosition(false);
               else  if(recycler_view_grupo!=null&&!showViewPager)
                   recycler_view_grupo.scrollTo(false);
               if(barridoPantalla.isBarridoActivado()){
                   analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Previous Button");
               }
                break;
            case R.id.down_button:
                if(showViewPager)
                    viewpager.scrollPosition(true);
                else  if(recycler_view_grupo!=null&&!showViewPager)
                    recycler_view_grupo.scrollTo(true);
                if(barridoPantalla.isBarridoActivado()){
                    analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Next Button");
                }
                break;
            case R.id.back_button:
                if(barridoPantalla.isBarridoActivado()){
                    analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Close Galery Groups");
                }
                    onBackPressed();

                break;
            case R.id.edit_button:
                if(!isOrdenar){
                    if(showViewPager){
                        analyticsFirebase.customEvents("Touch","Galeria Grupos","Edit Group");
                        viewpager.editItem(sharedPrefsDefault.getInt("premium", 0) == 1);
                    }else{
                        Intent intent=new Intent(this, FindAllPictograms.class);
                        intent.putExtra("isSearchingAll",true);
                        startActivityForResult(intent,IntentCode.SEARCH_ALL_PICTOGRAMS.getCode());
                    }
                }
                else{
                    recycler_view_sort_grupo.guardarOrden();
                    Intent databack = new Intent();
                    setResult(IntentCode.ORDENAR.getCode(), databack);
                     finish();
                }
                break;
            case R.id.btnTalk:
                if(showViewPager){
                    if(barridoPantalla.isBarridoActivado())
                        analyticsFirebase.customEvents("Accessibility","Galeria Grupos","Select Group");
                    viewpager.OnClickItem();
                }
                break;
            case R.id.btnBarrido:
                if (barridoPantalla.isBarridoActivado() && barridoPantalla.isAvanzarYAceptar()) {
                    Log.d(TAG, "onClick() returned: Barrido Pantalla");
                } else if (barridoPantalla.isBarridoActivado() && !barridoPantalla.isAvanzarYAceptar()) {
                    int posicion = barridoPantalla.getPosicionBarrido();
                    if (posicion != -1)
                        barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                }
                break;
            default:

        }
    }


    public static boolean setUpdateAdapter(Boolean b) {
        return updateAdapter = b;
    }

    public static SubirArchivosFirebase subirArchivos() {
        return uploadFirebaseFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Json.getInstance().setmContext(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        new ConfigurarIdioma(getApplicationContext(), preferences.getString(getApplicationContext().getString(R.string.str_idioma), "en"));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(newBase);
        String locale = sharedPrefsDefault.getString(newBase.getString(R.string.str_idioma), Locale.getDefault().getLanguage());
        new ConfigurarIdioma(newBase, locale);
        super.attachBaseContext(myContextWrapper.wrap(newBase, locale));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
       return deviceControl.makeClick(event);
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
                mJSONArrayBackupFotos = json.getmJSONArrayTodasLasFotosBackup();
            } catch (RuntimeException e) {
                e.printStackTrace();
                fallanDatosDelUsuario.falloAlLeerArchivo(true, "Fotos");
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

            /*Se encarga de abrir los grupos y manejar el drag-n-drop*/
            if(recycler_view_grupo!=null)
            recycler_view_grupo.cargarGrupo();
            else if(recycler_view_sort_grupo!=null)
                recycler_view_sort_grupo.cargarGrupo();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mProgressBarCargandoGrupos.setVisibility(View.GONE);
            mTextViewCargandoGrupos.setVisibility(View.GONE);
            isCargando = false;

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

   public class ShowDismissDialog extends Handler {
        private int position=-1;
        private void removeCreatedMessages(int value){
            if(super.hasMessages(value))
                super.removeMessages(value);
        }
        private void removeAllMessages(){
            removeCreatedMessages(position);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                default:
                    position=msg.what;
                    mostrarDialogo(msg.what);
            }
        }
        public void sendMessage(int what){
            if(what!=position){
                removeAllMessages();
                super.sendMessage(getHandler().obtainMessage(what));
            }
        }
        public android.os.Handler getHandler()
        {
            return this;
        }

    }

     public void mostrarDialogo(int show){
        switch (show){
            case 0:
                if(downloadDialog.isShowing())
                    downloadDialog.destruirDialogo();
                break;
            case 1:
                downloadDialog.mostrarDialogo();
                break;
        }
    }


    private void showView(View view,boolean showItem){
        if(showItem)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.INVISIBLE);
    }

    private void setButtonIcon(ImageView view, Drawable drawable){
        view.setImageDrawable(drawable);
    }

    private void changeEditButtonIcon(){
        if(showViewPager){
            setButtonIcon(editButton,getResources().getDrawable(R.drawable.ic_edit_white_24dp));
        }else if(isOrdenar) {
            setButtonIcon(editButton, getResources().getDrawable(R.drawable.ic_baseline_save_white_24));
            editButton.setVisibility(View.INVISIBLE);
        }else {
            editButton.setVisibility(View.VISIBLE);
            setButtonIcon(editButton,getResources().getDrawable(R.drawable.ic_baseline_search_24));
        }
    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    public ScrollFunctionGaleriaGrupos getFunction_scroll(){
        return function_scroll;
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

