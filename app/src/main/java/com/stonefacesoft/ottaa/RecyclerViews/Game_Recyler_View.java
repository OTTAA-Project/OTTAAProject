package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Intent;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaJuegosAdapter;
//import com.stonefacesoft.ottaa.Games.DescribirPictograma;
import com.stonefacesoft.ottaa.Views.MatchPictograms;
import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
/**
 * @author  Gonzalo Juarez
 * <h3>Objetive </h3>
 * <p>Choose the level of game one by one</p>
 * */
public class Game_Recyler_View extends Custom_recyclerView {
    private final int id;
    private GaleriaJuegosAdapter mGaleriaGruposAdapter;

    public Game_Recyler_View(AppCompatActivity mActivity,FirebaseAuth mAuth,int id) {
        super(mActivity,mAuth);
        this.id=id;
        array=json.getmJSONArrayTodosLosGrupos();
        createRecyclerLayoutManager();

    }


    public void cargarGrupo(){
        mGaleriaGruposAdapter = new GaleriaJuegosAdapter(mActivity, R.layout.grid_group_layout_3,mAuth,id);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mGaleriaGruposAdapter);
        mRecyclerView.addOnItemTouchListener(RecyclerItemClickListener());
    }

    //Encargado de manejar los doble click y click en cada grupo para abrirlo con sus pictos

    private RecyclerItemClickListener RecyclerItemClickListener() {
        return new RecyclerItemClickListener(mActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view != null) {

                    if (position != -1) {
                        Intent intent=null;
                        switch (id){
                            case 0:
                                intent = new Intent(mActivity, WhichIsThePicto.class);
                                break;
                            case 1:
                                intent = new Intent(mActivity, MatchPictograms.class);
                                break;
                            case 2:
                              //  intent=new Intent(mActivity, DescribirPictograma.class);
                                break;

                        }

                        try {
                            intent.putExtra("PictoID", array.getJSONObject(position).getInt("id"));
                            intent.putExtra("PositionPadre", position);
                            mActivity.startActivityForResult(intent, IntentCode.NOTIGAMES.getCode());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception ex){

                        }


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

    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }
}
