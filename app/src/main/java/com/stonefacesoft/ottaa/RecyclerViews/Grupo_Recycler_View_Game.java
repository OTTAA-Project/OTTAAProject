package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaGruposAdapter;
import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.Views.PictogramsGalleryGames;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Grupo_Recycler_View_Game extends Custom_recyclerView implements View.OnClickListener{
    private GaleriaGruposAdapter mGaleriaGruposAdapter;


    public Grupo_Recycler_View_Game(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        this.mActivity = mActivity;
        initArray();
        createRecyclerLayoutManager();
        cargarGrupo();
    }



    private RecyclerItemClickListener recyclerItemClickListener(){
        return new RecyclerItemClickListener(mRecyclerView,mActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JSONObject object = null;
                if (view != null) {
                    if (position != -1) {

                        saidPhrase(position);
                        /*Llamando a la misma Activity podemos pasarle un putExtra que despues el getIntent del setDialog va a tomar
                         * de esta forma podemos pasar la posicion del boton que clicamos y el nombre*/

                        //no tiene message
                        try {
                           object = array.getJSONObject(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent2 = new Intent(mActivity, PictogramsGalleryGames.class);
                        if(object!=null) {
                            try {
                                int id = json.getId(object);
                                TellAStoryUtils.getInstance().setGame(mActivity,id);
                                intent2.putExtra("Boton", json.getPosPicto(json.getmJSONArrayTodosLosGrupos(), id));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent2.putExtra("Nombre", JSONutils.getNombre(object, sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                            CrashlyticsUtils.getInstance().getCrashlytics().setCustomKey("Grupo Usado", JSONutils.getNombre(object, sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                        }
                        mActivity.startActivityForResult(intent2, IntentCode.GALERIA_PICTOS.getCode());
                        //NOTA: hay  que tener en cuenta que cuando se hace de manera local esto funciona de una ,para la sincronizacion esto puede pisar los datos cuando sea en simultaneo
                    }

                }
            }

            @Override
            public void onDoubleTap(View view, int position) {

            }

            @Override
            public void onLongClickListener(View view, int position) {

            }
        });
    }


    public void cargarGrupo(){
        mGaleriaGruposAdapter = new GaleriaGruposAdapter(mActivity, R.layout.grid_group_layout_2, mAuth);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mGaleriaGruposAdapter);
        mGaleriaGruposAdapter.setmArrayGrupos(array);
        mRecyclerView.addOnItemTouchListener(recyclerItemClickListener());
    }

    private void initArray(){
        array= new JSONArray();
        int[] idPictos = TellAStoryUtils.getInstance().getItem().getOptions();
        for (int i = 0; i < idPictos.length; i++) {
            array.put(json.getGrupoFromId(idPictos[i]));
        }
        arrayAux = array;
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    public GaleriaGruposAdapter  getmGaleriaGruposAdapter(){
        if(mGaleriaGruposAdapter == null)
            mGaleriaGruposAdapter = new GaleriaGruposAdapter(mActivity, R.layout.grid_group_layout_2, mAuth);
        return  mGaleriaGruposAdapter;
    }


}
