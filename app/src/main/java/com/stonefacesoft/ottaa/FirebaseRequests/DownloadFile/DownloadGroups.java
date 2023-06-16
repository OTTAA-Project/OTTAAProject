package com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class DownloadGroups extends DownloadFile{
    public DownloadGroups(Context mContext, DatabaseReference mDatabase, StorageReference mStorageReference, SharedPreferences sharedPreferences, ObservableInteger observableInteger,String locale) {
        super(mContext, mDatabase, mStorageReference, sharedPreferences, observableInteger,locale);
        TAG ="DownloadGroups";

    }

    public  void syncGroups(){
        final File gruposUsuarioFile = new File(rootPath, Constants.ARCHIVO_GRUPOS);

        mDatabase.child(Constants.Grupos).child(uid).child("URL_grupos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), locale)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Grupos").child("grupos_" + email + "_" + ConfigurarIdioma.getLanguaje()+ "." + "txt");

                 mStorageReference.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                         Log.d("BAF_descGYPN", "Tama&ntildeoArchivoGrupo :" + taskSnapshot.getTotalByteCount());
                         Log.d("BAF_descGYPN", "NombreArchivo:" + gruposUsuarioFile);
                         try {
                             if (!getStringFromFile(gruposUsuarioFile
                                     .getAbsolutePath()).equals("[]") &&
                                     gruposUsuarioFile.length() > 0 ) {
                                 json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                 if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
                                     Log.e(TAG, "Error al guardar Json");
                             }
                             observableInteger.incrementValue();
                         } catch (Exception e) {
                             e.printStackTrace();
                             Log.e("printStackTrace", "" + e);

                         }
                     }



                 });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void downloadOldOrNewGroups(){
        mDatabase.child(Constants.Grupos).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "bajarGrupos: entrando " );
                String child = "URL_" + Constants.Grupos.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.Grupos).child(Constants.Grupos.toLowerCase() + "_" +email + "_" + locale + "." + "txt");

                    final File gruposUsuarioFile = new File(rootPath, Constants.ARCHIVO_GRUPOS);
                    mStorageReference.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else{
                                    Log.e(TAG, "Groups saved");
                                    gruposUsuarioFile.delete();
                                }

                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                Log.e(TAG, "Fallo al guardar Grupo json 1" + ex.getMessage());
                                CrashlyticsUtils.getInstance().getCrashlytics().log(ex.getMessage());
                            }
                        }
                    }).addOnFailureListener(DownloadGroups.super::onFailure);

                }
                else {
                    mStorageReference = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/grupos/" + "grupos_" + locale + "." + "txt");
                    final File gruposUsuarioFile = new File(rootPath, Constants.ARCHIVO_GRUPOS);
                    mStorageReference.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else
                                    Log.e(TAG, "Grupo json");
                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                Log.e(TAG, "ex :" +ex.getMessage());

                            }
                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );
            }




        });
    }
}
