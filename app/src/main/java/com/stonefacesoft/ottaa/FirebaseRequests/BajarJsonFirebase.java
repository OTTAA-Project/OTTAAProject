package com.stonefacesoft.ottaa.FirebaseRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
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
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.FilesUtils;
import com.stonefacesoft.ottaa.utils.ObservableInteger;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.preferences.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Created by GastonSaillen on 10/5/2018.
 */

public class BajarJsonFirebase implements OnFailureListener {

    private String locale;
    private final SharedPreferences sharedPrefsDefault;
    private final DatabaseReference mDatabase;
    private final FirebaseAuth mAuth;
    //    private String storageUrl,fotoName;
    private static final String TAG = "BajarJsonFirebase";
    private final Context mContext;
    private StorageReference mStorageRefGrupos, mStorageRefPictos, mStorageRefFrases, mStorageRefFrasesJuegos, mStorageRefDescJuegos;
    private final Json json;
    private final FirebaseUtils firebaseUtils;
    //   private String uid;
    private FirebaseSuccessListener mFbSuccessListenerInterfaz;
    private SharedPreferences sharedPrefs;

    public BajarJsonFirebase(SharedPreferences sharedPrefsDefault, FirebaseAuth mAuth, Context mContext) {

        this.sharedPrefsDefault = sharedPrefsDefault;

        this.mAuth = mAuth;
        this.mContext = mContext;
        Json.getInstance().setmContext(mContext);
        this.json = Json.getInstance();
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this.mContext);
        firebaseUtils.setUpFirebaseDatabase();
        //this.json.leerPictosFrasesGrupos();



        this.mDatabase = firebaseUtils.getmDatabase();
        //  mAuth = FirebaseAuth.getInstance();
        //  uid = mAuth.getUid();
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
                                Log.e("printStackTrace", "" + e.toString());
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

    public void syncPictogramsandGroups() {
        if (!mAuth.getCurrentUser().getUid().isEmpty()) {
            locale = Locale.getDefault().getLanguage();
            Log.e("BAF_descGYPN", "locale :" + locale);
            mDatabase.child(Constants.Grupos).child(mAuth.getCurrentUser().getUid()).child("URL_grupos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), locale)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshotGrupos) {
                    mDatabase.child(Constants.PICTOS).child(mAuth.getCurrentUser().getUid()).child("URL_pictos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), locale)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshotPictos) {
                            mDatabase.child(Constants.Frases).child(mAuth.getCurrentUser().getUid()).child("URL_frases_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), locale)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotFrases) {
                                    Log.e("Descarga", "Existen Datos");
                                    //Creamos donde se van a guardar los archivos, en este caso en cache momentaneo
                                    File rootPath = new File(mContext.getCacheDir(), "Archivos_OTTAA");
                                    if (!rootPath.exists()) {
                                        rootPath.mkdirs();//si no existe el directorio lo creamos
                                    }
                                    mStorageRefGrupos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Grupos").child("grupos_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje()+ "." + "txt");
                                    mStorageRefPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Pictos").child("pictos_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje()+ "." + "txt");
                                    mStorageRefFrases = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child("Frases").child("frases_" + mAuth.getCurrentUser().getEmail() + "_" +ConfigurarIdioma.getLanguaje()+ "." + "txt");


                                    //    mStorageRefFrasesJuegos=FirebaseStorage.getInstance().getReference().child("Archivos_Paises").child("frases-juegos").child("frases_"+sharedPrefsDefault.getString("idioma","es")+".txt");
                                    final File pictosUsuarioFile = new File(rootPath, "pictos.txt");
                                    final File gruposUsuarioFile = new File(rootPath, "grupos.txt");
                                    final File frasesUsuarioFile = new File(rootPath, "frases.txt");
                                    final File frasesJuegosPictos=new File(rootPath,"frasesjuegos.txt");
                                    mStorageRefPictos.getFile(pictosUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Log.d("BAF_descGYPN", "Tama&ntildeoArchivoPicto:" + taskSnapshot.getTotalByteCount());
                                            Log.d("BAF_descGYPN", "NombreArchivo:" + pictosUsuarioFile);
                                            Log.d("BAF_descGYPN", "Tama&ntildeoArchivoss :" + pictosUsuarioFile.length());
                                            try {
                                                if (pictosUsuarioFile.length() > 0 && !getStringFromFile
                                                        (pictosUsuarioFile.getAbsolutePath()).equals("[]") &&
                                                        getStringFromFile(pictosUsuarioFile.getAbsolutePath()
                                                        ) != null) {
                                                    json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuarioFile.getAbsolutePath()));
                                                    if (!json.guardarJson(Constants.ARCHIVO_PICTOS))
                                                        Log.e(TAG, "Error al guardar Json");
                                                }
                                                mStorageRefGrupos.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        Log.d("BAF_descGYPN", "Tama&ntildeoArchivoGrupo :" + taskSnapshot.getTotalByteCount());
                                                        Log.d("BAF_descGYPN", "NombreArchivo:" + gruposUsuarioFile);
                                                        try {
                                                            if (!getStringFromFile(gruposUsuarioFile
                                                                    .getAbsolutePath()).equals("[]") &&
                                                                    gruposUsuarioFile.length() > 0 &&
                                                                    getStringFromFile(pictosUsuarioFile.getAbsolutePath()) != null) {
                                                                json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                                                if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
                                                                    Log.e(TAG, "Error al guardar Json");

                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Log.e("printStackTrace", "" + e.toString());
                                                        }
                                                        Log.e("BAF_descGYPN", "gruposBajados2 : " + "true");
                                                        Log.e("BAF_descGYPN", "gruposBajados3 :" + "true");
                                                        mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.GRUPOS_DESCARGADOS);
                                                    }
                                                });

                                                mStorageRefFrases.getFile(frasesUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                        try {

                                                            if (!getStringFromFile(frasesUsuarioFile.getAbsolutePath()).equals("[]") && frasesUsuarioFile.length() > 0) {

                                                                json.setmJSONArrayTodasLasFrases(json.readJSONArrayFromFile(frasesUsuarioFile.getAbsolutePath()));
                                                                json.guardarJson(Constants.ARCHIVO_FRASES);
                                                                mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.FRASES_DESCARGADOS);

                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Log.e("printStackTrace", "" + e.toString());
                                                        }
                                                  }
                                                });
                                                bajarFrasesFavoritas(locale,rootPath);
                                                bajarJuego(locale,rootPath);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.PICTOS_DESCARGADOS);
                                        }

                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });
        }
    }

    public void descargarPictosDatabase(StorageReference mStorageReferencePictosDatabase) {

        File rootPath = new File(mContext.getCacheDir(), "Archivos_OTTAA");
        if (!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }

         File pictosDatabaseFile = new File(rootPath, Constants.ARCHIVO_PICTOS_DATABASE);

        mStorageReferencePictosDatabase.getFile(pictosDatabaseFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Log.e("md5Hash", "onSuccess: Se bajo pictosDatabase ");

                try {
                    if (!getStringFromFile(pictosDatabaseFile.getAbsolutePath()).equals("[]") && pictosDatabaseFile.length() > 0) {
                        Log.e("prueba", getStringFromFile(pictosDatabaseFile.getAbsolutePath()));
                        json.setmJSONArrayPictosSugeridos(json.readJSONArrayFromFile(pictosDatabaseFile.getAbsolutePath()));
                        if (!json.guardarJson(Constants.ARCHIVO_PICTOS_DATABASE))
                            Log.e(TAG, "Error al guardar Json");
                        mFbSuccessListenerInterfaz.onPictosSugeridosBajados(true);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                    mFbSuccessListenerInterfaz.onPictosSugeridosBajados(true);
                }

            }

        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                mFbSuccessListenerInterfaz.onPictosSugeridosBajados(true);
            }
        });


    }

    public void descargarFirebaseBackup(String mStorageReferencePictos, String mStorageReferenceGrupos, String mStorageReferenceFrases){
        //Cuando clicamos el boton aceptar del dialog , va a traernos grupos y pictos del timestamp seleccionado, creamos donde se van a guardar.
        File rootPath = new File(mContext.getCacheDir(),"Archivos_OTTAA");
        if(!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }

        //La referencia apunta al archivo que esta en el pictosDownloadDispacher
        StorageReference backupPictosRef = FirebaseStorage.getInstance().getReferenceFromUrl(mStorageReferencePictos);
        StorageReference backupGrupospRef = FirebaseStorage.getInstance().getReferenceFromUrl(mStorageReferenceGrupos);
        StorageReference backupFrasespRef = FirebaseStorage.getInstance().getReferenceFromUrl(mStorageReferenceFrases);


        final File pictosFile = new File(rootPath, Constants.ARCHIVO_PICTOS);
        final File gruposFile = new File(rootPath, Constants.ARCHIVO_GRUPOS);
        final File frasesFile = new File(rootPath, Constants.ARCHIVO_FRASES);

        //Obtenemos ese archivo del dispacher y lo llamamos pictos.txt y lo ponemos en memoria interna
        backupPictosRef.getFile(pictosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosFile.getAbsolutePath()));
                    if (!json.guardarJson(Constants.ARCHIVO_PICTOS))
                        Log.e(TAG, "Error al guardar el Json");

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.PICTOS_DESCARGADOS);

            }

        });

        backupGrupospRef.getFile(gruposFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {

                    json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposFile.getAbsolutePath()));
                    if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
                        Log.e(TAG, "Error al guardar grupos");

                } catch (JSONException | FiveMbException e) {
                    e.printStackTrace();
                }

                mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.GRUPOS_DESCARGADOS);

            }
        });

        backupFrasespRef.getFile(frasesFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                try {

                    json.setmJSONArrayTodasLasFrases(json.readJSONArrayFromFile(frasesFile.getAbsolutePath()));
                    if (!json.guardarJson(Constants.ARCHIVO_FRASES))
                        Log.e(TAG, "Error al guardar Json");


                } catch (JSONException | FiveMbException e) {
                    e.printStackTrace();
                }

                mFbSuccessListenerInterfaz.onDescargaCompleta(Constants.FRASES_DESCARGADOS);


            }

        });





    }
    public void descargarFirebaseBackup(String mStorageReferenceFotos){


        File rootPath = new File(mContext.getCacheDir(),"Archivos_OTTAA");
        final File fotoBackupRootPath = new File(Environment.getExternalStorageDirectory().toString()+"/GcHgMf/Fotos/");
        final File ottaaFotosFolder = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getPackageName()
                + "/Files");


        if(!rootPath.exists()) {
            rootPath.mkdirs();//si no existe el directorio lo creamos
        }

        if(!fotoBackupRootPath.exists()){
            fotoBackupRootPath.mkdirs();
        }

        if(!ottaaFotosFolder.exists()){
            ottaaFotosFolder.mkdirs();
        }


        StorageReference backupFotospRef = FirebaseStorage.getInstance().getReferenceFromUrl(mStorageReferenceFotos);

        final File fotosFile = new File(rootPath, Constants.ARCHIVO_FOTO_BACKUP);


        backupFotospRef.getFile(fotosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                try {
                    json.setmJSONArrayTodasLasFotosBackup(json.readJSONArrayFromFile(fotosFile.getAbsolutePath()));
                    if (!json.guardarJson(Constants.ARCHIVO_FOTO_BACKUP))
                        Log.e(TAG, "Error al guardar el Json");

                } catch (JSONException | FiveMbException e) {
                    e.printStackTrace();
                }

            }

        });

        try{

            for(JSONObject jsonObject : json.getmArrayListTodasLasFotosBackup()) {
                String storageUrl = null;
                try {
                    //Obtenemos los datos de la foto, en este caso la url y el path picto que tiene el nombre de la misma
                    storageUrl = jsonObject.getString("urlFoto");
                    String fotoName = jsonObject.getString("picto");
                    //Ahora lo que necesitamos hacer es cortar la ultima parte de ese string del path de la foto para agarrar el nombre solo de la foto
                    String fotoNameCut = fotoName.replace(ottaaFotosFolder.toString(),"");
                    //Al obtener el nombre de la foto ya se la asignamos al path donde se tiene que guardar y con que nombre se tiene que guardar
                    final File fotosBackupFile = new File(fotoBackupRootPath, fotoNameCut);
                    Log.e("UrlFoto:",""+storageUrl);
                    StorageReference bajarFotosBackup = FirebaseStorage.getInstance().getReferenceFromUrl(storageUrl);
                    bajarFotosBackup.getFile(fotosBackupFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            mFbSuccessListenerInterfaz.onFotoDescargada(Constants.FOTO_DESCARGADA);
                            Log.e("Dir",""+taskSnapshot.getTotalByteCount());
                            //Movemos el directorio de la foto de un lado al otro para generar el backup
                            try {
                                FilesUtils.copyDirectory(fotoBackupRootPath,ottaaFotosFolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).addOnFailureListener(this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ArrayIndexOutOfBoundsException | FiveMbException e) {
            e.printStackTrace();
        }

    }

    public void bajarGrupos(String locale, File roothPath, ObservableInteger observableInteger) {

        mDatabase.child(Constants.Grupos).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "bajarGrupos: entrando " );
                String child = "URL_" + Constants.Grupos.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosGrupos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.Grupos).child(Constants.Grupos.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");

                    final File gruposUsuarioFile = new File(roothPath, Constants.ARCHIVO_GRUPOS);
                    mStorageRefUsuariosGrupos.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else{
                                    Log.e(TAG, "Grupo json");
                                    gruposUsuarioFile.delete();
                                }

                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                Log.e(TAG, "Fallo al guardar Grupo json 1" + ex.getMessage());
                                CrashlyticsUtils.getInstance().getCrashlytics().log(ex.getMessage());
                                //Todo add a backup interface
                                /*mStorageRefGrupos = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/grupos/" + "grupos_" + locale + "." + "txt");
                                final File gruposUsuarioFile = new File(roothPath, Constants.ARCHIVO_GRUPOS);
                                mStorageRefGrupos.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            json.setmJSONArrayTodosLosGrupos(json.readJSONArrayFromFile(gruposUsuarioFile.getAbsolutePath()));
                                            if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
                                                Log.e(TAG, "Fallo al guardar json");
                                            }else{
                                                Log.e(TAG, "Grupo json");
                                                gruposUsuarioFile.delete();
                                            }
                                            observableInteger.set(observableInteger.get() + 1);
                                        } catch (Exception ex) {

                                        }
                                    }

                                });*/
                            }
                        }



                    }).addOnFailureListener(BajarJsonFirebase.this);

                }
                else {
                    mStorageRefGrupos = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/grupos/" + "grupos_" + locale + "." + "txt");
                    final File gruposUsuarioFile = new File(roothPath, Constants.ARCHIVO_GRUPOS);
                    mStorageRefGrupos.getFile(gruposUsuarioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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


    public void bajarFrases(String locale, File roothPath, ObservableInteger observableInteger) {
        Log.e(TAG, "bajarFrases: " );
        mDatabase.child(Constants.Frases).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.Frases.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosFrases = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.Frases).child(Constants.Frases.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");
                    final File frasesUsuario = new File(roothPath, Constants.ARCHIVO_FRASES);
                    mStorageRefUsuariosFrases.getFile(frasesUsuario).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodasLasFrases(json.readJSONArrayFromFile(frasesUsuario.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_FRASES)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }
                                observableInteger.set(observableInteger.get() + 1);
                            } catch (JSONException | FiveMbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(BajarJsonFirebase.this);
                } else {
                    observableInteger.set(observableInteger.get() + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );
                Log.e(TAG, "bajar Frases: failure" );
            }
        });
    }
    public void bajarFrasesFavoritas(String locale, File roothPath) {
        Log.e(TAG, "bajarFrases: " );
        mDatabase.child(Constants.FrasesFavoritas).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.FrasesFavoritas.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosFrases = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.FrasesFavoritas).child(Constants.FrasesFavoritas.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");
                    final File frasesUsuario = new File(roothPath, Constants.ARCHIVO_FRASES_FAVORITAS);
                    mStorageRefUsuariosFrases.getFile(frasesUsuario).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSonArrayFrasesFavoritas(json.readJSONArrayFromFile(frasesUsuario.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_FRASES_FAVORITAS)) {

                                    Log.e(TAG, "Fallo al guardar json");
                                }
                            } catch (JSONException | FiveMbException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(BajarJsonFirebase.this);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );
                Log.e(TAG, "bajar Frases: failure" );
            }
        });
    }

    public void bajarPictos(String locale, File roothPath, ObservableInteger observableInteger) {
        Log.e(TAG, "bajar Pictos: " );
        mDatabase.child(Constants.PICTOS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.PICTOS.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.PICTOS).child(Constants.PICTOS.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");

                    final File pictosUsuariosFile = new File(roothPath, Constants.ARCHIVO_PICTOS);
                    mStorageRefUsuariosPictos.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else
                                    Log.e(TAG, "Pictos Usuarios json");
                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                Log.e(TAG, "Fallo al guardar pictos json 1");
                                CrashlyticsUtils.getInstance().getCrashlytics().log(ex.getMessage());
                                //Todo add a backup interface
                                /*

                                mStorageRefPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/pictos/" + "pictos_" + locale + "." + "txt");
                                final File pictosUsuariosFile = new File(roothPath, Constants.ARCHIVO_PICTOS);
                                mStorageRefPictos.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                            if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                                                Log.e(TAG, "Fallo al guardar json");
                                            }else
                                                Log.e(TAG, "Pictos Usuarios json" );
                                            observableInteger.set(observableInteger.get() + 1);
                                        } catch (Exception ex) {
                                            Log.e(TAG, ex.getMessage());
                                        }
                                    }

                                });*/

                            }
                        }

                    }).addOnFailureListener(BajarJsonFirebase.this);
                } else {
                    mStorageRefPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/pictos/" + "pictos_" + locale + "." + "txt");
                    String gender = getGenderByValue();
                    Log.d(TAG, "Gender onDataChange: "+ gender);
                    if(locale.equals("es"))
                        mStorageRefPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Paises/pictos/es/pictos_es_"+gender+".txt");

                    final File pictosUsuariosFile = new File(roothPath, Constants.ARCHIVO_PICTOS);
                    mStorageRefPictos.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {

                                json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_PICTOS)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }
                                observableInteger.set(observableInteger.get() + 1);
                            } catch (Exception ex) {
                                observableInteger.set(-1);
                                Log.e(TAG, ex.getMessage());
                            }
                        }

                    }).addOnFailureListener(BajarJsonFirebase.this);

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

    public void bajarFrasesJuegos(String locale, File roothPath) {
        mDatabase.child(Constants.JUEGOS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.JUEGOS.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosPictos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.PICTOS).child(Constants.PICTOS.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");

                    final File pictosUsuariosFile = new File(roothPath, Constants.ARCHIVO_JUEGO);
                    mStorageRefUsuariosPictos.getFile(pictosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSONArrayTodosLosPictos(json.readJSONArrayFromFile(pictosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_JUEGO)) {
                                    Log.e(TAG, "The file hasn't been saved");
                                }else
                                    Log.e(TAG, "The file has been saved");
                            } catch (Exception ex) {
                                Log.e(TAG, "The file not exist");
                            }
                        }

                    }).addOnFailureListener(BajarJsonFirebase.this);
                } else {

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );

            }
        });
    }

    public void bajarJuego(String locale, File roothPath) {

        Log.e(TAG, "bajar juegos: " );
        mDatabase.child(Constants.JUEGOS).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String child = "URL_" + Constants.JUEGOS.toLowerCase() + "_" + locale;
                if (dataSnapshot.hasChild(child)) {
                    StorageReference mStorageRefUsuariosJuegos = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.JUEGOS).child(Constants.JUEGOS.toLowerCase() + "_" + mAuth.getCurrentUser().getEmail() + "_" + locale + "." + "txt");

                    final File juegosUsuariosFile = new File(roothPath, Constants.ARCHIVO_JUEGO);
                    mStorageRefUsuariosJuegos.getFile(juegosUsuariosFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                json.setmJSonArrayJuegos(json.readJSONArrayFromFile(juegosUsuariosFile.getAbsolutePath()));
                                if (!json.guardarJson(Constants.ARCHIVO_JUEGO)) {
                                    Log.e(TAG, "Fallo al guardar json");
                                }else
                                    Log.e(TAG, "Pictos Usuarios json");
                            } catch (Exception ex) {

                            }
                        }

                    }).addOnFailureListener(BajarJsonFirebase.this::onFailure);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );

            }
        });
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        int errorCode = ((StorageException) e).getErrorCode();
        String errorMessage = e.getMessage();
        Log.e(TAG, "onFailure: "+errorCode +" :"+errorMessage );
    }
}
