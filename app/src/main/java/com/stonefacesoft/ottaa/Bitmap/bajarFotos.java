package com.stonefacesoft.ottaa.Bitmap;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class bajarFotos {
    //    private Context mContext;
    private File dir;
    private FirebaseSuccessListener mFbSuccessListenerInterfaz;
    private int position;
    private int size;
    private boolean isThelast;
    private final String TAG="DownloadPictures";

    //private String url_foto,nombre_foto;


    public bajarFotos() {

       // new bajarFotos.dataUsuarioFotosAsync().execute();

    }

    // Setter para el listener de la clase BajarJsonFirebase
    public void setFileDirectory(File dir){
        this.dir=dir;
    }

    // Setter para el listener de la clase BajarJsonFirebase
    public void setInterfaz(FirebaseSuccessListener interfaz){
        this.mFbSuccessListenerInterfaz = interfaz;
    }


    public void bajarFoto(String uid,boolean isThelast,FirebaseUtils firebaseUtils) {
        this.isThelast=isThelast;
        DatabaseReference mDatabase =firebaseUtils.getmDatabase();
        mDatabase.child(Constants.FOTOS).child(uid.replace(" ","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!= null){
                    String urlPhoto="";
                    String name ="";
                    if(dataSnapshot.hasChild("url_foto")) {
                       urlPhoto = getSubstringUri(dataSnapshot.child("url_foto").getValue().toString());
                    }
                    if(dataSnapshot.hasChild("nombre_foto"))
                        name=dataSnapshot.child("nombre_foto").getValue().toString();
                    if(!urlPhoto.isEmpty()&&!name.isEmpty())
                        downloadUserImages(urlPhoto,name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void downloadUserImages(String url_foto, final String nombre_picto){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        try{
            StorageReference mReference = storage.getReferenceFromUrl(String.valueOf(url_foto));
            final File pictosUsuarioFile = new File(dir,replaceJPGAsPNG(nombre_picto,mReference));
            if(!pictosUsuarioFile.exists()){
                downloadFile(mReference,pictosUsuarioFile);
            }else{
                mFbSuccessListenerInterfaz.onFotoDescargada(Constants.FOTOS_YA_DESCARGADAS);
                if(isThelast)
                    GaleriaGrupos2.showDismissDialog.sendMessage(0);
            }

        }catch (Exception ex) {
            Log.e(TAG,"error "+ex.getMessage());
            GaleriaGrupos2.showDismissDialog.sendMessage(0);
        }
    }

    private String replaceJPGAsPNG(String pictoName,StorageReference mReference){
        String name = pictoName;
        if(mReference.toString().contains(".png"))
            name = pictoName.replace(".jpg",".png");
        if(mReference.toString().contains("null"))
            name = pictoName.substring(0,pictoName.lastIndexOf("null"))+".jpg";
        return name;
    }

    private String getSubstringUri(String path){
        String result = path;
        if(path.contains("?alt"))
            result = path.substring(0,path.lastIndexOf("?alt"));
        return result;
    }

    private void downloadFile(StorageReference mReference,File file){
        mReference.getFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot.getTask().isComplete()) {
                    if(mFbSuccessListenerInterfaz!=null)
                        mFbSuccessListenerInterfaz.onFotoDescargada(Constants.FOTO_DESCARGADA);
                    if(isThelast)
                        GaleriaGrupos2.showDismissDialog.sendMessage(0);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GaleriaGrupos2.showDismissDialog.sendMessage(0);
            }
        });
    }
}