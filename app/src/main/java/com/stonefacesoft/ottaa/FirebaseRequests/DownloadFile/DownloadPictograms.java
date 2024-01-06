package com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

public class DownloadPictograms extends DownloadFile implements OnFailureListener {
    public DownloadPictograms(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger,String locale) {
        super(mContext, mDatabase, mStorageReference,sharedPreferences,observableInteger,locale);
        TAG ="DownloadPictograms";

    }

    public void syncPictograms() {
        mDatabase.child(Constants.PICTOS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String child = "URL_pictos_"+locale;
                if(snapshot.hasChild(child)){;
                    final File pictosUsuarioFile = new File(rootPath,Constants.ARCHIVO_PICTOS);
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.PICTOS).child(Constants.PICTOS.toLowerCase() + "_" + email+ "_" + locale + "." + "txt");
                    mStorageReference.getFile(pictosUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                if (pictosUsuarioFile.length() > 0 && !getStringFromFile
                                        (pictosUsuarioFile.getAbsolutePath()).equals("[]") &&
                                        getStringFromFile(pictosUsuarioFile.getAbsolutePath()
                                        ) != null) {
                                    json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuarioFile.getAbsolutePath()));
                                    json.guardarJson(Constants.ARCHIVO_PICTOS);

                                }
                                observableInteger.incrementValue();
                            } catch (Exception ex) {

                            }
                        }
                    }) ;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void downloadPictogramsWithNullOption(){
        mDatabase.child(Constants.PICTOS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.PICTOS.toLowerCase() + "_" + locale;
                Log.e(TAG, "bajar Pictos: "+ child );

                if (dataSnapshot.hasChild(child)) {
                    Log.e(TAG, "bajar Pictos: Has child" );
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.PICTOS).child(Constants.PICTOS.toLowerCase() + "_" + email+ "_" + locale + "." + "txt");

                    final File pictosUsuariosFile = new File(rootPath, Constants.ARCHIVO_PICTOS);
                    mStorageReference.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else
                                    Log.e(TAG, "Pictos Usuarios json");

                            } catch (Exception ex) {
                                Log.e(TAG, "Fallo al guardar pictos json 1");
                                CrashlyticsUtils.getInstance().getCrashlytics().log(ex.getMessage());

                            }
                         observableInteger.set(observableInteger.get() + 1);
                        }

                    }).addOnFailureListener(DownloadPictograms.super::onFailure);
                } else {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/pictos/" + "pictos_" + locale + "." + "txt");
                    String gender = getGenderByValue();
                    Log.d(TAG, "Gender onDataChange: "+ gender);
                    if(locale.equals("es"))
                        mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/pictos/es/pictos_es_"+gender+".txt");

                    final File pictosUsuariosFile = new File(rootPath, Constants.ARCHIVO_PICTOS);
                    mStorageReference.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {

                                json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else{
                                    Log.d(TAG,"Pictogram Saved");
                                }
                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                observableInteger.set(-1);
                                Log.e(TAG, ex.getMessage());
                            }
                        }

                    }).addOnFailureListener(DownloadPictograms.super::onFailure);

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );

            }
        });
    }

    private String getGenderByValue(){
        String value = sharedPrefsDefault.getString(Constants.GENERO,"other");
        Log.d(TAG, "getGenderByValue: "+value);
        switch (value.toLowerCase()){
            case "femenino":
                return "f";
            case "masculino":
                return "m";
            default:
                return "gen";
        }
    }



}
