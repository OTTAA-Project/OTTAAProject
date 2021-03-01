package com.stonefacesoft.ottaa.FirebaseRequests;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.utils.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;

public class SubirBackupFirebase {

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FileInputStream mPictosStream, mGruposStream,mFrasesStream,mFotosStream;
    private Context mContext;
    private String TAGFrases = "SubirBackFireb_frases";
    private String TAGPictos = "SubirBackFireb_pictos";
    private String TAGGrupos = "SubirBackFireb_grupos";
    private String TAGFotos = "SubirBackFireb_fotos";



    public SubirBackupFirebase(Context mContext) {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.mContext = mContext;
    }



    public Boolean subirFrasesFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef){

        final StorageReference referenciaFrases = mStorageRef;

        try {
            mFrasesStream = mContext.openFileInput(Constants.ARCHIVO_FRASES);


        referenciaFrases.putStream(mFrasesStream).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return referenciaFrases.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    String urlFrasesUpload = downloadUri.toString();

                    mDatabase.setValue(urlFrasesUpload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference referenciaFrases) {
                            if(referenciaFrases!=null){

                                Log.d(TAGFrases, "Se guardo correctamente url Frases");
                            }else{
                                Log.d(TAGFrases, "Error al subir url Frases");
                            }
                        }
                    });

                } else {
                    Log.d(TAGFrases,"Subida Fallida: "+task.getException());

                }
            }
        });
        mFrasesStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  false;
    }


    public Boolean subirPictosFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef){

        final StorageReference referenciaPictos = mStorageRef;

        try {
            mPictosStream = mContext.openFileInput(Constants.ARCHIVO_PICTOS);


        referenciaPictos.putStream(mPictosStream).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return referenciaPictos.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    String urlPictoUpload = downloadUri.toString();

                    mDatabase.setValue(urlPictoUpload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference referenciaPictos) {
                            if(referenciaPictos!=null){

                                Log.e(TAGPictos, "Se guardo correctamente url Pictos");
                            }else{
                                Log.e(TAGPictos, "Error al subir url Pictos");
                            }
                        }
                    });

                } else {
                    Log.e(TAGPictos,"Subida Faillida: "+task.getException());

                }
            }
        });
        mPictosStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  false;
    }



    public Boolean subirGruposFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {

        final StorageReference referenciaGrupos = mStorageRef;

        try {
            mGruposStream = mContext.openFileInput(Constants.ARCHIVO_GRUPOS);


        referenciaGrupos.putStream(mGruposStream).continueWithTask(new Continuation<UploadTask
                .TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return referenciaGrupos.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String urlGrupoUpload = downloadUri.toString();
                    Log.e("DownloadUrlGruposTask",""+urlGrupoUpload);
                    mDatabase.setValue(urlGrupoUpload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                            if(referenciaGrupos!=null){
                                Log.e(TAGGrupos, "Se guardo correctamente url Grupos");
                            }else
                            {
                                Log.e(TAGGrupos, "Error al subir url Grupos");
                            }

                        }
                    });

                } else {
                    Log.e(TAGGrupos,"Subida Fallida: "+task.getException());
                }
            }
        });
        mGruposStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean subirFotosBackupFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {

        final StorageReference referenciaFotos = mStorageRef;

        try {
            mFotosStream = mContext.openFileInput(Constants.ARCHIVO_FOTO_BACKUP);


        referenciaFotos.putStream(mFotosStream).continueWithTask(new Continuation<UploadTask
                .TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return referenciaFotos.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String urlFotoUpload = downloadUri.toString();
                    Log.e("DownloadUrlFotoTask",""+urlFotoUpload);
                    mDatabase.setValue(urlFotoUpload, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                            if(referenciaFotos!=null){
                                Log.e(TAGFotos, "Se guardo correctamente url Fotos Backup");
                            }else
                            {
                                Log.e(TAGFotos, "Error al subir url Fotos");
                            }

                        }
                    });

                } else {
                    Log.e(TAGFotos,"Subida Fallida: "+task.getException());
                }
            }
        });
            mFotosStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;

        }



}
