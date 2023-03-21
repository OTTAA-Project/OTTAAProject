package com.stonefacesoft.ottaa.utils.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.LoginActivity2;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

public class User {
    private Activity mActivity;
    private Context mContext;
    public GoogleSignInApi mGoogleSignInClient;
    private final GoogleApiClient mGoogleApiClient;
    private final SharedPreferences sharedPrefsDefault;
    private static FirebaseSuccessListener mFirebaseSuccess;
    private final FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static User _user;

    private String TAG ="User";

    public synchronized  static User getInstance(Activity mActivity){
        if(_user ==  null)
            _user = new User(mActivity);
        return _user;
    }

    public synchronized  static User getInstance(Context mContext){
        if(_user ==  null)
            _user = new User(mContext);
        return _user;
    }
    public synchronized  static User getInstance(){
        return _user;
    }

    public User(Activity mContext){
        this.mActivity=mContext;
        sharedPrefsDefault= PreferenceManager.getDefaultSharedPreferences(this.mActivity);
        GoogleSignInOptions gso =createGSO();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
       mAuth = FirebaseAuth.getInstance();

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
                else{
                    FirebaseUtils.getInstance().setEmail(firebaseAuth.getCurrentUser().getEmail());
                    FirebaseUtils.getInstance().setUid(firebaseAuth.getUid());
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

    public boolean isConnected(){
        return mGoogleApiClient.isConnected();
    }


    public void logOut(){
        sharedPrefsDefault.edit().putBoolean("usuario logueado", false)
                .apply();
        if(mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    disconnectClient();
                    FirebaseAuth.getInstance().signOut();
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

    public String getUserUid(FirebaseUser firebaseUser){
        return firebaseUser.getUid();
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
    public String getUserUid(Context context){
        String uid = "";
       try{
           uid = mAuth.getCurrentUser().getUid();
        }catch (Exception ex){
           uid = "";
       }
        return uid;
    }
    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }

    public boolean isPremium(){
        return sharedPrefsDefault.getInt("premium", 0) == 1;
    }
}
