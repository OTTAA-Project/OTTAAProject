package com.stonefacesoft.ottaa.FirebaseRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadFavoritePhrases;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadGames;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadGroups;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadPhrases;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadPictograms;
import com.stonefacesoft.ottaa.FirebaseRequests.DownloadFile.DownloadPredictionsPictograms;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Created by GastonSaillen on 10/5/2018.
 */

public class BajarJsonFirebase {

    private String locale;
    private final SharedPreferences sharedPrefsDefault;
    private final DatabaseReference mDatabase;
    private final FirebaseAuth mAuth;
    //    private String storageUrl,fotoName;
    private static final String TAG = "BajarJsonFirebase";
    private final Context mContext;
    private StorageReference mStorageRefGrupos, mStorageRefPictos, mStorageRefFrases, mStorageRefFrasesJuegos, mStorageRefDescJuegos;
    private final Json json;
    private  FirebaseUtils firebaseUtils;

    private volatile String uid="";
    private FirebaseSuccessListener mFbSuccessListenerInterfaz;


    public BajarJsonFirebase(SharedPreferences sharedPrefsDefault, FirebaseAuth mAuth, Context mContext) {

        this.sharedPrefsDefault = sharedPrefsDefault;

        this.mAuth = mAuth;
        this.mContext = mContext;
        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();
        setupFirebase();
        //this.json.leerPictosFrasesGrupos();
        this.mDatabase = firebaseUtils.getmDatabase();


        //  mAuth = FirebaseAuth.getInstance();

    }

    private void setupFirebase() {
        firebaseUtils= FirebaseUtils.getInstance();
        firebaseUtils.setmContext(mContext);
        firebaseUtils.setUpFirebaseDatabase();
        uid = firebaseUtils.getUid();
    }


    // Setter para el listener de la clase BajarJsonFirebase
    public void setInterfaz(FirebaseSuccessListener interfaz){
        this.mFbSuccessListenerInterfaz = interfaz;
    }

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public void descargarGruposNuevos() {
        if (!mAuth.getCurrentUser().getUid().isEmpty()) {
            locale = Locale.getDefault().getLanguage();
            Log.e("BAF_descGYPN", "locale :" + locale);

            mDatabase.child(Constants.Grupos).child(mAuth.getCurrentUser().getUid()).child("URL_grupos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), locale)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshotGrupos) {


                    Log.e("Descarga", "Existen Datos");
                    //Creamos donde se van a guardar los archivos, en este caso en cache momentaneo
                    File rootPath = new File(mContext.getCacheDir(), "Archivos_OTTAA");
                    if (!rootPath.exists()) {
                        rootPath.mkdirs();//si no existe el directorio lo creamos
                    }
                    mStorageRefGrupos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Grupos").child("grupos_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje()+ "." + "txt");
                    mStorageRefPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Pictos").child("pictos_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje() + "." + "txt");
                    mStorageRefFrases = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Frases").child("frases_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje() + "." + "txt");
                    final File pictosUsuarioFile = new File(rootPath, "pictos.txt");
                    final File gruposUsuarioFile = new File(rootPath, "grupos.txt");
                    final File frasesUsuarioFile = new File(rootPath, "frases.txt");


                    mStorageRefGrupos.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("printStackTrace", "" + e);
                            }
                            Log.e("BAF_descGYPN", "gruposBajados2 : " + "true");
                            Log.e("BAF_descGYPN", "gruposBajados3 :" + "true");
                            mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.GRUPOS_DESCARGADOS);
                        }


                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void syncFiles() {
        if (!FirebaseUtils.getInstance().getUid().isEmpty()) {
            locale = Locale.getDefault().getLanguage();
            Log.e("BAF_descGYPN", "locale :" + locale);


            ObservableInteger observableInteger = new ObservableInteger();
            observableInteger.setOnIntegerChangeListener(new ObservableInteger.OnIntegerChangeListener() {
                @Override
                public void onIntegerChanged(int newValue) {
                    Log.d(TAG, "onIntegerChanged: "+ newValue);
                    switch (newValue){
                        case 0:
                            syncPictograms(observableInteger);
                        break;
                        case 1:
                            syncGroups(observableInteger);
                        break;
                        case 2:
                            syncPhrasesFiles(observableInteger);
                        break;
                        case 3:
                            bajarFrasesFavoritas(locale,uid,observableInteger);
                        break;
                        case 4:
                            bajarJuego(locale,uid,observableInteger);
                        default:
                            mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.TODO_DESCARGADO);
                            break;
                    }
                }
            });
                observableInteger.set(0);


            }else{
                Log.d(TAG, "syncPictogramsandGroups: problem UID is empty");
        }

    }

    private void syncPhrasesFiles(ObservableInteger observableInteger){
        new DownloadPhrases(mContext,mDatabase,mStorageRefFrases,sharedPrefsDefault,observableInteger,ConfigurarIdioma.getLanguaje()).syncPhrases();
    }

    private void syncPictograms(ObservableInteger observableInteger){
        new DownloadPictograms(mContext,mDatabase,mStorageRefPictos,sharedPrefsDefault,observableInteger,ConfigurarIdioma.getLanguaje()).syncPictograms();
    }

    private void syncGroups(ObservableInteger observableInteger){
        new DownloadGroups(mContext,mDatabase,mStorageRefGrupos,sharedPrefsDefault,observableInteger,ConfigurarIdioma.getLanguaje()).syncGroups();
    }

    public void descargarPictosDatabase(StorageReference mStorageReferencePictosDatabase) {
        new DownloadPredictionsPictograms(mContext,mDatabase,mStorageReferencePictosDatabase,sharedPrefsDefault,null,ConfigurarIdioma.getLanguaje()).downloadPictograms(mFbSuccessListenerInterfaz);
    }


    public void bajarGrupos(String locale, ObservableInteger observableInteger) {
        new DownloadGroups(mContext,mDatabase,mStorageRefGrupos,sharedPrefsDefault,observableInteger,locale).downloadOldOrNewGroups();
    }


    public void bajarFrases(String locale, ObservableInteger observableInteger) {
        new DownloadPhrases(mContext,mDatabase,mStorageRefGrupos,sharedPrefsDefault,observableInteger,locale).downloadPhrases();
    }

    public void bajarFrasesFavoritas(String locale){
        bajarFrasesFavoritas( locale,uid,null);
    }

    public void bajarFrasesFavoritas(String locale,String uid,ObservableInteger observableInteger) {
        new DownloadFavoritePhrases(mContext,mDatabase,mStorageRefPictos,sharedPrefsDefault,observableInteger,locale).DownloadFavoritePhrases();
    }

    public void bajarPictos(String locale, ObservableInteger observableInteger) {
        new DownloadPictograms(mContext,mDatabase,mStorageRefPictos,sharedPrefsDefault,observableInteger,locale).downloadPictogramsWithNullOption();
    }





    public void bajarJuego(String locale){
            bajarJuego(locale,uid,null);
    }

    public void bajarJuego(String locale,String uid,ObservableInteger observableInteger) {
            new DownloadGames(mContext,mDatabase,mStorageRefDescJuegos,sharedPrefsDefault,observableInteger,locale).downloadGame();
    }
}
