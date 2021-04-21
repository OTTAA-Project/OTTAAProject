package com.stonefacesoft.ottaa.utils.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.LoginActivity2;
import com.stonefacesoft.ottaa.R;

public class User {
    private Activity mActivity;
    private Context mContext;
    public GoogleSignInApi mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPrefsDefault;
    private static FirebaseSuccessListener mFirebaseSuccess;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    public User(Activity mContext){
        this.mActivity=mContext;
        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(this.mActivity);
        GoogleSignInOptions gso =createGSO();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
       mAuth=FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    sharedPrefsDefault.edit().putBoolean("firstrun", true).apply();
                    Intent mainIntent = new Intent().setClass(
                            mContext, LoginActivity2.class);
                    mContext.startActivity(mainIntent);
                    mContext.finish();

                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public User(Context mContext){
        this.mContext=mContext;
        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(this.mContext);
        GoogleSignInOptions gso = createGSO();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mAuth=FirebaseAuth.getInstance();

    }

    public void connectClient(){
        mGoogleApiClient.connect();
    }

    public  void disconnectClient(){
        if(mGoogleApiClient.isConnected())
        mGoogleApiClient.disconnect();




    }



    public void logOut(){
        sharedPrefsDefault.edit().putBoolean("usuario logueado", false)
                .apply();
        if(mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                    disconnectClient();
                    mAuth.getInstance().signOut();
                    mActivity.finish();
                }

            });
        }

    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }
    /**
     * Authenticate the google user
     * */
    private GoogleSignInOptions createGSO(){
        if(mActivity!=null)
        return  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        else
            return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(this.mContext.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
    }

    public String getUserUid(){
        return mAuth.getCurrentUser().getUid();
    }
}
