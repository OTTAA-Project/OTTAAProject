package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaGruposAdapter;
import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Games.TellAStoryUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;

public class Grupo_Recycler_View_Game extends Custom_recyclerView implements View.OnClickListener{
    private GaleriaGruposAdapter mGaleriaGruposAdapter;


    public Grupo_Recycler_View_Game(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        initArray();
        createRecyclerLayoutManager();
        cargarGrupo();
    }

    public void cargarGrupo(){
        mGaleriaGruposAdapter = new GaleriaGruposAdapter(mActivity, R.layout.grid_group_layout_2, mAuth);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mGaleriaGruposAdapter);
        mRecyclerView.addOnItemTouchListener(recyclerItemClickListener());
        initArray();
        mGaleriaGruposAdapter.setmArrayGrupos(array);
    }

    private RecyclerItemClickListener recyclerItemClickListener(){
        return new RecyclerItemClickListener(mActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                saidPhrase(position);
                Intent intent2 = new Intent(mActivity, GaleriaPictos3.class);
                try {
                    intent2.putExtra("Nombre", JSONutils.getNombre(array.getJSONObject(position),sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent2.putExtra("Boton", position);
                mActivity.startActivityForResult(intent2, IntentCode.GALERIA_PICTOS.getCode());
            }

            @Override
            public void onDoubleTap(View view, int position) {

            }

            @Override
            public void onLongClickListener(View view, int position) {

            }
        });
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


}
