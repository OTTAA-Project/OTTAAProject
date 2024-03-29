package com.stonefacesoft.ottaa;

import static com.stonefacesoft.ottaa.utils.constants.Constants.RC_SIGN_IN;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseDatabaseRequest;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Interfaces.ActivityListener;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.CloudFunctionHTTPRequest;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.welcome.SplashActivity;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

import java.io.File;
import java.util.Locale;

//Code source https://developers.google.com/identity/sign-in/android/sign-in

//merge with new design-doc and new design-avatar
public class LoginActivity2 extends AppCompatActivity implements View.OnClickListener, FirebaseSuccessListener, ActivityListener {

    private static final String TAG = "LoginActivity";

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    //UI elements
    private TextView textViewLoginBig;
    private TextView textViewLoginSmall;
    private CardView cardViewLogin;
    private ImageView orangeBanner;
    private com.google.android.gms.common.SignInButton signInButton;

    private ObservableInteger observableInteger;
    private BajarJsonFirebase mBajarJsonFirebase;
    private SharedPreferences sharedPrefsDefault;
    private ProgressDialog dialog;
    private String locale;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private AnalyticsFirebase mAnalyticsFirebase;
    private RemoteConfigUtils remoteConfigUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_1);
        mAnalyticsFirebase=new AnalyticsFirebase(this);
        bindUI();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        remoteConfigUtils = RemoteConfigUtils.getInstance();
        setUpObservableInteger();

        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dialog = new ProgressDialog(LoginActivity2.this);
        startmAuthListener();
        animateEntrance();

    }

    private void bindUI(){
        cardViewLogin = findViewById(R.id.cardViewLogin);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setVisibility(View.INVISIBLE);
        signInButton.setOnClickListener(this);
        textViewLoginBig = findViewById(R.id.textLoginBig);
        textViewLoginBig.setVisibility(View.INVISIBLE);
        textViewLoginSmall = findViewById(R.id.textLoginSmall);
        textViewLoginSmall.setVisibility(View.INVISIBLE);
        orangeBanner = findViewById(R.id.orangeBanner);
        orangeBanner.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.sign_in_button){
           mAnalyticsFirebase.customEvents("Touch","LoginActivity2","signIn");
           signIn();
       }
    }

    private void signIn() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        IntentCode.PLAY_SERVICES_RESOLUTION_REQUEST.getCode()).show();
            } else {
                alertCheckPlayService();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, Authenticate with Firebase now.
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getMessage());
            Toast.makeText(this, R.string.problema_inet, Toast.LENGTH_SHORT).show();
        }
    }

    private void alertCheckPlayService() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setMessage(getResources().getString(R.string.pref_error_312));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), (dialogo11, id) -> {
            String url = "https://play.google.com/store/apps/details?id=com.google.android.gms";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            finish();
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "onComplete: firebaseAuth succesfull");
                        FirebaseUser user = mAuth.getCurrentUser();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "onComplete: firebaseAuth fail"+task.getException().getMessage());
                        Toast.makeText(LoginActivity2.this, R.string.problema_sign_in,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private  void animateEntrance(){
        //Alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setRepeatMode(Animation.ABSOLUTE);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);

        //CardView animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewLoginBig.startAnimation(alphaAnimation);
                textViewLoginSmall.startAnimation(alphaAnimation);
                signInButton.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        cardViewLogin.startAnimation(translateAnimation);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        orangeBanner.startAnimation(translateAnimation2);

    }

    private void animateTransition(){
        //CardView animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 900);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //With Firebase Auth is enough to get user data.
                Log.d(TAG, "onAnimationEnd: Closing login screen");
                remoteConfigUtils.setActivateDeactivateConfig(LoginActivity2.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            if(remoteConfigUtils.getBooleanByName("showFreeModeMessage")){
                                Intent intent = new Intent(LoginActivity2.this, LoginActivityEvaluationData.class);
                                startMActivity(intent);
                            }else{
                                Intent intent = new Intent(LoginActivity2.this, LoginActivity2Step2.class);
                                startMActivity(intent);
                            }
                        }
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cardViewLogin.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 0, 900);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        orangeBanner.startAnimation(translateAnimation2);
        textViewLoginBig.startAnimation(translateAnimation2);
        textViewLoginSmall.startAnimation(translateAnimation2);
    }



    public void setUpObservableInteger(){
        observableInteger=new ObservableInteger();
        observableInteger.set(0);
        observableInteger.setOnIntegerChangeListener(newValue -> {
            if(observableInteger.get()==0) {
                Log.d(TAG, "onIntegerChanged: obsInt 0");
            }
            if (observableInteger.get() == 1){
                Log.d(TAG, "onIntegerChanged: obsInt 1");
            }
            if (observableInteger.get() == 2) {
                Log.d(TAG, "onIntegerChanged: obsInt 2");
                Log.d(TAG, "onIntegerChanged: Se bajo grupos");
                uploadFiles();
                dialog.dismiss();
                animateTransition();
                // chequearArchivoSugerencias();
            }
        });
    }

    private void uploadFiles(){
        try{
            SubirArchivosFirebase subirArchivosFirebase = new SubirArchivosFirebase(LoginActivity2.this);
            subirArchivosFirebase.subirPictosFirebase(subirArchivosFirebase.getmDatabase(mAuth, Constants.PICTOS), subirArchivosFirebase.getmStorageRef(mAuth, Constants.PICTOS));
            subirArchivosFirebase.subirGruposFirebase(subirArchivosFirebase.getmDatabase(mAuth, Constants.Grupos), subirArchivosFirebase.getmStorageRef(mAuth, Constants.Grupos));

        }catch (Exception ex){}
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
        Log.d(TAG, "onIntegerChanged: Se bajaron las sugerencias");
        dialog.dismiss();
        animateTransition();
    }

    public void downloadFiles() {
        locale = Locale.getDefault().getLanguage();
        sharedPrefsDefault.edit().putString(getString(R.string.str_idioma), locale).apply();
        ConfigurarIdioma.setLanguage(sharedPrefsDefault.getString(getString(R.string.str_idioma),"en"));

        StorageReference mStorageRefUsuariosGruposOld = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("grupos_" + mAuth.getCurrentUser().getEmail() + "." + "txt");

        mBajarJsonFirebase.bajarPictos(locale, observableInteger);
        mBajarJsonFirebase.bajarGrupos(locale, observableInteger);
        mBajarJsonFirebase.bajarFrases(locale, observableInteger);

    }

    private void startmAuthListener() {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()!=null) {
                        Log.d(TAG, "onAuthStateChanged: currentUser not null");
                        FirebaseUtils.getInstance().setUid(mAuth.getUid());
                        FirebaseUtils.getInstance().setEmail(mAuth.getCurrentUser().getEmail());
                        sharedPrefsDefault.edit().putString(getString(R.string.str_userMail), mAuth.getCurrentUser().getEmail()).apply();
                        sharedPrefsDefault = getSharedPreferences(sharedPrefsDefault.getString(getString(R.string.str_userMail), "error"), Context.MODE_PRIVATE);

                        mBajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, getApplicationContext());
                        mBajarJsonFirebase.setInterfaz(LoginActivity2.this);
                        locale = Locale.getDefault().getLanguage();
                        if(ValidateContext.isValidContext(getApplicationContext()))
                            new CloudFunctionHTTPRequest(LoginActivity2.this,TAG).doHTTPRequest("https://us-central1-ottaa-project.cloudfunctions.net/add2listwelcome");
                        new BackgroundTask().execute();
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

    @Override
    public void startMActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            if (ValidateContext.isValidContext(LoginActivity2.this)) {
                dialog.setMessage(getResources().getString(R.string.pref_login_wait));
                dialog.setTitle(getResources().getString(R.string.pref_login_autentificando));
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                downloadFiles();
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: You cannot download picto" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public String getUserUid(){
        String uid = "";
        try{
            uid= mAuth.getCurrentUser().getUid();

        }catch (Exception ex){
            uid = "";
        }
        return uid;
    }

    public String getEmail(){
        String email = "";
        try{
            email= mAuth.getCurrentUser().getEmail();

        }catch (Exception ex){
            email = "";
        }
        return email;
    }
}
