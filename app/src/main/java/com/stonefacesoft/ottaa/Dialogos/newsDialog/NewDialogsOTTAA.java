package com.stonefacesoft.ottaa.Dialogos.newsDialog;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Adapters.CustomFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.Adapters.MostUsedFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.Adapters.SelectFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.AsignTags;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.Interfaces.ProgressBarListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.CloudFunctionHTTPRequest;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewDialogsOTTAA implements FirebaseSuccessListener {

    //TODO hacer esta clase abstracta e implementar cada dialogo NewFeature, hearth, ETC... APLICAR DISENO ORIENTADO A OBJETOS
    private static final String TAG = "NewDialogs";

    private final Activity mActivity;
    private GestionarBitmap mGestionarBitmap;
    private final Dialog dialog;
    private RecyclerView mRecyclerViewFrases, mRecyclerViewTags;
    private final ArrayList<FavModel> mArrayListFavoritos = new ArrayList<>();
    private TextView textViewNoData;
    private ProgressBar progressBar;
    private AsignTags asignTags;
    private BajarJsonFirebase bajarJsonFirebase;
    private int position;
    private ReturnPositionItem positionItemAdapter;

    private final AnalyticsFirebase analyticsFirebase;


    public NewDialogsOTTAA(Activity activity) {
        this.mActivity = activity;
        this.dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        analyticsFirebase = new AnalyticsFirebase(mActivity);

    }

    private void initDialog(int layoutResource, boolean isCancelable) {
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(layoutResource);
    }




    private void initProgress() {
        progressBar = dialog.findViewById(R.id.progressBar);
        textViewNoData = dialog.findViewById(R.id.no_data);
    }


    public void showBotonTextoDialog(String textoMensaje, String botonTexto, boolean dismissDialogOnButtonClick, boolean isCancelable) {
        // initDialog();

    }


    //Mostramos las sugerencias cuando es la primera vez de uso o se actualiza a >= 54
    public void showSugerenciasDialog(String msg) {

        //usamos esto para inicializar los dialogos y no repetir codigo en todos los metodos
        initDialog(R.layout.dialog_new_features, false);

        TextView textNews = dialog.findViewById(R.id.text_descripcion);
        textNews.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    //Mostramos las sugerencias cuando es la primera vez de uso o se actualiza a >= 54
    public void showCancelDialog(String msg) {

        //usamos esto para inicializar los dialogos y no repetir codigo en todos los metodos
        initDialog(R.layout.dialog_new_features, false);

        TextView textNews = dialog.findViewById(R.id.text_descripcion);
        textNews.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showSettingsDialog() {
        initDialog(R.layout.custom_dialog_options_interface, true);
        TextView textDescription = dialog.findViewById(R.id.text_descripcion);
        textDescription.setText(dialog.getContext().getResources().getString(R.string.str_select_age_sex));
        TextView ageDescription = dialog.findViewById(R.id.text_name1);
        ageDescription.setText(dialog.getContext().getResources().getString(R.string.str_seleccionar_edad_usuario));
        RadioGroup ageOptions = dialog.findViewById(R.id.age_preference);
        SharedPreferences mDefaultSharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        bajarJsonFirebase = new BajarJsonFirebase(mDefaultSharedPref, FirebaseAuth.getInstance(), mActivity);
        bajarJsonFirebase.setInterfaz(this);
        ageOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonAdulto1:
                        mDefaultSharedPref.edit().putString("prefEdad", "ADULTO").apply();
                        break;
                    case R.id.radioButtonJoven1:
                        mDefaultSharedPref.edit().putString("prefEdad", "JOVEN").apply();
                        break;
                    case R.id.radioButtonNino1:
                        mDefaultSharedPref.edit().putString("prefEdad", "NINO").apply();
                        break;
                }
            }
        });

        TextView sexDescription = dialog.findViewById(R.id.text_name2);
        sexDescription.setText(dialog.getContext().getResources().getString(R.string.user_sex_gender_description));
        RadioGroup sexOptions = dialog.findViewById(R.id.sex_preference);
        sexOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonMale:
                        mDefaultSharedPref.edit().putString("prefSexo", "MASCULINO").apply();
                        break;
                    case R.id.radioFemale:
                        mDefaultSharedPref.edit().putString("prefSexo", "FEMENINO").apply();
                        break;
                }
            }
        });
        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultSharedPref.edit().putBoolean("configurarPrediccion", true).apply();
                dialog.setTitle(mActivity.getResources().getString(R.string.estamos_cargando_sus_pictos));
                StorageReference reference = FirebaseStorage.getInstance().getReference();
                String sexo = mDefaultSharedPref.getString("prefSexo", "FEMENINO");
                String edad = mDefaultSharedPref.getString("prefEdad", "JOVEN");
                StorageReference mPredictionRef = reference.child("Archivos_Sugerencias").child("pictos_" + sexo + "_" + edad + ".txt");
                bajarJsonFirebase.descargarPictosDatabase(mPredictionRef);
            }
        });
        dialog.show();

    }

    private void setLinearLayoutManager(RecyclerView recyclerView) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void setGridLayoutManager(RecyclerView recyclerView) {
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, devolverCantidadColumnas());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    public int devolverCantidadColumnas() {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = mActivity.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / 250);
//        return mActivity.getResources().getConfiguration().screenWidthDp / convertToDp(150);
    }


    //Mostramos las frases mas usadas por el usuario, las primeras 5
    public void showDialogTags(final String tipoTag, JSONObject jsonObject, boolean esGrupo) {

        //Usamos esto para inicializar los dialogos y no repetir codigo en todos los metodos
        initDialog(R.layout.dialog_tags_selector, true);
        initProgress();

        Button buttonDialog = dialog.findViewById(R.id.btn_dialogTag);
        mRecyclerViewTags = dialog.findViewById(R.id.recyclerViewTags);
        mRecyclerViewTags.hasFixedSize();
        setGridLayoutManager(mRecyclerViewTags);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignTags.asignarTags(dialog);

            }
        });

        dialog.show();
        new cargarTags(esGrupo, jsonObject, tipoTag).execute();

    }


    private class cargarTags extends AsyncTask<Void, Void, Void> {

        boolean mEsGrupo;
        String mTipoTag;
        JSONObject jsonObject;

        cargarTags(boolean esGrupo, JSONObject pictoGrupoSeleccionado, String tipoTag) {
            this.mEsGrupo = esGrupo;
            this.jsonObject = pictoGrupoSeleccionado;
            this.mTipoTag = tipoTag;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            asignTags = new AsignTags(mActivity);
            asignTags.setExtras(jsonObject, mEsGrupo);
            asignTags.cargarTags(mTipoTag);
            //GASTON que es este return null
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
            asignTags.setAdapter(mRecyclerViewTags);
            if (asignTags.getTagsAdapter().getItemCount() == 0) {
                mRecyclerViewTags.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
            } else {
                mRecyclerViewTags.setVisibility(View.VISIBLE);
                textViewNoData.setVisibility(View.GONE);
                asignTags.setAdapter(mRecyclerViewTags);
            }
        }
    }


    //AsyncTask encargado de cargar las frases favoritas y ponerlas dentro del dialogo
    private class cargarFavoritos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            mGestionarBitmap = new GestionarBitmap(mActivity);
          try {
                DatosDeUso mDatosDeUso = new DatosDeUso(mActivity);
                List frases = mDatosDeUso.getArrayListFrasesMasUsadas(4);
                for (int i = 0; i < frases.size(); i++) {
                    FavModel model = new FavModel();
                        mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i),new LoadOnlinePictograms() {
                            @Override
                            public void preparePictograms() {
                            }
                            @Override
                            public void loadPictograms(Bitmap bitmap) {
                                model.setImagen(bitmap);
                            }

                            @Override
                            public void FileIsCreated() {

                            }

                            @Override
                            public void FileIsCreated(Bitmap bitmap) {

                            }
                        });

                        try {
                            model.setTexto(mDatosDeUso.getFrasesOrdenadas().get(i).getString("frase"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mArrayListFavoritos.add(model);

                }
            } catch (FiveMbException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);
            MostUsedFavoritePhrasesAdapter mAdapter = null;

                mAdapter = new MostUsedFavoritePhrasesAdapter(mActivity, new ProgressBarListener() {
                    @Override
                    public void initProgressDialog() {

                    }

                    @Override
                    public void setMessageProgressDialog(String messageProgressDialog) {

                    }

                    @Override
                    public void setTittleProgressDialog(String tittleProgressDialog) {

                    }

                    @Override
                    public void dismisProgressBar() {

                    }
                });

            if (mAdapter.getItemCount() == 0) {
                mRecyclerViewFrases.setVisibility(View.GONE);
                textViewNoData.setVisibility(View.VISIBLE);
            } else {
                mRecyclerViewFrases.setVisibility(View.VISIBLE);
                textViewNoData.setVisibility(View.GONE);
                mRecyclerViewFrases.setAdapter(mAdapter);
            }
            positionItemAdapter=new ReturnPositionItem(mArrayListFavoritos.size());
        }
    }

    private class cargarFrasesSeleccionadas extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomFavoritePhrasesAdapter adapter=null;
            progressBar.setVisibility(View.INVISIBLE);
            adapter=new CustomFavoritePhrasesAdapter(mActivity);
            mRecyclerViewFrases.setAdapter(adapter);
            positionItemAdapter=new ReturnPositionItem(mRecyclerViewFrases.getAdapter().getItemCount());
        }
    }

    private class cargarTodasLasFrases extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SelectFavoritePhrasesAdapter  adapter=null;
            progressBar.setVisibility(View.INVISIBLE);
            adapter=new SelectFavoritePhrasesAdapter(mActivity);
            mRecyclerViewFrases.setAdapter(adapter);
            positionItemAdapter=new ReturnPositionItem(mRecyclerViewFrases.getAdapter().getItemCount());

        }
    }


    // interfaces

    @Override
    public void onDescargaCompleta(int descargaCompleta) {

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
        if (descargado)
            dialog.dismiss();
    }



    //A dialog that show the user how to do the Autoworkshop
    public void showAutoWorkshopDialog() {
        //Init the layout
        initDialog(R.layout.dialog_auto_workshop, false);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        Button emailButton = dialog.findViewById(R.id.buttonEmailLink);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerEmail();
            }

        });

        Button copyButton = dialog.findViewById(R.id.buttonCopyLink);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    copyToClipboard();
            }
        });
        dialog.show();
    }

    //A dialog that show the user options to Book a demo
    public void showBookDemoDialog() {
        //Init the layout
        initDialog(R.layout.dialog_book_demo, false);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button demoButton = dialog.findViewById(R.id.buttonBookDemo);
        demoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendly();
            }
        });

        dialog.show();
    }

    private void copyToClipboard(){
        ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", "https://forms.gle/vp6EDqxq55vEB6YC9");
        clipboard.setPrimaryClip(clip);
        CustomToast customToast = CustomToast.getInstance(mActivity);
        customToast.mostrarFrase("Copiado correctamente!"); //TODO extraer resource
    }

    private void triggerEmail(){
        CustomToast customToast = CustomToast.getInstance(mActivity);
        customToast.mostrarFrase("Email enviado"); //TODO extraer resource
       new CloudFunctionHTTPRequest(mActivity,TAG).doHTTPRequest("https://us-central1-ottaa-project.cloudfunctions.net/add2list");
    }

    private void openCalendly(  ) {
        RemoteConfigUtils remoteConfigUtils = RemoteConfigUtils.getInstance();
        remoteConfigUtils.setActivateDeactivateConfig(mActivity, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    String url = "";
                    url = remoteConfigUtils.getStringByName("capacitacion_ottaa");
                    Log.d(TAG, "onComplete: "+url);
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url));
                    if(ValidateContext.isValidContext(mActivity))
                        mActivity.startActivity(Intent.createChooser(browse,"Browse With"));
                }
            }
        });

    }



}
