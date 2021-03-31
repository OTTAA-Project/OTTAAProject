package com.stonefacesoft.ottaa;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.login_package.GoogleLogin;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.DONUT)
public class LoginActivity extends AppCompatActivity implements View
        .OnClickListener, TextToSpeech.OnInitListener, FirebaseSuccessListener, RadioGroup.OnCheckedChangeListener {

    //TextView TvFecha;
    private TextView TvPago;
    //ProgressBar Pbar;
    private ImageView Imagen;

    //Analytics



    private BajarJsonFirebase mBajarJsonFirebase;
    private CardView cardView;

    private com.google.android.gms.common.SignInButton SingIn;
    private Button Contacto;

    private final Boolean Flag = false;


    private static String personID;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog progressDialog;


    //--------------------------------

    private String StrTipo, StrEdad, StrSexo, pais;

    private String dateStr;
    private String locale;
    private boolean mailEnviado = false;
    private static final int RC_SIGN_IN = 1;
    private int versionCode, ultimaVersion;
    private Json json;
    private RadioGroup mRadioGroupEdad;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uid;


    private SharedPreferences sharedPrefsDefault;

    private final int mProgressStatus = 0;
    private FileObserver observer;

    private ObservableInteger obsInt;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    InputStream is = null;
    private static final String TAG = "LoginActivity";
    private Button conditionsAndTerms;
    private Context mContext;
    private GoogleLogin login;

    private SharedPreferences userPreferences;

    /**
     * Returns the consumer friendly device name
     */
    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }

        return capitalize(manufacturer) + " " + model;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Json.getInstance().setmContext(this);
        json = Json.getInstance();
        // json.leerPictosFrasesGrupos();
        dialog = new ProgressDialog(LoginActivity.this);

        VersionApp();
        AuthListener();

        //Implemento el manejador de preferencias

        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        //configurando google-sign in con firebase
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(LoginActivity.this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Este es el boton para hacer el sign in, ver si lo ponemos en otro activity o donde
        SingIn = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
        SingIn.setOnClickListener(this);
        SingIn.setEnabled(true);
//        Contacto = (Button) findViewById(R.id.BotonContacto);
//        Contacto.setOnClickListener(this);
//        TvFecha = (TextView) findViewById(R.id.TextViewFecha);
        TvPago = (TextView) findViewById(R.id.TextViewPago);
        //Pbar = (ProgressBar) findViewById(R.id.login_progress);
        Imagen = (ImageView) findViewById(R.id.imageView);

        cardView = (CardView) findViewById(R.id.card_view2);

        mRadioGroupEdad = (RadioGroup) findViewById(R.id.radioGroupEdad);
        mRadioGroupEdad.setOnCheckedChangeListener(this);
        conditionsAndTerms = (Button) findViewById(R.id.terminosYCondicciones);
        conditionsAndTerms.setOnClickListener(this);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.

        // [END shared_tracker]

        //Listener
        obsInt = new ObservableInteger();
        obsInt.set(0);

        obsInt.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                if(obsInt.get()==0) {
                    Log.d(TAG, "onIntegerChanged: obsInt 0");
                }
                if (obsInt.get() == 1){
                    Log.d(TAG, "onIntegerChanged: obsInt 1");
                }
                if (obsInt.get() == 2) {
                    Log.d(TAG, "onIntegerChanged: obsInt 2");
                    Log.d(TAG, "onIntegerChanged: Se bajo grupos");
                    final StorageReference mPredictionRef = mStorageRef.child("Archivos_Sugerencias").child("pictos_" + sharedPrefsDefault.getString("prefSexo", "FEMENINO") + "_" + sharedPrefsDefault.getString("prefEdad", "JOVEN") + ".txt");
                    mBajarJsonFirebase.descargarPictosDatabase(mPredictionRef);

                    // chequearArchivoSugerencias();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        json=Json.getInstance();
        json.setmContext(this);

        if (sharedPrefsDefault.getBoolean("firstrun", true)) {
            sharedPrefsDefault.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    boolean visible = true;


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
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {
        if (descargado) {
            dialog.dismiss();
            Intent mainIntent = new Intent().setClass(LoginActivity.this, Principal.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void chequearArchivoSugerencias() {

        final StorageReference archivosRef = mStorageRef.child("Archivos_Paises").child("usuarioDatabase").child("pictosDatabase").child("pictosDatabase_es.txt");


        archivosRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {

                String hash = storageMetadata.getMd5Hash();
                Log.d(TAG, "onSuccess: md5hash: " + hash);
                // sharedPrefsDefault.edit().putString("md5HashPictos", pruebaHash).apply();

                if (sharedPrefsDefault.contains("md5HashPictos")) {
                    //question if the hash of file equals or the  the suggestion is empty
                    try {
                        assert hash != null;
                        if (!hash.equals(sharedPrefsDefault.getString("md5HashPictos", "NaN")) || json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS_DATABASE).length() == 0) {
                            //download the suggest pictos from the database
                            mBajarJsonFirebase.descargarPictosDatabase(archivosRef);
                        } else {

                            dialog.dismiss();

                            finish();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onSuccess: Error" + e.getMessage());
                    } catch (FiveMbException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onSuccess: Error" + e.getMessage());
                    }


                } else {
                    //edit the hash from de suggest picto database
                    sharedPrefsDefault.edit().putString("md5HashPictos", hash).apply();
                    mBajarJsonFirebase.descargarPictosDatabase(archivosRef);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "onFailure: md5hash" + exception.getLocalizedMessage());

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButtonNino:
                sharedPrefsDefault.edit().putString(getApplicationContext().getResources().getString(R.string.prefedad), Edad.NINO.toString()).apply();
                Log.d(TAG, "onRadioButtonClicked: Nino");
                break;

            case R.id.radioButtonJoven:
                sharedPrefsDefault.edit().putString(getApplicationContext().getResources().getString(R.string.prefedad), Edad.JOVEN.toString()).apply();
                Log.d(TAG, "onRadioButtonClicked: Joven");
                break;

            case R.id.radioButtonAdulto:
                sharedPrefsDefault.edit().putString(getApplicationContext().getResources().getString(R.string.prefedad), Edad.ADULTO.toString()).apply();
                Log.d(TAG, "onRadioButtonClicked: Adulto");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);

        // If you want position of Radiobutton
        int position = group.indexOfChild(radioButton);


        switch (position) {
            case 0:
                sharedPrefsDefault.edit().putString("prefEdad", Edad.NINO.toString()).apply();
                Log.d(TAG, "onCheckedChanged: Nino");
                break;

            case 1:
                sharedPrefsDefault.edit().putString("prefEdad", Edad.JOVEN.toString()).apply();
                Log.d(TAG, "onCheckedChanged: Joven");
                break;

            case 2:
                sharedPrefsDefault.edit().putString("prefEdad", Edad.ADULTO.toString()).apply();
                Log.d(TAG, "onCheckedChanged: Adulto");
                break;
        }


    }

    private void signIn() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        IntentCode.PLAY_SERVICES_RESOLUTION_REQUEST.getCode()).show();
            } else {
                AlertCheckPlayService();
            }
        }

    }






    private void Alert() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setMessage(getResources().getString(R.string.pref_error_222));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sign_in_button:

                onSignInClicked();
                break;
            case R.id.BotonContacto:

                String url = "http://www.ottaaproject.com/contacto.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                break;
            case R.id.terminosYCondicciones:
                Descargo();
                break;

        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        signIn();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, R.string.problema_inet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void AlertCheckPlayService() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
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


    private void VersionApp() {

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        mDatabase.child("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                ultimaVersion = Integer.valueOf(dataSnapshot.getValue().toString());

                if (versionCode != ultimaVersion) {
                    //ActualizarAppAlert();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }


    private void ActualizarAppAlert() {

        final String appPackageName = getPackageName();
        //Toast.makeText(this, "NECESITAS ACTUALIZAR LA APP WACHIIIN", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.Theme_AppCompat_Light));

        alertDialogBuilder.setTitle(this.getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(this.getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.stonefacesoft.ottaa")));
                }


                dialog.cancel();


            }
        });
        alertDialogBuilder.show();


    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
    }

    @Override
    public void onInit(int status) {

    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "onComplete: firebaseAuth succesfull");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "onComplete: firebaseAuth fail");
                            Toast.makeText(LoginActivity.this, R.string.problema_sign_in,
                                    Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void suscribeMailChamp() throws JSONException {
        String listid = "b9a5943047";
        String url = "https://us16.api.mailchimp.com/3.0/lists/" + listid + "/members/";
        String email_address = mAuth.getCurrentUser().getEmail();
        String FNAME = mAuth.getCurrentUser().getDisplayName();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        final JSONObject jsonBody = new JSONObject("{\"email_address\":\"" + email_address + "\"," +
                "\"status\":\"subscribed\"," +
                "\"merge_fields\":{\"FNAME\":\"" + FNAME + "\",\"LNAME\":\"\"}}");

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", getString(R.string.mailchimp_api_key));
                return headers;
            }
        };

        queue.add(myRequest);
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    ///////////////////////////  Mail Inicio  //////////////////////////////////////////////
    //TODO CAMBIAR EL NOMBRE DE ESTE METODO !!!

    private void AuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null) {
                    Log.d(TAG, "onAuthStateChanged: currentUser not null");
                    sharedPrefsDefault.edit().putString(getString(R.string.str_userMail), mAuth.getCurrentUser().getEmail()).apply();
                    sharedPrefsDefault = getSharedPreferences(sharedPrefsDefault.getString(getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);

                    mBajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, getApplicationContext());
                    mBajarJsonFirebase.setInterfaz(LoginActivity.this);


                    new sendMail().execute();

                    locale = Locale.getDefault().getLanguage();
                    pais = locale;
                    sharedPrefsDefault.edit().putBoolean("prediccion_usuario", false).apply();

                    FirebaseDatabaseRequest request = new FirebaseDatabaseRequest(getApplicationContext());
                    request.subirNombreUsuario(firebaseAuth);
                    request.subirPago(firebaseAuth);
                    request.subirEmail(firebaseAuth);
                   /*Intent mainIntent = new Intent().setClass(LoginActivity.this, Principal.class);
                    startActivity(mainIntent);*/

                }


            }


        };

    }

    public void ChequearSiExistenDatos() {
        locale = Locale.getDefault().getLanguage();
        sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), locale).apply();
        File rootPath = new File(getApplicationContext().getCacheDir(), "Archivos_OTTAA");
        if (!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }
        StorageReference mStorageRefUsuariosGruposOld = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("grupos_" + mAuth.getCurrentUser().getEmail() + "." + "txt");

        mBajarJsonFirebase.bajarGrupos(locale, rootPath, obsInt);
        mBajarJsonFirebase.bajarPictos(locale, rootPath, obsInt);
        mBajarJsonFirebase.bajarFrases(locale, rootPath, obsInt);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    ///////////////////////////  Comprobacion de datos  ////////////////////////////////////////////////

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                ChequearSiExistenDatos();


            } catch (Exception e) {
                Log.e(TAG, "doInBackground: You cannot download picto");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    //Usa un listener para ordenar y agarrar cuando los m[etodos terminan y pasar ahi a Principal


    public class sendMail extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            if (!LoginActivity.this.isFinishing()) {
                dialog.setMessage(getResources().getString(R.string.pref_login_wait));
                dialog.setTitle(getResources().getString(R.string.pref_login_autentificando));
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }


        }

        // automatically done on workerFirebase thread (separate from UI thread)
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {


            // Posteando datos de usuario en firebase !
            Long tsLong = System.currentTimeMillis() / 1000;
            final String ts = tsLong.toString();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    dateStr = response.getFirstHeader("Date").getValue();
                    Date startDate = df.parse(dateStr);
                    dateStr = String.valueOf(startDate.getTime() / 1000);
                    //Here I do something with the Date String

                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Response error: " + e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            mailEnviado = true;


            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            new BackgroundTask().execute();
        }


    }



    /*
     * Show the terms and condition from the aplication in the login activity
     * */
    public void Descargo() {
        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setCancelable(false);
        dialogo1.setMessage(getResources().getString(R.string.pref_descargode));//+ GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));

        dialogo1.setPositiveButton(getResources().getString(R.string.pref_confirmar_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();

                //   mostrarNuevasFunciones();

                //Analytics
                /////////////////////////////////////////////////////////////////////////////////////
                // [START custom_event]


            }
        });

        dialogo1.setNegativeButton(getString(R.string.str_saber_mas), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ottaaproject.com/contacto.php"));
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, mContext.getPackageName());
                    mContext.startActivity(intent);
                }catch (Exception ex){
                    Toast.makeText(mContext,"Por favor habilite un navegador",Toast.LENGTH_LONG).show();
                }
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }
}



