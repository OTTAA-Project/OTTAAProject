package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.prefs;


public class AboutOttaa extends AppCompatActivity {

    private static final String TAG = "AboutOTTAA";
    private String uid;
    private TextView mUserEmail,mAppVersion,mFirebaseAppVersion;
    private DatabaseReference mDatabase;
    private int versionCode;
    private Button mBtnActualizar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ottaa);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), prefs.class);
                startActivity(intent);
            }
        });


        mBtnActualizar = findViewById(R.id.btnActualizar);
        mAppVersion = findViewById(R.id.versionApp);
        mUserEmail = findViewById(R.id.userEmail);
        mFirebaseAppVersion = findViewById(R.id.versionServer);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: uid: " + uid);
        ObtenerVersion();
        ObtenerEmailUsuario();

    }


    private void ObtenerVersion(){

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            Log.d(TAG, "ObtenerVersion: Version Actual: " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "ObtenerVersion: Error: " + e.getMessage());
        }

        mDatabase.child("version").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int versionApp = Integer.valueOf(dataSnapshot.getValue().toString());
                mAppVersion.setText(getApplicationContext().getResources().getString(R.string.version_de_ottaa_instalada) + ": " + versionCode);
                mFirebaseAppVersion.setText(getApplicationContext().getResources().getString(R.string.versi_n_actual_de_ottaa) + ": " + versionApp);

                if(versionCode != versionApp){
                    mBtnActualizar.setEnabled(true);
                    mBtnActualizar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.stonefacesoft.ottaa")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                Log.e(TAG, "onClick: Error: Activity Not Found: " + anfe.getMessage());
                                Toast.makeText(AboutOttaa.this, "No se pudo abrir", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    mBtnActualizar.setEnabled(false);
                    mBtnActualizar.setText(getApplicationContext().getResources().getString(R.string.ottaa_actualizado));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Database Error: " + databaseError.getMessage());

            }
        });
    }

    private void ObtenerEmailUsuario(){

        //Todo Replacement with the firebaseCloudFunction
        FirebaseUtils firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        DatabaseReference firebaseRef=firebaseUtils.getmDatabase();
       firebaseRef.getDatabase().getReference().child("email").child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userEmail = dataSnapshot.getValue(String.class);
                mUserEmail.setText(getApplicationContext().getString(R.string.cuenta) +":"+userEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Database error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        new ConfigurarIdioma(newBase, preferences.getString(newBase.getString(R.string.str_idioma), "en"));
        super.attachBaseContext(myContextWrapper.wrap(newBase, preferences.getString(newBase.getString(R.string.str_idioma), "en")));

    }

}
