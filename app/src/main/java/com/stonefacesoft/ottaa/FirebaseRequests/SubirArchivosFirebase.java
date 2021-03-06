package com.stonefacesoft.ottaa.FirebaseRequests;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.FirebaseRequests.Files.UploadingFavoritePhrases;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.prefs;
import com.stonefacesoft.ottaa.utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Uri selectedImageUri;
    private Bitmap imageBitmap;
    private final StorageReference mStorageRef;
    private final DatabaseReference mDatabase;
    private final FirebaseAuth mAuth;
    private String uid;
    private String textoPicto;
    private String mTimeStamp;//tiempo, hora y dia
    private final String mNombreFirebase = "";//nombre que se la va a asignar al backup que se hace de la imagen en el firebase
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
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth!=null&&firebaseAuth.getCurrentUser()!=null){
                    uid = mAuth.getCurrentUser().getUid();
                }
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

    public Boolean subirPictosFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirPictosFirebase_TAG

        final StorageReference referenciaPictos = mStorageRef;

        try {

            pictos = mContext.openFileInput(Constants.ARCHIVO_PICTOS);
            if (pictos.available() > 3) {
                Log.e("subirArchivosLog", "subirGruposFirebase: " + pictos.available());

                Log.e("Subir Archivos Firebase", "subirPictosFirebase: " + pictos.getChannel().size());
                referenciaPictos.putStream(pictos).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                    if (referenciaPictos != null) {
                                        Log.d("SAF_SPF_TAG", "Se guardo correctamente url Pictos");
                                        try {
                                            pictos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.d("SAF_SPF_TAG", "Error al subir url Pictos");
                                    }
                                }
                            });

                        } else {
                            Log.e("error_SAF_SPF_TAG", "Subida fallida" + task.getException());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("No se subio el archivo", "onFailure: subir archivos");
                    }
                });
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void uploadFavoritePhrases(final DatabaseReference mDatabase,StorageReference mStorageRef){
        new UploadingFavoritePhrases(mContext,mDatabase,mStorageRef).uploadFile();
    }

    public void userDataExists(DatabaseReference mDatabasePictos,
                               final DatabaseReference mDatabaseGrupos, final DatabaseReference
                                       mDatabaseFrases) {

        mDatabasePictos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot pictosSnapshot) {
                if (pictosSnapshot.exists())
                    mFbSuccessListenerInterfaz.onDatosEncontrados(Constants.PICTOS_ENCONTRADOS);


                mDatabaseGrupos.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot gruposSnapshot) {

                        if (gruposSnapshot.exists())
                            mFbSuccessListenerInterfaz.onDatosEncontrados(Constants.GRUPOS_ENCONTRADOS);


                        mDatabaseFrases.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot frasesSnapshot) {

                                if (frasesSnapshot.exists())
                                    mFbSuccessListenerInterfaz.onDatosEncontrados(Constants.FRASES_ENCTONRADOS);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class getTiempoGoogle extends AsyncTask<Void, Void, Void> {
        //SAF_GTG: SubirArchivosFirebase_getTiempoGoogle
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    dateStr = response.getFirstHeader("Date").getValue();
                    Date startDate = df.parse(dateStr);
                    dateStr = String.valueOf(startDate.getTime() / 1000);
                    long horaActual = java.lang.Long.parseLong(dateStr);
                    //Here I do something with the Date String

                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
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

                mDatabase.child(Constants.PRIMERAULTIMACONEXION).child(mAuth.getCurrentUser().getUid()).child("UConexion").setValue(dateStr, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Principal.cerrarSession = true;
                        Log.d("SAF_GTG_TAG", "Se guardo correctamente");
                        mFbSuccessListenerInterfaz.onArchivosSubidos(Principal.cerrarSession);// use the to notify when the file has been upload
                        prefs.setMcerrarSesion(true);//when the file is finished notify to pref to close session from the user


                    }
                });
            }


        }
    }

    public Boolean subirGruposFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirGruposFirebase_TAG
        final StorageReference referenciaGrupos = mStorageRef;

        try {
            grupos = mContext.openFileInput(Constants.ARCHIVO_GRUPOS);
            Log.e("subirArchivosLog", "subirGruposFirebase: " + grupos.available());

            if (grupos.available() > 3) {
                referenciaGrupos.putStream(grupos).continueWithTask(new Continuation<UploadTask
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
                            Log.e("SAF_SGF__down", "" + urlGrupoUpload);
                            mDatabase.setValue(urlGrupoUpload, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                                    if (referenciaGrupos != null) {
                                        Log.d("SAF_SGF_TAG", "Se guardo correctamente url Grupos");
                                        try {
                                            grupos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.d("SAF_SGF_TAG", "Error al subir url Grupos");
                                    }

                                }
                            });

                        } else {
                            Log.e("SAF_SGF_TAG", "Subida fallida " + task.getException());
                        }
                    }
                });

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean subirFrasesFirebase(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirFrasesFirebase_TAG

        final StorageReference referenciaFrases = mStorageRef;

        try {
            frasesGuardadas = mContext.openFileInput(Constants.ARCHIVO_FRASES);
            if (frasesGuardadas.available() > 3) {
                Log.e("subirArchivosLog", "subirGruposFirebase: " + frasesGuardadas.available());

                referenciaFrases.putStream(frasesGuardadas).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                                    if (referenciaGrupos != null) {
                                        try {
                                            frasesGuardadas.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("SAF_SFF_TAG", "Se guardo correctamente url Frases");
                                    } else {
                                        Log.d("SAF_SFF_TAG", "Error al subir url Frases");
                                    }

                                }
                            });

                        } else {
                            Log.d("SAF_SFF_TAG", "Error al subir url Frases");
                            // Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean subirJuegos(final DatabaseReference mDatabase, StorageReference mStorageRef) {
        //SAF_SGF_TAG : SubirArchivosFirebase_subirFrasesFirebase_TAG
        final StorageReference referenciaJuegos = mStorageRef;
        try {
            Juegos = mContext.openFileInput(Constants.ARCHIVO_JUEGO);
            if (Juegos.available() > 3) {
                Log.e("subirArchivosLog", "subirGruposFirebase: " + Juegos.available());

                referenciaJuegos.putStream(Juegos).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return referenciaJuegos.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String urlFrasesUpload = downloadUri.toString();

                            mDatabase.setValue(urlFrasesUpload, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference referenciaGrupos) {
                                    if (referenciaGrupos != null) {
                                        Log.d("SAF_SFF_TAG", "Se guardo correctamente url Juegos");
                                        try {
                                            Juegos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.d("SAF_SFF_TAG", "Error al subir url Juegos");
                                    }

                                }
                            });

                        } else {
                            Log.d("SAF_SFF_TAG", "Error al subir url Juegos");
                            // Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                            referenciaFotos.putFile(Uri.fromFile(file)).continueWithTask(new Continuation<UploadTask
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
            public void onCancelled(DatabaseError error) {
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



    public void subirUltimaConexion(){
        new getTiempoGoogle().execute();
    }


}

