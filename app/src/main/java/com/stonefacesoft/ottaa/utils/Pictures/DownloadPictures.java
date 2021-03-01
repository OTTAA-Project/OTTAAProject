package com.stonefacesoft.ottaa.utils.Pictures;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.Bitmap.bajarFotos;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.preferences.User;

import java.io.File;

import static com.stonefacesoft.ottaa.GaleriaGrupos2.downloadDialog;

public class DownloadPictures {
    private String name;
    private String StoragePath;
    private String texto;
    private String id;
    private FirebaseUtils firebaseUtils;
    private AppCompatActivity mActivity;
    private User firebaseUser;
    private bajarFotos bajar;
    private File mDirectorio;
    private GaleriaGrupos2.ShowDismissDialog showDismissDialog;
    private FirebaseSuccessListener mFbSuccessListenerInterfaz;


    public DownloadPictures(  AppCompatActivity mActivity, GaleriaGrupos2.ShowDismissDialog showDismissDialog,FirebaseSuccessListener mFbSuccessListenerInterfaz){
        this.mActivity=mActivity;
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this.mActivity);
        firebaseUser=new User(this.mActivity);
        this.showDismissDialog=showDismissDialog;
        this.mFbSuccessListenerInterfaz=mFbSuccessListenerInterfaz;
        requestPermission();
    }


    public void downloadFile(){  showDismissDialog.sendMessage(1);
        firebaseUtils.getmDatabase().child(Constants.FOTOSUSUARIO).child(firebaseUser.getUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size=(int)dataSnapshot.getChildrenCount();
                Log.e("UsersPictures", "onDataChange: "+size );
                if(downloadDialog !=null){

                    downloadDialog.setMax((int)dataSnapshot.getChildrenCount());
                }
                //  pd.setMax((int) dataSnapshot.getChildrenCount());
                //si el valor a obtener del mapeo no esta vacio

                int value=0;
                showDismissDialog.sendMessage(0);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String child=snapshot.getValue().toString();
                    bajar.setFileDirectory(mDirectorio);
                    Log.e("UserUid", "onDataChange: "+ child );
                    if(!dataSnapshot.getChildren().iterator().hasNext())
                        bajar.bajarFoto(child,true,firebaseUtils);
                    else
                        bajar.bajarFoto(child,false,firebaseUtils);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showDismissDialog.sendMessage(0);
            }
        });}

    public void requestPermission(){
        bajar=new bajarFotos();
        bajar.setInterfaz(mFbSuccessListenerInterfaz);
        Log.e("GalGr_bajarFotos: ", "" + mDirectorio);
        mDirectorio = new UriFiles(mActivity.getApplicationContext()).dir();
        String text[]=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        requestActivityPermission();
    }

    public void setUpDirectory(){
        if(mDirectorio.exists() ) {
            bajar.setFileDirectory(mDirectorio);
            downloadFile();
        }
    }


    public File getmDirectorio() {
        return mDirectorio;
    }

    public bajarFotos getBajar() {
        return bajar;
    }

    public void  requestActivityPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
                .checkSelfPermission(mActivity.getApplicationContext(), android.Manifest
                        .permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
                (mActivity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, Constants.EXTERNAL_STORAGE);

            }
        }
    }

}
