package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;
/*
* @version 2.0
* @author gonzalo juarez
* this version was designed by  hector costa and  modified by gonzalo juarez
*
*
*
* */

public class AboutOttaa extends AppCompatActivity {

    private static final String TAG = "AboutOTTAA";
    private String uid;
    private TextView textViewAccount,textViewAccountType,versionServer,versionApp,deviceName;
    private DatabaseReference mDatabase;
    private int versionCode;
    private Button buttonContactSupport;
    private Toolbar toolbar;
    private PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ottaa2);
/*        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), prefs.class);
                startActivity(intent);
            }
        });*/
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        preferencesUtil=new PreferencesUtil(preferences);


        buttonContactSupport = findViewById(R.id.buttonContactSupport);
        textViewAccountType =findViewById(R.id.textViewAccountType);
        versionServer = findViewById(R.id.versionServer);
        versionApp= findViewById(R.id.versionApp);
        deviceName=findViewById(R.id.deviceName);
        textViewAccount = findViewById(R.id.textViewAccount);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: uid: " + uid);
        getVersion();
        getUserMail();
        getDeviceName();
        textViewAccountType.setText(getKindOfUser());
        buttonContactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }


    private void getVersion(){

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

                int codeVersion = Integer.valueOf(dataSnapshot.getValue().toString());
                versionApp.setText(getApplicationContext().getResources().getString(R.string.version_de_ottaa_instalada) + ": " + versionCode);
                versionServer.setText(getApplicationContext().getResources().getString(R.string.versi_n_actual_de_ottaa) + ": " + codeVersion);

                if(versionCode != codeVersion){

                    versionServer.setOnClickListener(new View.OnClickListener() {
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Database Error: " + databaseError.getMessage());

            }
        });
    }

    private void getUserMail(){

        //Todo Replacement with the firebaseCloudFunction
        FirebaseUtils firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        firebaseUtils.setUpFirebaseDatabase();
        DatabaseReference firebaseRef=firebaseUtils.getmDatabase();
       firebaseRef.getDatabase().getReference().child("email").child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userEmail = dataSnapshot.getValue(String.class);
                textViewAccount.setText(getApplicationContext().getString(R.string.cuenta) +":"+userEmail);
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

    private void getDeviceName(){
        deviceName.setText(Build.MODEL);
    }

    /**
     * the code has been extrated from this page
     * https://netcorecloud.com/tutorials/send-email-in-android-using-javamail-api/
     *
     * the function of this method is sending an email
     *
     * */
    private void sendMail(){
        Intent mail=new Intent(Intent.ACTION_SEND);
        mail.setType("message/rfc822");
        mail.putExtra(Intent.EXTRA_EMAIL, new String[]{ "support@ottaaproject.com"});
        mail.putExtra(Intent.EXTRA_SUBJECT,textViewAccountType.getText());
        mail.putExtra(Intent.EXTRA_TEXT,prepareDataToSend());

        startActivity(Intent.createChooser(mail,"Send mail ..."));
    }

    private String getKindOfUser(){
        if(preferencesUtil.getIntegerValue(Constants.PREMIUM,0)==1)
            return "Premiun";
        return "free";
    }

    private String prepareDataToSend(){
        StringBuilder builder=new StringBuilder();
        builder.append("Email: "+textViewAccount.getText()).append("\n");
        builder.append("Version de la aplicacion:"+versionApp.getText()).append("\n");
        builder.append("Tipo de cuenta:"+textViewAccountType.getText()).append("\n");
        builder.append("Nombre del dispositivo:"+deviceName.getText()).append("\n");
        return builder.toString();
    }



}
