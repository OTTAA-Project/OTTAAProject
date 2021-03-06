package com.stonefacesoft.ottaa.RecyclerViews;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaGruposAdapter;
import com.stonefacesoft.ottaa.Dialogos.Yes_noDialogs;
import com.stonefacesoft.ottaa.Edit_Picto_Visual;
import com.stonefacesoft.ottaa.GaleriaPictos3;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.CrashlyticsUtils;
import com.stonefacesoft.ottaa.utils.IntentCode;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Grupo_Recycler_View extends Custom_recyclerView implements  View.OnClickListener {

    private GaleriaGruposAdapter mGaleriaGruposAdapter;
    private int mPosition=-1;
    private ItemTouchHelper mItemTouchHelper;
    private final String TAG = "Grupo_Recycler";

    public Grupo_Recycler_View(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity,mAuth);
        array=json.getmJSONArrayTodosLosGrupos();
        createRecyclerLayoutManager();
    }


    public void cargarGrupo(){
        mGaleriaGruposAdapter = new GaleriaGruposAdapter(mActivity, R.layout.grid_group_layout_2, mAuth);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mGaleriaGruposAdapter);
        mRecyclerView.addOnItemTouchListener(RecyclerItemClickListener());
    }

    @Override
    public void sincronizeData(){
        json=Json.getInstance();
        json.setmContext(mActivity);
        array=json.getmJSONArrayTodosLosGrupos();
        json.setmJSONArrayTodosLosGrupos(array);
        mGaleriaGruposAdapter.setmArrayGrupos(array);
    }
    //Encargado de manejar los doble click y click en cada grupo para abrirlo con sus pictos

    private RecyclerItemClickListener RecyclerItemClickListener() {
        return new RecyclerItemClickListener(mRecyclerView,mActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                if (v != null) {


                    if (position != -1) {
                        try {
                            if (myTTS != null && array.getJSONObject(position).getJSONObject("texto").getString((sharedPrefsDefault.getString(mActivity.getString(R.string
                                    .str_idioma), "en"))) != null) {
                                myTTS.hablar(array.getJSONObject(position).getJSONObject("texto").getString((sharedPrefsDefault.getString(mActivity.getString(R.string
                                        .str_idioma), "en"))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /*Llamando a la misma Activity podemos pasarle un putExtra que despues el getIntent del setDialog va a tomar
                         * de esta forma podemos pasar la posicion del boton que clicamos y el nombre*/

                        //no tiene message
                        Intent intent2 = new Intent(mActivity, GaleriaPictos3.class);
                        intent2.putExtra("Boton", position);
                        try {
                            intent2.putExtra("Nombre", json.getNombre(array.getJSONObject(position)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log de cuantas frases se hacen
                        try {
                            Log.e("Answers", "Grupo: " + json.getNombre(array.getJSONObject(position)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            CrashlyticsUtils.getInstance().getCrashlytics().setCustomKey("Grupo Usado",json.getNombre(array.getJSONObject(position)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            (array.getJSONObject(position)).put("frecuencia", (array.getJSONObject(position)).getInt("frecuencia") + 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                (array.getJSONObject(position)).put("frecuencia", 1);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        json.setmJSONArrayTodosLosGrupos(array);
                        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS)) {
                            Log.d(TAG, "File warning");
                        }

                        //NOTA: hay  que tener en cuenta que cuando se hace de manera local esto funciona de una ,para la sincronizacion esto puede pisar los datos cuando sea en simultaneo
                        uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));
                        mActivity.startActivityForResult(intent2, IntentCode.GALERIA_PICTOS.getCode());
                    }
                }
            }


            @Override
            public void onDoubleTap(View view, final int position) {


            }

            @Override
            public void onLongClickListener(View view, int position) {
                if (position != -1) {
                    mPosition = position;
                    popupMenu = new PopupMenu(mActivity, view);
                    popupMenu.inflate(R.menu.popup_menu_juego);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.item_edit:
                                    analyticsFirebase.customEvents("Touch","Galeria Grupos","Edit Group");
                                    if (getmPosition() != -1) {
                                        Intent intent = new Intent(mActivity, Edit_Picto_Visual.class);
                                        int id = 0;
                                        try {
                                            id = array.getJSONObject(getmPosition()).getInt("id");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra("PositionPadre", getmPosition());
                                        intent.putExtra("PictoID", id);
                                        intent.putExtra("esGrupo", true);
                                        Log.e("GalGr_onMenuItemClick", "Editando un picto");
                                        myTTS.hablar(mActivity.getResources().getString(R.string.editar_group));
                                        mActivity.startActivityForResult(intent, IntentCode.EDITARPICTO.getCode());
                                    }

                                    return true;
                                case R.id.item_delete:
                                    analyticsFirebase.customEvents("Touch","Galeria Grupos","Delete Group");
                                    try {
                                        if(!(array.getJSONObject(mPosition).getJSONObject("texto").getString("en").equals("ALL") && array.getJSONObject(mPosition).getInt("id") == 24))
                                            AlertBorrar();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return true;


                            }
                            popupMenu.dismiss();
                            return false;
                        }
                    });

                    if (sharedPrefsDefault.getInt("premium", 0) == 1) {
                        popupMenu.getMenu().getItem(0).setEnabled(true);
                        popupMenu.getMenu().getItem(1).setEnabled(true);

                    } else {
                        popupMenu.getMenu().getItem(0).setEnabled(false);
                        popupMenu.getMenu().getItem(1).setEnabled(false);
                        popupMenu.getMenu().getItem(1).setIcon(R.drawable.ic_padlock);
                        popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_padlock);
                    }
                    try {
                        Field field = popupMenu.getClass().getDeclaredField("mPopup");
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> cls = Class.forName("androidx.appcompat.view.menu.MenuPopupHelper");
                        Method method = cls.getDeclaredMethod("setForceShowIcon", boolean.class);
                        method.setAccessible(true);
                        method.invoke(menuPopupHelper, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!mActivity.isFinishing())
                        popupMenu.show();
                }
            }


        });
    }



    @Override
    public void onClick(View view) {

    }




    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    public void setGrupos(){
        json=Json.getInstance();
        json.setmContext(mActivity);
        array=json.getmJSONArrayTodosLosGrupos();
        mGaleriaGruposAdapter.setmArrayGrupos(array);
        mGaleriaGruposAdapter.notifyDataSetChanged();
    }

    public int getmPosition() {
        return mPosition;
    }

    @Override
    protected void AlertBorrar() {
        Yes_noDialogs dialog_yes_no=new Yes_noDialogs(mActivity);
        dialog_yes_no.setTitle(mActivity.getResources().getString(R.string.pref_important_alert));
        dialog_yes_no.setMessage(mActivity.getResources().getString(R.string.pref_text5_alert));
        dialog_yes_no.setCancelable(true);
        dialog_yes_no.setOnClick(dialog_yes_no.getObject(R.id.yes_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int mPosition=getmPosition();
                    if(mPosition!=-1) {
                        if (!(json.getNombre(array.getJSONObject(mPosition)).toLowerCase().equals("all") &&json.getId(array.getJSONObject(mPosition)) == 24)) {
                            try {
//                                final String pushKeyFoto = array.getJSONObject(mPosition).getJSONObject("imagen").getString("pushKey");
//                                File f = new File(array.getJSONObject(mPosition).getJSONObject("imagen").getString("pictoEditado"));
//                                JSONArray mArrayListFotos;
//                                mArrayListFotos = json.addFoto2BackUp(mJSONArrayBackupFotos, recycler_view_grupo.getArray().getJSONObject(mPosition).getJSONObject("imagen"));
//                                json.setmJSONArrayTodasLasFotosBackup(mArrayListFotos);
//                                if (!json.guardarJson(Constants.ARCHIVO_FOTO_BACKUP))
//                                    Log.e(TAG, "Error al guardar Json");

                                array.remove(mPosition);

//                                if (f.exists())
//                                    f.delete();
                            } catch (Exception ex) {
                                array.remove(mPosition);
                            }

                            json.setmJSONArrayTodosLosGrupos(array);

                            if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
                                Log.e(TAG, "Error al guardar Json");

                            uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));
                        } else {
                            AlertDialog.Builder dialogo = new AlertDialog.Builder(mActivity);
                            dialogo.setTitle(R.string.warning_del);
                            dialogo.setMessage(mActivity.getResources().getString(R.string.dont_delete_group));
                            dialogo.show();

                        }
                        setGrupos();
                        dialog_yes_no.destruirDialogo();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("GalGr_AlertBorrar", "Borrando un picto");


            }

        });
        dialog_yes_no.setOnClick(dialog_yes_no.getObject(R.id.no_button), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_yes_no.cancelarDialogo();
            }
        });
        dialog_yes_no.mostrarDialogo();

    }

    public void guardarDatosGrupo() {
        if (!json.guardarJson(Constants.ARCHIVO_GRUPOS))
            Log.e(TAG, "Error al guardar el json");
        subirGrupos();
    }
}
