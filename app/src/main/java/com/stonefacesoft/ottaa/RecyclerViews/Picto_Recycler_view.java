package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaPictosAdapter;
import com.stonefacesoft.ottaa.Dialogos.DialogUtils.Yes_noDialogs;
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.PopupMenuUtils;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.preferences.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Picto_Recycler_view extends Custom_recyclerView {
    private int button;
    private GaleriaPictosAdapter galeriaPictos2;
    private int id=-1;
    private final String TAG="Picto_Recycler_View";
    private boolean editarPicto;

    public Picto_Recycler_view(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);

    }



    //Load
    public void setArray(int position){
        this.button=position;
        array=json.getHijosGrupo2(position);
        arrayAux=new JSONArray();
        createRecyclerLayoutManager();
        galeriaPictos2=new GaleriaPictosAdapter(mActivity,array, R.layout.grid_item_layout,mAuth);
        mRecyclerView.setAdapter(galeriaPictos2);
        mRecyclerView.addOnItemTouchListener(listener());
    }


    @Override
    public void sincronizeData() {
        json=Json.getInstance();
        json.setmContext(mActivity);
        array=json.getHijosGrupo2(button);
        galeriaPictos2.setmArrayPictos(array);
    }

    public void filterChildren(){
        galeriaPictos2.setmArrayPictos(arrayAux);
        galeriaPictos2.notifyDataSetChanged();
        mRecyclerView.invalidate();
    }


    /**
     * this method set up the visibility around a recycler view
     *
     * */


    public JSONArray getArray() {
        return array;
    }


    @Override
    public void onPictosFiltrados() {
        filterChildren();
    }

    @Override
    public void onPictosNoFiltrados() {
        sincronizeData();
    }

    private RecyclerItemClickListener listener(){
        return new RecyclerItemClickListener(mRecyclerView,mActivity,new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                try {
                    int idPicto=json.getId(galeriaPictos2.getmArrayPictos().getJSONObject(position));
                    if(id!=idPicto){
                        id=idPicto;
                      myTTS.hablar(JSONutils.getNombre(galeriaPictos2.getmArrayPictos().getJSONObject(position),sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                    }
                    else {
                        Intent databack = new Intent();
                        databack.putExtra("ID", id);
                        databack.putExtra("Boton", button);
                        mActivity.setResult(IntentCode.GALERIA_PICTOS.getCode(), databack);
                        mActivity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDoubleTap(View view, int position) {

                }

            @Override
            public void onLongClickListener(View view, int position) {
                int idPicto= 0;
                editarPicto = sharedPrefsDefault.getBoolean(mActivity.getString(R.string.str_editar_picto), true);
                if(editarPicto) {
                    try {
                        idPicto = json.getId(galeriaPictos2.getmArrayPictos().getJSONObject(position));
                        if (id != idPicto) {
                            id = idPicto;
                            myTTS.hablar(JSONutils.getNombre(galeriaPictos2.getmArrayPictos().getJSONObject(position), sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
                            ShowpopMenu(view, position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    onItemClick(view,position);
                }
            }
        } );
    }

    public void ShowpopMenu(View view,int position) {
        PopupMenuUtils popupMenuUtils = new PopupMenuUtils(mActivity,view);
        popupMenuUtils.addClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.item_edit:
                        if (galeriaPictos2.getmArrayPictos() != null) {
                            // JSONObject aux = pictosDelGrupo.get(position);
                            analyticsFirebase.customEvents("Touch","Galeria Pictos","Edit Pictogram");
                            if (id != -1) {
                                if(User.getInstance(mActivity).isPremium()){
                                    mActivity.startActivityForResult(startEditAction(position), IntentCode.EDITARPICTO.getCode());
                                }else {
                                    mActivity.startActivity(startExpiredLicense());
                                }
                            }
                        }
                        return true;
                    case R.id.item_delete:
                        analyticsFirebase.customEvents("Touch","Galeria Pictos","Delete Pictogram");
                        AlertBorrar();
                        return true;
                }
                return false;
            }
        });
        popupMenuUtils.inflateIt();

    }

    @Override
    public void AlertBorrar() {
        Yes_noDialogs dialogo1 = new Yes_noDialogs(mActivity);
        dialogo1.setTitle(mActivity.getResources().getString(R.string.pref_important_alert)).setMessage(mActivity.getResources().getString(R.string.pref_text4_alert));
        dialogo1.setOnClick(dialogo1.getObject(R.id.yes_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray pictosGrupos=json.getmJSONArrayTodosLosGrupos();
                try {
                    JSONutils.setHijosGrupo2(pictosGrupos, array, button);
                    JSONutils.desvincularJson(pictosGrupos.getJSONObject(button), id);
                    array = JSONutils.getHijosGrupo2(json.getmJSONArrayTodosLosPictos(),pictosGrupos.getJSONObject(button));
                    galeriaPictos2.setmArrayPictos(array);
                    galeriaPictos2.notifyDataSetChanged();
                    json.setmJSONArrayTodosLosGrupos(pictosGrupos);
                    if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
                        Log.e(TAG, "Error al guardar el json");
                    subirGrupos();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialogo1.cancelarDialogo();

            }
        });
        dialogo1.setOnClick(dialogo1.getObject(R.id.no_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo1.cancelarDialogo();

            }
        });

        dialogo1.mostrarDialogo();
    }
    public void guardarDatosGrupo() {
        JSONArray pictosGrupos = json.getmJSONArrayTodosLosGrupos();
        JSONutils.setHijosGrupo2(pictosGrupos, array, button);
        json.setmJSONArrayTodosLosGrupos(pictosGrupos);
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
            Log.e(TAG, "Error al guardar el json");
        subirGrupos();
    }

    public void guardarDatosGrupoOnlyId() {
        JSONArray pictosGrupos = json.getmJSONArrayTodosLosGrupos();
        JSONArray aux = new JSONArray();
        for (int i = 0; i <array.length() ; i++) {
            try {
                JSONObject child = new JSONObject();
                child.put("id",array.getJSONObject(i));
                aux.put(child);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONutils.setHijosGrupo2(pictosGrupos, aux, button);
        json.setmJSONArrayTodosLosGrupos(pictosGrupos);
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
            Log.e(TAG, "Error al guardar el json");
        subirGrupos();
    }


    @Override
    protected Intent startEditAction(int position) {
        Intent intent = new Intent(mActivity, Edit_Picto_Visual.class);
        loadPictogramsValue(intent,position);
        myTTS.hablar(mActivity.getString(R.string.editar_pictogram));
        json.setmJSONArrayTodosLosGrupos(json.getmJSONArrayTodosLosGrupos());
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
            Log.i(TAG, "onMenuItemClick: no se pudo guardar el mensaje");
        }
        return intent;
    }

    public void loadPictogramsValue(Intent intent,int position){
        intent.putExtra("PositionPadre", button);
        intent.putExtra("PositionHijo", position);
        intent.putExtra("PictoID", id);

        try {
            intent.putExtra("Texto", JSONutils.getNombre(galeriaPictos2.getmArrayPictos().getJSONObject(position),sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("esGrupo", false);
    }

}
