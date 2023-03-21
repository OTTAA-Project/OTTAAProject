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
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONException;

import java.io.File;
import java.util.Locale;

public class DownloadFavoritePhrases extends DownloadFile{
    public DownloadFavoritePhrases(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger, String locale) {
        super(mContext, mDatabase, mStorageReference, sharedPreferences, observableInteger,locale);
        TAG ="DownloadFavoritePhrase";
    }

    public void DownloadFavoritePhrases(){
        Log.e(TAG, "bajarFrases: " );
        mDatabase.child(Constants.FrasesFavoritas).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.FrasesFavoritas.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.FrasesFavoritas).child(Constants.FrasesFavoritas.toLowerCase() + "_" + email + "_" + locale + "." + "txt");
                    final File frasesUsuario = new File(rootPath, Constants.ARCHIVO_FRASES_FAVORITAS);
                    mStorageReference.getFile(frasesUsuario).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSonArrayFrasesFavoritas(json.readJSONArrayFromFile(frasesUsuario.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_FRASES_FAVORITAS)) {

                                    Log.e(TAG, "Fallo al guardar json");
                                }
                            } catch (JSONException | FiveMbException e) {
                                e.printStackTrace();
                            }
                            if(observableInteger!=null)
                                observableInteger.incrementValue();
                        }
                    }).addOnFailureListener(DownloadFavoritePhrases.super::onFailure);
                }else{
                    if(observableInteger!=null)
                        observableInteger.incrementValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );
                Log.e(TAG, "bajar Frases: failure" );
            }
        });
    }
}
