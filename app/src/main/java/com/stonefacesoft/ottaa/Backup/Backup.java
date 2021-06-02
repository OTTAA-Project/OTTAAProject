package com.stonefacesoft.ottaa.Backup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class Backup {
   private Context mContext;
   private long lastConnection;
   private String name;
   private ArrayList<String> photos;

   public Backup(Context mContext,String name){
      this.mContext=mContext;
      this.name=name;
   }

   public void prepareFirstLocalBackup(String timeStamp){

      if (ContextCompat.checkSelfPermission(mContext,
              Manifest.permission.WRITE_EXTERNAL_STORAGE)
              == PackageManager.PERMISSION_GRANTED) {

         //Una vez dado chequeamos que la carpeta de backup esta creada o no
         String destinationPathCheckFirstTime = Environment.getExternalStorageDirectory().toString();
         File myDirFirstTime = new File(destinationPathCheckFirstTime + "/GcHgMf/");
         if (myDirFirstTime.exists()) {
            //Si esta creada no hacemos nada y termina
            Log.d("Principal_PrimerBack", "Ya existe la carpeta, esperando 7 dias para hacer nuevo backup");
         } else {
            //Si no esta creada agarramos y hacemos el primer backup
            myDirFirstTime.mkdir();
           createBackupFile(timeStamp);
         }

      }
   }

   public void preparelocalBackup(String timeStamp){
      long lastConnection = System.currentTimeMillis() / 1000;
      long lastConnectionPlus7Days = (System.currentTimeMillis() / 1000) + 604800;
      String destinationPatasdh = Environment.getExternalStorageDirectory().toString();
      File myDiasgr = new File(destinationPatasdh + "/GcHgMf/");
      long ultimoDatoLocal = myDiasgr.lastModified() / 1000;
      Log.d("Principal_GuardLoc", "ultimoDatoLocal : " + ultimoDatoLocal);
      //preguntamos si paso 7 dias , si no pasaron no se hace nada, sino se hace backup
      if (ultimoDatoLocal + Constants.UNA_SEMANA < lastConnection) {
         Log.d("Principal_GuardLoc", "Pasaron 7 dias haciendo backup local");
         final File pictosUsuarioBackupFilasde = new File(myDiasgr, Constants.ARCHIVO_PICTOS);

         //Nos fijamos si el permiso de escribir en el storage esta dado para hacer el backup local.
         if (ContextCompat.checkSelfPermission(mContext,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 == PackageManager.PERMISSION_GRANTED) {
            createBackupFile(timeStamp);
         }
      }
   }
   public void prepareOnlineBackup(){

   }
   public void getLocalBackup(){

   }
   public void getOnlineBackup(){

   }
   private String getTimeStamp() {
      Long tslong = System.currentTimeMillis() / 1000;
      return tslong.toString();
   }

   private void createBackupFile(String timeStamp){
      String sourcePath = mContext.getFilesDir().getAbsolutePath();
      Log.e("Principal_sourcePath", "" + sourcePath);
      File file = new File(sourcePath, name);
      //Guardamos el backup localmente en una carpeta para poder restaurar sin conexion
      String destinationPath = Environment.getExternalStorageDirectory().toString();
      File myDir = new File(destinationPath + "/GcHgMf/" + timeStamp);
      if (!myDir.exists()) {
         myDir.mkdirs();//si no existe el directorio lo creamos
      }
      final File backupFile = new File(myDir, name);
      try {
         if(file.length()>0)
         FilesUtils.copyFile(file,backupFile);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
