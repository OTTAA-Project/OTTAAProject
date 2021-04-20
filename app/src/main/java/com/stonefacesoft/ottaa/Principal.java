package com.stonefacesoft.ottaa;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.BuildConfig;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kobakei.ratethisapp.RateThisApp;
import com.stonefacesoft.ottaa.Backup.BackupGroups;
import com.stonefacesoft.ottaa.Backup.BackupPhotos;
import com.stonefacesoft.ottaa.Backup.BackupPhrases;
import com.stonefacesoft.ottaa.Backup.BackupPictograms;
import com.stonefacesoft.ottaa.Dialogos.NewDialogsOTTAA;
import com.stonefacesoft.ottaa.Dialogos.Progress_dialog_options;
import com.stonefacesoft.ottaa.Dialogos.Yes_noDialogs;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirBackupFirebase;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.Interfaces.PlaceSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Viewpagers.Viewpager_tutorial;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.AboutOttaa;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.Gesture;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.PrincipalControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionMainActivity;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;
import com.stonefacesoft.ottaa.utils.TraducirFrase;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.location.PlacesImplementation;
import com.stonefacesoft.ottaa.utils.preferences.User;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.ottaa.utils.timer_pictogram_clicker;
import com.stonefacesoft.ottaa.utils.traducirTexto;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

/**
 *
 * VERSION 83 merge dev dev-hotfix83
 */
public class Principal extends AppCompatActivity implements View
        .OnClickListener,
        View.OnLongClickListener,
        OnMenuItemClickListener,
        FirebaseSuccessListener, NavigationView.OnNavigationItemSelectedListener,   PlaceSuccessListener, GoogleApiClient.ConnectionCallbacks, translateInterface, View.OnTouchListener, Make_Click_At_Time  {

    private static final String TAG = "Principal";
    public Uri bajarGrupos;
    private NLG nlg;
    private boolean nlgFlag;
    private Indicadores indicadores;
    static private boolean isConnected;

    private Button Registro;
    int heigth = 0, width = 0;

    private  DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private PrincipalControls navigationControls;


    //Declaracion de los botones
    private ImageButton Seleccion1;
    private ImageButton Seleccion2;
    private ImageButton Seleccion3;
    private ImageButton Seleccion4;
    private ImageButton Seleccion5;
    private ImageButton Seleccion6;
    private ImageButton Seleccion7;
    private ImageButton Seleccion8;
    private ImageButton Seleccion9;
    private ImageButton Seleccion10;
    private ImageButton button;
    private ImageButton ResetButton;
    private PictoView Opcion1;
    private PictoView Opcion2;
    private PictoView Opcion3;
    private PictoView Opcion4;

    private timer_pictogram_clicker  Opcion1_clicker;
    private timer_pictogram_clicker  Opcion2_clicker;
    private timer_pictogram_clicker  Opcion3_clicker;
    private timer_pictogram_clicker  Opcion4_clicker;

    private ImageButton MasPictos;
    private ImageButton TodosLosPictos;

    private User user;
    private boolean isSettings;
    private static FirebaseSuccessListener mFirebaseSuccessListener;
    private boolean mCerrarSesion;
    private LinearLayout layout;
    private int count = 0;

    private Button btnBarrido;
    private boolean barridoSwitch;
    private MenuItem locationItem;
    private ScrollFunctionMainActivity function_scroll;
    private FloatingActionButton talk;


    private BajarJsonFirebase mBajarJsonFirebase;

    // Boleano que dice si esta habilitado el sonido o no
    private boolean mute;


    //Declaro el manejador de preferencia
    private SharedPreferences sharedPrefs, sharedPrefsDefault;
    private boolean PrimerUso;

    // Declaracion de Arraylist en el que se guardan los Picto viejos
    //protected ArrayList<JSONObject> Historial;
    private Historial historial;

    //Declaracion de variables del TTS

    private final boolean ayudaFlag = false;
    //Declaro el fecha y hora del sistema
    private final Calendar SystemTime = Calendar.getInstance();
    //Obtengo la hora del dia y le doy formato
    private final SimpleDateFormat df = new SimpleDateFormat("H");
    // booleano para hacer refreshTTS solo para el 2do TTS
    private final boolean primerTTS = true;


    private boolean doubleBackToExitPressedOnce = false;
    private boolean click = true;
    //String que carga el texto para el TTS
    private String Oracion;

    private int versionCode, ultimaVersion;
    private long primeraConexion;
    public static boolean cerrarSession = false;// use this variable to notify when the session is closed


    //Indica el proximo lugar en la seleccion
    private int CantClicks;


    /* Client used to interact with Google APIs. */

    private Picto Agregar;

    //InputStream
    private FileInputStream pictos, grupos, frasesGuardadas;



    private long networkTS;


    // Editar picto
    private boolean editarPicto;

    //Bool que indica si se est&aacute usando la agenda o no
    private boolean boolAgenda;

    //Idioma de la base de Datos
    private static String idioma;


    private long Actual;
    //Handler para animar el boton de myTTS cuando no habla por cierto tiempo
    private final Handler handlerHablar = new Handler();


    // JSONObject que usamoss
    JSONObject pictoPadre, opcion1, opcion2, opcion3, opcion4, onLongOpcion;

    public Json json;

    // cuanta de mas pictos
    private int cuentaMasPictos;
    private int placeTypeActual;
    private int placeActual;


    private boolean vibrar = false;

    //Firebase Referencias
    private DatabaseReference mDatabaseBackupPictos, mDatabaseBackupGrupos, mDatabaseBackupFrases, mDatabaseBackup, mDatabaseBackupFotos;
    private StorageReference mStorageBackupPictos, mStorageBackupGrupos, mStorageBackupFrases;
    private StorageReference mStorageRef, mStorageRefPictos, mStorageRefFrases, mStorageRefGrupos, mStorageBackupFotos;
    private DatabaseReference mDatabase, mPictosDatabaseRef, mFrasesDatabaseRef, mGruposDatabaseRef;

    public String dateStr;

    private LottieAnimationView animationView;

    private textToSpeech myTTS;
    private final boolean isChristmas = false;
    private ConnectionDetector connectionDetector;
    private SubirBackupFirebase mFirebaseBackup;
    SubirArchivosFirebase subirArchivos;
    private traducirTexto traducirfrase;
    private Toolbar toolbar;


    String timeStamp;
    private int mCheckDescarga, mCheckDatos;
    private ConstraintLayout constraintBotonera;
    private LocationRequest mLastLocationRequest;
    private Location mLastLocation;
    private BarridoPantalla barridoPantalla;
    private ImageView cornerImageView;
    private VelocityTracker mVelocityTracker;

    private AnalyticsFirebase analitycsFirebase;
    private FirebaseUtils firebaseUtils;

    private TextView toolbarPlace;
    private final int ultima_Posicion_Barrido = 0;
    private HandlerComunicationClass cargarOpcionesEnFalla;
    private TraducirFrase traducirFrase;
    private PlacesImplementation placesImplementation;

    private Progress_dialog_options firebaseDialog;

    private ImageButton btn_share;

    private InmersiveMode inmersiveMode;
    //Handler para animar el Hablar cuando pasa cierto tiempo
    private final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            AnimarHablar();
            handlerHablar.postDelayed(this, 4000);
        }
    };

    public static FirebaseSuccessListener msuccesListener() {
        return mFirebaseSuccessListener;
    }

    private Gesture gesture;


    @Override
    public void onFotoDescargada(int fotosDescargadas) {

    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {
        if (descargado) {
            descargado = false;
            if(firebaseDialog.isShowing())
                firebaseDialog.destruirDialogo();
            try {
                json.setmJSONArrayPictosSugeridos(json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE));
            } catch (JSONException | FiveMbException e) {
                e.printStackTrace();
            } /*
                WeeklyBackup wb = new WeeklyBackup(this);
                wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);*/

        }
    }

    private void explicarSugerencias() {


        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);

            versionCode = pInfo.versionCode;
            if (versionCode >= 54) {

                new NewDialogsOTTAA(this).showSugerenciasDialog(getString(R.string.string_sugerencias_dialog));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {
        ConnectionDetector mConnectionDetector = new ConnectionDetector(getApplicationContext());

        mCheckDatos += datosEncontrados;
        Log.d(TAG, "onDatosEncontrados: " + mCheckDatos);
        if (mCheckDatos == Constants.TODO_ENCONTRADO) {
            mCheckDatos = 0;
            try {
                if (!this.isFinishing() && ConnectionDetector.isNetworkAvailable(this)  && json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS).length() > 0) {

                    mBajarJsonFirebase.setInterfaz(this);
                    if (firebaseDialog !=null) {
                        firebaseDialog.setTitle(getApplicationContext().getResources().getString(R.string.edit_sync)).setMessage(getApplicationContext().getResources().getString(R.string.edit_sync_pict)).setCancelable(false);
                        firebaseDialog.mostrarDialogo();
                    }
                    mBajarJsonFirebase.descargarGruposyPictosNuevos();
                }
            } catch (JSONException | FiveMbException e) {
                e.printStackTrace();
            } /*
                WeeklyBackup wb = new WeeklyBackup(this);
                wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);*/
        } else {
            Log.e(TAG, "onDatosEncontrados: No existen datos");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        boolean status_bar = intent.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        sharedPrefsDefault =PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPrefsDefault.getBoolean("skillHand",false))
        setContentView(R.layout.activity_main_rigth);
        else
            setContentView(R.layout.activity_main);

        //Facebook analytics
        setAutoLogAppEventsEnabled(true);
        if(BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
        }

        user =new User(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        mDatabase = firebaseUtils.getmDatabase();

        //  mDatabase.keepSynced(true);
        cornerImageView = findViewById(R.id.cornerImageViewLeft);
        analitycsFirebase=new AnalyticsFirebase(this);

        //Implemento el manejador de preferencias

        //TODO modificar esto desde firebase para habilitar el modo moderador
        sharedPrefsDefault.edit().putBoolean("esmoderador", false).apply();
        new ConfigurarIdioma(getApplicationContext(), sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
        sharedPrefs = getSharedPreferences(sharedPrefsDefault.getString(getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);
        barridoSwitch = sharedPrefsDefault.getBoolean("tipo_barrido", false);

        Json.getInstance().setmContext(this);
        json = Json.getInstance();
        Log.d(TAG, "hashCode: " + json.hashCode());

        timeStamp = getTimeStamp();
        firebaseDialog = new Progress_dialog_options(this);
        function_scroll=new ScrollFunctionMainActivity(this,this);


        if (user.getmAuth().getCurrentUser() != null) {
            subirArchivos = new SubirArchivosFirebase(this);

            /**
             * Referencias Storage grupos pictos frases
             * */
            //     mStorageRefPictos = subirArchivos.getmStorageRef(mAuth, "pictos");
            //    mStorageRefGrupos = subirArchivos.getmStorageRef(mAuth, "grupos");
            //   mStorageRefFrases = subirArchivos.getmStorageRef(mAuth, "frases");
            /**
             * Referencias database grupos pictos frases
             */
            mPictosDatabaseRef = subirArchivos.getmDatabase(user.getmAuth(), "Pictos");
            mFrasesDatabaseRef = subirArchivos.getmDatabase(user.getmAuth(), "Frases");
            mGruposDatabaseRef = subirArchivos.getmDatabase(user.getmAuth(), "Grupos");

            //    mStorageBackupGrupos = mStorageRef.child("Archivos_Usuarios").child("Backups").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("grupos_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en") + "." + "txt");
            //     mStorageBackupPictos = mStorageRef.child("Archivos_Usuarios").child("Backups").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("pictos_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en") + "." + "txt");
            //   mStorageBackupFrases = mStorageRef.child("Archivos_Usuarios").child("Backups").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("frases_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en") + "." + "txt");
            //  mStorageBackupFotos = mStorageRef.child("Archivos_Usuarios").child("Backups").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("fotos_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en") + "." + "txt");

            mBajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, user.getmAuth(), getApplicationContext());
//            File rootPath = new File(this.getCacheDir(), "Archivos_OTTAA");
//            mBajarJsonFirebase.bajarDescripcionJuegos(sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"),rootPath);

            //chequearArchivoSugerencias();
            /**Referencias database grupos pictos frases BACKUP*/

//            mDatabaseBackup = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup");
//            mDatabaseBackupPictos = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").child(timeStamp).child("Backup_pictos_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
//            mDatabaseBackupGrupos = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").child(timeStamp).child("Backup_grupos_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
//            mDatabaseBackupFrases = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").child(timeStamp).child("Backup_frases_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
//            mDatabaseBackupFotos = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").child(timeStamp).child("Backup_fotos_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));

            /**Referencias database grupos pictos frases Archivos_Usuario*/
            mPictosDatabaseRef =firebaseUtils.getmDatabase().child("Pictos").child(user.getmAuth().getCurrentUser().getUid()).child("URL_pictos_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
            mFrasesDatabaseRef = firebaseUtils.getmDatabase().child("Frases").child(user.getmAuth().getCurrentUser().getUid()).child("URL_frases_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
            mGruposDatabaseRef = firebaseUtils.getmDatabase().child("Grupos").child(user.getmAuth().getCurrentUser().getUid()).child("URL_grupos_" + sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma), "en"));
            consultarPago();

            subirArchivos.subirFotosOffline();

        } else {
            Log.e(TAG, "onCreate: CurrentUser == null");
            Intent mainIntent = new Intent().setClass(
                    Principal.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }

        //Instancia clase backup Firebase
        mFirebaseBackup = new SubirBackupFirebase(this);
        setPrimerBackupTimeLocal();
        guardarBackupLocal();


        handlerHablar.postDelayed(animarHablar, 4000);



        if(!BuildConfig.DEBUG) {
            RateThisApp.Config config = new RateThisApp.Config(10, 20);
            config.setTitle(R.string.my_own_title);
            config.setMessage(R.string.my_own_message);
            config.setYesButtonText(R.string.pref_yes_alert);
            config.setNoButtonText(R.string.my_own_thanks);
            config.setCancelable(false);
            config.setCancelButtonText(R.string.my_own_cancel);
            config.setUrl("https://play.google.com/store/apps/details?id=com.stonefacesoft.ottaa");
            RateThisApp.init(config);

            // Monitor launch times and interval from installation
            RateThisApp.onCreate(this);
            // If the condition is satisfied, "Rate this app" dialog will be shown
            try {
                RateThisApp.showRateDialogIfNeeded(this);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: Error RateThisApp");
            }
        }

        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setOnClickListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return windowInsets;
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        constraintBotonera = findViewById(R.id.constraintRightButtons);
        inmersiveMode=new InmersiveMode(this);




        //Seteo en false salvo que sea la primera vez q se usa
        TutoFlag = sharedPrefs.getBoolean("PrimerUso", true);
        historial=new Historial(this,json);
        //Implemento el vibrador
        Vibrator vibe = (Vibrator) Principal.this.getSystemService(Context.VIBRATOR_SERVICE);

        //Inicializar TTS
        try {
            if (resolveIntent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)) {
                Intent checkTTSIntent = new Intent();
                checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(checkTTSIntent, IntentCode.MY_DATA_CHECK_CODE.getCode());
            } else {
                Alert();
            }
        } catch (Exception e) {
            CrashlyticsUtils.getInstance().getCrashlytics().recordException(e);
            Alert();
        }
//        layout=(LinearLayout) findViewById(R.id.linearLayoutp1);

        //Implementacion de los botones
        Seleccion1 = findViewById(R.id.Seleccion1);
        Seleccion1.setOnClickListener(this);
        Seleccion1.setOnLongClickListener(this);

        Seleccion2 = findViewById(R.id.Seleccion2);
        Seleccion2.setOnClickListener(this);
        Seleccion2.setOnLongClickListener(this);

        Seleccion3 = findViewById(R.id.Seleccion3);
        Seleccion3.setOnClickListener(this);
        Seleccion3.setOnLongClickListener(this);

        Seleccion4 = findViewById(R.id.Seleccion4);
        Seleccion4.setOnClickListener(this);
        Seleccion4.setOnLongClickListener(this);

        Seleccion5 = findViewById(R.id.Seleccion5);
        Seleccion5.setOnClickListener(this);
        Seleccion5.setOnLongClickListener(this);

        Seleccion6 = findViewById(R.id.Seleccion6);
        Seleccion6.setOnClickListener(this);
        Seleccion6.setOnLongClickListener(this);

        Seleccion7 = findViewById(R.id.Seleccion7);
        Seleccion7.setOnClickListener(this);
        Seleccion7.setOnLongClickListener(this);

        Seleccion8 = findViewById(R.id.Seleccion8);
        Seleccion8.setOnClickListener(this);
        Seleccion8.setOnLongClickListener(this);

        Seleccion9 = findViewById(R.id.Seleccion9);
        Seleccion9.setOnClickListener(this);
        Seleccion9.setOnLongClickListener(this);

        Seleccion10 = findViewById(R.id.Seleccion10);
        Seleccion10.setOnClickListener(this);
        Seleccion10.setOnLongClickListener(this);

        ImageButton borrar = findViewById(R.id.btn_borrar);
        ImageButton botonFavoritos = findViewById(R.id.btnFavoritos);
        botonFavoritos.setOnClickListener(this);
        borrar.setOnClickListener(this);
        borrar.setOnLongClickListener(this);
        MasPictos = findViewById(R.id.btnMasPictos);

        MasPictos.setOnClickListener(this);
        MasPictos.setOnLongClickListener(this);
        TodosLosPictos = findViewById(R.id.btnTodosLosPictos);
        TodosLosPictos.setOnClickListener(this);
        TodosLosPictos.setOnLongClickListener(this);

        ResetButton=findViewById(R.id.action_reiniciar);
        ResetButton.setOnClickListener(this);
        ResetButton.setOnLongClickListener(this);

        btn_share=findViewById(R.id.action_share);
        btn_share.setOnClickListener(this);
        btn_share.setOnLongClickListener(this);

        btnBarrido = findViewById(R.id.btnBarrido);
        btnBarrido.setOnClickListener(this);
        btnBarrido.setOnTouchListener(this);
        btnBarrido.setVisibility(View.GONE);

        talk=findViewById(R.id.btnTalk);
        talk.setOnClickListener(this);

        AjustarAncho(R.id.Seleccion1);
        AjustarAncho(R.id.Seleccion2);
        AjustarAncho(R.id.Seleccion3);
        AjustarAncho(R.id.Seleccion4);
        AjustarAncho(R.id.Seleccion5);
        AjustarAncho(R.id.Seleccion6);
        AjustarAncho(R.id.Seleccion7);
        AjustarAncho(R.id.Seleccion8);
        AjustarAncho(R.id.Seleccion9);
        AjustarAncho(R.id.Seleccion10);
        button = Seleccion1;

        Opcion1 = findViewById(R.id.Option1);
        Opcion1.setOnClickListener(this);
        Opcion1.setOnLongClickListener(this);
        Opcion2 = findViewById(R.id.Option2);
        Opcion2.setOnClickListener(this);
        Opcion2.setOnLongClickListener(this);
        Opcion3 = findViewById(R.id.Option3);
        Opcion3.setOnClickListener(this);
        Opcion3.setOnLongClickListener(this);
        Opcion4 = findViewById(R.id.Option4);
        Opcion4.setOnClickListener(this);
        Opcion4.setOnLongClickListener(this);


        ImageButton image1 = new ImageButton(getContext());
        image1.setVisibility(View.VISIBLE);
        image1.setPadding(20, 20, 20, 20);
        image1.setImageDrawable(getResources().getDrawable(R.drawable.antipatico));
        //Manda el mail con las estadisticas de uso todos los vierness
        try {
            indicadores = new Indicadores(getContext());
        } catch (FiveMbException e) {/*
            WeeklyBackup wb = new WeeklyBackup(this);
            wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);*/

        }
        //Vibraicion inicial
        long[] patron = {0, 10, 20, 15, 20, 20};
        vibe.vibrate(patron, -1);

        //Inicializo hora del sistema
        int tiempoFormateado = Integer.parseInt(df.format(SystemTime.getTime()));

        Agregar = new Picto(0, getResources().getDrawable(R.drawable.agregar_picto_transp), "", "", R.color.Black);

        // Se fija si esta habilitado o desabilitado editPicto
        editarPicto = sharedPrefsDefault.getBoolean(getString(R.string.str_editar_picto), true);

        ////////////////////////////////////////    IDIOMA    //////////////////////////////////////
        // Se setea el idioma en base al idioma del dispositivo, por defecto es ingles
        idioma = sharedPrefsDefault.getString(getString(R.string.str_idioma), "en");
        ////////////////////////////////////////////////////////////////////////////////////////////


        myTTS = new textToSpeech(getApplicationContext());

        if (json.getmJSONArrayTodosLosPictos() != null && json.getmJSONArrayTodosLosPictos().length() > 0) {
            try {
                pictoPadre = json.getmJSONArrayTodosLosPictos().getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        cuentaMasPictos = 0;
        placeTypeActual = 0;
        placeActual = 0;

        //Aca chequeamos si el usuario quiere utilizar la agenda para modificar sus pictos
        // Se fija si esta habilitado o desabilitado editPicto
        //barridoPantalla = new BarridoPantalla(getContext(), Principal.this);


        boolean primerUso = sharedPrefs.getBoolean("PrimerUso", false);

        CargarOpciones(json, pictoPadre, cuentaMasPictos);   // y despues cargamos las opciones con el orden correspondiente
        nlg = new NLG(getApplicationContext());
        ResetSeleccion();
        mute = sharedPrefsDefault.getBoolean("mBoolMute", true);
        sharedPrefsDefault.edit().putBoolean("usuario logueado", true).apply();
        animationView = findViewById(R.id.lottieAnimationView);
        //gestionarBitmap = new GestionarBitmap(getContext());

        heigth = Seleccion1.getHeight();
        width = Seleccion1.getWidth();

        if (Build.VERSION.SDK_INT >= 21) {
            connectionDetector = new ConnectionDetector(getApplicationContext());
            isConnected = connectionDetector.isConnectedToInternet();
        }
        if (subirArchivos != null) {
            subirArchivos.setInterfaz(this);
        }

        if (user.getmAuth().getCurrentUser() != null && subirArchivos != null) {
            subirArchivos.userDataExists(subirArchivos.getmDatabase(user.getmAuth(), "Pictos"), subirArchivos.getmDatabase(user.getmAuth(), "Grupos"), subirArchivos.getmDatabase(user.getmAuth(), "Frases"));
        }

        Opcion1_clicker=new timer_pictogram_clicker(this);
        Opcion2_clicker=new timer_pictogram_clicker(this);
        Opcion3_clicker=new timer_pictogram_clicker(this);
        Opcion4_clicker=new timer_pictogram_clicker(this);

        iniciarBarrido();
        //Instancia de Ubicacion
        if (sharedPrefsDefault.getBoolean(getString(R.string.bool_ubicacion),false)) {
            placesImplementation=new PlacesImplementation(this);
            placesImplementation.iniciarClientePlaces();
            placesImplementation.locationRequest();

        }
        cargarOpcionesEnFalla = new HandlerComunicationClass(this);
        //dialog.dismiss();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                Log.d(TAG, "onComplete: InstanceID" + FirebaseInstanceId.getInstance().getId());
            }
        });
//        if (!sharedPrefsDefault.getBoolean("configurarPrediccion", false) && !sharedPrefs.getBoolean("PrimerUso", true)) {
//            new NewDialogsOTTAA(this).showSettingsDialog();
//        }
        if(barridoPantalla.isBarridoActivado())
            editarPicto=false;
        try{
        if(!sharedPrefsDefault.getBoolean("skillHand",false)&&sharedPrefsDefault.getInt("showMenu",3)>0){
            drawerLayout.open();
            int value=sharedPrefsDefault.getInt("showMenu",4);
            value--;
            sharedPrefsDefault.edit().putInt("showMenu",value).apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.close();
                }
             },5000);
             }
            }catch (Exception ex){

        }
        gesture=new Gesture(drawerLayout);
        if (TutoFlag) {
            sharedPrefs.edit().putBoolean("PrimerUso",false).apply();
            startActivity(new Intent(this,Viewpager_tutorial.class));
        }
        navigationControls=new PrincipalControls(this);

    }


    /**
     * Se fija el estado de la variable TutoFlag
     *
     * @return TutoFlag
     */
    public boolean isTutoFlag() {
        return TutoFlag;
    }


    public void AlertCheckPlayService() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Principal.this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setMessage(getResources().getString(R.string.pref_error_312));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                String url = "https://play.google.com/store/apps/details?id=com.google.android.gms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }


    @Override
    public void onDescargaCompleta(int termino) {

        mCheckDescarga += termino;
        Log.d(TAG, "onDescargaCompleta: " + mCheckDescarga);

        if (mCheckDescarga == Constants.TODO_DESCARGADO) {
            mCheckDescarga = 0;
            try {
                json.refreshJsonArrays();
            } catch (JSONException e) {
                Log.e(TAG, "onDescargaCompleta: Error al refrescar los pictos" + e.toString());
            } catch (FiveMbException e) {
                e.printStackTrace();
                /*
                WeeklyBackup wb = new WeeklyBackup(this);
                wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);
                */
            }
            if (firebaseDialog !=null){
               firebaseDialog.destruirDialogo();
            }

        }

    }

    /////////////////////////////////////////////////////////////////////////////////////

    private boolean resolveIntent(String action) {
        PackageManager pm = getPackageManager();
        Intent installIntent = new Intent();
        installIntent.setAction(action);
        ResolveInfo resolveInfo = pm.resolveActivity(installIntent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo != null;
    }

    /**
     * Crea una dialogo con el mensaje de error deseado
     */
    public void Alert() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Principal.this);
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



    public static String getIdioma() {
        return idioma;
    }

    //////////////////////////////  Barrido Inicio  ////////////////////////////////////////////

    ///////////////////////// Funcion para asignar el ancho a seleccion /////////////////
//    TODO sacar esto
    private void AjustarAncho(int Rid) {
        ImageButton view_instance = findViewById(Rid);
        android.view.ViewGroup.LayoutParams params = view_instance.getLayoutParams();

        params.width = view_instance.getLayoutParams().height;
        Log.d(TAG, "AjustarAncho: " + "Ancho " + params.width + " Alto " + params.height);
        view_instance.setLayoutParams(params);
        button = new ImageButton(getApplicationContext());
        button.setLayoutParams(params);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_edit:
                analitycsFirebase.customEvents("Touch","Principal","Edit Pictogram");
                if(sharedPrefsDefault.getInt("premium",0)==1){
                if (onLongOpcion == null) {
                    return true;
                }
                Intent intent = new Intent(Principal.this, Edit_Picto_Visual.class);
                intent.putExtra("principal", true);
                try {
                    intent.putExtra("PictoID", json.getId(onLongOpcion));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("Texto", json.getNombre(onLongOpcion));
                intent.putExtra("Sel", 1);
                intent.putExtra("Nombre", json.getNombre(onLongOpcion));
                intent.putExtra("Color", cargarColor(json.getTipo(onLongOpcion)));
                myTTS.hablar(getString(R.string.editar_pictogram));

                //Abrir pantalla de edicion de pictograma
                isSettings = false;
                startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
                }
                else{
                    Intent i=new Intent(Principal.this,LicenciaExpirada.class);
                    startActivity(i);
                }
                return true;
            case R.id.item_delete:
                analitycsFirebase.customEvents("Touch","Principal","Delete Pictogram");
                try {
                    if(onLongOpcion!=null)
                    AlertBorrar(json.getId(onLongOpcion));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;

        }
        return false;
    }


    private void setPrimerBackupTimeLocal() {
        //Nos fijamos si el permiso de escribir en el storage esta dado para hacer el backup local.
        BackupPictograms backupPictograms=new BackupPictograms(this,Constants.ARCHIVO_PICTOS);
        BackupGroups backupGroups=new BackupGroups(this,Constants.ARCHIVO_GRUPOS);
        BackupPhrases backupPhrases=new BackupPhrases(this,Constants.ARCHIVO_FRASES);
        BackupPhotos backupPhotos=new BackupPhotos(this,Constants.ARCHIVO_PICTOS);

        backupPictograms.prepareFirstLocalBackup(timeStamp);
        backupGroups.prepareFirstLocalBackup(timeStamp);
        backupPhrases.prepareFirstLocalBackup(timeStamp);
        backupPhotos.prepareFirstLocalBackup(timeStamp);
    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (sharedPrefsDefault.getBoolean("bool_ubicacion", false)) {
            startLocationUpdate();
            //Reset();
        }
    }

    private void initLocationRequest() {
        mLastLocationRequest = new LocationRequest();
        mLastLocationRequest.setSmallestDisplacement(10);
        mLastLocationRequest.setInterval(10000); // Update location every 1 minute
        mLastLocationRequest.setFastestInterval(10000);
        mLastLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    private void startLocationUpdate() {
        initLocationRequest();
        if(placesImplementation!=null&&sharedPrefsDefault.getBoolean(getString(R.string.bool_ubicacion),false)){
            if(!placesImplementation.isStarted())
                placesImplementation.iniciarClientePlaces();
            else
                placesImplementation.locationRequest();

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    private void setPrimerBackupTimeFirebase() {
/*
        mDatabaseBackup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("FirebaseBackup", "Ya hay un ultimoBackup");
                } else {

                    mFirebaseBackup.subirGruposFirebase(mDatabaseBackupGrupos, mStorageBackupGrupos);
                    mFirebaseBackup.subirPictosFirebase(mDatabaseBackupPictos, mStorageBackupPictos);
                    mFirebaseBackup.subirFrasesFirebase(mDatabaseBackupFrases, mStorageBackupFrases);
                    mDatabaseBackup.child("UltimoBackup").setValue(timeStamp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


    private String getTimeStamp() {
        Long tslong = System.currentTimeMillis() / 1000;
        return tslong.toString();
    }

    private void guardarBackupLocal() {
        BackupPictograms backupPictograms=new BackupPictograms(this,Constants.ARCHIVO_PICTOS);
        BackupGroups backupGroups=new BackupGroups(this,Constants.ARCHIVO_GRUPOS);
        BackupPhrases backupPhrases=new BackupPhrases(this,Constants.ARCHIVO_FRASES);
        BackupPhotos backupPhotos=new BackupPhotos(this,Constants.ARCHIVO_PICTOS);

        backupPictograms.preparelocalBackup(timeStamp);
        backupGroups.preparelocalBackup(timeStamp);
        backupPhrases.preparelocalBackup(timeStamp);
        backupPhotos.preparelocalBackup(timeStamp);
    }

    private void subirBackupFirebase() {

        if (mDatabaseBackup != null && user.getmAuth() != null) {
            mDatabaseBackup.child("UltimoBackup").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot UltimoBackupDataSnapshot) {

                    final DatabaseReference ultimaConRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(Objects.requireNonNull(user.getmAuth().getCurrentUser()).getUid()).child("UConexion");
                    ultimaConRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot UltimaConexionDataSnapshot) {

                            if (UltimaConexionDataSnapshot != null && UltimoBackupDataSnapshot != null) {

                                try {

                                    long ultimoBackup = java.lang.Long.parseLong(UltimoBackupDataSnapshot.getValue().toString());
                                    long ultimaConexion = java.lang.Long.parseLong(UltimaConexionDataSnapshot.getValue().toString());
                                    Log.d(TAG, "onDataChange: " + "UltimoBackup: " + ultimoBackup + "UltimaConexion: " + ultimaConexion);

                                    if (ultimoBackup + Constants.UNA_SEMANA < ultimaConexion) {

                                        mFirebaseBackup.subirGruposFirebase(mDatabaseBackupGrupos, mStorageBackupGrupos);
                                        mFirebaseBackup.subirPictosFirebase(mDatabaseBackupPictos, mStorageBackupPictos);
                                        mFirebaseBackup.subirFrasesFirebase(mDatabaseBackupFrases, mStorageBackupFrases);
                                        mFirebaseBackup.subirFotosBackupFirebase(mDatabaseBackupFotos, mStorageBackupFotos);
                                        mDatabaseBackup.child("UltimoBackup").setValue(timeStamp);

                                    }
                                } catch (NullPointerException e) {
                                    Log.e(TAG, "onDataChange: Firebase Backup NPE" + e.getMessage());
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.e(TAG, "subirBackupFirebase: " + "error al subir archivos");
        }
    }


    /**
     * Pone todos los botones del color original (Fondo Boton), o sea los inicializa para dsp cambiar el color del boton adecuado
     */

    public View vista(int v) {
        return findViewById(v);
    }

//////////////////////////////  Barrido Fin  ////////////////////////////////////////////




    /**
     * Traforma una fecha que esta en formato String en una fecha que esta en formato java.util.Date
     *
     * @param s (String)
     * @return
     */
    private java.util.Date stringToDate(String s) {
        String aux;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            aux = s.replaceAll("\\\\", "/");
            return simpleDateFormat.parse(aux);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        user.connectClient();

    }

    @Override
    protected void onResume() {

        CargarJson();
        Log.d(TAG, "onResume: idioma" + getApplication().getResources().getConfiguration().locale.toString());
        super.onResume();
        if (firebaseDialog != null) {
            firebaseDialog.destruirDialogo();
        }
        if(placesImplementation!=null){
           placesImplementation.locationRequest();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CargarJson();
        startLocationUpdate();

    }

    @Override
    protected void onPause() {
        if (json.getmJSONArrayTodosLosPictos().length() > 0)
            if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                Log.e(TAG, "onPause: Error al guardar el json");
            }
        if (firebaseDialog != null) {
            firebaseDialog.destruirDialogo();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (!json.getFallJson() && user.getmAuth().getCurrentUser() != null) {
            if (isSettings) {
                subirArchivos.new getTiempoGoogle().execute();
            }
        }

        if (json.getmJSONArrayTodosLosPictos().length() > 0)
            if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                Log.e(TAG, "onStop: Error al guardar el json");
            }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(firebaseDialog !=null){
            firebaseDialog.destruirDialogo();
        }

        super.onDestroy();

    }


    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1; )
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }


    //TODO re hacer la presnetacion personalizada

    /**
     * Implementa este metodo de TTS interface, cuando se inicializa el TTS lo setea por defecto
     * @param initStatus
     */


    /**
     * Crea el menu del ActionBar
     *
     * @param menu (Menu)
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        locationItem = menu.getItem(0);

        if (mute) {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
        } else {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
        }


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Reset();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 500);
    }

    /**
     * Carga el texto del picto seleccionado en la oracion final
     *
     * @param jsonObject (Picto)
     */


    void CargarOracion(JSONObject jsonObject) {
        Oracion = Oracion + json.getNombre(jsonObject) + " ";
        Log.d(TAG, "CargarOracion: " + Oracion);
    }

    /**
     * Carga el picto seleccionado en la barra de seleccion
     *
     * @param opcion
     */
    void CargarSeleccion(JSONObject opcion) {
        GlideAttatcher attatcher=new GlideAttatcher(this);
        Pictogram pictogram=new Pictogram(opcion,idioma);
        switch (CantClicks) {
            case 0:
                loadDrawable(attatcher,pictogram,Seleccion1);
                Seleccion1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion2.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 1:
                loadDrawable(attatcher,pictogram,Seleccion2);
                Seleccion2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion3.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 2:
                loadDrawable(attatcher,pictogram,Seleccion3);
                Seleccion3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion4.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 3:
                loadDrawable(attatcher,pictogram,Seleccion4);
                Seleccion4.setImageDrawable(json.getIcono(opcion));
                Seleccion4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion5.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 4:
                loadDrawable(attatcher,pictogram,Seleccion5);
                Seleccion5.setImageDrawable(json.getIcono(opcion));
                Seleccion5.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion6.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 5:
                loadDrawable(attatcher,pictogram,Seleccion6);
                Seleccion6.setImageDrawable(json.getIcono(opcion));
                Seleccion6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion7.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 6:
                loadDrawable(attatcher,pictogram,Seleccion7);
                Seleccion7.setImageDrawable(json.getIcono(opcion));
                Seleccion7.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion8.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 7:
                loadDrawable(attatcher,pictogram,Seleccion8);
                Seleccion8.setImageDrawable(json.getIcono(opcion));
                Seleccion8.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion9.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
                break;
            case 8:
                loadDrawable(attatcher,pictogram,Seleccion9);
                Seleccion9.setImageDrawable(json.getIcono(opcion));
                Seleccion9.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                Seleccion10.setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
            case 9:
                loadDrawable(attatcher,pictogram,Seleccion10);
                Seleccion10.setImageDrawable(json.getIcono(opcion));
                Seleccion10.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
                break;
        }

    }


    /**
     * Inicializa la barra de seleccion poniendo la imagen por defecto
     */
    private void inicializar_seleccion() {
        Seleccion1.setImageDrawable(Agregar.getIcono());
        Seleccion1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion2.setImageDrawable(Agregar.getIcono());
        Seleccion2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion3.setImageDrawable(Agregar.getIcono());
        Seleccion3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion4.setImageDrawable(Agregar.getIcono());
        Seleccion4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion5.setImageDrawable(Agregar.getIcono());
        Seleccion5.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion6.setImageDrawable(Agregar.getIcono());
        Seleccion6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion7.setImageDrawable(Agregar.getIcono());
        Seleccion7.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion8.setImageDrawable(Agregar.getIcono());
        Seleccion8.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion9.setImageDrawable(Agregar.getIcono());
        Seleccion9.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        Seleccion10.setImageDrawable(Agregar.getIcono());
        Seleccion10.startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
    }


    @SuppressLint("Range")
    private void CargarOpciones(Json json, JSONObject padre, int cuentaMasPictos) {


        Animation alphaAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_show);

        Log.d(TAG, "CargarOpciones: pictoPadre" + json.getNombre(padre));
        Log.d(TAG, "CargarOpciones: Padre" + padre);
        //ESta linea se encarga de bajar los archivos en cache
        File rootPath = new File(this.getCacheDir(), "Archivos_OTTAA");
        final File pictosUsuarioFile = new File(rootPath, "pictos.txt");
        Opcion1.setEnabled(true);
        Opcion2.setEnabled(true);
        Opcion3.setEnabled(true);
        Opcion4.setEnabled(true);


        if (!json.getFallJson() && json.getmJSONArrayTodosLosPictos() != null) {

            JSONArray opciones = new JSONArray();
            try {
                opciones = json.cargarOpciones(padre,cuentaMasPictos);

            } catch (JSONException e) {
                Log.e(TAG, "CargarOpciones: " + e.toString());

            } catch (FiveMbException e) {
                e.printStackTrace();
                /*
                WeeklyBackup wb = new WeeklyBackup(this);
                wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);
                */
            }

            try {
                if (opciones.getJSONObject(0).getInt("id") != -1) {
                    //            Opcion1.setVisibility(View.VISIBLE);
                    opcion1 = opciones.getJSONObject(0);
                    addOption(opcion1, Opcion1, alphaAnimation);
                } else {
                    opcion1 = null;
                    addOpcionNull(Opcion1, alphaAnimation);
                    //     this.cuentaMasPictos = -1;// linea encargada de indicar que el contador esta en 0
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (opciones.getJSONObject(1).getInt("id") != -1) {
                    Opcion2.setVisibility(View.VISIBLE);
                    opcion2 = opciones.getJSONObject(1);
                    addOption(opcion2, Opcion2, alphaAnimation);

                } else {
                    opcion2 = null;
                    addOpcionNull(Opcion2, alphaAnimation);

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            try {
                if (opciones.getJSONObject(2).getInt("id") != -1) {
                    Opcion3.setVisibility(View.VISIBLE);
                    opcion3 = opciones.getJSONObject(2);
                    addOption(opcion3, Opcion3, alphaAnimation);
                } else {
                    opcion3 = null;
                    addOpcionNull(Opcion3, alphaAnimation);

                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            try {
                if (opciones.getJSONObject(3).getInt("id") != -1) {
                    Opcion4.setVisibility(View.VISIBLE);
                    opcion4 = opciones.getJSONObject(3);
                    addOption(opcion4, Opcion4, alphaAnimation);
                } else {
                    opcion4 = null;
                    addOpcionNull(Opcion4, alphaAnimation);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (json.getCantFallas() < 4) {
            boolean falloPictos=false,falloGrupos=false,falloFrases=false;
            if (json.getmJSONArrayTodosLosPictos().length() == 0 || json.getmJSONArrayTodosLosPictos() == null) {
                //  json.initJsonArrays();
                falloPictos=true;
                Log.e(TAG, "CargarOpciones: " + "fallo Leer Json 1");
                //     CargarJson();
            } else if(json.getmJSONArrayTodosLosGrupos().length()==0||json.getmJSONArrayTodosLosGrupos()==null) {
                falloGrupos=true;
                Log.e(TAG, "CargarOpciones: " + "fallo Leer grupos 2");
            }else if(json.getmJSONArrayTodasLasFrases().length()==0||json.getmJSONArrayTodasLasFrases()==null){
                falloFrases=true;
                Log.e(TAG, "CargarOpciones: " + "fallo Leer frases 3");
            }
            if(isFallo(falloPictos,falloGrupos,falloFrases)){
                Log.e("CargarOpciones_error", "Pictos o grupos no bajados correctamente");

                ObservableInteger observableInteger = new ObservableInteger();
                observableInteger.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
                    @Override
                    public void onIntegerChanged(int newValue) {
                        if (observableInteger.get() == 3) {
                            firebaseDialog.destruirDialogo();
                            json.resetearError();
                            CargarJson();

                        }
                    }
                });

                firebaseDialog.setTitle(getApplicationContext().getResources().getString(R.string.edit_sync));
                firebaseDialog.setMessage(getApplicationContext().getResources().getString(R.string.edit_sync_pict));
                firebaseDialog.mostrarDialogo();
                mBajarJsonFirebase.bajarPictos(idioma, rootPath, observableInteger);
                mBajarJsonFirebase.bajarGrupos(idioma, rootPath, observableInteger);
                mBajarJsonFirebase.bajarFrases(idioma, rootPath, observableInteger);
                mBajarJsonFirebase.bajarJuego(idioma,rootPath);
                mBajarJsonFirebase.bajarFrasesFavoritas(idioma,rootPath);
                mBajarJsonFirebase.bajarDescripcionJuegos(idioma,rootPath);
            }

               // json.resetearError();
            Log.e(TAG, "CargarOpciones: " + json.getFallJson());

            String uid = "";

            }
    }

    private boolean isFallo(boolean pictos,boolean grupos,boolean frases){
        if(pictos)
            return pictos;
        if(grupos)
            return grupos;
        if(frases)
            return frases;
        return false;
    }


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

    //Esta funcion vuelve al estado inicial
    private void Reset() {
        pictoPadre = historial.removePictograms(true);
        ResetSeleccion();
        cuentaMasPictos = 0;
        CargarOpciones(json, pictoPadre, cuentaMasPictos);
    }

    private void ResetSeleccion() {
        Log.d(TAG, "ResetSeleccion: ");

        nlgFlag = true;
        Oracion = "";
        int situacionActual = 0;
        CantClicks = 0;
        inicializar_seleccion();
        if(!historial.getListadoPictos().isEmpty()){
            for (int i = 0; i <historial.getListadoPictos().size() ; i++) {
                CargarOracion(historial.getListadoPictos().get(i));
            }
        }
    }

    private void cargarSelec(JSONObject jsonObject) {
        CargarOracion(jsonObject);
        CargarSeleccion(jsonObject);
        CantClicks++;
    }


    private void volver() {

        pictoPadre=historial.removePictograms(false);
        ResetSeleccion();
        cuentaMasPictos = 0;
        for (int i = 0; i <historial.getListadoPictos().size() ; i++) {
            CargarSeleccion(historial.getListadoPictos().get(i));
            CantClicks++;
        }
        CargarOpciones(json, pictoPadre, cuentaMasPictos);




    }

    private void click(JSONObject opcion) {
        Log.d(TAG, "click: ");
        //   verificarDatosActualizados();

        cuentaMasPictos = 0;
        if (opcion == null || pictoPadre == null) {
            Log.d(TAG, "click: Opcion es null");
            return;
        }
      //  Historial.add(opcion);
            historial.addPictograma(opcion);
        try {
            int pos = json.getPosPicto(json.getmJSONArrayTodosLosPictos(), pictoPadre.getInt("id"));
            json.aumentarFrec(pictoPadre, opcion);
            json.getmJSONArrayTodosLosPictos().put(pos, pictoPadre);

            json.guardarJson(Constants.ARCHIVO_PICTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pictoPadre = opcion;
        cargarSelec(pictoPadre);
        CargarOpciones(json, opcion, cuentaMasPictos);
        Log.d(TAG, "click: " + opcion.toString());
    }


    private void cargarMasPictos() {
        cuentaMasPictos++;
        if (cuentaMasPictos > Constants.VUELTAS_CARRETE) {
            cuentaMasPictos = 0;
            /*placeTypeActual++;
            if (placeTypeActual > uv2.getmCantTypes(placeActual))
                placeTypeActual = 0;
            json.setmPlaceType(placeTypeActual);*/
        }
        CargarOpciones(json, pictoPadre, cuentaMasPictos);
    }

    public void AlertBorrar(final int pos) {
        vibrar = false;

        Yes_noDialogs dialogs=new Yes_noDialogs(this);
        dialogs.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogs.setMessage(getResources().getString(R.string.pref_text4_alert));
        dialogs.setCancelable(true);
        dialogs.setOnClick(dialogs.getObject(R.id.yes_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //pregunto el tipo de usuario
                    if (sharedPrefsDefault.getBoolean("esmoderador", false)) {
                        //
                        JSONArray pictosSugeridos = json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE);
                        //pregunto por la posicion y el id
                        json.desvincularJson(pictosSugeridos.getJSONObject(pictoPadre.getInt("id")), pos);
                        //
                        json.setmJSONArrayPictosSugeridos(pictosSugeridos);
                        //guardo las opciones
                        if (!json.guardarJson(Constants.ARCHIVO_PICTOS_DATABASE))
                            Log.e(TAG, "onClick: eliminar: Error al guardar pictos sugeridos");
                        //  CargarOpciones(json, pictoPadre, cuentaMasPictos);
                    }

                } catch (JSONException | FiveMbException e) {
                    e.printStackTrace();
                } /*
                    WeeklyBackup wb = new WeeklyBackup(getParent());
                    wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);*/

                try {

                    json.desvincularJson(pictoPadre, pos);
                    json.setJsonEditado2(json.getmJSONArrayTodosLosPictos(), pictoPadre);
                    if (!json.guardarJson(Constants.ARCHIVO_PICTOS))
                        Log.e(TAG, "onClick: Error al guardar pictos sugeridos");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialogs.cancelarDialogo();
                CargarOpciones(json, pictoPadre, cuentaMasPictos);
            }
        });

        dialogs.setOnClick(dialogs.getObject(R.id.no_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.Option1).clearAnimation();
                findViewById(R.id.Option2).clearAnimation();
                findViewById(R.id.Option3).clearAnimation();
                findViewById(R.id.Option4).clearAnimation();
              dialogs.cancelarDialogo();
            }
        });
        dialogs.mostrarDialogo();



    }



    private void inflatePopup(PopupMenu menu) {
        try {
            Field field = menu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(menu);
            Class<?> cls = Class.forName("androidx.appcompat.view.menu.MenuPopupHelper");
            @SuppressLint("SoonBlockedPrivateApi") Method method = cls.getDeclaredMethod("setForceShowIcon", boolean.class);
            method.setAccessible(true);
            method.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String EjecutarNLG(boolean flag) {
        Log.d(TAG, "EjecutarNLG: flag:" + flag);
        if (flag) {
           Oracion=historial.talkWithtNLG();
           return Oracion;
        } else
            return Oracion;
    }



    public void speak() {


        if (!myTTS.getTTS().isSpeaking()) {
            Log.d(TAG, "speak: Oracion:" + Oracion);
            boolean existe = false;
            handlerHablar.removeCallbacks(animarHablar);
//            CrashlyticsUtils.getInstance().getCrashlytics().log();
//            Answers.getInstance().logCustom(new CustomEvent("Frase Creada")
//                    .putCustomAttribute("Locale", sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));

            if (mute) {
                myTTS.hablar(Oracion, analitycsFirebase);
                try {
                    json.crearFrase(Oracion, historial.getListadoPictos(), Calendar.getInstance().getTimeInMillis());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "speak: Time in millis: " + System.currentTimeMillis() / 1000);
                if (json.getmJSONArrayTodasLasFrases().length() > 0)
                    if (!json.guardarJson(Constants.ARCHIVO_FRASES))
                        Log.e(TAG, "speak: Error al guardar Frases");
                if (sharedPrefsDefault.getBoolean("hablar_borrar", true)) {
                    analitycsFirebase.customEvents("Talk", "Principal", "Talk and Erase");
                    Reset();
                }
            } else {
                myTTS.mostrarAlerta(Oracion, analitycsFirebase);

            }

            handlerHablar.postDelayed(animarHablar, 10000);
            if (user.getmAuth().getCurrentUser() != null) {
                //Al myTTS subimos pictos y frases
                SubirArchivosFirebase subirArchivos = new SubirArchivosFirebase(getApplicationContext());
                subirArchivos.subirPictosFirebase(subirArchivos.getmDatabase(user.getmAuth(), Constants.PICTOS), subirArchivos.getmStorageRef(user.getmAuth(), Constants.PICTOS));
                subirArchivos.subirFrasesFirebase(subirArchivos.getmDatabase(user.getmAuth(), Constants.Frases), subirArchivos.getmStorageRef(user.getmAuth(), Constants.Frases));
            }
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btn_borrar) {
            //Registo que uso un funcion que nos interesa que use
            analitycsFirebase.customEvents("Erase","Principal","Erase all pictograms");
                Reset();
        }
        if (editarPicto) {
            Intent intent = new Intent(Principal.this, Edit_Picto_Visual.class);
            PopupMenu popupMenu;
            switch (v.getId()) {
                case R.id.Option1:
                    onLongOpcion = opcion1;
                    if (v.getAlpha() != (0.65) || sharedPrefsDefault.getBoolean("esmoderador", false)) {
                        popupMenu = new PopupMenu(Principal.this, Opcion1);
                        popupMenu.setOnMenuItemClickListener(Principal.this);
                        popupMenu.inflate(R.menu.popup_menu);
                        if(sharedPrefsDefault.getInt("premium",0)!=1){
                        popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
                        }
                        else
                            popupMenu.getMenu().getItem(0).setEnabled(true);


                        inflatePopup(popupMenu);
                        popupMenu.show();
                    }

                    break;

                case R.id.Option2:
                    onLongOpcion = opcion2;
                    if (v.getAlpha() != (0.65) || sharedPrefsDefault.getBoolean("esmoderador", false)) {
                        popupMenu = new PopupMenu(Principal.this, Opcion2);
                        popupMenu.setOnMenuItemClickListener(Principal.this);
                        popupMenu.inflate(R.menu.popup_menu);
                        if(sharedPrefsDefault.getInt("premium",0)!=1){
                            popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
                        }
                        else
                            popupMenu.getMenu().getItem(0).setEnabled(true);
                        inflatePopup(popupMenu);
                        popupMenu.show();
                    }
                    break;

                case R.id.Option3:
                    onLongOpcion = opcion3;
                    if (v.getAlpha() != (0.65) || sharedPrefsDefault.getBoolean("esmoderador", false)) {
                        popupMenu = new PopupMenu(Principal.this, Opcion3);
                        popupMenu.setOnMenuItemClickListener(Principal.this);
                        popupMenu.inflate(R.menu.popup_menu);
                        if(sharedPrefsDefault.getInt("premium",0)!=1){
                            popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
                        }
                        else
                            popupMenu.getMenu().getItem(0).setEnabled(true);
                        inflatePopup(popupMenu);
                        popupMenu.show();
                    }
                    break;

                case R.id.Option4:
                    if (v.getAlpha() != (0.65) || sharedPrefsDefault.getBoolean("esmoderador", false)) {
                        onLongOpcion = opcion4;
                        popupMenu = new PopupMenu(Principal.this, Opcion4);
                        popupMenu.setOnMenuItemClickListener(Principal.this);
                        popupMenu.inflate(R.menu.popup_menu);
                        if(sharedPrefsDefault.getInt("premium",0)!=1){
                            popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
                        }
                        else
                            popupMenu.getMenu().getItem(0).setEnabled(true);
                        inflatePopup(popupMenu);
                        popupMenu.show();
                    }
                    break;


                default:

                    onClick(v);
                    break;
            }
        } else {

            onClick(v);
        }
        return true;
    }

    public Context getContext() {
        return this;
    }

    //Bandera global del tutorial
    //TODO hacer que en version tablet la letra sea mas grande
    private boolean TutoFlag;

    private void AnimarHablar() {
        switch (CantClicks) {
            case 1:
                Seleccion2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 2:
                Seleccion3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 3:
                Seleccion4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 4:
                Seleccion5.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 5:
                Seleccion6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 6:
                Seleccion7.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
        }
    }


    public void hablarModoExperimental() {
        if (sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {

            //Registo que uso un funcion que nos interesa que use
            analitycsFirebase.customEvents("Talk","Principal","Phrase With NLG");
            nlgFlag = true;


            Oracion = EjecutarNLG(nlgFlag);



            if (!sharedPrefsDefault.getString(getString(R.string.str_idioma), "en").equals("en")) {
                traducirFrase = new TraducirFrase(this, sharedPrefsDefault, animationView, getApplicationContext(), Oracion);
                traducirFrase.setOracion(Oracion);
                traducirFrase.execute();
            } else {
                speak();
            }


        } else {
            //Registo que uso un funcion que nos interesa que use
            analitycsFirebase.customEvents("Talk","Principal","Phrase without  NLG");
            speak();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //esto soluciona el error que salta en el nokia 8 por que si no elimina la vista
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void takeKeyEvents(boolean get) {

        super.takeKeyEvents(get);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: " + keyCode);
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

        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onClick(View v) {
        View vista=findViewById(v.getId());
        Log.d(TAG, "onClick: " + vista.getId());

        if (barridoPantalla.isBarridoActivado() && !barridoPantalla.isAvanzarYAceptar()) {

            switch (barridoPantalla.getPosicionBarrido()) {
                case 0:

                    click(opcion1);
                    break;
                case 1:
                    click(opcion2);

                    break;
                case 2:
                    click(opcion3);
                    break;
                case 3:
                    click(opcion4);

                    break;
                case 4:

                    //  speak();
                    volver();
                    break;
                case 5:
                    analitycsFirebase.customEvents("Accessibility","Principal","Talk");
                    hablarModoExperimental();

                    break;
                case 6:
                    analitycsFirebase.customEvents("Accessibility","Principal","More Options");
                    cargarMasPictos();
                    break;
                case 7:

                    analitycsFirebase.customEvents("Accessibility","Principal","Group Galery");
                    Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                    intent2.putExtra("Boton", 0);
                    startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                    break;
                case 8:
                    analitycsFirebase.customEvents("Accessibility","Principal","Games");
                     intent2 = new Intent(Principal.this, MainJuegos.class);
                     startActivity(intent2);
                    break;

            }

        } else if (vibrar) {
            switch (v.getId()) {
                case R.id.Option1:
                    if (opcion1 != null) {
                        try {
                            AlertBorrar(json.getId(opcion1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case R.id.Option2:
                    if (opcion2 != null) {
                        try {
                            AlertBorrar(json.getId(opcion2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case R.id.Option3:
                    if (opcion3 != null) {
                        try {
                            AlertBorrar(json.getId(opcion3));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case R.id.Option4:
                    if (opcion4 != null) {
                        try {
                            AlertBorrar(json.getId(opcion4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                default:

                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.Option1:

                    if (opcion1 == null&&!barridoPantalla.isBarridoActivado()) {
                        analitycsFirebase.customEvents("Touch","Principal","Add Pictogram");
                        Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                        intent2.putExtra("Boton", 0);
                        startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                    }
                    function_clickOption(opcion1,Opcion1_clicker);

                    break;

                case R.id.Option2:

                      if (opcion2 == null&&!barridoPantalla.isBarridoActivado()) {
                          analitycsFirebase.customEvents("Touch","Principal","Add Pictogram");
                          Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                          intent2.putExtra("Boton", 0);
                          startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                      }
                      function_clickOption(opcion2, Opcion2_clicker);


                    break;

                case R.id.Option3:
                        if (opcion3 == null&&!barridoPantalla.isBarridoActivado()) {
                            analitycsFirebase.customEvents("Touch","Principal","Add Pictogram");
                            Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                            intent2.putExtra("Boton", 0);
                            startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                        }
                        function_clickOption(opcion3, Opcion3_clicker);

                    break;

                case R.id.Option4:
                        if (opcion4 == null&&!barridoPantalla.isBarridoActivado()) {
                            analitycsFirebase.customEvents("Touch","Principal","Add Pictogram");
                            Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                            intent2.putExtra("Boton", 0);
                            startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                        }
                        function_clickOption(opcion4, Opcion4_clicker);

                    break;

                case R.id.Seleccion3:

                    hablarModoExperimental();
                    boolean hijos = true;
                    break;

                case R.id.btn_borrar:

                    analitycsFirebase.customEvents("Erase","Principal","Delete one pictogram");
                    volver();

                    break;

                case R.id.btnMasPictos:
                    ///////////noveno paso del tutorial


                    ///////////

                    //Registo que uso un funcion que nos interesa que use
                    analitycsFirebase.customEvents("Touch","Principal","More Pictograms");
                    cargarMasPictos();

                    break;

                case R.id.btnTodosLosPictos:
                    ////decimo paso del tutorial
                    /// Termino el tutorial
                    if(barridoPantalla.isBarridoActivado()&&barridoPantalla.isAvanzarYAceptar()){
                        Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                        intent2.putExtra("Boton", 0);
                        startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                    }
                    if(!barridoPantalla.isBarridoActivado()) {
                        //Registo que uso un funcion que nos interesa que use
                        if(barridoPantalla.isBarridoActivado())
                            analitycsFirebase.customEvents("Accessibility","Principal","Group Galery");
                        else
                            analitycsFirebase.customEvents("Touch","Principal","Group Galery");
                        Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
                        intent2.putExtra("Boton", 0);
                        startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
                    }else{
                      if(function_scroll.isClickEnabled()){
                          function_scroll.descansar();
                          break;
                      }else{
                          function_scroll.wakeUp();
                      }
                    }
                    break;
                case R.id.constraintRightButtons:

                    break;

                case R.id.btnFavoritos:

                    //octavo paso del tutorial


                        //Registo que uso un funcion que nos interesa que use
                        analitycsFirebase.customEvents("Touch","Principal","Favorite Phrases");

                        NewDialogsOTTAA newDialogsOTTAA=new NewDialogsOTTAA(this);

                        newDialogsOTTAA.initCustomFavoritePhrase(false);


                    break;
                case R.id.btnBarrido:
                    Log.d(TAG, "onClick: Btn barrido");
                    if (barridoPantalla.isBarridoActivado() && barridoPantalla.isAvanzarYAceptar()) {

                        if (barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId() == R.id.btnTalk) {
                            analitycsFirebase.customEvents("Accessibility","Principal","Talk with screen scanning");
                            hablarModoExperimental();
                        }

                    }else if(barridoPantalla.isBarridoActivado()&&!barridoPantalla.isAvanzarYAceptar()){
                        barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                    }

                    break;
                case R.id.action_reiniciar:
                        Intent intent = new Intent(this, MainJuegos.class);
                        this.startActivity(intent);


                    break;
                case R.id.action_share:
                    //Analytics
                    //Registo que uso un funcion que nos interesa que use
                    analitycsFirebase.customEvents("Touch","Principal","Share");
                        if (historial.getListadoPictos().size() > 0) {
                            if (!sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {
                                if (myTTS != null) {
                                    CompartirArchivos compartirArchivos = new CompartirArchivos(getContext(), myTTS);
                                    compartirArchivos.setHistorial(historial.getListadoPictos());
                                    compartirArchivos.seleccionarFormato(Oracion);
                                }
                            } else if (sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {
                                Log.d(TAG, "onClick: " + historial.getListadoPictos().toString());
                                traducirfrase = new traducirTexto(getApplication(), sharedPrefsDefault);
                                if (Oracion.isEmpty() && historial.getListadoPictos().size() > 0)
                                    CargarOracion(historial.getListadoPictos().get(0));
                                Oracion = EjecutarNLG(true);
                                traducirfrase.traducirIdioma(this, Oracion, "en", sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"), true);
                            }
                            // if(myTTS().devolverPathAudio().exists())
                        }

                   break;
                default:
                    Log.d(TAG, "onClick: Oracion:" + Oracion);
                    if(barridoPantalla.isBarridoActivado()&&(barridoPantalla.isScrollMode()||barridoPantalla.isAvanzarYAceptar()))
                        analitycsFirebase.customEvents("Accessibility","Principal","Talk with accessibility device");
                    hablarModoExperimental();
                    hijos = true;
                    break;
            }
        }

        new Handler().postDelayed(new Runnable() {      // Esto sirve para esperar un tiempo dsp del primer
            // click y asi evitar que los que toquen toda la pantalla entren
            @Override
            // un solo lugar, tmb ayua a los que le tiembla la mano para
            public void run() { // TODO poner un pref para setear el valor del filtro para temblores.
                click = true;
            }
        }, 400);
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

    public void updateLanguaje(){
        Locale locale = new Locale(sharedPrefsDefault.getString(this.getString(R.string.str_idioma),"en"));
        Locale.setDefault(locale);

        Resources res = this.getResources();
        Configuration config = new Configuration(res.getConfiguration());

            config.setLocale(locale);
             this.createConfigurationContext(config);

    }





    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        

        //myContextWrapper.setLocale(sharedPrefsDefault.getString("en","en"));

    }

    /**
     * Realiza las tareas correspodientes cuando vuelve de un Activity
     *
     * @param requestCode (int)
     * @param resultCode  (int)
     * @param data        (Intent)
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentCode.GALERIA_GRUPOS.getCode()) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    int Picto = extras.getInt("ID");
                    Log.d(TAG, "onActivityResult: Obteniendo Picto" + Picto);
                    if (Picto != 0) {
                        click(json.getPictoFromId2(Picto));

                    }
                }

            }

        }

        if (requestCode == IntentCode.EDITARPICTO.getCode()) {

            Log.d(TAG, "onActivityResult: EditarPicto");
            try {
                CargarOpciones(json, pictoPadre, cuentaMasPictos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == IntentCode.CONFIG_SCREEN.getCode()) {

            myTTS = new textToSpeech(getApplicationContext());
            Reset();

            // Si esta habilitado el barrido de pantalla empieza la rutina
            barridoPantalla.cambiarEstadoBarrido();

            if (!barridoPantalla.isBarridoActivado()) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        btnBarrido.setVisibility(View.GONE);
                        barridoPantalla.cambiarEstadoBarrido();
                        if (barridoPantalla.isButtonVisible())
                            barridoPantalla.changeButtonVisibility();
                    }
                });

            } else if (barridoPantalla.isBarridoActivado() && btnBarrido.getVisibility() == View.GONE) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        btnBarrido.setVisibility(View.VISIBLE);
                    }
                });
            }

            if(sharedPrefsDefault.getBoolean("skillHand",false)){
                    setContentView(R.layout.activity_main_rigth);
                    recreate();
            }
            else{
                setContentView(R.layout.activity_main);
               recreate();
            }


            // Acualizo el booleano de editar picto
            editarPicto = sharedPrefsDefault.getBoolean(getString(R.string.str_editar_picto), true);
            //actualizo la voz del tts

            if (!sharedPrefsDefault.getBoolean("bool_ubicacion", false)) {
                //TODO esto se reemplaza con Places
                // json.setPosicion(Posicion.NADA);
                // json.inicializarGPS();
                Reset();
            }

            if (data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Log.d(TAG, "onActivityResult: Reiniciar: " + extras.getBoolean(getString(R.string.boolean_cambio_idioma), false));
                if (extras.getBoolean(getString(R.string.boolean_cambio_idioma), false)) {
//                    Intent intent = new Intent(getApplicationContext(), Principal.class);
//                    startActivity(intent);
//                    this.finish();
                    if(sharedPrefsDefault.getBoolean("skillHand",false)){
                        setContentView(R.layout.activity_main_rigth);
                        recreate();
                    }
                    else{
                        setContentView(R.layout.activity_main);
                        recreate();
                    }
                }

            }


        }
        if (requestCode == IntentCode.LOGIN_ACTIVITY.getCode()) {
            Log.d(TAG, "onActivityResult: enter LoginActivity");
        }
        if(requestCode==IntentCode.CUSTOMPHRASES.getCode()){
            NewDialogsOTTAA newDialogsOTTAA=new NewDialogsOTTAA(this);
            newDialogsOTTAA.initCustomFavoritePhrase(false);
        }

    }

    private void consultarPago() {
        new LicenciaUsuario(getApplicationContext());
        final DatabaseReference pagoRef =firebaseUtils.getmDatabase();
        pagoRef.child(Constants.PAGO).child(user.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String valuePago = dataSnapshot.child(Constants.PAGO).getValue() + "";
                    Log.d(TAG, "onDataChange: PagoDatabase: " + valuePago);
                    if (Integer.parseInt(valuePago) == 0) {
                        sharedPrefsDefault.edit().putBoolean(Constants.BARRIDO_BOOL, false).apply();
                        sharedPrefsDefault.edit().putInt(Constants.PREMIUM, 0).apply();

                        if (btnBarrido.getVisibility() == View.VISIBLE) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // Stuff that updates the UI
                                    btnBarrido.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                    barridoPantalla.cambiarEstadoBarrido();
                } catch (Exception ex) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("premium", 0).apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onTextoTraducido(boolean traduccion) {
        if (traduccion ) {
            if (myTTS != null) {
                CompartirArchivos compartirArchivos = new CompartirArchivos(getContext(), myTTS);
                compartirArchivos.setHistorial(historial.getListadoPictos());
                Oracion = traducirfrase.getTexto();
                compartirArchivos.seleccionarFormato(Oracion);
            }
            traduccion = false;

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp: " + event.getAction());
        if (barridoPantalla.isBarridoActivado()) {
            tocarTeclaAcordeUbicacion(event, keyCode, sharedPrefsDefault.getInt("orientacion_joystick", 0));
        }
        return super.onKeyUp(keyCode, event);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {

       return navigationControls.makeClick(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
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



    //metodo encargado de pasar una vista a un formato semitransparente
    private void formatoTransparencia(View v, JSONObject opcion) {
        try {
            //pregunto si el objeto es una  sugerencia
            if (opcion.getBoolean("esSugerencia") == true) {// si es asi le doy una transparencia del 50%
                v.setAlpha((float) 0.65);
                if (!sharedPrefsDefault.getBoolean("prediccion_usuario", false)) {
                    sharedPrefsDefault.edit().putBoolean("prediccion_usuario", true).apply();
                    explicarSugerencias();
                }
            } else {
                // si no , no se le da transparencia
                v.setAlpha((float) 1);
            }
        } catch (JSONException ex) {
            v.setAlpha((float) 1);
        }
    }






    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case Principal.TRIM_MEMORY_BACKGROUND:
                break;
            case Principal.TRIM_MEMORY_MODERATE:
                break;
            case Principal.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
        }
    }



    public void iniciarBarrido() {
        /*
         * Preparo el inicio del barrido de pantalla, para eso es necesario pasarle el listado de objetos
         * */
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        listadoObjetosBarrido.add(Opcion1);
        listadoObjetosBarrido.add(Opcion2);
        listadoObjetosBarrido.add(Opcion3);
        listadoObjetosBarrido.add(Opcion4);
        listadoObjetosBarrido.add(findViewById(R.id.btn_borrar));
        listadoObjetosBarrido.add(findViewById(R.id.btnTalk));
        listadoObjetosBarrido.add(findViewById(R.id.btnMasPictos));
        listadoObjetosBarrido.add(findViewById(R.id.btnTodosLosPictos));
        listadoObjetosBarrido.add(findViewById(R.id.action_reiniciar));

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

    public void CargarJson(){
        Json.getInstance().setmContext(this);
        json=Json.getInstance();
        try {

            json.initJsonArrays();
            json.cargarPictosSugeridosJson();
            if (json.getmJSONArrayTodosLosPictos() != null && json.getmJSONArrayTodosLosPictos().length() > 0) {
                try {
                    if (pictoPadre == null || pictoPadre.getInt("id") == 0)
                        pictoPadre = json.getmJSONArrayTodosLosPictos().getJSONObject(0);
                    cuentaMasPictos=0;
                    CargarOpciones(json, pictoPadre, cuentaMasPictos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }
    }

    public void mostrarMensajeSinConexionInternet() {
        Toast.makeText(this, "Check internet connection", Toast
                .LENGTH_SHORT)
                .show();
    }

    public void intentarDeNuevo() {
        Toast.makeText(this, R.string.tryAgain, Toast.LENGTH_SHORT).show();
    }

    public void setOracion(String oracion) {
        this.Oracion = oracion;
    }

    public boolean tocarTeclaAcordeUbicacion(KeyEvent event, int keyCode, int ubic) {
        switch (ubic) {
            case 0:
                if (event.isTracking() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    barridoPantalla.volverAtrasBarrido();
                    return true;
                } else if (event.isTracking() && (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
                    return true;
                } else if (event.isTracking() && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    barridoPantalla.avanzarBarrido();
                    return true;
                }
                return false;
            case 1:
                if (event.isTracking() && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    barridoPantalla.avanzarBarrido();

                    return true;
                } else if (event.isTracking() && (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                    onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
                    return true;

                } else if (event.isTracking() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    barridoPantalla.volverAtrasBarrido();
                    return true;
                }
                return false;

        }
        return false;
    }

    private void addOption(JSONObject opcion, PictoView picto, Animation animation) {
        Log.d(TAG, "addOption: "+opcion.toString() );
        Pictogram pictogram=new Pictogram(opcion,idioma);
        picto.setUpGlideAttatcher(this);
        picto.setUpContext(this);
        picto.setPictogramsLibraryPictogram(pictogram);
       // picto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        formatoTransparencia(picto, opcion);
        picto.startAnimation(animation);
    }

    /**
     * use custom_picto opcion , animation alphaAnimation
     * */
    private void addOpcionNull(PictoView Opcion, Animation alphaAnimation) {
        Opcion.setCustom_Img(getResources().getDrawable(R.drawable.ic_agregar_nuevo));
        Opcion.setCustom_Texto(getResources().getString(R.string.agregar_picto));
        Opcion.setCustom_Color(getResources().getColor(R.color.Black));
        Opcion.startAnimation(alphaAnimation);
        Opcion.setAlpha((float) 1);
        this.cuentaMasPictos = -1;// linea encargada de indicar que el contador esta en 0
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_dismiss);



        if (opcion1 == null) {


            Opcion2.startAnimation(animation);
            Opcion3.startAnimation(animation);
            Opcion4.startAnimation(animation);
            Opcion2.setVisibility(View.INVISIBLE);
            Opcion3.setVisibility(View.INVISIBLE);
            Opcion4.setVisibility(View.INVISIBLE);
            Opcion2.setEnabled(false);
            Opcion3.setEnabled(false);
            Opcion4.setEnabled(false);

        } else if (opcion2 == null && opcion1 != null) {
            Opcion3.startAnimation(animation);
            Opcion4.startAnimation(animation);
            Opcion3.setVisibility(View.INVISIBLE);
            Opcion4.setVisibility(View.INVISIBLE);
            Opcion3.setEnabled(false);
            Opcion4.setEnabled(false);

        } else if (opcion3 == null && opcion2 != null) {
            Opcion4.startAnimation(animation);
            Opcion4.setVisibility(View.INVISIBLE);
            Opcion4.setEnabled(false);
        }else if(opcion4==null&&opcion3!=null&&Opcion3.getVisibility()==View.VISIBLE){
            Opcion4.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.onTrimMemory(TRIM_MEMORY_RUNNING_LOW);
        Log.d(TAG, "onLowMemory: Trimming Memory");
    }

    @Override
    public void onPlaceEncontrado(int placeEncontrado) {

    }

    private void function_clickOption(JSONObject pictoPadre,timer_pictogram_clicker pictogram_clicker){
        if(!pictogram_clicker.isClicked()){
            pictogram_clicker.disableClick();
            click(pictoPadre);
            pictogram_clicker.resetClick();
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
    public void onPostCreate(@androidx.annotation.Nullable Bundle savedInstanceState, @androidx.annotation.Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle presses on the action bar items
        Log.d(TAG, "onNavigationItemSelected: " );
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            case R.id.action_parar:


                    //Registo que uso un funcion que nos interesa que use


                    analitycsFirebase.customEvents("Touch","Principal","Silence");
                    if (mute) {
                        item.setIcon(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
                        mute = false;
                    } else {
                        item.setIcon(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                        mute = true;
                    }

                    myTTS.mute();
                    sharedPrefs.edit().putBoolean("mBoolMute", mute).apply();

                    if (isChristmas) {
                        count++;
                        if (count == 4) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(Principal.this, R.raw.navidad);
                            mediaPlayer.start();
                        }
                    }

                break;


            case R.id.action_settings:
                analitycsFirebase.customEvents("Touch","Principal","Settings");
                //Abrimos otra pantalla
                isSettings = true;
                Intent intent12 = new Intent(Principal.this, prefs.class);
                startActivityForResult(intent12, IntentCode.CONFIG_SCREEN.getCode());


                return true;
            case R.id.ubic:
                analitycsFirebase.customEvents("Touch","Principal","Location");
                if(placesImplementation!=null){
                    if(placesImplementation.typesSizes()>0){
                        Place place=placesImplementation.getPlace();
                        String name=placesImplementation.getPlaceName(place);
                        String placeType=placesImplementation.getPlaceType(place);
                        Log.d(TAG, "onOptionsItemSelected: "+name +" "+placeType);
                        item.setTitle(name+" : "+placesImplementation.getPlaceName(placeType));
                        json.setPlaceName(placeType);
                    }
                }
                return true;
            case R.id.exit:
                this.doubleBackToExitPressedOnce = true;
                super.onBackPressed();
                break;
            case R.id.tutorial:
                Intent intent1=new Intent(this,Viewpager_tutorial.class);
                startActivity(intent1);
                break;
            case R.id.logout:
                analitycsFirebase.customEvents("Touch","Principal","LogOut");
                user.logOut();
                break;
            case R.id.report:
                //todo firebase analitycs
                analitycsFirebase.customEvents("Touch","Principal","Report");
                if(sharedPrefsDefault.getInt("premium",0)==1) {
                    Intent i = new Intent(getApplicationContext(), ActivityInformes.class);
                    startActivity(i);
                }  else{
                    Intent i=new Intent(Principal.this,LicenciaExpirada.class);
                    startActivity(i);
                }
                break;
            case R.id.about:
                //todo firebase analitycs
                analitycsFirebase.customEvents("Touch","Principal","About that");
                Intent intent = new Intent(getApplicationContext(), AboutOttaa.class);
                startActivity(intent);
                break;
        }

       return true;
    }
    public BarridoPantalla getBarridoPantalla(){
        return  barridoPantalla;
    }

    public ScrollFunctionMainActivity getFunction_scroll(){
        return function_scroll;
    }
    public  void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView){
        if(pictogram.getEditedPictogram().isEmpty()){
            attatcher.UseCornerRadius(true).loadDrawable(this.getResources().getDrawable(this.getContext().getResources().getIdentifier(pictogram.getPictogram(),
                    "drawable", this.getPackageName())),imageView);
        }else{
            File picto=new File(pictogram.getEditedPictogram());
            if(picto.exists())
                attatcher.UseCornerRadius(true).loadDrawable(picto,imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(Uri.parse(pictogram.getUrl()),imageView);
        }
    }
}

