package com.stonefacesoft.ottaa.Bitmap;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;


public class UriFiles {

    private final Context mContext;
    private String path;

    public UriFiles(Context mContext){
        this.mContext=mContext;
    }


    public String downloadBeforeAndroidQ(){
        String pathAux = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getApplicationContext().getPackageName()
                + "/Files";
        return pathAux;
    }
    public String downloadAfterAndroidQ(){
        String path=mContext.getExternalFilesDir(null).getPath();
        return path;
    }

    public File dir(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            path=downloadAfterAndroidQ();
        }else
            path=downloadBeforeAndroidQ();
        final File dir = new File(path);
        if(!dir.exists())
        dir.mkdirs(); //create folders where write files
        return dir;
    }

    public String getPath() {
        return path;
    }


    public boolean existDir(String dir){
        File directory=new File(dir);
        return directory.exists();
    }
}
