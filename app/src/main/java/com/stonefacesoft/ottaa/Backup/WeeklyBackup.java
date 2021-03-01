package com.stonefacesoft.ottaa.Backup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;

public class WeeklyBackup implements FirebaseSuccessListener {

    private String mBackupDates, mLocalBackupDates, mHumanDate, mSelectedBackupDate, mSelectedLocalBackupDate, mPictosBackupUrl, mFrasesBackupUrl, mGruposBackupUrl, mFotosBackupUrl;
    private String mPictosDownloadUrlDispacher, mFrasesDownloadUrlDispacher, mGruposDownloadUrlDispacher, mFotosDownloadUrlDispacher;
    private List<String> lstBackups;
    private List<String> lstBackupsLocal;
    private ArrayList<String> lstBackupsLocalMilis;
    private ArrayList<String> lstBackupsMilis;
    private SharedPreferences sharedPrefsDefault;
    private Context mContext;
    private BajarJsonFirebase bajarJsonFirebase;
    private DatabaseReference mDatabase, mPictosDatabaseRefBackup, mFrasesDatabaseRefBackup, mGruposDatabaseRefBackup;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef, mStorageRefGruposBackup, mStorageRefPictosBackup, mStorageRefFrasesBackup;
    private int mCheckDescarga;
    private ProgressDialog dialog;
    ConnectionDetector cd;
    private FirebaseAnalytics mFirebaseAnalytics;


    public WeeklyBackup(Context context) {
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
        this.mContext = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.dialog = new ProgressDialog(mContext);
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.cd = new ConnectionDetector(mContext);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "WeeklyBackup");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);

        if (mAuth.getCurrentUser() != null) {
            //referencia database storage archivos
            mStorageRefPictosBackup = mStorageRef.child("Archivos_Usuarios").child("Pictos").child("pictos_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en") + "." + "txt");
            mStorageRefGruposBackup = mStorageRef.child("Archivos_Usuarios").child("Grupos").child("grupos_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en") + "." + "txt");
            mStorageRefFrasesBackup = mStorageRef.child("Archivos_Usuarios").child("Frases").child("frases_" + mAuth.getCurrentUser().getEmail() + "_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en") + "." + "txt");
            //Referencia database archivos
            mPictosDatabaseRefBackup = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("URL_ArchivosUsuario").child("URL_pictos_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en"));
            mFrasesDatabaseRefBackup = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("URL_ArchivosUsuario").child("URL_frases_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en"));
            mGruposDatabaseRefBackup = mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("URL_ArchivosUsuario").child("URL_grupos_" + sharedPrefsDefault.getString(context.getString(R.string.str_idioma), "en"));
        }

        bajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, context);
        bajarJsonFirebase.setInterfaz(this);
    }


    public void weeklyBackupDialog(boolean isCancelable, int mensaje, boolean hasNegativeButton) {
        //you shall not PASS...
        //Inflamos el spinner adentro del AlertDialog
        View spinnerView = LayoutInflater.from(mContext).inflate(R.layout.activity_custom_spinner, null);
        final Spinner fechasBackupUsuario = spinnerView.findViewById(R.id.spinnerBackup);

        if (!ConnectionDetector.isNetworkAvailable(mContext)) {
            //backupLocal
            lstBackupsLocal = new ArrayList<>();
            lstBackupsLocalMilis = new ArrayList<>();
            String destinationPath = Environment.getExternalStorageDirectory().toString();
            File f = new File(destinationPath + "/GcHgMf/");
            File[] files = f.listFiles();
            if (files != null) {
                for (File inFile : files) {
                    if (inFile.isDirectory()) {

                        String mLocalBackupDates = inFile.getName();
                        lstBackupsLocalMilis.add(String.valueOf(inFile.getName()));
                        Log.d("weeklyBackupDialog: ", "" + inFile.getName());
                        try {
                            long serverDate = Long.parseLong(mLocalBackupDates);
                            String humanDate = convertTimeWithTimeZome(serverDate * 1000) + " " + mContext.getString(R.string.datos_locales_backup);
                            lstBackupsLocal.add(humanDate);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            } else {
                Toast.makeText(mContext, "No existen archivos para hacer backup.", Toast.LENGTH_SHORT).show();

            }

            //Ahora creamos el spinner con estos datos de backup
            final ArrayAdapter<String> backupAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, lstBackupsLocal);
            backupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fechasBackupUsuario.setAdapter(backupAdapter);



            fechasBackupUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    lstBackupsLocalMilis.remove("Fotos");

                    mSelectedLocalBackupDate = lstBackupsLocalMilis.get(i);


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Creamos una lista donde almacenamos el dato de los backups del usuario
                    lstBackups = new ArrayList<>();
                    lstBackupsMilis = new ArrayList<>();

                    //TODO ver de sacar el for snapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mBackupDates = snapshot.getKey();
                        if (!mBackupDates.contains("UltimoBackup")) {
                            long serverDate = Long.parseLong(mBackupDates);
                            String humanDate = convertTimeWithTimeZome(serverDate * 1000)+" "+mContext.getString(R.string.datos_online_backup);
                            Log.d("FirebaseBackup", "HumanDate: " + humanDate + " \\n ServerDate: " + mBackupDates);
                            lstBackups.add(humanDate);
                            lstBackupsMilis.add(mBackupDates);
                            int cantBackups = lstBackups.size();
                            Log.d("FirebaseBackups", "Cantidad de backups: " + cantBackups);
                        }

                        //Ahora creamos el spinner con estos datos de backup
                        final ArrayAdapter<String> backupAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, lstBackups);
                        backupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fechasBackupUsuario.setAdapter(backupAdapter);


                        //Seteamos un click para cada elemento de nuestro spinner
                        fechasBackupUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mSelectedBackupDate = lstBackupsMilis.get(i);
                                Log.e("FirebaseBackup", "" + mSelectedBackupDate);

                                //Ahora, para cualquier seleccion que tome el getItemAtPosition obtenemos los datos, en este caso seleccionamos un timestamp y traemos los datos de ese timestamp
                                mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Backup").child(mSelectedBackupDate).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        //Obtenemos los valores de la referencia tanto como pictos y grupos backup dependiendo a su pais de origen
                                        mPictosBackupUrl = dataSnapshot.child("Backup_pictos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")).getValue(String.class);
                                        mGruposBackupUrl = dataSnapshot.child("Backup_grupos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")).getValue(String.class);
                                        mFrasesBackupUrl = dataSnapshot.child("Backup_frases_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")).getValue(String.class);
                                        mFotosBackupUrl = dataSnapshot.child("Backup_fotos_" + sharedPrefsDefault.getString(mContext.getString(R.string.str_idioma), "en")).getValue(String.class);

                                        Log.e("FirebaseBackups", "Url Pictos: " + mPictosBackupUrl + " \\n Url Grupos: " + mGruposBackupUrl + "Url Frases: " + mFrasesBackupUrl);
                                        //Usamos este dispacher global para guardar las url de pictos y grupos para poder usarlos despues en una referencia del storage y traer los datos a OTTAA

                                        mPictosDownloadUrlDispacher = mPictosBackupUrl;
                                        mGruposDownloadUrlDispacher = mGruposBackupUrl;
                                        mFrasesDownloadUrlDispacher = mFrasesBackupUrl;

                                        //Es el unico que comparo por que en el primer backup siempre voy a tener grupos pictos y frases, pero fotosbackup lo tengo a partir
                                        // del segundo backup, entonces para no generar un databasereference null , lo pregunto antes de obtenerlo
                                        if (mFotosBackupUrl != null) {

                                            mFotosDownloadUrlDispacher = mFotosBackupUrl;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //Ahora nos queda armar el AlertDialog para mostrar el spinner y poder usarlo para traer nuestros datos
        alertDialogBackup(spinnerView, isCancelable, mensaje, hasNegativeButton);
    }

    private void alertDialogBackup(View spinnerView, boolean isCancelable, final int mensaje, final boolean hasNegativeButton) {

        final AlertDialog.Builder builderBackup;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builderBackup = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);

        } else {
            builderBackup = new AlertDialog.Builder(mContext);
        }

        builderBackup.setTitle(R.string.pref_header_backup)
                .setView(spinnerView)
                .setMessage(mensaje)
                .setCancelable(isCancelable);

        builderBackup.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                try {


                    dialog.setTitle("Estamos restaurando OTTAA");
                    dialog.setMessage("Aguarde...");
                    dialog.setCancelable(false);
                    dialog.show();

                    if (!ConnectionDetector.isNetworkAvailable(mContext)) {

                        String sourcePath = mContext.getFilesDir().getAbsolutePath();
                        final File pictosSourcePath = new File(sourcePath, Constants.ARCHIVO_PICTOS);
                        final File gruposSourcePath = new File(sourcePath, Constants.ARCHIVO_GRUPOS);
                        final File frasesSourcePath = new File(sourcePath, Constants.ARCHIVO_FRASES);
                        final File fotosSourcePath = new File(sourcePath, Constants.ARCHIVO_FOTO_BACKUP);
                        Log.e( "onClick: ",""+mSelectedLocalBackupDate );
                        String destinationPath = Environment.getExternalStorageDirectory().toString();
                        File backupDir = new File(destinationPath + "/GcHgMf/" + mSelectedLocalBackupDate);
                        File[] files = backupDir.listFiles();
                        if (files != null && files.length > 0) {
                            for (File inFile : files) {

                                Log.e("onItemSelected: ", "" + inFile.getName());

                                final File pictosUsuarioBackupFile = new File(backupDir, Constants.ARCHIVO_PICTOS);
                                final File gruposUsuarioBackupFile = new File(backupDir, Constants.ARCHIVO_GRUPOS);
                                final File frasesUsuarioBackupFile = new File(backupDir, Constants.ARCHIVO_FRASES);
                                final File fotosUsuarioBackupFile = new File(backupDir, Constants.ARCHIVO_FOTO_BACKUP);
                                try {
                                    //copio los pictos,grupos y frases
                                    if (FilesUtils.copyFile(pictosUsuarioBackupFile, pictosSourcePath) &&
                                            FilesUtils.copyFile(gruposUsuarioBackupFile, gruposSourcePath) &&
                                            FilesUtils.copyFile(frasesUsuarioBackupFile, frasesSourcePath)) {
                                        if(dialog.isShowing()) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(mContext, Principal.class);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();
                                        }
                                    }

                                    if (fotosSourcePath.length() == 0) {
                                        Log.e("guardarBackupLocal: ", "Size: " + fotosUsuarioBackupFile.length());
                                    } else {
                                        //copio el directorio
                                        FilesUtils.copyFile(fotosUsuarioBackupFile, fotosSourcePath);

                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {

                        //Bueno, entonces, si el dispacher de url fotos no es null ni vacio podemos bajar las fotos y despues continuamos y bajamos las otras tres cosas
                        // si esta condicion evalua que es null o vacio directamente bajamos los otros 3
                        if (mFotosDownloadUrlDispacher != null && !mFotosDownloadUrlDispacher.isEmpty()) {

                            bajarJsonFirebase.descargarFirebaseBackup(mFotosDownloadUrlDispacher);

                        }

                        bajarJsonFirebase.descargarFirebaseBackup(mPictosDownloadUrlDispacher, mGruposDownloadUrlDispacher, mFrasesDownloadUrlDispacher);

                    }


                    //Aca ya obtuvimos los dos archivos y los pusimos, por lo tanto el backup se realizo con exito, aca es un buen lugar donde poner el dismiss del progressDialog
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, R.string.please_select_date, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });


        if (hasNegativeButton) {
            builderBackup.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
        }

        builderBackup.setIcon(R.drawable.ic_settings_backup_restore_black_24dp);
        builderBackup.show();
    }


    @Override
    public void onDescargaCompleta(int termino) {

        mCheckDescarga += termino;
        Log.e("checkDescarga", "" + mCheckDescarga);

        if (mCheckDescarga == Constants.TODO_DESCARGADO) {
            SubirArchivosFirebase subirArchivos = new SubirArchivosFirebase(mContext);
            subirArchivos.subirPictosFirebase(mPictosDatabaseRefBackup, mStorageRefPictosBackup);
            subirArchivos.subirFrasesFirebase(mFrasesDatabaseRefBackup, mStorageRefFrasesBackup);
            subirArchivos.subirGruposFirebase(mGruposDatabaseRefBackup, mStorageRefGruposBackup);

            dialog.dismiss();
            mCheckDescarga = 0;
            Intent intent = new Intent(mContext,Principal.class);
            mContext.startActivity(intent);
            ((Activity)mContext).finish();

        }


    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {

    }

    @Override
    public void onFotoDescargada(int fotosDescargadas) {

    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {

    }

    public String convertTimeWithTimeZome(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time);
        return (cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.DAY_OF_MONTH));

    }

}
