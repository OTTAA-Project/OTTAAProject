package com.stonefacesoft.ottaa;


import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

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
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
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
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Activities.Phrases.FavoritePhrases;
import com.stonefacesoft.ottaa.Activities.Phrases.MostUsedPhrases;
import com.stonefacesoft.ottaa.Backup.BackupGroups;
import com.stonefacesoft.ottaa.Backup.BackupPhotos;
import com.stonefacesoft.ottaa.Backup.BackupPhrases;
import com.stonefacesoft.ottaa.Backup.BackupPictograms;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Progress_dialog_options;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Yes_noDialogs;
import com.stonefacesoft.ottaa.Dialogos.newsDialog.NewDialogsOTTAA;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirBackupFirebase;
import com.stonefacesoft.ottaa.Interfaces.FailReadPictogramOrigin;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.Make_Click_At_Time;
import com.stonefacesoft.ottaa.Interfaces.PlaceSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.SortPictogramsInterface;
import com.stonefacesoft.ottaa.Interfaces.TTSListener;
import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.JSONutils.Json0Recover;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.AboutOttaa;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;
import com.stonefacesoft.ottaa.utils.Accesibilidad.Gesture;
import com.stonefacesoft.ottaa.utils.Accesibilidad.SayActivityName;
import com.stonefacesoft.ottaa.utils.Accesibilidad.devices.PrincipalControls;
import com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions.ScrollFunctionMainActivity;
import com.stonefacesoft.ottaa.utils.AvatarPackage.Avatar;
import com.stonefacesoft.ottaa.utils.AvatarPackage.AvatarUtils;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.UserLicence.LicenciaUsuario;
import com.stonefacesoft.ottaa.utils.MovableFloatingActionButton;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.PopupMenuUtils;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
import com.stonefacesoft.ottaa.utils.TalkActions.Historial;
import com.stonefacesoft.ottaa.utils.TraducirFrase;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.constants.ConstantsAnalyticsValues;
import com.stonefacesoft.ottaa.utils.constants.ConstantsMainActivity;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.location.PlacesImplementation;
import com.stonefacesoft.ottaa.utils.preferences.User;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.ottaa.utils.timer_pictogram_clicker;
import com.stonefacesoft.ottaa.utils.traducirTexto;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;
import com.stonefacesoft.pictogramslibrary.view.PictoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * V92 en produccion
 */
public class Principal extends AppCompatActivity implements View
        .OnClickListener,
        View.OnLongClickListener,
        OnMenuItemClickListener,
        FirebaseSuccessListener, NavigationView.OnNavigationItemSelectedListener, PlaceSuccessListener, ConnectionCallbacks, translateInterface, View.OnTouchListener, Make_Click_At_Time , SortPictogramsInterface {

    private static final String TAG = "Principal";
    public static boolean cerrarSession = false;// use this variable to notify when the session is closed
    private static FirebaseSuccessListener mFirebaseSuccessListener;

    private final Handler handlerHablar = new Handler();
    public Uri bajarGrupos;
    public volatile Json json;
    public String dateStr;
    // JSONObject que usamoss
    JSONObject pictoPadre, opcion1, opcion2, opcion3, opcion4, onLongOpcion;
    SubirArchivosFirebase subirArchivos;
    String timeStamp;
    private Avatar avatar;
    private MovableFloatingActionButton movableFloatingActionButton;
    private Button Registro;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private PrincipalControls navigationControls;
    //Declaracion de los botones
    private ArrayList<ImageView> selectedImage;

    private PictoView Opcion1;
    private PictoView Opcion2;
    private PictoView Opcion3;
    private PictoView Opcion4;
    private timer_pictogram_clicker Opcion1_clicker;
    private timer_pictogram_clicker Opcion2_clicker;
    private timer_pictogram_clicker Opcion3_clicker;
    private timer_pictogram_clicker Opcion4_clicker;
    private boolean uploadFile = true;

    //Declaracion de variables del TTS
    private User user;
    private boolean isSettings;
    private boolean mCerrarSesion;
    private LinearLayout layout;
    private Button btnBarrido;
    private MenuItem locationItem;
    private ScrollFunctionMainActivity function_scroll;
    private FloatingActionButton talk;
    private BajarJsonFirebase mBajarJsonFirebase;
    private boolean mute; // this boolean show the sound status
    /* Client used to interact with Google APIs. */
    //Declaro el manejador de preferencia
    private SharedPreferences sharedPrefs, sharedPrefsDefault;
    private boolean PrimerUso;
    // Declaracion de Arraylist en el que se guardan los Picto viejos
    //protected ArrayList<JSONObject> Historial;
    private Historial historial;
    private boolean doubleBackToExitPressedOnce = false;
    private final boolean click = true;

    //Idioma de la base de Datos
    //String que carga el texto para el TTS
    private String Oracion = "";
    private int versionCode, ultimaVersion;
    private long primeraConexion;
    //Indica el proximo lugar en la seleccion
    private int CantClicks;
    //Handler para animar el Hablar cuando pasa cierto tiempo
    private final Runnable animarHablar = new Runnable() {
        @Override
        public void run() {
            AnimarHablar();
            handlerHablar.postDelayed(this, 4000);
        }
    };
    private PictoView Agregar;
    //InputStream
    private FileInputStream pictos, grupos, frasesGuardadas;
    private long networkTS;
    // Editar picto
    private boolean editarPicto;
    //Bool que indica si se est&aacute usando la agenda o no
    private boolean boolAgenda;
    private long Actual;
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
    private LottieAnimationView animationView;
    private textToSpeech myTTS;
    private ConnectionDetector connectionDetector;
    private SubirBackupFirebase mFirebaseBackup;
    private traducirTexto traducirfrase;
    private Toolbar toolbar;
    private int mCheckDescarga, mCheckDatos;
    private ConstraintLayout constraintBotonera;
    private LocationRequest mLastLocationRequest;
    private Location mLastLocation;
    private BarridoPantalla barridoPantalla;
    private VelocityTracker mVelocityTracker;
    private AnalyticsFirebase analitycsFirebase;
    private FirebaseUtils firebaseUtils;
    private TextView toolbarPlace;
    private TraducirFrase traducirFrase;
    private PlacesImplementation placesImplementation;
    private Progress_dialog_options firebaseDialog;
    private ImageButton btn_share;

    private RemoteConfigUtils remoteConfigUtils;

    private InmersiveMode inmersiveMode;
    private Gesture gesture;
    //Bandera global del tutorial
    //version 6.7.5
    private boolean TutoFlag;
    private ImageView menuAvatarIcon;
    private AvatarUtils avatarUtils;
    private ImageButton botonFavoritos;
    private ImageButton borrar;
    private ImageButton masPictos;
    private ImageButton todosLosPictos;
    private ImageButton resetButton;


    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1; )
                os.write(buffer, 0, len);
            os.flush();
            return os.toByteArray();
        }
    }

    public String getOracion() {
        return Oracion;
    }

    public void setOracion(String oracion) {
        this.Oracion = oracion;
    }

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
            if (getFirebaseDialog().isShowing())
                getFirebaseDialog().destruirDialogo();
            try {
                json.setmJSONArrayPictosSugeridos(json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE));
            } catch (JSONException | FiveMbException e) {
                e.printStackTrace();
            }
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
                if (!this.isFinishing() && ConnectionDetector.isNetworkAvailable(this) && json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS).length() > 0) {

                    mBajarJsonFirebase.setInterfaz(this);
                    getFirebaseDialog().setTitle(getApplicationContext().getResources().getString(R.string.edit_sync)).setMessage(getApplicationContext().getResources().getString(R.string.edit_sync_pict)).setCancelable(false);
                    getFirebaseDialog().mostrarDialogo();
                    mBajarJsonFirebase.syncPictogramsandGroups();
                }
            } catch (JSONException | FiveMbException e) {
                e.printStackTrace();
            }
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
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(this);
        prepareLayout();
        //Facebook analytics
        setAutoLogAppEventsEnabled(true);
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
        }
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    Log.d("Installations", "Installation ID: " + task.getResult());
                } else {
                    Log.e("Installations", "Unable to get Installation ID");
                }
            }
        });
        initComponents();
        System.gc();

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
            }
            initFirstPictograms();

        }
        getFirebaseDialog().destruirDialogo();

    }

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

    ///////////////////////// Funcion para asignar el ancho a seleccion /////////////////

    private void AjustarAncho(int Rid) {
        ImageButton view_instance = findViewById(Rid);
        android.view.ViewGroup.LayoutParams params = view_instance.getLayoutParams();
        params.width = view_instance.getLayoutParams().height;
        Log.d(TAG, "AjustarAncho: " + "Ancho " + params.width + " Alto " + params.height);
        view_instance.setLayoutParams(params);
        ImageButton button = new ImageButton(getApplicationContext());
        button.setLayoutParams(params);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_edit:
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Edit Pictogram");
                if (user.isPremium()) {
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
                    intent.putExtra("Texto", JSONutils.getNombre(onLongOpcion, sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                    intent.putExtra("Sel", 1);
                    intent.putExtra("Nombre", JSONutils.getNombre(onLongOpcion, sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
                    intent.putExtra("Color", cargarColor(JSONutils.getTipo(onLongOpcion)));
                    myTTS.hablar(getString(R.string.editar_pictogram));

                    //Abrir pantalla de edicion de pictograma
                    isSettings = false;
                    startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
                } else {
                    Intent i = new Intent(Principal.this, LicenciaExpirada.class);
                    startActivity(i);
                }
                return true;
            case R.id.item_delete:
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Delete Pictogram");
                try {
                    if (onLongOpcion != null)
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
        BackupPictograms backupPictograms = new BackupPictograms(this, Constants.ARCHIVO_PICTOS);
        BackupGroups backupGroups = new BackupGroups(this, Constants.ARCHIVO_GRUPOS);
        BackupPhrases backupPhrases = new BackupPhrases(this, Constants.ARCHIVO_FRASES);
        BackupPhotos backupPhotos = new BackupPhotos(this, Constants.ARCHIVO_PICTOS);
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
        mLastLocationRequest = LocationRequest.create();
        mLastLocationRequest.setSmallestDisplacement(10);
        mLastLocationRequest.setInterval(1000); // Update location every 1 minute
        mLastLocationRequest.setFastestInterval(1000);
        mLastLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();
        if (placesImplementation != null && sharedPrefsDefault.getBoolean(getString(R.string.bool_ubicacion), false)) {
            if (!placesImplementation.isStarted())
                placesImplementation.iniciarClientePlaces();
            else{
                placesImplementation.locationRequest();
                useLocation();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

/*    private void setPrimerBackupTimeFirebase() {

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
        });

    }*/

    private String getTimeStamp() {
        Long tslong = System.currentTimeMillis() / 1000;
        return tslong.toString();
    }

//////////////////////////////  Barrido Fin  ////////////////////////////////////////////

    private void guardarBackupLocal() {
        BackupPictograms backupPictograms = new BackupPictograms(this, Constants.ARCHIVO_PICTOS);
        BackupGroups backupGroups = new BackupGroups(this, Constants.ARCHIVO_GRUPOS);
        BackupPhrases backupPhrases = new BackupPhrases(this, Constants.ARCHIVO_FRASES);
        BackupPhotos backupPhotos = new BackupPhotos(this, Constants.ARCHIVO_PICTOS);

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
        if(user!= null){
            if(!user.isConnected())
                user.connectClient();
        }
    }

    @Override
    protected void onResume() {
        CargarJson();
        Log.d(TAG, "onResume: idioma : " + getApplication().getResources().getConfiguration().locale.toString());
        super.onResume();
        myTTS = textToSpeech.getInstance(this);
        getFirebaseDialog().destruirDialogo();
        if (placesImplementation != null) {
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
        getFirebaseDialog().destruirDialogo();
        super.onPause();
    }


    /**
     * Implementa este metodo de TTS interface, cuando se inicializa el TTS lo setea por defecto
     */

    @Override
    protected void onStop() {
        if (!json.getFallJson() && user != null) {
            if(user.getmAuth().getCurrentUser() != null)
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
            getFirebaseDialog().destruirDialogo();
        super.onDestroy();
    }

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


    void CargarOracion(JSONObject jsonObject, String idioma) {
        Oracion = Oracion + JSONutils.getNombre(jsonObject, idioma) + " ";
        Log.d(TAG, "CargarOracion: " + Oracion);
    }

    /**
     * Carga el picto seleccionado en la barra de seleccion
     *
     * @param opcion
     */
    private void CargarSeleccion(JSONObject opcion) {
        GlideAttatcher attatcher = new GlideAttatcher(this);
        Pictogram pictogram = new Pictogram(opcion, ConfigurarIdioma.getLanguaje());
        if(CantClicks<=9&&CantClicks>=0){
            int indexAux = CantClicks+1;
            loadDrawable(attatcher, pictogram, selectedImage.get(CantClicks));
            selectedImage.get(CantClicks).startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
            if(indexAux<10)
                selectedImage.get(indexAux).setImageDrawable(getResources().getDrawable(R.drawable.icono_ottaa));
        }
    }

    /**
     * Inicializa la barra de seleccion poniendo la imagen por defecto
     */
    private void inicializar_seleccion() {
        for (int i = 0; i < selectedImage.size(); i++) {
            selectedImage.get(i).setImageDrawable(Agregar.getCustom_Imagen());
            selectedImage.get(i).startAnimation(AnimationUtils.loadAnimation(this, R.anim.overshoot_arriba));
        }
    }

    @SuppressLint("Range")
    private void loadOptions(Json json, JSONObject padre) {
        Animation alphaAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_show);
        Opcion1.setEnabled(true);
        Opcion2.setEnabled(true);
        Opcion3.setEnabled(true);
        Opcion4.setEnabled(true);
        if (json.getCantFallas() ==0)
            loadChilds(padre, alphaAnimation);
       if(json.getCantFallas()<4 && json.getCantFallas()>0)
           downloadFailedFile(3);
    }

    public void loadChildOption(JSONArray opciones, int index, Animation alphaAnimation) {
        try {
            if (opciones.getJSONObject(index).getInt("id") != -1) {
                selectJsonOption(index, opciones.getJSONObject(index), alphaAnimation);
            } else {
                loadOptionValue(index, null);
                addOpcionNull(returnOption(index), alphaAnimation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selectJsonOption(int position, JSONObject object, Animation alphaAnimation) {
        loadOptionValue(position, object);
        switch (position) {
            case 0:
                addOption(opcion1, returnOption(position), alphaAnimation);
                break;
            case 1:
                addOption(opcion2, returnOption(position), alphaAnimation);
                break;
            case 2:
                addOption(opcion3, returnOption(position), alphaAnimation);
                break;
            case 3:
                addOption(opcion4, returnOption(position), alphaAnimation);
                break;
        }
    }

    private PictoView returnOption(int position) {
        switch (position) {
            case 0:
                return Opcion1;
            case 1:
                return Opcion2;
            case 2:
                return Opcion3;
            case 3:
                return Opcion4;
            default:
                return Opcion1;
        }
    }

    private void loadOptionValue(int position, JSONObject value) {
        switch (position) {
            case 0:
                opcion1 = value;
                break;
            case 1:
                opcion2 = value;
                break;
            case 2:
                opcion3 = value;
                break;
            case 3:
                opcion4 = value;
                break;
        }
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
        loadOptions(json, pictoPadre);
    }

    public void ResetSeleccion() {
        Log.d(TAG, "ResetSeleccion: ");
        Oracion = "";
        int situacionActual = 0;
        CantClicks = 0;
        inicializar_seleccion();
        if (!historial.getListadoPictos().isEmpty()) {
            for (int i = 0; i < historial.getListadoPictos().size(); i++) {
                CargarOracion(historial.getListadoPictos().get(i), sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"));
            }
        }
    }

    private void cargarSelec(JSONObject jsonObject) {
        CargarOracion(jsonObject, sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"));
        CargarSeleccion(jsonObject);
        CantClicks++;
    }

    private void volver() {

        pictoPadre = historial.removePictograms(false);
        ResetSeleccion();
        cuentaMasPictos = 0;
        for (int i = 0; i < historial.getListadoPictos().size(); i++) {
            CargarSeleccion(historial.getListadoPictos().get(i));
            CantClicks++;
        }
        loadOptions(json, pictoPadre);
    }

    private void click(JSONObject opcion) {
        Log.d(TAG, "click: ");
        cuentaMasPictos = 0;
        if (opcion == null || pictoPadre == null) {
            Log.d(TAG, "click: Opcion es null");
            return;
        }
        historial.addPictograma(opcion);
        try {
            int pos = JSONutils.getPositionPicto2(json.getmJSONArrayTodosLosPictos(), pictoPadre.getInt("id"));
            if(pos != -1) {
                JSONutils.aumentarFrec(pictoPadre, opcion);
                json.getmJSONArrayTodosLosPictos().put(pos, pictoPadre);
                json.guardarJson(Constants.ARCHIVO_PICTOS);
                json.setmJSONArrayTodosLosPictos(json.getmJSONArrayTodosLosPictos());
            }
        } catch (JSONException e) {
            CrashlyticsUtils.getInstance().getCrashlytics().recordException(e.getCause());
        }
        sayPictogramName(JSONutils.getNombre(opcion, sharedPrefsDefault.getString(getResources().getString(R.string.str_idioma), "en")));
        pictoPadre = opcion;
        cargarSelec(pictoPadre);
        loadOptions(json, opcion);
        Log.d(TAG, "click: " + opcion.toString());
    }

    private void cargarMasPictos() {
        cuentaMasPictos++;
        if (cuentaMasPictos > Constants.VUELTAS_CARRETE) {
            cuentaMasPictos = 0;
        }
        loadOptions(json, pictoPadre);
    }

    public void AlertBorrar(final int pos) {
        vibrar = false;

        Yes_noDialogs dialogs = new Yes_noDialogs(this);
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
                        JSONutils.desvincularJson(pictosSugeridos.getJSONObject(pictoPadre.getInt("id")), pos);
                        //
                        json.setmJSONArrayPictosSugeridos(pictosSugeridos);
                        //guardo las opciones
                        if (!json.guardarJson(Constants.ARCHIVO_PICTOS_DATABASE))
                            Log.e(TAG, "onClick: eliminar: Error al guardar pictos sugeridos");
                        //  CargarOpciones(json, pictoPadre, cuentaMasPictos);
                    }

                } catch (JSONException | FiveMbException e) {
                    e.printStackTrace();
                }
                try {

                    JSONutils.desvincularJson(pictoPadre, pos);
                    JSONutils.setJsonEditado2(json.getmJSONArrayTodosLosPictos(), pictoPadre);
                    if (!json.guardarJson(Constants.ARCHIVO_PICTOS))
                        Log.e(TAG, "onClick: Error al guardar pictos sugeridos");
                    cargarMasPictos();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialogs.cancelarDialogo();
                loadOptions(json, pictoPadre);
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


    private String EjecutarNLG() {
        return historial.talkWithtNLG();
    }

    public void speak() {
        if (!Oracion.isEmpty()) {
            if (!myTTS.getTTS().isSpeaking()) {
                speakBlock();
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btn_borrar) {
            //Registo que uso un funcion que nos interesa que use
            getAnalyticsFirebase().customEvents("Erase", "Principal", "Erase all pictograms");
            Reset();
        }
        if (editarPicto) {
            switch (v.getId()) {
                case R.id.Option1:
                    longClick(Opcion1, opcion1);
                    break;
                case R.id.Option2:
                    longClick(Opcion2, opcion2);
                    break;
                case R.id.Option3:
                    longClick(Opcion3, opcion3);
                    break;
                case R.id.Option4:
                    longClick(Opcion4, opcion4);
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

    private void AnimarHablar() {
        switch (CantClicks) {
            case 1:
                selectedImage.get(1).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 2:
                selectedImage.get(2).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 3:
                selectedImage.get(3).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 4:
                selectedImage.get(4).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 5:
                selectedImage.get(5).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
            case 6:
                selectedImage.get(6).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                break;
        }
    }

    public void hablarModoExperimental() {
        if (sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {
            getAnalyticsFirebase().customEvents("Talk", "Principal", "Phrase With NLG");
            nlgTalkAction();
        } else {
            getAnalyticsFirebase().customEvents("Talk", "Principal", "Phrase without  NLG");
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
    public void onClick(View v) {
        View vista = findViewById(v.getId());
        Log.d(TAG, "onClick: " + vista.getId());
        switch (v.getId()) {
            case R.id.Option1:
                onClickOption(opcion1, Opcion1_clicker);
                break;
            case R.id.Option2:
                onClickOption(opcion2, Opcion2_clicker);
                break;
            case R.id.Option3:
                onClickOption(opcion3, Opcion3_clicker);
                break;
            case R.id.Option4:
                onClickOption(opcion4, Opcion4_clicker);
                break;
            case R.id.btnFavoritos:
                startFavoritePhrases();
                break;
            case R.id.action_share:
                getAnalyticsFirebase().customEvents(ConstantsAnalyticsValues.TOUCH, this.getClass().getName(), ConstantsAnalyticsValues.FAVORITEPHRASES);
                shareAction();
                break;
            case R.id.btn_borrar:
                analyticsAction(ConstantsAnalyticsValues.TOUCH, ConstantsAnalyticsValues.ERASE, "Principal", ConstantsAnalyticsValues.DELETEONEPICTOGRAM);
                volver();
                break;
            case R.id.btnTalk:
                analyticsAction(ConstantsAnalyticsValues.ACCESSIBILITY, ConstantsAnalyticsValues.TOUCH, "Principal", ConstantsAnalyticsValues.TALK);
                hablarModoExperimental();
                break;
            case R.id.btnMasPictos:
                analyticsAction(ConstantsAnalyticsValues.ACCESSIBILITY, ConstantsAnalyticsValues.TOUCH, "Principal", ConstantsAnalyticsValues.MOREOPTIONS);
                cargarMasPictos();
                break;
            case R.id.action_reiniciar:
                startGames();
                break;
            case R.id.btnTodosLosPictos:
                analyticsAction("Accessibility", "Touch", "Principal", "Group Gallery");
                startGroupGallery();
                break;
            case R.id.btnBarrido:
                barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).callOnClick();
                break;
            case R.id.avatarIconMain:
                startIconSelector();
                break;
            default:
                hablarModoExperimental();
                break;
        }
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
        switch (requestCode) {
            case ConstantsMainActivity.GALERIA_GRUPOS:
                galeriaGruposResult(data);
                break;
            case ConstantsMainActivity.EDITARPICTO:
                editPictoResult(data);
                break;
            case ConstantsMainActivity.CONFIG_SCREEN:
                myTTS =textToSpeech.getInstance(this);
                if(barridoPantalla != null)
                    barridoPantalla.updateSharePrefs(sharedPrefsDefault).cambiarEstadoBarrido();
                boolean isEnableScreenScanning = enableDisableScreenScanning();
                editarPicto = sharedPrefsDefault.getBoolean(getString(R.string.str_editar_picto), true);
                if (data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    if (extras.getBoolean(getString(R.string.boolean_cambio_idioma), false) || extras.getBoolean(getString(R.string.boolean_cambio_mano), false) || !isEnableScreenScanning) {
                        json.setmJSONArrayTodosLosPictos(json.getmJSONArrayTodosLosPictos());
                        Reset();
                        prepareLayout();
                        recreate();
                    }
                }
                CustomToast.getInstance(this).updateToastMessageLetters();
                break;
            case ConstantsMainActivity.LOGIN_ACTIVITY:
                Log.d(TAG, "onActivityResult: enter LoginActivity");
                break;
            case ConstantsMainActivity.AVATAR:
                loadAvatar();
                break;
            case ConstantsMainActivity.MY_DATA_CHECK_CODE:
                    myTTS = textToSpeech.getInstance(this);
                break;
        }
    }

    private void consultarPago() {
        new LicenciaUsuario(getApplicationContext());
        final DatabaseReference pagoRef = firebaseUtils.getmDatabase();

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
                    barridoPantalla.updateSharePrefs(sharedPrefsDefault).cambiarEstadoBarrido();
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
        if (traduccion) {
            if (myTTS != null) {
                CompartirArchivos compartirArchivos = new CompartirArchivos(this, myTTS);
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
        if (barridoPantalla!= null)
            if(barridoPantalla.isBarridoActivado()) {
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

                    if (barridoPantalla.isScrollMode() || barridoPantalla.isScrollModeClicker()) {
                        if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                            if (barridoPantalla.isScrollMode())
                                getFunction_scroll().HacerClickEnTiempo();
                            barridoPantalla.avanzarBarrido();
                        } else {
                            if (barridoPantalla.isScrollMode())
                                getFunction_scroll().HacerClickEnTiempo();
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
            case Principal.TRIM_MEMORY_UI_HIDDEN:
                break;
            case Principal.TRIM_MEMORY_RUNNING_MODERATE:
            case Principal.TRIM_MEMORY_RUNNING_LOW:
            case Principal.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
            case Principal.TRIM_MEMORY_BACKGROUND:
            case Principal.TRIM_MEMORY_MODERATE:
                break;
            default:
        }
    }

    public void initBarrido() {
        /*
         * Preparo el inicio del barrido de pantalla, para eso es necesario pasarle el listado de objetos
         * */
        ArrayList<View> listadoObjetosBarrido = new ArrayList<>();
        barridoPantalla = new BarridoPantalla(this, addObjects(listadoObjetosBarrido));
        if (barridoPantalla.isBarridoActivado() && barridoPantalla.devolverpago()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    btnBarrido.setVisibility(View.GONE);
                }
            });
        }
        if (barridoPantalla.isBarridoActivado())
            editarPicto = false;
    }

    private ArrayList<View> addObjects(ArrayList<View> listadoObjetosBarrido){
        listadoObjetosBarrido.add(Opcion1);
        listadoObjetosBarrido.add(Opcion2);
        listadoObjetosBarrido.add(Opcion3);
        listadoObjetosBarrido.add(Opcion4);
        listadoObjetosBarrido.add(botonFavoritos);
        listadoObjetosBarrido.add(borrar);
        listadoObjetosBarrido.add(talk);
        listadoObjetosBarrido.add(masPictos);
        listadoObjetosBarrido.add(todosLosPictos);
        listadoObjetosBarrido.add(resetButton);
        return listadoObjetosBarrido;
    }

    public void CargarJson() {
        initJson();
        try {
            json.initJsonArrays();
            json.cargarPictosSugeridosJson();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException e) {
            e.printStackTrace();
        }

        if (json.getmJSONArrayTodosLosPictos() != null && json.getmJSONArrayTodosLosPictos().length() > 0) {
            try {
                if (pictoPadre == null || pictoPadre.getInt("id") == 0)
                    pictoPadre = json.getPictoFromId2(0);

                cuentaMasPictos = 0;
                if(pictoPadre != null)
                    loadOptions(json, pictoPadre);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        if(ValidateContext.isValidContext(this)){
            Log.d(TAG, "addOption: " + opcion.toString());
            Pictogram pictogram = new Pictogram(opcion, ConfigurarIdioma.getLanguaje());
            picto.setUpGlideAttatcher(this);
            picto.setUpContext(this);
            picto.setPictogramsLibraryPictogram(pictogram);
            picto.setVisibility(View.VISIBLE);
            formatoTransparencia(picto, opcion);
            picto.startAnimation(animation);
        }
    }

    /**
     * use custom_picto opcion , animation alphaAnimation
     */
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
            setInvisibleOption(Opcion2,animation);
            setInvisibleOption(Opcion3,animation);
            setInvisibleOption(Opcion4,animation);
        } else if (opcion2 == null && opcion1 != null) {
            setInvisibleOption(Opcion3,animation);
            setInvisibleOption(Opcion4,animation);
        } else if (opcion3 == null && opcion2 != null) {
            setInvisibleOption(Opcion4,animation);
        } else if (opcion4 == null && opcion3 != null && Opcion3.getVisibility() == View.VISIBLE) {
            Opcion4.setVisibility(View.VISIBLE);
        }
    }

    private void setInvisibleOption(PictoView option,Animation animation){
        option.startAnimation(animation);
        option.setVisibility(View.INVISIBLE);
        option.setEnabled(false);
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

    private void function_clickOption(JSONObject pictoPadre, timer_pictogram_clicker pictogram_clicker) {
        if (!pictogram_clicker.isClicked()) {
            pictogram_clicker.disableClick();
            click(pictoPadre);
            pictogram_clicker.resetClick();
        }
    }


    @Override
    public void OnClickBarrido() {
        if (getFunction_scroll().isClickEnabled() && barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()).getId() == R.id.btnTodosLosPictos)
            onClick(barridoPantalla.getmListadoVistas().get(barridoPantalla.getPosicionBarrido()));
        else if (!getFunction_scroll().isClickEnabled()) {
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
        Log.d(TAG, "onNavigationItemSelected: ");
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_parar:
                //Registo que uso un funcion que nos interesa que use
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Silence");
                if (mute) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
                    mute = false;
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                    mute = true;
                }
                myTTS.mute();
                sharedPrefs.edit().putBoolean("mBoolMute", mute).apply();
                break;
            case R.id.action_settings:
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Settings");
                //Abrimos otra pantalla
                isSettings = true;
                Intent intent12 = new Intent(Principal.this, prefs.class);
                startActivityForResult(intent12, IntentCode.CONFIG_SCREEN.getCode());
                return true;
            case R.id.ubic:
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Location");
                useLocation();
                return true;
            case R.id.exit:
                this.doubleBackToExitPressedOnce = true;
                super.onBackPressed();
                break;
            case R.id.tutorial:
                Intent intent1 = new Intent(this, LoginActivity2Step3.class);
                intent1.putExtra("comingFromMainActivity", true);
                startActivity(intent1);
                break;
            case R.id.logout:
                getAnalyticsFirebase().customEvents("Touch", "Principal", "LogOut");
                user.logOut();
                break;
            case R.id.report:
                //NOTA firebase analitycs
                getAnalyticsFirebase().customEvents("Touch", "Principal", "Report");
                if (sharedPrefsDefault.getInt("premium", 0) == 1) {
                    Intent i = new Intent(getApplicationContext(), ActivityInformes.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Principal.this, LicenciaExpirada.class);
                    startActivity(i);
                }
                break;
            case R.id.about:
                //NOTA firebase analitycs
                getAnalyticsFirebase().customEvents("Touch", "Principal", "About that");
                Intent intent = new Intent(getApplicationContext(), AboutOttaa.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void useLocation() {
        if (placesImplementation != null) {
            if (placesImplementation.typesSizes() > 0) {
                Place place = placesImplementation.getPlace();
                String name = placesImplementation.getPlaceName(place);
                String placeType = placesImplementation.getPlaceType(place);
                Log.d(TAG, "onOptionsItemSelected: " + name + " " + placeType);
                locationItem.setTitle(name + " : " + placesImplementation.getPlaceName(placeType));
                json.setPlaceName(placeType);
            }else{
                placesImplementation.locationRequest();
                if(myTTS != null)
                myTTS.mostrarAlerta(getResources().getString(R.string.no_location));
            }
        }
    }

    public BarridoPantalla getBarridoPantalla() {
        return barridoPantalla;
    }

    public ScrollFunctionMainActivity getFunction_scroll() {
        if(function_scroll == null)
            function_scroll = new ScrollFunctionMainActivity(this,this);
        return function_scroll;
    }

    public void loadDrawable(GlideAttatcher attatcher, Pictogram pictogram, ImageView imageView) {
        if (pictogram.getEditedPictogram().isEmpty()) {
            attatcher.UseCornerRadius(true).loadDrawable(this.getResources().getDrawable(this.getContext().getResources().getIdentifier(pictogram.getPictogram(),
                    "drawable", this.getPackageName())), imageView);
        } else {
            File picto = new File(pictogram.getEditedPictogram());
            if (picto.exists())
                attatcher.UseCornerRadius(true).loadDrawable(picto, imageView);
            else
                attatcher.UseCornerRadius(true).loadDrawable(Uri.parse(pictogram.getUrl()), imageView);
        }
    }

    public void showAvatar() {
        remoteConfigUtils.setActivateDeactivateConfig(Principal.this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    if (remoteConfigUtils.enableAvatar()) {
                        movableFloatingActionButton.setVisibility(View.VISIBLE);
                        String phrase = remoteConfigUtils.avatarMessages();
                        myTTS.getUtilsTTS().setTtsListener(new TTSListener() {
                            @Override
                            public void TTSonDone() {
                                Log.d(TAG, "TTSonDone: ");
                                Principal.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        avatar.finishTalking();
                                    }
                                });
                            }

                            @Override
                            public void TTSOnAudioAvailable() {

                            }

                            @Override
                            public void TTSonError() {

                            }
                        });
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!validateMessages(phrase)){
                                    myTTS.hablar(avatar.animateTalk(phrase));
                                    sharedPrefsDefault.edit().putString("avatarMessage",phrase).apply();
                                }
                                else{
                                    avatar.finishTalking();
                                }

                            }
                        }, 10500);

                    }
                }

            }
        });
    }

    public void shareAction() {
        getAnalyticsFirebase().customEvents("Touch", "Principal", "Share");
        if (historial.getListadoPictos().size() > 0) {
            if (!sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {
                if (myTTS != null) {
                    CompartirArchivos compartirArchivos = new CompartirArchivos(getContext(), myTTS);
                    compartirArchivos.setHistorial(historial.getListadoPictos());
                    compartirArchivos.seleccionarFormato(Oracion);
                }
            } else if (sharedPrefsDefault.getBoolean(getString(R.string.mBoolModoExperimental), false)) {
                Log.d(TAG, "onClick: " + historial.getListadoPictos().toString());
                traducirfrase = new traducirTexto(getApplication());
                if (Oracion.isEmpty() && historial.getListadoPictos().size() > 0)
                    CargarOracion(historial.getListadoPictos().get(0), sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"));
                Oracion = EjecutarNLG();
                traducirfrase.traducirIdioma(this, Oracion, "en", sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"), true);
            }
            // if(myTTS().devolverPathAudio().exists())
        }else{
            myTTS.mostrarAlerta(getResources().getString(R.string.createPhrasesAlert));
        }
    }

    private void startGames() {
        analyticsAction("Accessibility", "Touch", "Principal", "Game");
        Intent intent = new Intent(this, MainJuegos.class);
        this.startActivity(intent);
    }

    private void startGroupGallery() {
        Intent intent2 = new Intent(Principal.this, GaleriaGrupos2.class);
        intent2.putExtra("Boton", 0);
        startActivityForResult(intent2, IntentCode.GALERIA_GRUPOS.getCode());
    }

    private void startIconSelector() {
        Intent intent1 = new Intent(this, LoginActivity2Avatar.class);
        intent1.putExtra("comingFromMainActivity", true);
        startActivityForResult(intent1, IntentCode.AVATAR.getCode());
    }

    private void startFavoritePhrases() {
        initCustomFavoritePhrase();
    }

    private void analyticsAction(String event0, String event1, String activity, String action) {
        if (barridoPantalla.isBarridoActivado())
            getAnalyticsFirebase().customEvents(event0, activity, action);
        else
            getAnalyticsFirebase().customEvents(event1, activity, action);
    }

    private void sayPictogramName(String name) {
        if (sharedPrefsDefault.getBoolean(getResources().getString(R.string.say_pictogram_name_key), false)) {
            myTTS.hablar(name);
        }
    }

    private void onClickOption(JSONObject option, timer_pictogram_clicker clicker) {
        if (option == null) {
            analyticsAction("Accessibility", "Touch", "Principal", "More Options");
            startGroupGallery();
        } else {
            analyticsAction("Accessibility", "Touch", "Principal", "Add Pictogram");
            if (!barridoPantalla.isBarridoActivado())
                function_clickOption(option, clicker);
            else
                click(option);
        }
    }

    public void initCustomFavoritePhrase() {
        int option = sharedPrefsDefault.getInt("favoritePhrase", 0);
        switch (option) {
            case 0:
                analyticsAction("Accessibility", "Touch", "Principal", "More Used Phrases");
                SayActivityName.getInstance(this).sayTitle(getResources().getString(R.string.frases_musadas));
                startActivity(new Intent(this, MostUsedPhrases.class));
                break;
            case 1:
                analyticsAction("Accessibility", "Touch", "Principal", "Favorite Phrases");
                SayActivityName.getInstance(this).sayTitle(getResources().getString(R.string.favorite_phrases));
                startActivity(new Intent(this, FavoritePhrases.class));
                break;
        }
    }

    private void galeriaGruposResult(Intent data) {
        if (data != null) {
           //todo review here because the json can be null
            json.setmJSONArrayTodosLosPictos(json.getmJSONArrayTodosLosPictos());
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

    private void editPictoResult(Intent data) {
        Log.d(TAG, "onActivityResult: EditarPicto");
        try {
            loadOptions(json, pictoPadre);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAvatar() {
        Context context = this;
        AtomicBoolean validContext = new AtomicBoolean(false);
                Executor executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    validContext.set(ValidateContext.isValidContext(context));
                    avatarUtils = new AvatarUtils(context, menuAvatarIcon);
                    handler.post(() -> {
                        if(validContext.get()) {
                                avatarUtils.getFirebaseAvatar();
                                CustomToast.getInstance(context).updateToastIcon(context);
                            }
                        }
                    );
                });
    }

    private void prepareLayout() {
        if (sharedPrefsDefault.getBoolean("skillHand", false)) {
            setContentView(R.layout.activity_main_rigth);
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    private boolean enableDisableScreenScanning() {
        Log.e(TAG, "enableDisableScreenScanning visibility: "+ btnBarrido.getVisibility() );
        if (!barridoPantalla.isBarridoActivado()) {
            if (btnBarrido.getVisibility() == View.GONE){
                Log.e(TAG, "enableDisableScreenScanning visibility: "+ btnBarrido.getVisibility() );
                return true;
            }
            else{
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Stuff that updates the UI
                    Log.e(TAG, "enableDisableScreenScanning visibility: "+ btnBarrido.getVisibility() );
                    btnBarrido.setVisibility(View.GONE);
                }
            });
                return false;
            }
        } else if (barridoPantalla.isBarridoActivado() && btnBarrido.getVisibility() == View.GONE) {
            runOnUiThread(() -> {
                // Stuff that updates the UI
                Log.e(TAG, "enableDisableScreenScanning visibility: "+ btnBarrido.getVisibility() );
                btnBarrido.setVisibility(View.VISIBLE);

            });
            return true;
        }

        return true;
    }

    private void initComponents() {
        initFlags();
        initBackUp();
        initMenu();
        initSelectionComponents();
        initActionButtons();
        initPictograms();
        initAvatar();
        initTTS();
        initBarrido();
        new initComponentsClass().execute();

    }

    private void initLocationIcon() {
        locationItem = navigationView.getMenu().findItem(R.id.ubic);
    }

    private void initFirebaseComponents() {
        user = new User(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        mDatabase = firebaseUtils.getmDatabase();
        analitycsFirebase = new AnalyticsFirebase(this);
    }

    private void initDefaultSettings(Context context) {
        sharedPrefsDefault.edit().putBoolean("esmoderador", false).apply();
        ConfigurarIdioma.setLanguage(sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"));
        Log.d(TAG, "ConfigurarIdioma : " + ConfigurarIdioma.getLanguaje());
        new ConfigurarIdioma(getApplicationContext(), ConfigurarIdioma.getLanguaje());
        initJson(context);
        json.initSharedPrefs();
        Log.d(TAG, "hashCode: " + json.hashCode());
        timeStamp = getTimeStamp();
        historial = new Historial(json);
        sharedPrefsDefault.edit().putBoolean("usuario logueado", true).apply();
        cuentaMasPictos = 0;
        placeTypeActual = 0;
        placeActual = 0;
    }

    private void initJson(Context context){
        Json.getInstance().setmContext(context);
        json = Json.getInstance();
    }

    private void initJson(){
        Json.getInstance().setmContext(this);
        json = Json.getInstance();
    }

    private void initFlags() {
        sharedPrefs = getSharedPreferences(sharedPrefsDefault.getString(getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);
        TutoFlag = sharedPrefs.getBoolean("PrimerUso", true);
        mute = sharedPrefsDefault.getBoolean("mBoolMute", true);
        editarPicto = sharedPrefsDefault.getBoolean(getString(R.string.str_editar_picto), true);
    }

    private void initPlaceImplementationClass() {
        if (sharedPrefsDefault.getBoolean(getString(R.string.bool_ubicacion), false)) {
            placesImplementation = new PlacesImplementation(this);
            placesImplementation.iniciarClientePlaces();
            placesImplementation.locationRequest();
            useLocation();
        }
    }

    private void firstUserAuthVerification() {
        if (user.getmAuth().getCurrentUser() != null) {
            subirArchivos = new SubirArchivosFirebase(this);
            /**
             * Referencias Storage grupos pictos frases
             * */
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
            mPictosDatabaseRef = firebaseUtils.getmDatabase().child("Pictos").child(user.getmAuth().getCurrentUser().getUid()).child("URL_pictos_" + ConfigurarIdioma.getLanguaje());
            mFrasesDatabaseRef = firebaseUtils.getmDatabase().child("Frases").child(user.getmAuth().getCurrentUser().getUid()).child("URL_frases_" + ConfigurarIdioma.getLanguaje());
            mGruposDatabaseRef = firebaseUtils.getmDatabase().child("Grupos").child(user.getmAuth().getCurrentUser().getUid()).child("URL_grupos_" + ConfigurarIdioma.getLanguaje());
            consultarPago();
            subirArchivos.subirFotosOffline();

        } else {
            Log.e(TAG, "onCreate: CurrentUser == null");
            Intent mainIntent = new Intent().setClass(
                    Principal.this, LoginActivity2.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void initBackUp() {
        mFirebaseBackup = new SubirBackupFirebase(this);
        setPrimerBackupTimeLocal();
        guardarBackupLocal();
    }

    private void initMenu() {
        handlerHablar.postDelayed(animarHablar, 4000);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setOnClickListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setOnApplyWindowInsetsListener((view, windowInsets) -> windowInsets);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        constraintBotonera = findViewById(R.id.constraintRightButtons);
        inmersiveMode = new InmersiveMode(this);
        gesture = new Gesture(drawerLayout);
        initLocationIcon();
    }


    private void initSelectionComponents() {
        if(selectedImage == null)
            selectedImage = new ArrayList<>();
        selectedImage.add(findViewById(R.id.Seleccion1));
        selectedImage.add(findViewById(R.id.Seleccion2));
        selectedImage.add(findViewById(R.id.Seleccion3));
        selectedImage.add(findViewById(R.id.Seleccion4));
        selectedImage.add(findViewById(R.id.Seleccion5));
        selectedImage.add(findViewById(R.id.Seleccion6));
        selectedImage.add(findViewById(R.id.Seleccion7));
        selectedImage.add(findViewById(R.id.Seleccion8));
        selectedImage.add(findViewById(R.id.Seleccion9));
        selectedImage.add(findViewById(R.id.Seleccion10));
        for (int i = 0; i < selectedImage.size(); i++) {
            AjustarAncho(getResources().getIdentifier("Seleccion"+(i+1),"id",getPackageName()));
        }
    }


    private void setOnLongClickListener(){
        botonFavoritos.setOnClickListener(this);
        talk.setOnClickListener(this);
        for (int i = 0; i < selectedImage.size()-1; i++) {
            setClickLongListener(selectedImage.get(i));
        }
        setClickLongListener(borrar);
        setClickLongListener(masPictos);
        setClickLongListener(todosLosPictos);
        setClickLongListener(resetButton);
        setClickLongListener(btn_share);
        setClickOnTouchListener(btnBarrido);
        setClickLongListener(Opcion1);
        setClickLongListener(Opcion2);
        setClickLongListener(Opcion3);
        setClickLongListener(Opcion4);
    }

    private void initActionButtons() {
        borrar = findViewById(R.id.btn_borrar);
        botonFavoritos = findViewById(R.id.btnFavoritos);
        masPictos = findViewById(R.id.btnMasPictos);
        todosLosPictos = findViewById(R.id.btnTodosLosPictos);
        resetButton = findViewById(R.id.action_reiniciar);
        btn_share = findViewById(R.id.action_share);
        btnBarrido = findViewById(R.id.btnBarrido);
        btnBarrido.setVisibility(View.GONE);
        talk = findViewById(R.id.btnTalk);
        animationView = findViewById(R.id.lottieAnimationView);
    }

    private void initPictograms() {
        Opcion1 = findViewById(R.id.Option1);
        Opcion2 = findViewById(R.id.Option2);
        Opcion3 = findViewById(R.id.Option3);
        Opcion4 = findViewById(R.id.Option4);
        Agregar = new PictoView(this);
        Agregar.setCustom_Color(R.color.Black);
        Agregar.setCustom_Texto("");
        Agregar.setCustom_Img(getDrawable(R.drawable.agregar_picto_transp));
        Agregar.setIdPictogram(0);
        Opcion1_clicker = new timer_pictogram_clicker(this);
        Opcion2_clicker = new timer_pictogram_clicker(this);
        Opcion3_clicker = new timer_pictogram_clicker(this);
        Opcion4_clicker = new timer_pictogram_clicker(this);
    }

    private void initFirstPictograms() {
        new loadPictograms().execute();
    }

    private void initAvatar() {
        menuAvatarIcon = navigationView.getHeaderView(0).findViewById(R.id.avatarIconMain);
        menuAvatarIcon.setOnClickListener(this);
        movableFloatingActionButton = findViewById(R.id.movableButton);
        movableFloatingActionButton.setOnClickListener(this);
        avatar = new Avatar(this, movableFloatingActionButton);
    }

    private void initTTS() {
        //  Vibrator vibe = (Vibrator) Principal.this.getSystemService(Context.VIBRATOR_SERVICE);
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
    }

    private void showMenu() {
        try {
            if (!sharedPrefsDefault.getBoolean(getString(R.string.skillhand), false) && sharedPrefsDefault.getInt(getString(R.string.showmenu), 3) > 0) {
                drawerLayout.open();
                int value = sharedPrefsDefault.getInt(getString(R.string.showmenu), 4);
                value--;
                sharedPrefsDefault.edit().putInt(getString(R.string.showmenu), value).apply();
                new Handler().postDelayed(() -> drawerLayout.close(), 5000);
            }
        } catch (Exception ex) {
            Log.e(TAG, "showMenu: " + ex.getMessage());
        }
    }

    private void setClickLongListener(View v) {
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
    }

    private void setClickOnTouchListener(View v) {
        v.setOnClickListener(this);
        v.setOnTouchListener(this);
    }

    private void uploadFiles() {
        connectionDetector = new ConnectionDetector(getApplicationContext());
        if (subirArchivos != null) {
            subirArchivos.setInterfaz(this);
        }
        if (user.getmAuth().getCurrentUser() != null && subirArchivos != null) {
            subirArchivos.userDataExists(subirArchivos.getmDatabase(user.getmAuth(), "Pictos"), subirArchivos.getmDatabase(user.getmAuth(), "Grupos"), subirArchivos.getmDatabase(user.getmAuth(), "Frases"));
        }
    }

    private void longClick(PictoView pictoView, JSONObject json) {
        onLongOpcion = json;
        if (pictoView.getAlpha() != (0.65) || sharedPrefsDefault.getBoolean(getString(R.string.ismoderator), false)) {
            new PopupMenuUtils(this, pictoView, this);
        }
    }


    public void loadChilds(JSONObject padre, Animation alphaAnimation){
            if(padre!= null){
                json.cargarOpciones(padre, cuentaMasPictos,this);
            }
            else {
                if(ConnectionDetector.isNetworkAvailable(getApplicationContext())){
                    json.sumarFallas();
                    loadOptions(json,padre);
                }
            }

    }

    public void downloadFailedFile(int size) {

        getFirebaseDialog().setTitle(getApplicationContext().getResources().getString(R.string.edit_sync));
        getFirebaseDialog().setMessage(getApplicationContext().getResources().getString(R.string.edit_sync_pict));
        getFirebaseDialog().mostrarDialogo();
        File rootPath = new File(this.getCacheDir(), "Archivos_OTTAA");
        ObservableInteger observableInteger = loadObservableInteger(size,rootPath);
        observableInteger.set(0);
        mBajarJsonFirebase.bajarJuego(ConfigurarIdioma.getLanguaje(), rootPath);
        mBajarJsonFirebase.bajarFrasesFavoritas(ConfigurarIdioma.getLanguaje(), rootPath);
    }

    public ObservableInteger loadObservableInteger(int size,File rootPath) {
        ObservableInteger observableInteger = new ObservableInteger();
        observableInteger.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                switch (newValue){
                    case 0:
                        mBajarJsonFirebase.bajarPictos(ConfigurarIdioma.getLanguaje(), rootPath, observableInteger);
                        break;
                    case 1:
                        mBajarJsonFirebase.bajarGrupos(ConfigurarIdioma.getLanguaje(), rootPath, observableInteger);
                        break;
                    case 2:
                        mBajarJsonFirebase.bajarFrases(ConfigurarIdioma.getLanguaje(), rootPath, observableInteger);
                        break;
                }
                System.out.println("size:"+ size);
                if (observableInteger.get() == size) {
                    getFirebaseDialog().destruirDialogo();
                    json.resetearError();
                    CargarJson();
                }
            }
        });
        return observableInteger;
    }

    public void speakBlock() {
        Log.d(TAG, "speak: Oracion:" + Oracion);
        handlerHablar.removeCallbacks(animarHablar);
        if (mute) {
            speakAction();
        } else {
            myTTS.mostrarAlerta(Oracion, getAnalyticsFirebase());
        }
        handlerHablar.postDelayed(animarHablar, 10000);
        uploadUserPhrases();
    }

    public void speakAction() {
        myTTS.hablar(Oracion, getAnalyticsFirebase());
        savePhrases(Oracion, historial);
        Log.d(TAG, "speak: Time in millis: " + System.currentTimeMillis() / 1000);
        resetSpeakAction();
    }

    public void uploadUserPhrases() {
        if (user.getmAuth().getCurrentUser() != null) {
            SubirArchivosFirebase subirArchivos = new SubirArchivosFirebase(getApplicationContext());
            subirArchivos.subirPictosFirebase(subirArchivos.getmDatabase(user.getmAuth(), Constants.PICTOS), subirArchivos.getmStorageRef(user.getmAuth(), Constants.PICTOS));
            subirArchivos.subirFrasesFirebase(subirArchivos.getmDatabase(user.getmAuth(), Constants.Frases), subirArchivos.getmStorageRef(user.getmAuth(), Constants.Frases));
        }
    }

    public void savePhrases(String phrase, Historial historial) {
        try {
            json.crearFrase(phrase, historial.getListadoPictos(), Calendar.getInstance().getTimeInMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json.getmJSONArrayTodasLasFrases().length() > 0)
            if (!json.guardarJson(Constants.ARCHIVO_FRASES))
                Log.e(TAG, "speak: Error al guardar Frases");
    }

    public void resetSpeakAction() {
        if (sharedPrefsDefault.getBoolean(getString(R.string.hablarborrar), true)) {
            getAnalyticsFirebase().customEvents("Talk", "Principal", "Talk and Erase");
            Reset();
        }
    }

    public AnalyticsFirebase getAnalyticsFirebase(){
        if(analitycsFirebase == null)
            analitycsFirebase = new AnalyticsFirebase(this);
        return analitycsFirebase;
    }

    public void nlgTalkAction(){
        Oracion = EjecutarNLG();
        if (!sharedPrefsDefault.getString(getString(R.string.str_idioma), "en").equals("en")) {
            traducirFrase = new TraducirFrase(this, sharedPrefsDefault, animationView, getApplicationContext(), Oracion);
            traducirFrase.setOracion(Oracion);
            traducirFrase.execute();
        } else {
            speak();
        }
    }


    public boolean validateMessages(String message){
        if(message != null)
            return sharedPrefsDefault.getString(getString(R.string.avatarmessage),"").equals(message);
        return false;
    }

    public Progress_dialog_options getFirebaseDialog(){
        if(firebaseDialog == null)
            firebaseDialog = new Progress_dialog_options(this);
        return firebaseDialog;
    }

    @Override
    public void pictogramsAreSorted(JSONArray array) {
        JSONArray opciones = array;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Animation alphaAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.alpha_show);
                loadChildOption(opciones, 0, alphaAnimation);
                loadChildOption(opciones, 1, alphaAnimation);
                loadChildOption(opciones, 2, alphaAnimation);
                loadChildOption(opciones, 3, alphaAnimation);
            }
        });

    }

    public class initComponentsClass extends AsyncTask<Void,Void,Void>{
        boolean used = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initFirebaseComponents();
            firstUserAuthVerification();
            initDefaultSettings(Principal.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            user.connectClient();
            initFirstPictograms();
            uploadFiles();
            initPlaceImplementationClass();
            showMenu();
            if (TutoFlag) {
                sharedPrefs.edit().putBoolean("PrimerUso", false).apply();
            }
            navigationControls = new PrincipalControls(Principal.this);
            movableFloatingActionButton.setIcon();
            remoteConfigUtils = RemoteConfigUtils.getInstance();
            loadAvatar();
            showAvatar();
            setOnLongClickListener();
        }
    }

    public class loadPictograms extends AsyncTask<Void,Void,Void> implements FailReadPictogramOrigin {
        boolean used;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (json.getmJSONArrayTodosLosPictos() != null && json.getmJSONArrayTodosLosPictos().length() > 0&& historial.getListadoPictos().isEmpty()) {
               pictoPadre = json.getPictoFromId2(0);
                new Json0Recover().backupPictogram0(getApplicationContext(),pictoPadre,this);
            }
            if(pictoPadre == null){
                new Json0Recover().restorePictogram0(getApplicationContext(),this);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
           if(used) {
                used = false;
               ResetSeleccion();
           }
        }

        @Override
        public void setParent(JSONObject parent) {
            json.getmJSONArrayTodosLosPictos().put(parent);
            json.setmJSONArrayTodosLosPictos(json.getmJSONArrayTodosLosPictos());
            json.guardarJson(Constants.ARCHIVO_PICTOS);
            try {
                json.refreshJsonArrays();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FiveMbException e) {
                e.printStackTrace();
            }finally {
                pictoPadre =  json.getPictoFromId2(0);
            }
            loadDialog();
        }

        @Override
        public void loadDialog() {
            if (pictoPadre != null) {
                loadOptions(json, pictoPadre);   // y despues cargamos las opciones con el orden correspondiente
                used = true;
            }
        }
    }

    private boolean requestScreenScanningIsEnabled(){
        if(barridoPantalla != null)
           return barridoPantalla.isBarridoActivado();
        return false;
    }
}