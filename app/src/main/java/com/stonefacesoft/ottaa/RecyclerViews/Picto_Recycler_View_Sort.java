package com.stonefacesoft.ottaa.RecyclerViews;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.GaleriaPictosAdapter;
import com.stonefacesoft.ottaa.Helper.SimpleItemTouchHelperCallback;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;

public class Picto_Recycler_View_Sort extends Custom_recyclerView{
    private int button;
    private GaleriaPictosAdapter galeriaPictos2;
    private final String TAG="Picto_R_View_sort";
    public Picto_Recycler_View_Sort(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
    }
    public void setArray(int position){
        this.button=position;
        array=json.getHijosGrupo2(position);
        arrayAux=new JSONArray();
        createRecyclerLayoutManager();
        galeriaPictos2=new GaleriaPictosAdapter(mActivity,array, R.layout.grid_item_layout,mAuth);
        mRecyclerView.setAdapter(galeriaPictos2);
        itemTouchHelperCallback = new SimpleItemTouchHelperCallback(galeriaPictos2);
        ItemTouchHelper.Callback callback = itemTouchHelperCallback;
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }
    @Override
    public void onPictosFiltrados() {

    }

    @Override
    public void onPictosNoFiltrados() {

    }

    public void guardarOrden() {
        JSONArray grupos = json.getmJSONArrayTodosLosGrupos();
        JSONutils.setHijosGrupo2(grupos, galeriaPictos2.getmArrayPictos(), button);
        if (!json.guardarJson(Constants.Grupos))
            Log.e(TAG, "Error al guardar los grupos guardarOrden: ");
    }
}
