package com.stonefacesoft.ottaa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.Adapters.LicenciaExpiradaAdapter;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
import com.stonefacesoft.ottaa.utils.preferences.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Hector Costa
 */

public class LicenciaExpirada extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //TODO move to util
    public static String STR_LOGOUT;
    public GoogleSignInApi mGoogleSignInClient;
    ViewPager viewPager;
    int[] images = new int[]{R.drawable.licencia_accesibilidad, R.drawable.licencia_games, R.drawable.licencia_informe, R
            .drawable.licencia_ubicacion};
    int[] textos = new int[]{R.string.obtain_ottaa_project,
            R.string.test_game_vocabulary,
            R.string.premium_report_description
            , R.string.location_text};
    LicenciaExpiradaAdapter licenciaExpiradaAdapter;
    int funcion = 0;
    Timer timer;
    private TextView mEndDateTxtView, mSignOutButton;
    private GoogleApiClient mGoogleApiClient;
    private TextView mFrasesTxt;
    private long Actual;
    private User loginGoogle;
    private RemoteConfigUtils remoteConfigUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licencia_expirada);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        STR_LOGOUT = "banderaUpload";
        viewPager = findViewById(R.id.viewPagerLicenciaExpirada);

        licenciaExpiradaAdapter = new LicenciaExpiradaAdapter(LicenciaExpirada.this, images, textos);
        viewPager.setAdapter(licenciaExpiradaAdapter);

        pageSwitcher(5);

        mEndDateTxtView = findViewById(R.id.TextViewDate);

        Button mLicenciaBtn = findViewById(R.id.btnLicencia);
        Button mLoginActivityBtn = findViewById(R.id.btnLoginActivity);
        remoteConfigUtils = RemoteConfigUtils.getInstance();

        mSignOutButton = findViewById(R.id.mSignOutBtn);
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
                                LicenciaExpirada.this, LoginActivity2.class);
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
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
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
                remoteConfigUtils.setActivateDeactivateConfig(LicenciaExpirada.this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {

                        if (task.isSuccessful()) {
                            String url = "";
                            boolean updated = task.getResult();
                            if(remoteConfigUtils.isRegionPriceEnabled()){
                                url = remoteConfigUtils.paymentUtriPremium();
                            }else{
                                url = remoteConfigUtils.paymentUtri();
                            }
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(Intent.createChooser(intent, "Browse with"));
                        }

                    }
                });
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.
            }
        });

        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("UConexion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    remoteConfigUtils.setActivateDeactivateConfig(LicenciaExpirada.this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                if(remoteConfigUtils.isRegionPriceEnabled()){
                                    mEndDateTxtView.setText(remoteConfigUtils.changePrice(getString(R.string.licencia_expirada1),"price_premium"));
                                }else{
                                    mEndDateTxtView.setText(remoteConfigUtils.changePrice(getString(R.string.licencia_expirada1),"price"));
                                }
                            }
                        }
                    });
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



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String s) {

        if (STR_LOGOUT.equals(s)) {
            //   Log.e("BotonSignOutLicencia","pref else if: "+ prefs.getBoolean(s,false));
            mSignOutButton.setEnabled(prefs.getBoolean(s, false));
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
                    if (funcion > 3) { // In my case the number of pages are 4
                        funcion = 0;
                    } else {
                        viewPager.setCurrentItem(funcion++);
                    }
                }
            });

        }
    }

}
