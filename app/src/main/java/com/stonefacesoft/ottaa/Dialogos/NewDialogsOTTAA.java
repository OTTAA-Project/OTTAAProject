package com.stonefacesoft.ottaa.Dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stonefacesoft.ottaa.Adapters.CustomFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.Adapters.FrasesFavoritasAdapter;
import com.stonefacesoft.ottaa.Adapters.SelectFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.AsignTags;
import com.stonefacesoft.ottaa.Bitmap.GestionarBitmap;
import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.Interfaces.FirebaseSuccessListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.VincularFrases;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewDialogsOTTAA implements FirebaseSuccessListener {

    private Activity mActivity;
    private GestionarBitmap mGestionarBitmap;
    private DatosDeUso mDatosDeUso;
    private Dialog dialog;
    private RecyclerView mRecyclerViewFrases, mRecyclerViewTags;
    private ArrayList<FavModel> mArrayListFavoritos = new ArrayList<>();
    private TextView textViewNoData;
    private ProgressBar progressBar;
    private AsignTags asignTags;
    private BajarJsonFirebase bajarJsonFirebase;
    private int position;
    private ReturnPositionItem positionItemAdapter;


    public NewDialogsOTTAA(Activity activity) {
        this.mActivity = activity;
        this.dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

    public void initCustomFavoritePhrase(boolean isBarrido){
        SharedPreferences defaultSharedPreferences=PreferenceManager.getDefaultSharedPreferences(mActivity);
        int option=defaultSharedPreferences.getInt("favoritePhrase",0);
        switch (option){
            case 0:
                showHeartDialog();
                break;
            case 1:
                showCustomPhrases();
                break;
        }
    }

    //Mostramos las frases mas usadas por el usuario, las primeras 5
    // uses them to show the five favorites phrases from the user
    public void showHeartDialog() {
        //usamos esto para inicializar los dialogos y no repetir codigo en todos los metodos
        // use them to start the dialogs

        initDialog(R.layout.dialog_most_used_frases, true);
        initProgress();
        position=0;

        mRecyclerViewFrases = dialog.findViewById(R.id.recyclerViewFrases);
        setLinearLayoutManager(mRecyclerViewFrases);
        new cargarFavoritos().execute();
        ImageButton foward,next,sort;
        sort=dialog.findViewById(R.id.actionButton);
        foward=dialog.findViewById(R.id.frase_anterior);
        next=dialog.findViewById(R.id.frase_siguiente);
        TextView title=dialog.findViewById(R.id.textTiulo);
        title.setText(mActivity.getResources().getText(R.string.frases_musadas));
        sort.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_sort_phrases));
        foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.subtract();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.add();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences defaultSharedPreferences=PreferenceManager.getDefaultSharedPreferences(mActivity);
                defaultSharedPreferences.edit().putInt("favoritePhrase",1).apply();
                showCustomPhrases();
            }
        });


    }

    public void showCustomPhrases(){
        initDialog(R.layout.dialog_most_used_frases,true);
        initProgress();
        position=0;
        mRecyclerViewFrases = dialog.findViewById(R.id.recyclerViewFrases);
        setLinearLayoutManager(mRecyclerViewFrases);
        new cargarFrasesSeleccionadas().execute();
        ImageButton foward,next,sort,addPhrases;
        addPhrases=dialog.findViewById(R.id.addButton);
        addPhrases.setVisibility(View.VISIBLE);
        sort=dialog.findViewById(R.id.actionButton);
        TextView title=dialog.findViewById(R.id.textTiulo);
        title.setText(mActivity.getResources().getText(R.string.favorite_phrases));
        sort.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        foward=dialog.findViewById(R.id.frase_anterior);
        next=dialog.findViewById(R.id.frase_siguiente);
        foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.subtract();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.add();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences defaultSharedPreferences=PreferenceManager.getDefaultSharedPreferences(mActivity);
                defaultSharedPreferences.edit().putInt("favoritePhrase",0).apply();
                showHeartDialog();
            }
        });
        addPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, VincularFrases.class);
                mActivity.startActivityForResult(intent, IntentCode.CUSTOMPHRASES.getCode());
                dialog.dismiss();
            }
        });
    }

    public void showAllPhrases(){

        initDialog(R.layout.dialog_most_used_frases,true);
        initProgress();
        position=0;
        mRecyclerViewFrases = dialog.findViewById(R.id.recyclerViewFrases);
        setLinearLayoutManager(mRecyclerViewFrases);
        new cargarTodasLasFrases().execute();
        ImageButton foward,next,sort;

        sort=dialog.findViewById(R.id.actionButton);
        sort.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        foward=dialog.findViewById(R.id.frase_anterior);
        next=dialog.findViewById(R.id.frase_siguiente);
        foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.subtract();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=positionItemAdapter.add();
                mRecyclerViewFrases.smoothScrollToPosition(position);

            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeartDialog();
            }
        });
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
                mDatosDeUso = new DatosDeUso(mActivity);
                List frases = mDatosDeUso.getArrayListFrasesMasUsadas(4);
                for (int i = 0; i < frases.size(); i++) {
                    FavModel model = new FavModel();
                    if (mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i)) != null) {

                        model.setImagen(mGestionarBitmap.getBitmapDeFrase(mDatosDeUso.getFrasesOrdenadas().get(i)));
                        try {
                            model.setTexto(mDatosDeUso.getFrasesOrdenadas().get(i).getString("frase"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mArrayListFavoritos.add(model);
                    }
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
            FrasesFavoritasAdapter mAdapter = null;
            try {
                mAdapter = new FrasesFavoritasAdapter(R.layout.item_favoritos_row, mActivity, mArrayListFavoritos, dialog);
            } catch (FiveMbException e) {
                e.printStackTrace();
                //TODO poner algo aca
            }

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







}
