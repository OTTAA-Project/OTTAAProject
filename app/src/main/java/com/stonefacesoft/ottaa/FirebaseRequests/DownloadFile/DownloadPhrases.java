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
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONException;

import java.io.File;

public class DownloadPhrases extends DownloadFile{
    public DownloadPhrases(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger,String locale) {
        super(mContext, mDatabase, mStorageReference, sharedPreferences, observableInteger,locale);
        TAG ="DownloadPhrases";

    }

    public void syncPhrases(){
        mDatabase.child(Constants.Frases).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String child = "URL_frases_" + locale;
                if(snapshot.hasChild(child)){
                    Log.e(TAG, "bajarFrases: "+ child );

                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.Frases).child(Constants.Frases.toLowerCase() + "_" + email + "_" + locale + "." + "txt");
                    final File frasesUsuarioFile = new File(rootPath, "frases.txt");
                    mStorageReference.getFile(frasesUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.e(TAG, "bajarFrases: "+ child );
                            boolean areTheSameFile = false;
                            try{
                                 areTheSameFile = json.verifyFiles(Constants.ARCHIVO_FRASES,frasesUsuarioFile);
                            }catch (Exception ex){

                            }
                            if(!areTheSameFile){
                                try {
                                    if (!getStringFromFile(frasesUsuarioFile.getAbsolutePath()).equals("[]") && frasesUsuarioFile.length() > 0) {

                                        json.setmJSONArrayTodasLasFrases(json.readJSONArrayFromFile(frasesUsuarioFile.getAbsolutePath()));
                                        json.guardarJson(Constants.ARCHIVO_FRASES);
                                    }
                                } catch (JSONException | FiveMbException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    }
                            }

                            observableInteger.incrementValue();

                        }
                    });
                }else{
                    observableInteger.incrementValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void downloadPhrases(){
        Log.e(TAG, "bajarFrases: " );
        mDatabase.child(Constants.Frases).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.Frases.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.Frases).child(Constants.Frases.toLowerCase() + "_" + email + "_" + locale + "." + "txt");
                    final File frasesUsuario = new File(rootPath, Constants.ARCHIVO_FRASES);
                    mStorageReference.getFile(frasesUsuario).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodasLasFrases(json.readJSONArrayFromFile(frasesUsuario.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_FRASES)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }
                                else {
                                    Log.d(TAG, "Phrases Saved");
                                }

                            } catch (JSONException | FiveMbException e) {
                                e.printStackTrace();
                            }
                            observableInteger.set(observableInteger.get() + 1);
                        }
                    }).addOnFailureListener(DownloadPhrases.super::onFailure);
                } else {
                    observableInteger.set(observableInteger.get() + 1);
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
