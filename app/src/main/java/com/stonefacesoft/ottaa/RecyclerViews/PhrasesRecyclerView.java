package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.SelectFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.R;

import org.json.JSONException;

public class PhrasesRecyclerView extends Custom_recyclerView {
    private SelectFavoritePhrasesAdapter adapter;


    public PhrasesRecyclerView(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        cantColumnas = 1;
        setArray();

    }

    public void setArray() {
        array = json.getmJSONArrayTodasLasFrases();
        createRecyclerLayoutManager();
        adapter = new SelectFavoritePhrasesAdapter(mActivity);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPictosFiltrados() {
        adapter.setUserPhrases(arrayAux);
        if (createReturnPositionItem())
            sincronizeData();
    }

    @Override
    public void onPictosNoFiltrados() {
        adapter.setUserPhrases(array);
        sincronizeData();
    }

    @Override
    public void scrollTo(boolean add) {
        if (createReturnPositionItem()) {
            sincronizeData();
            if (add) {
                getPositionItem.add();
            } else {
                getPositionItem.subtract();
            }
            mRecyclerView.scrollToPosition(getPositionItem.getPosition());
        }
    }

    @Override
    public void sincronizeData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void recorrerListado(int k, int tam, String query) {
        for (int i = k; i < tam; i++) {
            try {
                if (array.getJSONObject(i).getString("frase").toLowerCase().
                        replace(mActivity.getString(R.string.chart_a), "a").
                        replace(mActivity.getString(R.string.chart_e), "e").
                        replace(mActivity.getString(R.string.chart_i), "i").
                        replace(mActivity.getString(R.string.chart_o), "o").
                        replace(mActivity.getString(R.string.chart_u), "u").
                        contains(query.toLowerCase())) {
                    arrayAux.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   Log.e("positionBusqueda",i+"");
        }
    }

    public void savePhrases() {
        adapter.saveList();
    }

    @Override
    protected void createRecyclerLayoutManager() {
        ScrollManager manager = new ScrollManager(mActivity, 1, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
    }

}
