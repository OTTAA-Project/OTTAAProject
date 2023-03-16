package com.stonefacesoft.ottaa.FirebaseRequests;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles.UploadGames;
import com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles.UploadPhrase;
import com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles.UploadingFavoritePhrases;
import com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles.UploadingGroups;
import com.stonefacesoft.ottaa.FirebaseRequests.UploadFiles.UploadingPictos;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by GastonSaillen on 10/5/2018.
 */

public class SubirArchivosFirebase {
    private final SharedPreferences sharedPrefsDefault;
    private final StorageReference mStorageRef;
    private final DatabaseReference mDatabase;
    private final FirebaseAuth mAuth;
    private String uid;
    //tiempo, hora y dia
    //nombre que se la va a asignar al backup que se hace de la imagen en el firebase
    private FileInputStream pictos, grupos, frasesGuardadas,Juegos;
    private final Context mContext;
    private String dateStr;
    private FirebaseSuccessListener mFbSuccessListenerInterfaz;
    private static final String TAG="SubirArchivos";
    private final FirebaseUtils firebaseUtils;

    public SubirArchivosFirebase(Context mContext) {
        this.mContext = mContext;
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(mContext);
        firebaseUtils.setUpFirebaseDatabase();
        this.mDatabase = firebaseUtils.getmDatabase();
        mAuth=FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(firebaseAuth -> {
            if(firebaseAuth!=null&&firebaseAuth.getCurrentUser()!=null){
                uid = mAuth.getCurrentUser().getUid();
            }
        });

        this.sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    // Setter para el listener de la clase BajarJsonFirebase
    public void setInterfaz(FirebaseSuccessListener interfaz){
        this.mFbSuccessListenerInterfaz = interfaz;
    }

    public DatabaseReference getmDatabase(FirebaseAuth mAuth, String NombreDeArchivo) {
        return mDatabase.child(NombreDeArchivo).child(mAuth.getCurrentUser().getUid()).child("URL_" + NombreDeArchivo.toLowerCase() + "_" + sharedPrefsDefault.getString(mContext
                .getString(R.string.str_idioma), "en"));

    }

    public StorageReference getmStorageRef(FirebaseAuth mAuth, String
            NombreDeArchivo) {
        return mStorageRef.child("Archivos_Usuarios").child(NombreDeArchivo).child
                (NombreDeArchivo.toLowerCase() + "_" + mAuth
                .getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(mContext.getString(R
                .string.str_idioma), "en") + "." + "txt");
    }

    public void subirPictosFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirPictosFirebase_TAG
        new UploadingPictos(mContext,mDatabase,mStorageRef).uploadFile();
    }

    public void uploadFavoritePhrases(final DatabaseReference mDatabase,StorageReference mStorageRef){
        new UploadingFavoritePhrases(mContext,mDatabase,mStorageRef).uploadFile();
    }

    public void userDataExists(DatabaseReference mDatabasePictos,
                               final DatabaseReference mDatabaseGrupos, final DatabaseReference
                                       mDatabaseFrases) {
         userDataExistListener(mDatabasePictos,Constants.PICTOS_ENCONTRADOS);
         userDataExistListener(mDatabaseGrupos,Constants.GRUPOS_ENCONTRADOS);
         userDataExistListener(mDatabaseFrases,Constants.FRASES_ENCONTRADOS);
    }

    public void userDataExistListener(DatabaseReference mDatabase,int value){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mFbSuccessListenerInterfaz.onDatosEncontrados(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class getTiempoGoogle extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                URL url = new URL("https://google.com");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    dateStr = urlConnection.getHeaderField("Date");
                    Date startDate = df.parse(dateStr);
                    dateStr = String.valueOf(startDate.getTime() / 1000);
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                Log.d("SAF_GTG_Response", e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute(final Void unused) {
            if (mAuth.getCurrentUser().getUid() != null) {

                mDatabase.child(Constants.PRIMERAULTIMACONEXION).child(mAuth.getCurrentUser().getUid()).child("UConexion").setValue(dateStr, (databaseError, databaseReference) -> {
                    Principal.cerrarSession = true;
                    Log.d("SAF_GTG_TAG", "Se guardo correctamente");
                    mFbSuccessListenerInterfaz.onArchivosSubidos(Principal.cerrarSession);// use the to notify when the file has been upload
                });
            }


        }
    }

    public void subirGruposFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirGruposFirebase_TAG
        new UploadingGroups(mContext,mDatabase,mStorageRef).uploadFile();
    }


    public boolean subirFrasesFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirFrasesFirebase_TAG
        new UploadPhrase(mContext,mDatabase,mStorageRef).uploadFile();
        return false;
    }
    public boolean subirJuegos(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirFrasesFirebase_TAG
        new UploadGames(mContext,mDatabase,mStorageRef).uploadFile();
        return false;
    }



    public void subirFotosOffline() {

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    Log.e(TAG, "onDataChange: Connected");
                    //chequear por fotos en el directorio, si existe, subirlas y borrarlas de la carpeta una vez terminada.
                    if (checkPhotosInFolder()) {
                        //Una vez que sabemos que existe la carpeta y tiene contenido adentro, buscamos ese contenido para subir a Firebase
                        String destinationPathCheckFirstTime = Environment.getExternalStorageDirectory().toString();
                        final File myDirOfflinePhotos = new File(destinationPathCheckFirstTime + "/GcHgMf/offlineFotos");
                        //Listamos los archivos en la carpeta
                        File[] files = myDirOfflinePhotos.listFiles();
                        for (final File file : files) {
                            Log.e(TAG, "onDataChange: " + file.getName());

                            final StorageReference referenciaFotos = mStorageRef.child("Archivos_Usuarios").child("Fotos").child(mAuth.getCurrentUser().getUid()).child(file.getName());

                            referenciaFotos.putFile(Uri.fromFile(file)).continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return referenciaFotos.getDownloadUrl();
                            }).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String urlFoto = downloadUri.toString();
                                    Log.e("downloadUrlTask", "" + urlFoto);

                                    String pushKey = mDatabase.push().getKey();

                                    Log.e("pushKey", "" + pushKey);

                                    Map<String, Object> fotosUsuario = new HashMap<>();
                                    fotosUsuario.put("nombre_foto", file.getName());
                                    fotosUsuario.put("url_foto", urlFoto);
                                    mDatabase.child(Constants.FOTOSUSUARIO).child(mAuth.getCurrentUser().getUid()).child(pushKey).setValue(pushKey);
                                    mDatabase.child(Constants.FOTOS).child(pushKey).updateChildren(fotosUsuario);

                                } else {
                                    Log.e("Subida fallida", "" + task.getException());
                                }

                            });
                        }
                        //Borrar el contenido de la carpeta offlineFotos
                        if (myDirOfflinePhotos.isDirectory()) {
                            String[] children = myDirOfflinePhotos.list();
                            for (int i = 0; i < children.length; i++) {
                                if (children[i] != null)
                                    new File(myDirOfflinePhotos, children[i]).delete();
                            }
                        }
                    } else {
                        //Todas las fotos subidas
                        Log.e(TAG, "onDataChange: Empty folder ");
                    }
                } else {
                    Log.e(TAG, "onDataChange: Not Connected");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    private boolean checkPhotosInFolder() {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            //Chequeamos que el directorio donde estan las fotos existe
            String destinationPathOfflinePhotos = Environment.getExternalStorageDirectory().toString();
            File myDirOfflinePhotos = new File(destinationPathOfflinePhotos + "/GcHgMf/offlineFotos/");
            if (myDirOfflinePhotos.exists()) {
                Log.e(TAG, "checkPhotosInFolder: directory exists ");
                //Chequeamos que en el directorio existe contenido
                File[] files = myDirOfflinePhotos.listFiles();
                if (files.length > 0) {
                    Log.e(TAG, "checkPhotosInFolder: Hay archivos");
                    return true;
                }
            }
        }

        return false;

    }







}

