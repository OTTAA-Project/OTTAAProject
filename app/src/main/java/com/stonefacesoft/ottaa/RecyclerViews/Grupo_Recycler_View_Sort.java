package com.stonefacesoft.ottaa.RecyclerViews;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaGruposAdapter;
import com.stonefacesoft.ottaa.Helper.SimpleItemTouchHelperCallback;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.constants.Constants;

public class Grupo_Recycler_View_Sort extends Custom_recyclerView {
    protected static final String TAG ="Grupo_R_V_sort" ;
    protected GaleriaGruposAdapter mGaleriaGruposAdapter;
    protected final int mPosition = -1;
    public Grupo_Recycler_View_Sort(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        array=json.getmJSONArrayTodosLosGrupos();
        arrayAux=json.getmJSONArrayTodosLosGrupos();
        createRecyclerLayoutManager();

    }
    public void cargarGrupo(){

        mGaleriaGruposAdapter = new GaleriaGruposAdapter(mActivity, R.layout.grid_group_layout_2, mAuth);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mGaleriaGruposAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mGaleriaGruposAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }


    public void guardarOrden() {
        json.setmJSONArrayTodosLosGrupos(mGaleriaGruposAdapter.getmArrayGrupos());
        if (!json.guardarJson(Constants.Grupos))
            Log.e(TAG, "Error al guardar los grupos guardarOrden: ");
        uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));
    }

    public void cancelOrder() {
        json.setmJSONArrayTodosLosGrupos(arrayAux);
        if (!json.guardarJson(Constants.Grupos))
            Log.e(TAG, "Error al guardar los grupos guardarOrden: ");
        uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));
    }


}
