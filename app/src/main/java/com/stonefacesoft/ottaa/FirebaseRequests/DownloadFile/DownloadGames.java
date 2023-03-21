package com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.File;
import java.util.Locale;

public class DownloadGames extends DownloadFile{
    public DownloadGames(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger, String locale) {
        super(mContext, mDatabase, mStorageReference, sharedPreferences, observableInteger,locale);
        TAG ="DownloadGames";

    }

    public void downloadGame() {
        Log.e(TAG, "bajar juegos: ");

        mDatabase.child(Constants.JUEGOS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.JUEGOS.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.JUEGOS).child(Constants.JUEGOS.toLowerCase() + "_" + email + "_" + locale + "." + "txt");

                    final File juegosUsuariosFile = new File(rootPath, Constants.ARCHIVO_JUEGO);
                    mStorageReference.getFile(juegosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSonArrayJuegos(json.readJSONArrayFromFile(juegosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_JUEGO)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                } else
                                    Log.e(TAG, "Pictos Usuarios json");
                            } catch (Exception ex) {

                            }
                            if (observableInteger != null)
                                observableInteger.incrementValue();
                        }

                    }).addOnFailureListener(DownloadGames.super::onFailure);
                } else {
                    if (observableInteger != null)
                        observableInteger.incrementValue();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());


            }
        });
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        int errorCode = ((StorageException) e).getErrorCode();
        String errorMessage = e.getMessage();
        Log.e(TAG, "onFailure: "+errorCode +" :"+errorMessage );
    }

}
