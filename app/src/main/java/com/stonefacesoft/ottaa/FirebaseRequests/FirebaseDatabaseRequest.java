package com.stonefacesoft.ottaa.FirebaseRequests;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Interfaces.LoadUserInformation;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.preferences.DataUser;

/*
 * usar esta clase solo para subir datos especificos como el pago , nombre y edad del usuario
 * */

public class FirebaseDatabaseRequest {
    private final DatabaseReference mDatabase;
    private StorageReference mStorageReference;
    private final FirebaseAuth mAuth;
    private final SharedPreferences sharedPrefsDefault;
    private final Context mContext;
    private final FirebaseUtils firebaseUtils;

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
        mDatabase.child(Constants.USUARIOS).child(auth.getCurrentUser().getUid()).child(Constants.NOMBRE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    mDatabase.child(Constants.USUARIOS).child(auth.getCurrentUser().getUid()).child(Constants.NOMBRE).setValue(auth.getCurrentUser().getDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void subirEdadUsuario(String edad, FirebaseAuth auth) {
        mDatabase.child(Constants.EDAD.replaceFirst("e","E")).child(auth.getCurrentUser().getUid()).setValue(edad);
    }

    public void subirTipoUsuario(){

    }

    public void subirPago(FirebaseAuth auth) {
        mDatabase.child(Constants.PAGO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                    dataSnapshot.child(auth.getCurrentUser().getUid()).child(Constants.PAGO).getRef().setValue("0");
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

   public void FillUserInformation(LoadUserInformation loadUserInformation){
        DataUser user = new DataUser();
        mDatabase.child(Constants.USUARIOS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                  if(snapshot.hasChild(Constants.NOMBRE))
                    user.setFirstAndLastName(String.valueOf( snapshot.child(Constants.NOMBRE).getValue()));
                  if(snapshot.hasChild(Constants.FECHACUMPLE))
                    user.setBirthDate(snapshot.child(Constants.FECHACUMPLE).getValue(Long.class));
                  if(snapshot.hasChild(Constants.GENERO))
                    user.setGender(snapshot.child(Constants.GENERO).getValue(String.class));
                  else
                   user.setGender("");
                }
                loadUserInformation.getUserInformation(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   }




}
