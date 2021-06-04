package com.stonefacesoft.ottaa.JSONutils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.stonefacesoft.ottaa.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadFiles extends AsyncTask<Void,Void,String> {
    private String string;
    private final String fileName;
    private final Context mContext;
    private final static String TAG="ReadFilesClass";
    public ReadFiles(String fileName,Context mContext){
        this.mContext=mContext;
        this.fileName=fileName;
    }
    @Override
    protected String doInBackground(Void... voids) {
        File archivo = new File(mContext.getFilesDir(), fileName);
        if (archivo.length() > Constants.CINCO_MEGAS) {
            Log.d(TAG, "doInBackground: Archivo menor a 5 mb");
        } else {
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            FileInputStream fis = null;
            try {
                if (fileName.equals(Constants.ARCHIVO_PICTOS) || fileName.equals(Constants.ARCHIVO_GRUPOS) || fileName.equals(Constants.ARCHIVO_FRASES) || fileName.equals(Constants.ARCHIVO_PICTOS_DATABASE) || fileName.equals(Constants.ARCHIVO_FRASES_JUEGOS) || fileName.equals(Constants.ARCHIVO_JUEGO) || fileName.equals(Constants.ARCHIVO_JUEGO_DESCRIPCION))
                    fis = mContext.openFileInput(fileName);
                else
                    fis = new FileInputStream(new File(fileName));

                reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                fis.close();
            } catch (IOException e) {
                Log.e(TAG, "readFromFile: " + e.toString());
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "readFromFile: " + e.toString());

                    }
                }
            }
            return builder.toString();
        }
        return "[]";

    }
}
