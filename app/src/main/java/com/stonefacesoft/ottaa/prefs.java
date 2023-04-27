package com.stonefacesoft.ottaa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Adapters.Item_adapter;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Devices_Version_Dialog;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Progress_dialog_options;
import com.stonefacesoft.ottaa.Dialogos.custom_dialog_option_item;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Interfaces.EstaConectada;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.interface_show_dialog_options;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.Accesibilidad.ControlFacial;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.Handlers.HandlerComunicationClass;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.constants.ConstantsAnalyticsValues;
import com.stonefacesoft.ottaa.utils.preferences.PersonalSwitchPreferences;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.Locale;

/**
 * Created by Hector on 27/03/2015.
 * <h3>Objetive</h3>
 * Preference setting activity
 */


//https://github.com/afarber/android-newbie/blob/master/MyPrefs/res/xml/preferences.xml


public class prefs extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, FirebaseSuccessListener, EstaConectada, View.OnClickListener, Preference.OnPreferenceChangeListener, interface_show_dialog_options {


    private final static String TAG = "prefs";
    public static String BOOL_TTS;
    public static String BOOL_SUBTITULO;
    public static String STR_TAM_SUBTITULO;
    public static String BOOL_SUBTITULO_MAYUSCULA;
    public static String BOOL_HABLAR_BORRAR;
    public static String BOOL_CONTROLFACIAL;
    public static String BOOL_UBICACION;
    public static String BOOL_EDITAR;
    public static String BOOL_BARRIDO;
    public static String BOOL_TIPO_BARRIDO;
    public static String STR_PRESENTACION;
    public static String STR_EDITAR;
    public static String STR_Modo_Exp;
    public static String NUM_Vel;
    public static String NUM_VelB;
    public static String NUM_Tono;
    public static String STR_IDIOMA;
    public static String STR_MANOHABIL;
    public static String STR_LOGOUT;
    public static String STR_TUTORIAL;
    public static String STR_ABOUT;
    public static String STR_BACKUP;
    public static String STR_OPCIONES;
    public static String STR_Velocidad_Click;
    public static String STR_SCROLL_FUNCTION;
    public static String STR_SCROLL_FUNCTION_CLICKER;
    public static String STR_SCROLL_SPEED;
    public static String STR_BARRIDO_PANTALLA;
    public static String STR_SIP_AND_PUFF;
    public static String BOOL_SAY_PICTOGRAM;

    public static String CHATGPT;
    public static String REPEATPHRASE;
    int permission = 0;
    DownloadFilesTask downloadFilesTask;
    private String strIdioma_original;
    private boolean status;
    private ObservableInteger obsInt;
    private ControlFacial mFaceControl;
    private AnalyticsFirebase analyticsFirebase;
    private SharedPreferences sharedPrefsDefault;
    private PersonalSwitchPreferences mBoolTTS;
    private PersonalSwitchPreferences mBoolSubtitulo;
    private PersonalSwitchPreferences mBoolUbicacion;
    private PersonalSwitchPreferences mBoolUsarScroll;
    private PersonalSwitchPreferences mBoolUsarScrollClick;
    private PersonalSwitchPreferences mBoolTipoBarrido;
    private PersonalSwitchPreferences mBoolBarridoPantalla;
    private PersonalSwitchPreferences mBoolExperimental;
    private PersonalSwitchPreferences mBoolSipAndPuff;
    private PersonalSwitchPreferences mBoolSayPictogram;
    private PersonalSwitchPreferences mBoolChatGPT;
    private PersonalSwitchPreferences mBoolRepeatPhrase;
    private StorageReference mStorageRef;
    // private ProgressDialog progressDialog,dialog;
    private Progress_dialog_options firebaseDialog;
    private String locale;
    private PersonalSwitchPreferences mBoolBarrido;
    private PersonalSwitchPreferences mBoolEditar;
    private PersonalSwitchPreferences mboolHablarBorrar;
    private PersonalSwitchPreferences mboolControlFacial;
    private PersonalSwitchPreferences mOpciones;
    private Preference mStrIdioma;
    private Preference mStrSubtituloTamano;
    private Preference mOrientacionJoystick;
    private Preference mEdadUsuario;
    private Preference mSexoUsuario;
    private Preference edad;
    private Preference manoHabil;
    private Preference mNumVelocidad, mNumVelocidadBarrido, mNumVelocidadClicker, mNumTono;
    private Preference mNumVelocidadScroll;
    private Preference device1, device2;
    private EditTextPreference mStrPresentacion;
    private FirebaseAuth mAuth;
    private custom_dialog_option_item customDialogOptionItem;


    // private BajarJsonFirebase bajarJsonFirebase;
    private Json json;
    private boolean cambioIdioma;
    private boolean cambioDeLado;
    private boolean cambioBarrido;
    private textToSpeech myTTS;
    private BajarJsonFirebase bajarJsonFirebase;
    private HandlerComunicationClass handlerComunicationClass;
    private String message = "";
    private FirebaseUtils firebaseUtils;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        analyticsFirebase = new AnalyticsFirebase(this);
        initComponents();
        isConnected();
        habilitarFuncionesPremiun(sharedPrefsDefault.getInt("premium", 0));
        if (sharedPrefsDefault.getInt("premium", 0) == 0) {
            mBoolBarrido.setChecked(false);
            mBoolUbicacion.setChecked(false);

        }
        bajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, getApplicationContext());
        bajarJsonFirebase.setInterfaz(this);
        firebaseDialog = new Progress_dialog_options(this);
        handlerComunicationClass = new HandlerComunicationClass(this);


    }

    /**
     * Init the components of the preference activity
     */
    private void initComponents() {
        //Implemento el manejador de preferencias
        mAuth = FirebaseAuth.getInstance();
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Json.getInstance().setmContext(this);
        json = Json.getInstance();
        strIdioma_original = sharedPrefsDefault.getString(getString(R.string.str_idioma), "en");
        downloadFilesTask = new DownloadFilesTask(strIdioma_original);
        myTTS =textToSpeech.getInstance(this);

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        BOOL_TTS = getString(R.string.bool_TTS);
        BOOL_SUBTITULO = getString(R.string.bool_subtitulo);
        STR_TAM_SUBTITULO = getString(R.string.str_subtitulo_tamano);
        BOOL_SUBTITULO_MAYUSCULA = getString(R.string.bool_subtitulo_mayuscula);
        STR_PRESENTACION = getString(R.string.str_presentacion);
        NUM_Vel = getString(R.string.tts_velocidad);
        NUM_Tono = getString(R.string.pitch_tts);
        BOOL_UBICACION = getString(R.string.bool_ubicacion);
        NUM_VelB = "velocidad_barrido";
        BOOL_CONTROLFACIAL = "control_facial";
        BOOL_BARRIDO = getString(R.string.bool_barrido);
        BOOL_EDITAR = getString(R.string.bool_editar_picto);
        STR_IDIOMA = getString(R.string.str_idioma);
        BOOL_HABLAR_BORRAR = getString(R.string.talk_erase);
        STR_LOGOUT = getString(R.string.banderaUpload);
        STR_MANOHABIL = "skillHand";
        STR_TUTORIAL = getString(R.string.title_activity_tutorial);
        STR_ABOUT = getString(R.string.pref_header_acerca_de);
        STR_BACKUP = getString(R.string.pref_header_backup);
        BOOL_TIPO_BARRIDO = "tipo_barrido";
        STR_OPCIONES = "bool_sugerencias";
        STR_Velocidad_Click = getString(R.string.str_time_click);
        STR_SCROLL_FUNCTION = getResources().getString(R.string.usar_scroll);
        STR_SCROLL_FUNCTION_CLICKER = getResources().getString(R.string.usar_scroll_click);
        STR_SCROLL_SPEED = getResources().getString(R.string.scroll_speed);
        STR_BARRIDO_PANTALLA = "tipo_barrido_normal";
        STR_Modo_Exp = getResources().getString(R.string.mBoolModoExperimental);
        STR_SIP_AND_PUFF = getResources().getString(R.string.sip_and_puff);
        BOOL_SAY_PICTOGRAM = getResources().getString(R.string.say_pictogram_name_key);
        CHATGPT =getResources().getString(R.string.mBoolChatGPT);
        REPEATPHRASE = getResources().getString(R.string.repeat_pictogram_name_key);


        //PersonalSwitchPreference
        mBoolTTS = (PersonalSwitchPreferences) findPreference(BOOL_TTS);
        mBoolSubtitulo = (PersonalSwitchPreferences) findPreference(BOOL_SUBTITULO);
        mBoolBarrido = (PersonalSwitchPreferences) findPreference(BOOL_BARRIDO);
        mBoolUbicacion = (PersonalSwitchPreferences) findPreference(BOOL_UBICACION);
        mboolHablarBorrar = (PersonalSwitchPreferences) findPreference(BOOL_HABLAR_BORRAR);
        mboolControlFacial = (PersonalSwitchPreferences) findPreference(BOOL_CONTROLFACIAL);
        mOpciones = (PersonalSwitchPreferences) findPreference(STR_OPCIONES);
        mBoolEditar = (PersonalSwitchPreferences) findPreference(BOOL_EDITAR);
        mBoolExperimental = (PersonalSwitchPreferences) findPreference(STR_Modo_Exp);
        PersonalSwitchPreferences mBoolSubtituloMayuscula = (PersonalSwitchPreferences) findPreference(BOOL_SUBTITULO_MAYUSCULA);
        mBoolTipoBarrido = (PersonalSwitchPreferences) findPreference(BOOL_TIPO_BARRIDO);
        mBoolUsarScroll = (PersonalSwitchPreferences) findPreference(STR_SCROLL_FUNCTION);
        mBoolUsarScrollClick = (PersonalSwitchPreferences) findPreference(STR_SCROLL_FUNCTION_CLICKER);
        mBoolBarridoPantalla = (PersonalSwitchPreferences) findPreference(STR_BARRIDO_PANTALLA);
        mBoolSipAndPuff = (PersonalSwitchPreferences) findPreference(STR_SIP_AND_PUFF);
        mBoolSayPictogram = (PersonalSwitchPreferences) findPreference(BOOL_SAY_PICTOGRAM);
        mBoolChatGPT = (PersonalSwitchPreferences) findPreference(CHATGPT);
        mBoolRepeatPhrase = (PersonalSwitchPreferences) findPreference(CHATGPT);

        // preference
        mNumTono = findPreference(NUM_Tono);
        mNumVelocidad = findPreference(NUM_Vel);
        mNumVelocidadClicker = findPreference(STR_Velocidad_Click);
        mNumVelocidadBarrido = findPreference(NUM_VelB);
        mStrIdioma = findPreference(STR_IDIOMA);
        mSexoUsuario = findPreference("sexo");
        mEdadUsuario = findPreference("edad");
        mOrientacionJoystick = findPreference("posicion_joystick");
        mStrSubtituloTamano = findPreference(STR_TAM_SUBTITULO);
        mNumVelocidadScroll = findPreference(getResources().getString(R.string.scroll_speed));
        manoHabil = findPreference(STR_MANOHABIL);
        device1 = findPreference("v1");
        device2 = findPreference("v2");
        //Edit text preference
        mStrPresentacion = (EditTextPreference) findPreference(STR_PRESENTACION);
        getFragmentManager().executePendingTransactions();

        mStrIdioma.setOnPreferenceChangeListener(this);
        mSexoUsuario.setOnPreferenceChangeListener(this);
        mEdadUsuario.setOnPreferenceChangeListener(this);
        mOrientacionJoystick.setOnPreferenceChangeListener(this);
        manoHabil.setOnPreferenceChangeListener(this);
        device1.setOnPreferenceChangeListener(this);
        device2.setOnPreferenceChangeListener(this);

        createOnClickItemBarrido();
       /*
       buttonTutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
           @Override
           public boolean onPreferenceClick(Preference preference) {
               //Poner PrimerUso en true y lanzar tutorial
               SharedPreferences sharedPrefs = getSharedPreferences(sharedPrefsDefault.getString
                       (getString(R.string.str_userMail),"error"), Context.MODE_PRIVATE);
               sharedPrefs.edit().putBoolean("PrimerUso", true).apply();

                finish();
                return true;
            }
        });*/


        //Listener
        obsInt = new ObservableInteger();
        obsInt.set(0);

        obsInt.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                if (obsInt.get() == 1)
                    Log.d(TAG, "onIntegerChanged: obsInt 1");
                Log.d(TAG, "onIntegerChanged: Picto is downloaded");
                if (obsInt.get() == 2) {
                    Log.d(TAG, "onIntegerChanged: obsInt 2");
                    Log.d(TAG, "onIntegerChanged: Grupos is downloaded");
                    handlerComunicationClass.sendMessage(
                            Message.obtain(handlerComunicationClass, HandlerComunicationClass.DISMISSDIALOG, ""));
                    obsInt.set(0);
                }
                if (obsInt.get() == -1) {
                    handlerComunicationClass.sendMessage(
                            Message.obtain(handlerComunicationClass, HandlerComunicationClass.DISMISSDIALOG, ""));
                }
            }
        });

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
        //prefs_pfc:prefs_PreferenceChanged


    /*    if (BOOL_PRESENTACION.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
            mBoolPresentacion.setSummary(b ? getResources().getString(R.string.pref_activado) : getResources().getString(R.string.pref_desactivado));
        } else */
        if (BOOL_TTS.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
            mBoolTTS.setSummary(b ? getResources().getString(R.string.pref_activado) : getResources().getString(R.string.pref_desactivado));
         /* if (STR_PRESENTACION.equals(key)) {
            String str = sharedPrefs.getString(key, "");
            mStrPresentacion.setSummary(str);
        }*/
        } else if (NUM_Vel.equals(key)) {
            int i = sharedPrefs.getInt(key, 10);
            mNumVelocidad.setSummary(" " + i);
        } else if (NUM_VelB.equals(key)) {
            int i = sharedPrefs.getInt(key, 5);
            mNumVelocidadBarrido.setSummary(" " + i);
        } else if (NUM_Tono.equals(key)) {
            int i = sharedPrefs.getInt(key, 10);
            mNumTono.setSummary(" " + i);
        } else if (BOOL_SUBTITULO.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
            mBoolSubtitulo.setSummary(b ? getResources().getString(R.string.pref_activado) : getResources().getString(R.string.pref_desactivado));
        } else if (STR_Velocidad_Click.equals(key)) {
            int i = sharedPrefs.getInt(key, 10);
            mNumVelocidadClicker.setSummary(" " + i);
        } else if (BOOL_TIPO_BARRIDO.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);

        } else if (BOOL_HABLAR_BORRAR.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (BOOL_UBICACION.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
            if (b && mBoolUbicacion.getSummary() == getResources().getString(R.string.pref_desactivado)) {
                //Check si tenemoslos permisos necesarios para ejecutar el calendario.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
                        .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat
                        .checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            //Permisos para poder leer y escribir el calendar
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                            .ACCESS_FINE_LOCATION
                    }, Constants.PERMISSION_UBICACION);
                } else {
                    Log.d(TAG, "onSharedPreferenceChanged: Location permit granted");
                }
            }
            mBoolUbicacion.setSummary(b ? getResources().getString(R.string.pref_activado) : getResources().getString(R.string.pref_desactivado));
        } else if (BOOL_BARRIDO.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (BOOL_EDITAR.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, true);

        } else if (BOOL_SUBTITULO_MAYUSCULA.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, true);

        } else if (STR_OPCIONES.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, true);
        } else if (BOOL_CONTROLFACIAL.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (STR_SCROLL_FUNCTION.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (STR_SCROLL_FUNCTION_CLICKER.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (STR_BARRIDO_PANTALLA.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (STR_SIP_AND_PUFF.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        } else if (BOOL_SAY_PICTOGRAM.equals(key)) {
            boolean b = sharedPrefs.getBoolean(key, false);
        }else if(CHATGPT.equals(key)){
            boolean b = sharedPrefs.getBoolean(key, false);
        }else if(REPEATPHRASE.equals(key)){
            boolean b = sharedPrefs.getBoolean(key, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // set the summaries from saved values
        //  onSharedPreferenceChanged(sharedPrefsDefault, BOOL_PRESENTACION);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_TTS);
        onSharedPreferenceChanged(sharedPrefsDefault, STR_Modo_Exp);
        onSharedPreferenceChanged(sharedPrefsDefault, NUM_Vel);
        onSharedPreferenceChanged(sharedPrefsDefault, NUM_Tono);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_SUBTITULO);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_SUBTITULO_MAYUSCULA);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_UBICACION);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_BARRIDO);
        onSharedPreferenceChanged(sharedPrefsDefault, BOOL_EDITAR);
        onSharedPreferenceChanged(sharedPrefsDefault, STR_IDIOMA);
        onSharedPreferenceChanged(sharedPrefsDefault, STR_OPCIONES);
        onSharedPreferenceChanged(sharedPrefsDefault, STR_LOGOUT);
        onSharedPreferenceChanged(sharedPrefsDefault, STR_SCROLL_FUNCTION);
        onSharedPreferenceChanged(sharedPrefsDefault, "hablar_borrar");
        onSharedPreferenceChanged(sharedPrefsDefault,CHATGPT);


        sharedPrefsDefault.registerOnSharedPreferenceChangeListener(this);

        Json.getInstance().setmContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Json.getInstance().setmContext(this);
        Log.d(TAG, "onStop: Yes");

    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPrefsDefault.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Yes");
        // super.onBackPressed();

        Intent databack = new Intent();
        databack.putExtra(getString(R.string.boolean_cambio_idioma), cambioIdioma);
        databack.putExtra(getString(R.string.boolean_cambio_mano),cambioDeLado);
        databack.putExtra(getString(R.string.boolean_cambio_barrido),cambioBarrido);

        setResult(IntentCode.CONFIG_SCREEN.getCode(), databack);
        finish();

    }

    /**
     * Download the files related with an specific language
     */
    private void DescargarArchivosPais(final String s) {

        locale = Locale.getDefault().getLanguage();
        Configuration config = new Configuration(getApplicationContext().getResources().getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            config.setLocale(Locale.forLanguageTag(sharedPrefsDefault.getString(getString(R.string.str_idioma), "en")));
        }
        Log.d(TAG, "DescargarArchivosPais: Locale: " + locale);

        bajarJsonFirebase.bajarGrupos(s, obsInt);
        bajarJsonFirebase.bajarPictos(s, obsInt);
        sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), locale).apply();

    }

    @Override
    public void onDescargaCompleta(int descargaCompleta) {

    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {

    }

    @Override
    public void onFotoDescargada(int fotosDescargadas) {

    }


    @Override
    public void onPictosSugeridosBajados(boolean descargado) {
        if (descargado) {
            handlerComunicationClass.sendMessage(
                    Message.obtain(handlerComunicationClass, HandlerComunicationClass.DISMISSDIALOG, ""));
        }


    }

    @Override
    public void isconnected(boolean b) {
        if (!b) {
            if (firebaseDialog != null && firebaseDialog.isShowing()) {
                firebaseDialog.destruirDialogo();
            }
        }

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: view: " + v.toString());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "onPreferenceChange: " + newValue.toString());
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.PERMISSION_UBICACION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //permission not granted
                    permission = PackageManager.PERMISSION_DENIED;
                    break;
                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permission = PackageManager.PERMISSION_GRANTED;
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String locale = preferences.getString(newBase.getString(R.string.str_idioma), Locale.getDefault().getLanguage());
        new ConfigurarIdioma(newBase, locale);
        super.attachBaseContext(myContextWrapper.wrap(newBase, locale));
    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    private void habilitarFuncionesPremiun(int n) {
        if (n == 1) {
            mBoolBarrido.setEnabled(true);
            mBoolUbicacion.setEnabled(true);
            //  mBtnBackup.setEnabled(true);

        } else {
            mBoolBarrido.setIcon(R.drawable.locked);
            mBoolUbicacion.setIcon(R.drawable.locked);
            //   mBtnBackup.setIcon(R.drawable.locked);

        }

    }



    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String keyValue = preference.getKey();
        if(keyValue != null) {
            switch (preference.getKey()) {
                case "bool_barrido":
                    showPaymentMessage(mBoolBarrido);
                    return true;
                case "mBoolModoExperimental":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.experimentalModeAnalyticsValue));
                    break;
                case "bool_sugerencias":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.suggestedPictogramsAnalyticsValue));
                    showPaymentMessage(mOpciones);
                    return true;
                case "bool_subtitulo":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.CustomSubtitleAnalyticsValue));
                    break;
                case "bool_tts":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.CustomTTSAnalyticsValue));
                    break;
                case "control_facial":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.FacialControlAnalyticsValue));
                    mFaceControl = new ControlFacial(preference.getContext());
                    break;
                case "subtitulo_tamanio":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.SubtitleTextSizeAnalyticsValue));
                    showDialogOptionsSettings(getString(R.string.pref_subtitulo_tamanio), R.array.pref_subtitulos_tamanio_nombres, R.array.pref_subtitulos_tamanio_valores, getResources().getString(R.string.str_subtitulo_tamano));
                    break;
                case "bool_editar":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.EditPictogramAnalyticsValue));
                    break;
                case "hablar_borrar":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.TalkAndEraseAnalyticsValue));
                    break;
                case "posicion_joystick":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.JoystickAnalyticsValue));
                    showDialogOptionsSettings(getString(R.string.joystick_orientation), R.array.pref_posicion, R.array.pref_posicion_valores, "orientacion_joystick");
                    return true;
                case "sexo":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.GenderUserAnalyticsValue));
                    showDialogOptionsDownloadFile(getResources().getString(R.string.gender_string), Constants.GENERO, sharedPrefsDefault.getString(Constants.GENERO, "MASCULINO"), R.array.listSexo, R.array.list_sexo_valores, "pref_sexo");
                    //  new ordenarPictos().execute();
                    return true;
                case "edad":
                    //  progressDialog = new ProgressDialog(this);
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.AgeUserAnalyticsValue));
                    showDialogOptionsDownloadFile(this.getResources().getString(R.string.str_seleccionar_edad_usuario), getString(R.string.prefedad), sharedPrefsDefault.getString(getString(R.string.prefedad), "ADULTO"), R.array.listEdad, R.array.list_Edad_valores, getString(R.string.prefedad));
                    return true;
                case "idioma":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.LanguageAnalyticsValue));
                    showLanguajeDialog(this.getResources().getString(R.string.pref_idioma), R.array.pref_idiomas, R.array.pref_idiomas_valores);
                    return true;

                case "skillHand":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.SkillHandAnalyticsValue));
                    showDialogOptionsSkilledHand("Mano Habil", sharedPrefsDefault.getBoolean(Constants.SKILLHAND, false), R.array.Mano, R.array.Mano_valores, manoHabil.getKey());
                    return true;
                case "pitch_tts":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.TTSVoiceAnalyticsValue));
                    preparePickerDialog(this.getResources().getString(R.string.pref_option2_tts), NUM_Tono, 1, 20);
                    return true;
                case "velocidad_tts":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.TTSSpeedAnalyticsValue));
                    preparePickerDialog(this.getResources().getString(R.string.pref_option1_tts), NUM_Vel, 1, 20);
                    return true;
                case "velocidad_barrido":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.ScreenScanningSpeedAnalyticsValue));
                    preparePickerDialog(this.getResources().getString(R.string.scanning_resources), NUM_VelB, 1, 10);
                    return true;
                case "tiempo_retraso":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.TimeBetweenClicksAnalyticsValue));
                    preparePickerDialog(this.getResources().getString(R.string.str_delay_time_click), STR_Velocidad_Click, 1, 20);
                    return true;
                case "Scroll_speed":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.ScrollSpeedAnalyticsValue));
                    preparePickerDialog(this.getResources().getString(R.string.scroll_speed_title), STR_SCROLL_SPEED, 1, 20);
                    return true;
                case "bool_ubicacion":
                    analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, getString(R.string.LocationAnalyticsValue));
                    showPaymentMessage(mBoolUbicacion);
                    return true;
                case "v1":
                    sharedPrefsDefault.edit().putInt("deviceId", 0).apply();
                    new Devices_Version_Dialog(this, false);
                    break;
                case "v2":
                    sharedPrefsDefault.edit().putInt("deviceId", 1).apply();
                    new Devices_Version_Dialog(this, true);
                    break;
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showPaymentMessage(PersonalSwitchPreferences preference){
        if(sharedPrefsDefault.getInt("premium", 0) == 0){
            preference.setChecked(false);
            Intent i = new Intent(preference.getContext(), LicenciaExpirada.class);
            startActivity(i);
        }

    }

    /**
     * This method request the connection to firebase database
     */
    public void isConnected() {
        DatabaseReference connectedRef = firebaseUtils.getmDatabase().getDatabase().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                status = connected;
                isconnected(connected);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Listener cancelled");

            }
        });
    }

    public void mostrarDialogo() {
        firebaseDialog.setMessage(message);
        firebaseDialog.mostrarDialogo();
        firebaseDialog.setCancelable(false);

    }

    public void cancelarDialogo() {
        if (firebaseDialog != null)
            firebaseDialog.destruirDialogo();
        if(customDialogOptionItem != null)
            customDialogOptionItem.DissmisDialog();
    }

    public int devolverOrientacion(String texto) {
        if (texto.contains("1"))
            return 1;
        return 0;
    }

    public void cargarValorSeleccionado(ListPreference preference, int value) {
        preference.setValueIndex(value);
    }

    private void preparePickerDialog(String title, String key, int min, int max) {
        NumberPickerPreference pickerPreference = new NumberPickerPreference(this, title, key);
        pickerPreference.createDialog();
        pickerPreference.setmPicker(min, max);
        pickerPreference.createPicker();
    }

    @Override
    public void showLanguajeDialog(String name, int value, int options) {

        customDialogOptionItem = new custom_dialog_option_item(this);
        customDialogOptionItem.prepareDialog(this.getResources().getString(R.string.pref_idioma), name, sharedPrefsDefault.getString(getString(R.string.str_idioma), Locale.getDefault().getLanguage()), value, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                final String s = adapter.getmArrayListValues()[adapter.devolverPosition()];
                message = getString(R.string.downloadingFoto);

                if (status) {
                    handlerComunicationClass.sendMessage(
                            Message.obtain(handlerComunicationClass, HandlerComunicationClass.SHOWDIALOG, ""));
                    sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), s).apply();
                    sharedPrefsDefault.edit().putBoolean("pictosEliminados", false).apply();
                    downloadFilesTask = new DownloadFilesTask(s);
                    downloadFilesTask.execute();

                } else {
                    isconnected(false);
                    myTTS.mostrarAlerta(getString(R.string.error_bajarFotos));
                    sharedPrefsDefault.edit().putString(getString(R.string.str_idioma_buffer), strIdioma_original).apply();
                    sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), strIdioma_original).apply();
                }


            }
        }).ShowDialog();

    }

    @Override
    public void showDialogOptionsDownloadFile(String name, int value, int options, String preferences) {
        customDialogOptionItem  = new custom_dialog_option_item(this);
        customDialogOptionItem.prepareDialog(name, value, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                    final String value = adapter.getmArrayListValues()[adapter.devolverPosition()];
                    sharedPrefsDefault.edit().putString(preferences, value).apply();
                    message = "Bajando sugerencias";
                    final StorageReference mPredictionRef = mStorageRef.child("Archivos_Sugerencias").child("pictos_" + sharedPrefsDefault.getString("prefSexo", "FEMENINO") + "_" + sharedPrefsDefault.getString("prefEdad", "JOVEN") + ".txt");
                    bajarJsonFirebase.descargarPictosDatabase(mPredictionRef);
                    handlerComunicationClass.sendMessage(
                            Message.obtain(handlerComunicationClass, HandlerComunicationClass.SHOWDIALOG, ""));
                }

            }
        });
        customDialogOptionItem.ShowDialog();
    }

    @Override
    public void showDialogOptionsDownloadFile(String name, String key, String selectedValue, int values, int options, String preference) {
        customDialogOptionItem = new custom_dialog_option_item(this);
        customDialogOptionItem.prepareDialog(name, preference, selectedValue, values, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                    final String value = adapter.getmArrayListValues()[adapter.devolverPosition()];
                    sharedPrefsDefault.edit().putString(preference, value).apply();
                    message = "Bajando sugerencias";
                    final StorageReference mPredictionRef = mStorageRef.child("Archivos_Sugerencias").child("pictos_" + sharedPrefsDefault.getString("prefSexo", "FEMENINO") + "_" + sharedPrefsDefault.getString("prefEdad", "JOVEN") + ".txt");
                    bajarJsonFirebase.descargarPictosDatabase(mPredictionRef);
                    handlerComunicationClass.sendMessage(
                            Message.obtain(handlerComunicationClass, HandlerComunicationClass.SHOWDIALOG, ""));
                }

            }
        });
        customDialogOptionItem.ShowDialog();
    }

    @Override
    public void showDialogOptionsSettings(String name, int value, int options, String preferences) {
        customDialogOptionItem = new custom_dialog_option_item(this);
        customDialogOptionItem.prepareDialog(name, value, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                    final String value = adapter.getmArrayListValues()[adapter.devolverPosition()];
                    if (value != null)
                        sharedPrefsDefault.edit().putInt(preferences, Integer.parseInt(value)).apply();
                }
            }
        }).ShowDialog();

    }

    @Override
    public void showDialogOptionsSkilledHand(String name, int value, int options, String preferences) {
        customDialogOptionItem = new custom_dialog_option_item(this);
        customDialogOptionItem.prepareDialog(name, value, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                    int value = adapter.devolverPosition();
                    cambioDeLado = true;
                    if (value == 0) {
                        sharedPrefsDefault.edit().putBoolean(preferences, false).apply();
                    }else {
                        sharedPrefsDefault.edit().putBoolean(preferences, true).apply();
                    }
                }

            }
        }).ShowDialog();
    }

    @Override
    public void showDialogOptionsSkilledHand(String name, boolean value, int values, int options, String preferences) {
        customDialogOptionItem  = new custom_dialog_option_item(this);
        String valor = "false";
        if (value == true)
            valor = "true";
        customDialogOptionItem.prepareDialog(name, preferences, valor, values, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    Item_adapter adapter = (Item_adapter) customDialogOptionItem.getRecycler().getAdapter();
                    int value = adapter.devolverPosition();
                    if (value == 0)
                        sharedPrefsDefault.edit().putBoolean(preferences, false).apply();
                    else
                        sharedPrefsDefault.edit().putBoolean(preferences, true).apply();
                    //    new ordenarPictos().execute();
                }

            }
        }).ShowDialog();
    }

    private void createOnClickItemBarrido() {
        mBoolBarrido.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                analyticsFirebase.customEvents("Settings", "pref", "Screen Scanning");
                mBoolBarrido.setChecked(!mBoolBarrido.isChecked());
                cambioBarrido = true;
                if (mBoolBarrido.isChecked()) {
                    if (!mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked())
                        mBoolBarridoPantalla.setChecked(true);
                    analyticsFirebase.customEvents("screen-Scanning", "pref", "enabled");
                }

                return false;
            }
        });
        mBoolSipAndPuff.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mBoolSipAndPuff.setChecked(!mBoolSipAndPuff.isChecked());
                sendAnalitycsDevice(mBoolBarrido, "Sip and puff");

                if (mBoolSipAndPuff.isChecked()) {
                    mBoolUsarScroll.setChecked(false);
                    mBoolUsarScrollClick.setChecked(false);
                    mBoolBarridoPantalla.setChecked(false);
                    mBoolTipoBarrido.setChecked(false);
                } else if (!mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked() && !mBoolSipAndPuff.isChecked()) {
                    mBoolBarridoPantalla.setChecked(true);
                }
                return false;
            }
        });
        mBoolTipoBarrido.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mBoolTipoBarrido.setChecked(!mBoolTipoBarrido.isChecked());
                sendAnalitycsDevice(mBoolBarrido, "advanced and accept");
                if (mBoolTipoBarrido.isChecked()) {
                    mBoolUsarScroll.setChecked(false);
                    mBoolUsarScrollClick.setChecked(false);
                    mBoolBarridoPantalla.setChecked(false);
                    mBoolSipAndPuff.setChecked(false);
                } else if (!mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked() && !mBoolSipAndPuff.isChecked()) {
                    mBoolBarridoPantalla.setChecked(true);
                }
                return false;
            }
        });
        mBoolUsarScroll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mBoolUsarScroll.setChecked(!mBoolUsarScroll.isChecked());
                sendAnalitycsDevice(mBoolBarrido, "Scroll");
                if (mBoolUsarScroll.isChecked()) {
                    mBoolTipoBarrido.setChecked(false);
                    mBoolUsarScrollClick.setChecked(false);
                    mBoolBarridoPantalla.setChecked(false);
                    mBoolSipAndPuff.setChecked(false);
                } else if (!mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked() && !mBoolSipAndPuff.isChecked()) {
                    mBoolBarridoPantalla.setChecked(true);
                }

                return false;
            }
        });
        mBoolUsarScrollClick.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mBoolUsarScrollClick.setChecked(!mBoolUsarScrollClick.isChecked());
                sendAnalitycsDevice(mBoolBarrido, "Scroll with Click");
                if (mBoolUsarScrollClick.isChecked()) {
                    mBoolTipoBarrido.setChecked(false);
                    mBoolUsarScroll.setChecked(false);
                    mBoolBarridoPantalla.setChecked(false);
                    mBoolSipAndPuff.setChecked(false);
                } else if (!mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked() && !mBoolSipAndPuff.isChecked()) {
                    mBoolBarridoPantalla.setChecked(true);
                }
                return false;
            }
        });
        mBoolBarridoPantalla.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                mBoolBarridoPantalla.setChecked(!mBoolBarridoPantalla.isChecked());
                sendAnalitycsDevice(mBoolBarrido, "Screen-Scanning");
                if (mBoolBarridoPantalla.isChecked()) {
                    mBoolTipoBarrido.setChecked(false);
                    mBoolUsarScroll.setChecked(false);
                    mBoolUsarScrollClick.setChecked(false);
                    mBoolSipAndPuff.setChecked(false);
                }
                if (!mBoolBarridoPantalla.isChecked() && !mBoolTipoBarrido.isChecked() && !mBoolUsarScroll.isChecked() && !mBoolUsarScrollClick.isChecked() && !mBoolSipAndPuff.isChecked()) {
                    mBoolBarrido.setChecked(false);
                }
                return false;
            }
        });


    }

    public void sendAnalitycsDevice(PersonalSwitchPreferences personalSwitchPreferences, String device) {
        if (personalSwitchPreferences.isChecked())
            analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, "enabled " + device);
        else
            analyticsFirebase.customEvents(ConstantsAnalyticsValues.SETTINGEVENT, ConstantsAnalyticsValues.PREFSCLASSNAME, "disabled " + device);


    }

    public interface OnIntegerChangeListener {
        void onIntegerChanged(int newValue);
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        String s;

        public DownloadFilesTask(String s) {
            this.s = s;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DescargarArchivosPais(s);
            sharedPrefsDefault.edit().putString(getString(R.string.str_idioma_buffer), s).apply();
            sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), s).apply();
            ConfigurarIdioma.setLanguage(sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"));
            cambioIdioma = true;
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}