package com.stonefacesoft.ottaa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.Adapters.LicenciaExpiradaAdapter;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.preferences.User;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
/**
 * @author Hector Costa
 *
 * */

public class LicenciaExpirada extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mEndDateTxtView,mSignOutButton;
    private GoogleApiClient mGoogleApiClient;
    public GoogleSignInApi mGoogleSignInClient;
    private TextView mFrasesTxt;
    public static String STR_LOGOUT;

    private long Actual;
    private User loginGoogle;

    ViewPager viewPager;
    int images[] =new int[]{R.drawable.licencia_accesibilidad, R.drawable.licencia_games, R.drawable.licencia_informe, R
            .drawable.licencia_ubicacion};
    int textos[] = new int[]{R.string.obtain_ottaa_project,
            R.string.test_game_vocabulary,
            R.string.premium_report_description
            ,R.string.location_text};

    LicenciaExpiradaAdapter licenciaExpiradaAdapter;
    int funcion = 0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licencia_expirada);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        STR_LOGOUT = "banderaUpload";

        viewPager = findViewById(R.id.viewPagerLicenciaExpirada);

        licenciaExpiradaAdapter = new LicenciaExpiradaAdapter (LicenciaExpirada.this, images, textos);
        viewPager.setAdapter(licenciaExpiradaAdapter);

        pageSwitcher(5);

        mEndDateTxtView = findViewById(R.id.TextViewDate);

        Button mLicenciaBtn = findViewById(R.id.btnLicencia);
        Button mLoginActivityBtn = findViewById(R.id.btnLoginActivity);

        mSignOutButton= findViewById(R.id.mSignOutBtn);
        mSignOutButton.setClickable(true);
        mSignOutButton.setEnabled(false);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        FirebaseAuth.getInstance().signOut();
                        mGoogleApiClient.disconnect();
                        Intent mainIntent = new Intent().setClass(
                                LicenciaExpirada.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();


                    }
                });
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        mLoginActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent mainIntent = new Intent().setClass(LicenciaExpirada.this, Principal.class);
                startActivity(mainIntent);
                finish();



            }
        });
        mLicenciaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent mainIntent = new Intent().setClass(LicenciaExpirada.this, CheckoutExampleActivity.class);
                //startActivity(mainIntent);
               try{
                   String url = "https://www.mercadopago.com/mla/checkout/start?pref_id=15410477-026f44d9-c8e5-4d28-9e30-d34e375b470b";
                   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                   intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
                   getApplicationContext().startActivity(intent);
               }catch (Exception ex){
                   CustomToast toast=new CustomToast(getApplicationContext());
                   toast.mostrarFrase("Por favor habilite un navegador");
               }
            }
        });

        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("UConexion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


//                Actual = java.lang.Long.parseLong(dataSnapshot.getValue().toString());
//                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy ");
//                String dateString = formatter.format(new Date(Actual * 1000L));
                mEndDateTxtView.setText(getString(R.string.licencia_expirada1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        onSharedPreferenceChanged(prefs, STR_LOGOUT);

        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    private void runStep(Activity activity) {

        Intent exampleIntent = new Intent(this, activity.getClass());
        startActivity(exampleIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String s) {

        if (STR_LOGOUT.equals(s)){
         //   Log.e("BotonSignOutLicencia","pref else if: "+ prefs.getBoolean(s,false));
            mSignOutButton.setEnabled(prefs.getBoolean(s,false));
        }

    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (funcion > 3) { // In my case the number of pages are 5
                        funcion = 0;
                    } else {
                        viewPager.setCurrentItem(funcion++);
                    }
                }
            });

        }
    }

}
