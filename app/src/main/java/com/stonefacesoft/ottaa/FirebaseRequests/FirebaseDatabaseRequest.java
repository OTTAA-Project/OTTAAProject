package com.stonefacesoft.ottaa.FirebaseRequests;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;

/*
 * usar esta clase solo para subir datos especificos como el pago , nombre y edad del usuario
 * */

public class FirebaseDatabaseRequest {
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPrefsDefault;
    private Context mContext;
    private FirebaseUtils firebaseUtils;

    public FirebaseDatabaseRequest(Context mContext) {
        mAuth = FirebaseAuth.getInstance();
        this.mContext = mContext;
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this.mContext);
        firebaseUtils.setUpFirebaseDatabase();
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);
        mDatabase =firebaseUtils.getmDatabase();
    }

    public void subirNombreUsuario(FirebaseAuth auth) {
        mDatabase.child(Constants.USUARIOS).child(auth.getCurrentUser().getUid()).child(Constants.NOMBRE).setValue(auth.getCurrentUser().getDisplayName());
    }

    public void subirEdadUsuario(String edad, FirebaseAuth auth) {
        mDatabase.child(Constants.EDADUSUARIO).child(auth.getCurrentUser().getUid()).setValue(edad);
    }

    public void subirTipoUsuario(){

    }

    public void subirPago(FirebaseAuth auth) {
        mDatabase.child(Constants.PAGO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppEventsLogger logger = AppEventsLogger.newLogger(mContext);
                if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())){
                    dataSnapshot.child(auth.getCurrentUser().getUid()).child(Constants.PAGO).getRef().setValue("0");

                        logger.logEvent("Usuario Nuevo");
                }else{
                        logger.logEvent("Usuario Viejo");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void subirConexion(){

    }

    public void subirEmail(FirebaseAuth auth) {
        mDatabase.child("email").child(auth.getCurrentUser().getUid()).child("email").setValue(auth.getCurrentUser().getEmail());
    }

    public void UploadUserData(DataUser userData){
        mDatabase.child(Constants.USUARIOS).child(mAuth.getCurrentUser().getUid()).child(Constants.NOMBRE).setValue(userData.getFirstAndLastName());
        mDatabase.child(Constants.USUARIOS).child(mAuth.getCurrentUser().getUid()).child(Constants.FECHACUMPLE).setValue(userData.getBirthDate());
        mDatabase.child(Constants.USUARIOS).child(mAuth.getCurrentUser().getUid()).child(Constants.GENERO).setValue(userData.getGender());
    }

   public void uploadUserAvatar(String name){
        mDatabase.child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).child(name);
   }
   public void uploadUserAvatar(String name, String file){
       mDatabase.child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).child(name);
       mDatabase.child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).child(file);
   }



}
