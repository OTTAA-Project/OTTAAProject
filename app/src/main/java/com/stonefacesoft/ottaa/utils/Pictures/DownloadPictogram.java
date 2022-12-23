package com.stonefacesoft.ottaa.utils.Pictures;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Interfaces.FindAllPictogramsInterface;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadPictogram extends DownloadTask {
    private String TAG ="DownloadPictogram";
    private String title;
    private JSONObject object;
    private FirebaseAuth mAuth;
    private String uid;
    private String pushKey;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseUtils firebaseUtils;

    private FindAllPictogramsInterface findAllPictogramsInterface;
    public DownloadPictogram(AppCompatActivity context, String text, int tipo,JSONObject object,FindAllPictogramsInterface findAllPictogramsInterface) {
        super(context, text, tipo);
        this.object = object;
        this.findAllPictogramsInterface = findAllPictogramsInterface;
        setupFirebase();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mDatabase =firebaseUtils.getmDatabase();
    }

    @Override
    protected void makeAction(String result) {
        cargarFotosFirebase();

    }

    private void cargarFotosFirebase() {

        File file = new File(path);
        final String mImageName = file.getName();
        final StorageReference referenciaFotos = mStorageRef.child("Archivos_Usuarios").child("Fotos").child(uid).child(mImageName);

        referenciaFotos.putFile(Uri.fromFile(file)).continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                Task<Uri>>() {
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
                    String urlFotoUpload = downloadUri.toString();
                    Pictogram pictogram = new Pictogram(object, ConfigurarIdioma.getLanguaje());
                    pictogram.createObject();
                    pushKey = mDatabase.push().getKey();
                    Map<String, Object> fotosUsuario = new HashMap<>();
                    fotosUsuario.put("nombre_foto", mImageName);
                    fotosUsuario.put("texto_picto", pictogram.getName());
                    fotosUsuario.put("url_foto", urlFotoUpload);
                    mDatabase.child(Constants.FOTOSUSUARIO).child(mAuth.getCurrentUser().getUid()).child(pushKey).setValue(pushKey);
                    mDatabase.child(Constants.FOTOS).child(pushKey).updateChildren(fotosUsuario);
                    // pd.dismiss();
                    JSONutils.setImagen(object,path,urlFotoUpload,pushKey);
                    pictogram = new Pictogram(object,ConfigurarIdioma.getLanguaje());
                    findAllPictogramsInterface.downloadPictogram(pictogram);
                } else {
                    Log.e(TAG, "onComplete: Error" + task.getException());
                }

            }

        });

    }

    private void setupFirebase() {
        firebaseUtils= FirebaseUtils.getInstance();
        firebaseUtils.setmContext(appCompatActivity);
        firebaseUtils.setUpFirebaseDatabase();
    }
}
