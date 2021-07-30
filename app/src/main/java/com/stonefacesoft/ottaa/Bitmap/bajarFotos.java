package com.stonefacesoft.ottaa.Bitmap;


import android.net.Uri;
import android.util.Log;

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
import com.stonefacesoft.ottaa.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;


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
                String urlPhoto=dataSnapshot.child("url_foto").getValue().toString();
                String name=dataSnapshot.child("nombre_foto").getValue().toString();
                Log.d(TAG, "onDataChange: "+ urlPhoto);
                if(dataSnapshot!=null)
                    downloadUserImages(urlPhoto,name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Que hace sta clase?
    public void collectUserData(Map<String,Object> users) {

        int i = 0;
        ArrayList<String> urlfotos = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Creamos un map para obtener los datos del usuario en firebase
            if(!users.entrySet().isEmpty()) {
                if(entry.getValue()!=null){
                    Map userData = (Map) entry.getValue();
                    urlfotos.add((String) userData.get("nombre_foto"));
                    urlfotos.add((String) userData.get("url_foto"));
                    urlfotos.add((String) userData.get("texto_picto"));
                    downloadUserImages(((String) userData.get("url_foto")),((String) userData.get("nombre_foto")));

                }
            }


        }
    }

    private void downloadUserImages(String url_foto, final String nombre_picto){
        //bajarFotos_DUI bajarFotos_downloadUserImages
        FirebaseStorage storage = FirebaseStorage.getInstance();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Log.d(TAG, "downloadUserImages: "+url_foto);
        try{
            StorageReference mReference = storage.getReferenceFromUrl(String.valueOf(url_foto));
            String name=nombre_picto;

            if(mReference.toString().contains(".png")) {
                name=nombre_picto.replace(".jpg",".png");
            }

            Log.d(TAG,"bajarFotos_DUI : "+"nombreArchivo :"+name);
            final File pictosUsuarioFile = new File(dir,name);
            //metodo para bajar la foto en caso de no existir
            if(!pictosUsuarioFile.exists()){
                mReference.getFile(Uri.fromFile(pictosUsuarioFile)).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getTask().isComplete()) {
                            Log.d(TAG,"bajarFoto_dowldUsrImag :"+" Downloading Pictures");
                            if(mFbSuccessListenerInterfaz!=null)
                                 mFbSuccessListenerInterfaz.onFotoDescargada(Constants.FOTO_DESCARGADA);
                            Log.d(TAG,"position : "+"onSuccess: "+position +" - size:"+ size );
                            if(isThelast)
                                GaleriaGrupos2.showDismissDialog.sendMessage(0);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"bajarFoto_dowldUsrImag : "+"Downloading picture error.\n" +
                                "The error has  happened  at the position :"+position+" ,and the list size is : "+ size);
                            GaleriaGrupos2.showDismissDialog.sendMessage(0);
                    }
                });
            }else{
                Log.d(TAG,"bajarFoto_dowldUsrImag : "+"Los archivos ya existen y estan descargados");
                mFbSuccessListenerInterfaz.onFotoDescargada(Constants.FOTOS_YA_DESCARGADAS);
                Log.d(TAG,"position "+ "onSuccess: "+position +" - size:"+ size );
                if(isThelast)
                    GaleriaGrupos2.showDismissDialog.sendMessage(0);
            }

        }catch (Exception ex) {
            Log.e(TAG,"error "+ex.getMessage());
            GaleriaGrupos2.showDismissDialog.sendMessage(0);
        }
    }
}