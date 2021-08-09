package com.stonefacesoft.ottaa;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.Dialogos.NewDialogsOTTAA;
import com.stonefacesoft.ottaa.Dialogos.Progress_dialog_options;
import com.stonefacesoft.ottaa.Dialogos.Yes_no_otheroptionDialog;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Interfaces.DialogInterfaceTags;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.TagInterfazJson;
import com.stonefacesoft.ottaa.Interfaces.translateInterface;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.idioma.myContextWrapper;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.Custom_button;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;
import com.stonefacesoft.ottaa.utils.traducirTexto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author Hect&oacuter costa
 * <p>
 * ultima modificacion  : 24/10/18
 * realizada por : gonzalo
 * cambios : se modifico la interfaz y se agrego la funcionalidad a nuevos botones
 */

/*Con la KEY de ARASAAC, pedir el pictograma "castillo":
        http://arasaac.org/api/index.php?callback=json&language=ES&word=castillo&catalog=colorpictos&nresults=10&thumbnails=150&TXTlocate=4&KEY=XXXXXXXXXXXXXXXXXXXX
*/


public class Edit_Picto_Visual extends AppCompatActivity implements View.OnClickListener, FirebaseSuccessListener, translateInterface,
        DialogInterfaceTags, TagInterfazJson {

    private static final String TAG = "Edit_Picto";
    int permission = 0;
    //TTS
    private textToSpeech myTTS;

    //Shared prefs
    SharedPreferences sharedPrefsDefault;
    private textToSpeech hablar;

    //ID del picto editado y el texto del mismo

    EditText etOutLoud;
    //    EditText etNombrePicto;
    ImageButton btnHablar;
    ImageButton btnEditText;
    ImageButton btnEditFrame;
    ImageButton btnEditTAGs;
    ImageButton Negro;
    ImageButton Verde;
    ImageButton Amarillo;
    ImageButton Azul;
    ImageButton Naranja;
    ImageButton Magenta;
    ImageButton btnTagHora;
    ImageButton btnTagUbicacion;
    ImageButton btnTagCalendario;
    ImageButton btnTagEdad;



    Custom_Picto Picto;
    boolean iniciar = false;
    private String formato;
    private String urlfoto;
    // Datos que le paso por el intent!!!
    int PictoID;                // picto actual
    String texto = "";               // Texto del picto
    String nombre;              // Nombre del picto
    int padre;                  // pictoPadre del picto
    int sel;                    // cual de las 4 opciones fue seleccionada
    int grupo;                  // cual de las 4 opciones fue seleccionada
    boolean esGrupo;            // dice si este activity fue llamado de un picto que va a ser grupo o no
    int color;                  // el color del background del picto
    int tipo;                   // el tipo es una variable utilizada para el ottaa labs basada en el codigo FITZGERALD
    boolean esNuevo;
    String mCurrentPhotoPath;   // Guardamos el path de la foto
    String mCurrentBackupPhotoPath;
    String imagen;              // Guardamos la imagen que esta en la base de datos
    static String pushKey;
    JSONObject jsonObject;

    private Uri selectedImageUri;

    private StorageReference mStorageRef, mStorageRefGrupos, mStorageRefPictos;
    private DatabaseReference mDatabase, mDatabaseGrupos, mDatabasePictos;
    private FirebaseUtils firebaseUtils;
    private FirebaseAuth mAuth;
    private String uid;
    private String textoPicto;
    private String mTimeStamp;//tiempo, hora y dia
    private String mNombreFirebase = "";//nombre que se la va a asignar al backup que se hace de la imagen en el firebase
    private BajarJsonFirebase bajarJsonFirebase;
    private SubirArchivosFirebase uploadFile;
    boolean vienePrincipal;
    private int mCheckDescarga;
    private Json json;

    private ConnectionDetector mConnectionDetector;
    private traducirTexto traducirTexto;
    private TextView textViewTexto, textViewBtnFrame;
    private TextView textPickColor;
    private Context mContext;
    private AsignTags asignTags;

    private CardView cardViewTexto;
    private CardView cardViewFrame;
    private CardView cardViewTAGs;
    private Toolbar toolbar;
    private ImageView cornerImageView;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ConstraintLayout constraintBotonera;
    private Progress_dialog_options dialogs;
    private AnalyticsFirebase analyticsFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar", false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        new ConfigurarIdioma(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getApplicationContext().getString(R.string.str_idioma), "en"));
        setContentView(R.layout.edit_picto_visual);
        firebaseUtils=FirebaseUtils.getInstance();
        firebaseUtils.setmContext(mContext);
        firebaseUtils.setUpFirebaseDatabase();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        constraintBotonera = findViewById(R.id.constraintRightButtons);
        dialogs=new Progress_dialog_options(this);
        cornerImageView = findViewById(R.id.cornerImageViewLeft);
      /*  try{
            strictModeClassUtils.init(this);
        }catch (Exception ex){
            Log.e("StrictMode","No es valido en este punto");
        }*/
        //Obtenemos el userID del usuario logueado
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        //LLamamos a la clase subirArchivosFirebase para subir grupos, pictos y frases donde necesitemos
        uploadFile = new SubirArchivosFirebase(getApplicationContext());

        //Declaro el timestamp para el nombre de las fotos que se suben a firebase
        mTimeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        //Implemento el manejador de preferencias
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Json.getInstance().setmContext(getApplicationContext());
        json = Json.getInstance();


        asignTags = new AsignTags(getApplicationContext());
        asignTags.setInterfaz(this);
        asignTags.setInterfazTag(this);

        /*Referencias storage grupos, pictos*/
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /*Referencias database grupos pictos*/
        mDatabase =firebaseUtils.getmDatabase();

        bajarJsonFirebase = new BajarJsonFirebase(sharedPrefsDefault, mAuth, getApplicationContext());

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, IntentCode.MY_DATA_CHECK_CODE.getCode());


        Intent intent = getIntent();
        PictoID = intent.getIntExtra("PictoID", 0);
        texto = intent.getStringExtra("Texto");
        padre = intent.getIntExtra("Padre", 0);
        sel = intent.getIntExtra("Sel", 0);
        nombre = intent.getStringExtra("Nombre");
        esGrupo = intent.getBooleanExtra("esGrupo", false);
        esNuevo = intent.getBooleanExtra("esNuevo", false);
        grupo = intent.getIntExtra("Grupo", 0);
        color = intent.getIntExtra("Color", getResources().getColor(R.color.Orange));
        vienePrincipal = intent.getBooleanExtra("principal", false);
        textViewBtnFrame = findViewById(R.id.textViewBtnFrame);


        myTTS = new textToSpeech(this);


        Drawable draw = null;

        if (esNuevo) {
            draw = getResources().getDrawable(R.drawable.ic_agregar_nuevo);
            this.setTitle(getApplicationContext().getResources().getString(R.string.add_pictograma));
            if (esGrupo) {
                this.setTitle(getApplicationContext().getResources().getString(R.string.add_grupo));

            } else {

                tipo = 2;

            }
        } else if (esGrupo) {

            this.setTitle(getApplicationContext().getResources().getString(R.string.editar_group));

            jsonObject = json.getGrupoFromId(PictoID);

            if (jsonObject != null)
                draw = json.getIcono(jsonObject);
            try {
                if (jsonObject.getJSONObject("imagen").getString("pictoEditado").equals(""))
                    mCurrentPhotoPath = jsonObject.getJSONObject("imagen").getString("picto");
                else
                    mCurrentPhotoPath = jsonObject.getJSONObject("imagen").getString("pictoEditado");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(TAG, "onCreate: Error" + ex.getMessage());
            }
            texto = JSONutils.getNombre(jsonObject, ConfigurarIdioma.getLanguaje());
            Log.d(TAG, "onCreate: Nombre: " + texto);

            try {
                urlfoto = jsonObject.getJSONObject("imagen").getString("urlFoto");
            } catch (Exception e) {
                urlfoto = "";
                Log.e(TAG, "onCreate: Error: " + e.getMessage());
            }

        } else {
            this.setTitle(getApplicationContext().getResources().getString(R.string.editar_pictogram));
            jsonObject = json.getPictoFromId2(PictoID);
            if (jsonObject != null) {
                draw = json.getIcono(jsonObject);
                tipo = JSONutils.getTipo(jsonObject);

            }
        }


        //Binding
        Picto = findViewById(R.id.ElPicto);
        Picto.setOnClickListener(this);
        Picto.setCustom_Img(draw);
        Picto.setCustom_Texto(texto);
        Picto.setCustom_Color(color);
        cargarColor(tipo);


        etOutLoud = findViewById(R.id.TextViewTextOutLoud);
        etOutLoud.setOnClickListener(this);
        etOutLoud.setHint(getApplicationContext().getResources().getString(R.string.EscribaAqui));
        textViewTexto = findViewById(R.id.TextOutloud);
        textViewTexto.setText(getApplicationContext().getResources().getString(R.string.TextOutloud));


        etOutLoud.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                etOutLoud.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etOutLoud.getWindowToken(), 0);
                        }
                        return false;
                    }
                });

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    Log.d(TAG, "onTextChanged: " + s.toString());
                    Picto.setCustom_Texto(s.toString());
                }
            }
        });
        etOutLoud.setText(texto);


        Negro = findViewById(R.id.Negro);
        Negro.setOnClickListener(this);
        Amarillo = findViewById(R.id.Amarillo);
        Amarillo.setOnClickListener(this);
        Azul = findViewById(R.id.Azul);
        Azul.setOnClickListener(this);
        Verde = findViewById(R.id.Verde);
        Verde.setOnClickListener(this);
        Naranja = findViewById(R.id.Naranja);
        Naranja.setOnClickListener(this);
        Magenta = findViewById(R.id.Magenta);
        Magenta.setOnClickListener(this);
        btnHablar = findViewById(R.id.IconoOTTAA);
        btnHablar.setOnClickListener(this);

        btnEditText = findViewById(R.id.btnTexto);
        btnEditText.setOnClickListener(this);
        btnEditFrame = findViewById(R.id.btnFrame);
        btnEditFrame.setOnClickListener(this);
        btnEditTAGs = findViewById(R.id.btnTAG);
        btnEditTAGs.setOnClickListener(this);


        btnTagHora = findViewById(R.id.btnTagHora);
        btnTagHora.setOnClickListener(this);
        btnTagUbicacion = findViewById(R.id.btnTagUbicacion);
        btnTagUbicacion.setOnClickListener(this);
        btnTagCalendario = findViewById(R.id.btnTagCalendario);
        btnTagCalendario.setOnClickListener(this);
        btnTagEdad = findViewById(R.id.btnTagEdad);
        btnTagEdad.setOnClickListener(this);

        cardViewFrame = findViewById(R.id.cardViewFrame);
        cardViewTexto = findViewById(R.id.cardViewTexto);
        cardViewTAGs = findViewById(R.id.cardViewChooseTAGS);
        if (sharedPrefsDefault.getInt(Constants.PREMIUM, 1) != 1) {

            btnTagUbicacion.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_off_black_24dp));

        }


        if (savedInstanceState != null) {
            iniciar = true;
        }

        if (esGrupo) {
            btnEditFrame.setVisibility(View.GONE);
            textViewBtnFrame.setVisibility(View.GONE);
        }


        if (ConnectionDetector.isNetworkAvailable(this)) {

            bajarJsonFirebase.setInterfaz(this);
            dialogs.setTitle(getApplicationContext().getString(R.string.edit_sync));
            dialogs.setCancelable(false);
            dialogs.setMessage(getApplicationContext().getString(R.string.edit_sync_pict));
            dialogs.mostrarDialogo();
            bajarJsonFirebase.descargarGruposyPictosNuevos();

        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent mainIntent = new Intent().setClass(
                            Edit_Picto_Visual.this, LoginActivity2.class);
                    startActivity(mainIntent);
                    finish();

                }

            }
        };
        analyticsFirebase=new AnalyticsFirebase(this);

    }

    @Override
    public void onDescargaCompleta(int termino) {
        mCheckDescarga += termino;
        Log.d(TAG, "onDescargaCompleta: " + mCheckDescarga);
        if (mCheckDescarga == Constants.TODO_DESCARGADO)
            dialogs.destruirDialogo();

    }

    @Override
    public void onDatosEncontrados(int datosEncontrados) {

    }

    @Override
    public void onFotoDescargada(int fotoDescarga) {

    }

    @Override
    public void onArchivosSubidos(boolean subidos) {

    }

    @Override
    public void onPictosSugeridosBajados(boolean descargado) {

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_editar_picto, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
                return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap imageBitmap;
        if (requestCode == IntentCode.CAMARA.getCode() && resultCode == RESULT_OK) {
            imageBitmap = null;
            //por cambios en la version 24 con respecto a las versiones anteriores
            if (Build.VERSION.SDK_INT < 24) {
                final File file = getTempFile(this);
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    selectedImageUri = Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                imageBitmap = (Bitmap) data.getExtras().get("data");

            }
            //paso el parametro del bitmap
            if (imageBitmap != null) {

                Drawable d = new BitmapDrawable(getResources(), imageBitmap);
                Picto.setCustom_Img(d);
                selectedImageUri = storeImage(imageBitmap, "Fotos");
                storeOffline(imageBitmap);
                formato = ".jpg";
            }
        }
        if (requestCode == IntentCode.GALERIA.getCode() && resultCode == RESULT_OK) {
            imageBitmap = null;
            selectedImageUri = data.getData();
            imageBitmap = decodeUri(selectedImageUri);
            storeImage(imageBitmap, "Fotos");
            storeOffline(imageBitmap);
            Drawable d = new BitmapDrawable(getResources(), imageBitmap);
            Picto.setCustom_Img(d);
            formato = ".jpg";

            //Agregar imagen al picto custom
        }

        if (requestCode == IntentCode.ARASAAC.getCode()) {
            imageBitmap = null;
            if (data != null) {
                String path = data.getStringExtra("Path");
                tipo = data.getIntExtra("Type", 0);
                String name = data.getStringExtra("Text");

                if ((path == null) || (color == 0) || (name == null)) {
                    return;
                }

                Picto.setCustom_Img(AbrirBitmap(path));
                mCurrentPhotoPath = path;
                File f = new File(mCurrentPhotoPath);
                selectedImageUri = Uri.fromFile(f);
                mNombreFirebase = f.getName();

                etOutLoud.setText(name);
                Picto.setCustom_Texto(name);

                cargarColor(tipo);
                textoPicto = name;
                formato = ".png";
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void storeOffline(final Bitmap bitmap) {
        DatabaseReference connectedRef = firebaseUtils.checkFirebaseNetworkConnection();
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    String destinationPathCheckFirstTime = Environment.getExternalStorageDirectory().toString();
                    final File myDirOfflinePhotos = new File(destinationPathCheckFirstTime + "/GcHgMf/offlineFotos");
                    if (!myDirOfflinePhotos.exists()) {
                        myDirOfflinePhotos.mkdirs();
                    }
                    storeImage(bitmap, "offlineFotos");

                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }


    private void cargarFotosFirebase(Uri imageUri, final String textoPicto) {

        if (mNombreFirebase.equals(""))
            mNombreFirebase = "MI_" + mTimeStamp + formato;

        final String mImageName = mNombreFirebase;


        final StorageReference referenciaFotos = mStorageRef.child("Archivos_Usuarios").child("Fotos").child(uid).child(mImageName);

        referenciaFotos.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot,
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
                    pushKey = mDatabase.push().getKey();
                    Map<String, Object> fotosUsuario = new HashMap<>();
                    fotosUsuario.put("nombre_foto", mImageName);
                    fotosUsuario.put("texto_picto", textoPicto);
                    fotosUsuario.put("url_foto", urlFotoUpload);
                    urlfoto = urlFotoUpload;
                    mDatabase.child(Constants.FOTOSUSUARIO).child(mAuth.getCurrentUser().getUid()).child(pushKey).setValue(pushKey);
                    mDatabase.child(Constants.FOTOS).child(pushKey).updateChildren(fotosUsuario);
                    // pd.dismiss();
                    aceptar();

                } else {
                    Log.e(TAG, "onComplete: Error" + task.getException());
                }

            }

        });

    }

    /**
     * metodo encargado de pintar el fondo del picto
     *
     * @param color esta variable se encarga de pintar el fondo segun el codigo de FITZGERALD
     **/

    private void cargarColor(int color) {
        switch (color) {
            case 1:
                Picto.setCustom_Color(getResources().getColor(R.color.Yellow));
                break;
            case 2:
                Picto.setCustom_Color(getResources().getColor(R.color.Orange));
                break;
            case 3:
                Picto.setCustom_Color(getResources().getColor(R.color.YellowGreen));
                break;
            case 4:
                Picto.setCustom_Color(getResources().getColor(R.color.DodgerBlue));
                break;
            case 5:
                Picto.setCustom_Color(getResources().getColor(R.color.Magenta));
                break;
            case 6:
                Picto.setCustom_Color(getResources().getColor(R.color.Black));
                break;
            default:
                Picto.setCustom_Color(getResources().getColor(R.color.Black));
                break;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////


    public void showCustomAlert(CharSequence Testo) {
        //Retrieve the layout inflator
        LayoutInflater inflater = getLayoutInflater();
        //Assign the custom layout to view
        //Parameter 1 - Custom layout XML
        //Parameter 2 - Custom layout ID present in linearlayout tag of XML
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));
        TextView tv = layout.findViewById(R.id.text);
        tv.setTextSize(sharedPrefsDefault.getInt("subtitulo_tamanio", 25));
        tv.setText(Testo);
        //Return the application mContext
        Toast toast = new Toast(getApplicationContext());
        //Set custom_toast gravity to bottom
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        //Set custom_toast duration
        toast.setDuration(Toast.LENGTH_SHORT);
        //Set the custom layout to Toast
        toast.setView(layout);
        //Display custom_toast
        toast.show();

    }

    //------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Amarillo:
                Picto.setCustom_Color(getResources().getColor(R.color.Yellow));
                tipo = 1;
                break;
            case R.id.Azul:
                Picto.setCustom_Color(getResources().getColor(R.color.DodgerBlue));
                tipo = 4;
                break;
            case R.id.Magenta:
                Picto.setCustom_Color(getResources().getColor(R.color.Magenta));
                tipo = 5;
                break;
            case R.id.Naranja:
                Picto.setCustom_Color(getResources().getColor(R.color.Orange));
                tipo = 2;
                break;
            case R.id.Negro:
                Picto.setCustom_Color(getResources().getColor(R.color.Black));
                tipo = 6;
                break;
            case R.id.Verde:
                Picto.setCustom_Color(getResources().getColor(R.color.YellowGreen));
                tipo = 3;
                break;
            case R.id.IconoOTTAA:

                myTTS.hablar(etOutLoud.getText().toString());

                break;
            case R.id.ElPicto:

                //Pido los permisos para escribir en memoria externa
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat
                        .checkSelfPermission(getApplicationContext(), Manifest
                                .permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
                        (getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                //Ver a donde hay q pasarlo, posiblemente Galeria Pictos
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, Constants.EXTERNAL_STORAGE);
                    }
                } else
                    SelectorFuente();
                break;
            case R.id.btnTexto:

                if (cardViewTexto.getVisibility() == View.INVISIBLE) {
                    cardViewTexto.setVisibility(View.VISIBLE);
                    cardViewTAGs.setVisibility(View.INVISIBLE);
                    cardViewFrame.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.btnFrame:

                if (cardViewFrame.getVisibility() == View.INVISIBLE) {
                    cardViewFrame.setVisibility(View.VISIBLE);
                    cardViewTAGs.setVisibility(View.INVISIBLE);
                    cardViewTexto.setVisibility(View.INVISIBLE);
                    analyticsFirebase.customEvents("Touch","Editar Pictos","Select Pictograms Category");


                }


                break;
            case R.id.btnTAG:

                if (cardViewTAGs.getVisibility() == View.INVISIBLE) {
                    cardViewTAGs.setVisibility(View.VISIBLE);
                    cardViewTexto.setVisibility(View.INVISIBLE);
                    cardViewFrame.setVisibility(View.INVISIBLE);
                }

                break;

            case R.id.TextViewTextOutLoud:
                analyticsFirebase.customEvents("Touch","Edit Pictogram","Say Pictogram Name");

                break;

            case R.id.btnTagHora:
                if (chequearDatosBeforeTags()) {
                    new NewDialogsOTTAA(this).showDialogTags(Constants.HORA, jsonObject, esGrupo);
                    analyticsFirebase.customEvents("Pictogram","Edit Pictogram","Hour Tag");
                }

                break;

            case R.id.btnTagUbicacion:
                if (sharedPrefsDefault.getInt(Constants.PREMIUM, 1) == 1) {
                    new NewDialogsOTTAA(this).showDialogTags(Constants.UBICACION, jsonObject, esGrupo);
                    analyticsFirebase.customEvents("Pictogram","Edit Pictogram","Location Tag");

                } else {
                    Intent i = new Intent(this, LicenciaExpirada.class);
                    startActivity(i);
                }



                break;

            case R.id.btnTagCalendario:
                new NewDialogsOTTAA(this).showDialogTags(Constants.SEXO, jsonObject, esGrupo);
                analyticsFirebase.customEvents("Pictogram","Edit Pictogram","Gender Tag");
                break;

            case R.id.btnTagEdad:
                if (chequearDatosBeforeTags()) {
                    new NewDialogsOTTAA(this).showDialogTags(Constants.EDAD, jsonObject, esGrupo);
                    analyticsFirebase.customEvents("Pictogram","Edit Pictogram","Age Tag");
                }


                break;
        }
    }


    private boolean chequearDatosBeforeTags() {

        if (esNuevo) {
            if (esGrupo) {

                myTTS.mostrarAlerta(getString(R.string.alert_guardar_grupo));
                return false;
            } else {
                myTTS.mostrarAlerta(getString(R.string.alert_guardar_picto));
                return false;
            }

        } else {
            if (esGrupo) {
                try {
                    if (JSONutils.getHijosGrupo2(json.getmJSONArrayTodosLosPictos(),jsonObject).length() > 0)
                        return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myTTS.mostrarAlerta(getString(R.string.alert_anadir_pictos));
            } else {
                return true;
            }
        }

        return false;

    }

    private File getTempFile(Context context) {
        //it will return /sdcard/image.tmp
        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    private void SelectorFuente() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "Imagen Editada");
        analyticsFirebase.customEvents("Touch","Edit Pictogram","Select Custom Picto Image");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_imagen);
        dialog.getWindow().setBackgroundDrawable(this.getDrawable(R.color.colorTransparent));
        dialog.show();

        TextView camera, galery, araasac,titulo;
        titulo=dialog.getWindow().findViewById(R.id.titulo);
        camera = dialog.getWindow().findViewById(R.id.textAudio);
        galery = dialog.getWindow().findViewById(R.id.textImagen);
        araasac = dialog.getWindow().findViewById(R.id.textArasaac);

        titulo.setText(getApplicationContext().getResources().getString(R.string.pref_elija));
        camera.setText(getApplicationContext().getResources().getString(R.string.pref_select_camara));
        galery.setText(getApplicationContext().getResources().getString(R.string.pref_select_galeria));
        araasac.setText(getApplicationContext().getResources().getString(R.string.pref_select_pictos));


        ImageButton BtnCamera = dialog.getWindow().findViewById(R.id.Camara);
        BtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log de pictoeditado y como
                if (texto != null) {
//                    Answers.getInstance().logCustom(new CustomEvent("Picto Editado")
//                            .putCustomAttribute("Picto", texto)
//                            .putCustomAttribute("Origen Imagen", "Camara"));
                }

                if (Build.VERSION.SDK_INT < 24)//de esta forma se evita borrar lo de la version anterior
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getApplicationContext())));
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        dialog.dismiss();
                        startActivityForResult(takePictureIntent, IntentCode.CAMARA.getCode());
                    }
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        startActivityForResult(takePictureIntent, IntentCode.CAMARA.getCode());
                        dialog.dismiss();

                    }
                }
            }


        });
        ImageButton BtnGaleria = dialog.getWindow().findViewById(R.id.Galeria);
        BtnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log de pictoeditado y como
                //   Log.e("Answers", "EditGaleria: "+ texto);
                if (texto != null) {
//                    Answers.getInstance().logCustom(new CustomEvent("Picto Editado")
//                            .putCustomAttribute("Picto", texto)
//                            .putCustomAttribute("Origen Imagen", "Galeria"));
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                dialog.dismiss();
                startActivityForResult(Intent.createChooser(intent, "Complete la accion usando: "), IntentCode.GALERIA.getCode());
            }
        });
        ImageButton BtnArasaac = dialog.getWindow().findViewById(R.id.Arasaac);
        BtnArasaac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log de pictoeditado y como
                //   Log.e("Answers", "EditGaleria: "+ texto);
                if (texto != null) {
//                    Answers.getInstance().logCustom(new CustomEvent("Picto Editado")
//                            .putCustomAttribute("Picto", texto)
//                            .putCustomAttribute("Origen Imagen", "ARASAAC"));
                }
                Intent intent = new Intent(Edit_Picto_Visual.this, GaleriaArasaac.class);
                startActivityForResult(intent, IntentCode.ARASAAC.getCode());
                dialog.dismiss();
                //CustomToast("Estamos implementando esta funci&oacuten, pronto estar&aacute disponible");
            }
        });
    }


    @Override
    public void onBackPressed() {
        Alert();
    }

    public void Alert() {
        Yes_no_otheroptionDialog dialogo1=new Yes_no_otheroptionDialog(this);
        dialogo1.setTitle(getString(R.string.pref_important_alert)).setMessage(getString(R.string.pref_text1_alert));
        dialogo1.setCancelable(true);
        dialogo1.setOnClick(dialogo1.getObject(R.id.yes_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.setTitle(getString(R.string.edit_agregarImagen)).setMessage(getString(R.string.edit_wait_sync));
                dialogs.setCancelable(false);
                dialogs.mostrarDialogo();
                if (Picto.getCustom_Texto() != null && !etOutLoud.getText().toString().trim().equalsIgnoreCase("") && Picto.getCustom_Imagen() != null) {

                    if (selectedImageUri != null) {
                        if (ConnectionDetector.isNetworkAvailable(Edit_Picto_Visual.this)) {
                            cargarFotosFirebase(selectedImageUri, textoPicto);


                        } else {
                            aceptar();
                            dialogs.destruirDialogo();
                        }

                    } else {
                        aceptar();
                        dialogs.destruirDialogo();

                    }

                } else {
                    dialogs.destruirDialogo();
                    Toast.makeText(Edit_Picto_Visual.this, getString(R.string.ingrese_nombre), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogo1.setOnClick(dialogo1.changeIcon((Custom_button) dialogo1.getObject(R.id.unknow_Button),R.drawable.ic_replay_black_24dp).getObject(R.id.unknow_Button) ,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo1.cancelarDialogo();
            }
        });
        dialogo1.setOnClick(dialogo1.getObject(R.id.no_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });
        dialogo1.mostrarDialogo();


    }

    /**
     * Este metodo se encarga de aceptar los cambios que se realicen en el pictograma, primero  se encarga de traducirTexto
     *
     * @params textoPicto texto encargado de tomar el nombre del pictograma
     * @params traducirTexto clase encargada de traducirTexto el texto
     **/

    public void aceptar() {


        textoPicto = Picto.getCustom_Texto();
        traducirTexto = new traducirTexto(getApplicationContext());
        traducirTexto.traducirIdioma(this, Picto.getCustom_Texto(), sharedPrefsDefault.getString(getString(R.string.str_idioma), "en"), "en", ConnectionDetector.isNetworkAvailable(this));
        if (ConnectionDetector.isNetworkAvailable(Edit_Picto_Visual.this)) {
            // pd.setTitle(getString(R.string.translating_languaje));
           dialogs.setCancelable(false);
            dialogs.setMessage(getString(R.string.translating_languaje) + textoPicto);
            //pd.show();

        }


    }

    public void cancelar() {
        this.finish();
    }

    public Drawable AbrirBitmap(String Path) {
        Drawable d = getResources().getDrawable(R.drawable.ic_agregar_nuevo);
        if (Path != "nada") {
            Bitmap bitmap = BitmapFactory.decodeFile(Path);
            d = Drawable.createFromPath(Path);
        }
        return d;
    }

    private Uri storeImage(Bitmap image, String storeInto) {
        File pictureFile = getOutputMediaFile();
        File backupPictureFile = getOutputMediaBackupFile(storeInto);
        if (pictureFile == null && backupPictureFile == null) {
            Log.e(TAG, "storeImage: Error creating media file, check permision");
            return Uri.EMPTY;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image = Bitmap.createScaledBitmap(image, 500, 500, false);
            image.compress(Bitmap.CompressFormat.WEBP, 100, fos);
            fos.close();
            if (backupPictureFile != null) {
                FileOutputStream fosBackup = new FileOutputStream(backupPictureFile);
                image = Bitmap.createScaledBitmap(image, 500, 500, false);
                image.compress(Bitmap.CompressFormat.WEBP, 100, fosBackup);
                fosBackup.close();
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG, "storeImage: Error: " + "File not found: " + e.getMessage());

        } catch (IOException e) {
            Log.e(TAG, "storeImage: Error: " + "Error accessing file: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "storeImage: Error: " + "Error pictogram is not created: " + e.getMessage());
        }
        return Uri.fromFile(pictureFile);
    }


    private File getOutputMediaBackupFile(String path) {
        File mediaStorageBackupDir = new File(Environment.getExternalStorageDirectory()
                + "/GcHgMf/" + path);

        if (!mediaStorageBackupDir.exists()) {
            if (!mediaStorageBackupDir.mkdirs()) {
                return null;
            }
        }
        File mediaFileBackup;
        String mImageName = "MI_" + mTimeStamp + ".jpg";
        mediaFileBackup = new File(mediaStorageBackupDir.getPath() + File.separator + mImageName);

        return mediaFileBackup;

    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new UriFiles(getApplicationContext()).dir();

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile, mediaFileBackup;
        String mImageName = "MI_" + mTimeStamp + ".jpg";

        mCurrentPhotoPath = (mediaStorageDir.getPath() + File.separator + mImageName);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);


        return mediaFile;
    }


    public Bitmap decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        Bitmap bitmap = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 500;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);


        } catch (Exception e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {

                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
        return bitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.EXTERNAL_STORAGE) {

            if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectorFuente();
                } else {
                    //permission granted
                    showCustomAlert(getString(R.string.edit_permisos_archivos));
                }
/*
            if (permission == PackageManager.PERMISSION_GRANTED) {
            } else if (permission == PackageManager.PERMISSION_DENIED) {
                //this.finish();
            }
*/

        }


    }

    @Override
    protected void onDestroy() {

        dialogs.destruirDialogo();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //esto soluciona el error que salta en el nokia 8 por que si no elimina la vista
        super.onSaveInstanceState(outState, outPersistentState);
    }


    /**
     * Interfaz encargada de notificar cuando se ternino de traducir el texto
     *
     * @params traduccion variable booleana que se encarga de notificar si se logro traducir el texto o no
     **/
    @Override
    public void onTextoTraducido(boolean traduccion) {
        //TODO aca ya tenes el texto traducido y hacer el dismis del dialogo q dice q estan traduciendo el texto.
        if (traduccion) {
            dialogs.destruirDialogo();
            aceptarCambios();
            //ver si poner lo del aceptar aca
        }


    }

    /**
     * metodo encargado de aceptar,agregar  los cambios realizados dentro del picto o el grupo
     *
     * @params databack variable que se encarga de volver hacia el galeria grupos o galeria pictos
     * @params picto variable que toma el id del picto a modificar
     * @params mArrayListAGuardar arraylist que va a guardar la relacion entre pictos y grupos
     */
    private void aceptarCambios() {
        Intent databack = new Intent();//
        int picto = PictoID;//
        JSONArray mArrayListAGuardar = new JSONArray();//

        if (textoPicto.length() > 0) {
            if (esNuevo) {
                if ((mCurrentPhotoPath == null) || (mCurrentPhotoPath.length() == 0)) {
                    cancelar();
                    return;
                }
                if (esGrupo) {
                    analyticsFirebase.customEvents("Pictogram", "Editar Grupos", "Add Group");
                    try {
                        mArrayListAGuardar = json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
                    } catch (JSONException | FiveMbException e) {
                        e.printStackTrace();
                    }
                    //aca tiene que ir la interfaz de traduccion

                    try {
                        mArrayListAGuardar = JSONutils.crearGrupo(mArrayListAGuardar, ConfigurarIdioma.getLanguaje(), Picto.getCustom_Texto(), traducirTexto.getTexto(), mCurrentPhotoPath, tipo, urlfoto, pushKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    json.setmJSONArrayTodosLosGrupos(mArrayListAGuardar);
                    json.guardarJson(Constants.ARCHIVO_GRUPOS);
                    databack.putExtra("esNuevo", true);
                } else {
                    analyticsFirebase.customEvents("Pictogram", "Editar Grupos", "Add Pictogram");
                    JSONArray mArrayListGrupos = null;
                    try {
                        mArrayListGrupos = json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS);
                    } catch (JSONException | FiveMbException e) {
                        e.printStackTrace();
                    }
                    try {
                        mArrayListAGuardar = json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS);
                    } catch (JSONException | FiveMbException e) {
                        e.printStackTrace();
                    } //                        WeeklyBackup wb = new WeeklyBackup(mContext);
                    //                        wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);

                    mArrayListAGuardar = JSONutils.crearPicto(mArrayListGrupos, mArrayListAGuardar, ConfigurarIdioma.getLanguaje(), padre, Picto.getCustom_Texto(), traducirTexto.getTexto(), mCurrentPhotoPath, tipo, urlfoto, pushKey);
                    databack.putExtra("esNuevo", true);
                    json.setmJSONArrayTodosLosPictos(mArrayListAGuardar);
                    json.guardarJson(Constants.ARCHIVO_PICTOS);
                    json.setmJSONArrayTodosLosGrupos(mArrayListGrupos);
                    json.guardarJson(Constants.ARCHIVO_GRUPOS);

                }
                uploadFile.subirGruposFirebase(uploadFile.getmDatabase(mAuth, Constants.Grupos), uploadFile.getmStorageRef(mAuth, Constants.Grupos));
                uploadFile.subirPictosFirebase(uploadFile.getmDatabase(mAuth, Constants.PICTOS), uploadFile.getmStorageRef(mAuth, Constants.PICTOS));
                setResult(IntentCode.EDITARPICTO.getCode(), databack);
                finish();
            } else {

                JSONutils.setNombre(jsonObject, Picto.getCustom_Texto(), traducirTexto.getTexto(), traducirTexto.getmSource(), traducirTexto.getmTarget());
                JSONutils.setTipo(jsonObject, tipo);
                if (mCurrentPhotoPath != null) {
                    JSONutils.setImagen(jsonObject, mCurrentPhotoPath, urlfoto, pushKey);
                }
                if (esGrupo) {
                    analyticsFirebase.customEvents("Pictogram", "Editar Grupos", "Edit Group");
                    try {
                        /** actualizo los grupos agregando 1
                         *  @autor gonzalo
                         * @since 4/1/2019
                         */
                        json.setmJSONArrayTodosLosGrupos(JSONutils.setJsonEditado2(json.readJSONArrayFromFile(Constants.ARCHIVO_GRUPOS), jsonObject));
                        json.guardarJson(Constants.ARCHIVO_GRUPOS);
                    } catch (JSONException | FiveMbException e) {
                        e.printStackTrace();
                    }
                    uploadFile.subirGruposFirebase(uploadFile.getmDatabase(mAuth, Constants.Grupos), uploadFile.getmStorageRef(mAuth, Constants.Grupos));
                    databack.putExtra("ID", picto);
                    databack.putExtra("esNuevo", false);
                    setResult(IntentCode.GALERIA_GRUPOS.getCode(), databack);
                    finish();
                } else {
                    analyticsFirebase.customEvents("Pictogram", "Editar Grupos", "Edit Pictogram");
                    try {
                        /** actualizo los pictos agregando 1
                         *  @autor gonzalo
                         * @since 4/1/2019
                         */
                        json.setmJSONArrayTodosLosPictos(JSONutils.setJsonEditado2(json.readJSONArrayFromFile(Constants.ARCHIVO_PICTOS), jsonObject));
                        json.guardarJson(Constants.ARCHIVO_PICTOS);
                    } catch (JSONException | FiveMbException e) {
                        e.printStackTrace();
                    }
                    uploadFile.subirPictosFirebase(uploadFile.getmDatabase(mAuth, Constants.PICTOS), uploadFile.getmStorageRef(mAuth, Constants.PICTOS));
                    databack.putExtra("ID", picto);
                    databack.putExtra("esNuevo", false);
                    setResult(IntentCode.EDITARPICTO.getCode(), databack);
                    finish();
                }

            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Json.getInstance().setmContext(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        new ConfigurarIdioma(getApplicationContext(), preferences.getString(getApplicationContext().getString(R.string.str_idioma), Locale.getDefault().getLanguage()));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String locale = preferences.getString(newBase.getString(R.string.str_idioma), Locale.getDefault().getLanguage());
        new ConfigurarIdioma(newBase, locale);
        super.attachBaseContext(myContextWrapper.wrap(newBase, locale));

    }


    //Interfaz que escucha cuando se asignan los tags en AsignarTags,
    @Override
    public void onTagAsignado(String jsonTagsAsignados) {


        //Ya son iguales en algun lado las referencias lo asignan y modifican
        if (!jsonTagsAsignados.isEmpty() && jsonTagsAsignados != null) {
            try {
                if (esGrupo) {
                    Log.d(TAG, "onTagAsignado: Grupo");
                    jsonObject = new JSONObject(jsonTagsAsignados);
                } else {
                    jsonObject = new JSONObject(jsonTagsAsignados);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onTagAsignado: Error: Picto not found" + e.getMessage());
            }
        }

    }

    @Override
    public void refrescarJsonTags(JSONObject jsonTags) {

        jsonObject = jsonTags;
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }

}



