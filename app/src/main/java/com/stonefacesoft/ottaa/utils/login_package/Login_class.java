package com.stonefacesoft.ottaa.utils.login_package;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ObservableInteger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login_class implements FirebaseSuccessListener {
    private  StorageReference mStorageRef;
    protected AppCompatActivity mActivity;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected FirebaseAuth mAuth;
    private SharedPreferences sharedPrefsDefault;
    private BajarJsonFirebase mBajarJsonFirebase;
    private DatabaseReference mDatabase;
    private static final String TAG = "LoginActivity";
    private String StrTipo, StrEdad, StrSexo, pais;
    private String locale;
    private String dateStr;
    protected ObservableInteger obsInt;
    private ProgressDialog dialog;


    public Login_class(AppCompatActivity mActivity){
        this.mActivity=mActivity;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(this.mActivity);
        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FacebookSdk.sdkInitialize(this.mActivity);
        dialog = new ProgressDialog(this.mActivity);
        AuthListener();

        obsInt = new ObservableInteger();
        obsInt.set(0);
        Log.e("LoginActivity_oc_Desc", " obsInt 0");
        obsInt.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                if (obsInt.get() == 1)
                    Log.d("LoginActivity_oc_Desc", " obsInt 1");
                if (obsInt.get() == 2) {
                    Log.d("LoginActivity_oc_Desc", " obsInt 2");
                    Log.d("LoginActivity_oc_DescGr", " se bajo grupos");
                    Intent mainIntent = new Intent().setClass(mActivity, Principal.class);
                    mActivity.startActivity(mainIntent);
                    final StorageReference mPredictionRef = mStorageRef.child("Archivos_Sugerencias").child("pictos_" + sharedPrefsDefault.getString("prefSexo", "FEMENINO") + "_" + sharedPrefsDefault.getString("prefEdad", "JOVEN") + ".txt");
                    mBajarJsonFirebase.descargarPictosDatabase(mPredictionRef);

                    // chequearArchivoSugerencias();
                }
            }
        });

    }


    protected void AuthListener() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.d("LoginActivity_authlist", "CurrentUser != null");
                    sharedPrefsDefault.edit().putString(mActivity.getString(R.string.str_userMail), mAuth.getCurrentUser().getEmail()).apply();
                    sharedPrefsDefault = mActivity.getSharedPreferences(sharedPrefsDefault.getString(mActivity.getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);

                    mBajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, mActivity);

                    mBajarJsonFirebase.setInterfaz(Login_class.this);

                    new sendMail().execute();


                    locale = Locale.getDefault().getLanguage();
                    pais = locale;
                    sharedPrefsDefault.edit().putBoolean("prediccion_usuario", false).apply();
                    sharedPrefsDefault.edit().putString("email",firebaseAuth.getCurrentUser().getEmail()).apply();
                    FirebaseDatabaseRequest request = new FirebaseDatabaseRequest(mActivity);
                    request.subirNombreUsuario(firebaseAuth);
                    request.subirPago(firebaseAuth);
                    request.subirEmail(firebaseAuth);
                    /*Intent mainIntent = new Intent().setClass(LoginActivity.this, Principal.class);
                    startActivity(mainIntent);*/

                }


            }


        };

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
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {
        if (descargado) {
            dialog.dismiss();
            Intent mainIntent = new Intent().setClass(mActivity, Principal.class);
            mActivity.startActivity(mainIntent);
            mActivity.finish();
        }
    }


    public class BackgroundTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                ChequearSiExistenDatos();


            } catch (Exception e) {
                Log.e("LoginActivity_ASYNCLOG", "No se pueden bajar los pictos del pais");
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
            if (!mActivity.isFinishing()) {
                dialog.setMessage(mActivity.getResources().getString(R.string.pref_login_wait));
                dialog.setTitle(mActivity.getResources().getString(R.string.pref_login_autentificando));
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        // automatically done on workerFirebase thread (separate from UI thread)
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            new BackgroundTask().execute();
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
                Log.d("Response", e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }finally {

            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {

            mDatabase.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PConexion").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue(String.class) == null) {
                        if (FirebaseAuth.getInstance() != null)
                            mDatabase.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PConexion").setValue(dateStr);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());

                }
            });

            final String key = mDatabase.push().getKey();

            mDatabase.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    //Map<String, String> map = new HashMap<>();
                    if (FirebaseAuth.getInstance() != null)
                        mDatabase.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Dispositivo").setValue(getDeviceName());

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());

                }
            });


        }


    }
    public void ChequearSiExistenDatos() {
        locale = Locale.getDefault().getLanguage();
        Log.d("LoginActivity_checkDat", "entro");
        sharedPrefsDefault.edit().putString(mActivity.getString(R.string.str_idioma), locale).apply();
        File rootPath = new File(mActivity.getCacheDir(), "Archivos_OTTAA");
        if (!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }
        StorageReference mStorageRefUsuariosGruposOld = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("grupos_" + mAuth.getCurrentUser().getEmail() + "." + "txt");

        mBajarJsonFirebase.bajarGrupos(locale, rootPath, obsInt);
        mBajarJsonFirebase.bajarPictos(locale, rootPath, obsInt);
        mBajarJsonFirebase.bajarFrases(locale, rootPath, obsInt);



    }
    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }

        return capitalize(manufacturer) + " " + model;
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

    public void destroyDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public void addListener(){
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void removeListener(){
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
