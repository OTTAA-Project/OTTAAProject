package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.MostUsedFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.Interfaces.ProgressBarListener;

public class MostUsedPhrases_Recycler_View extends Custom_recyclerView {
    private MostUsedFavoritePhrasesAdapter mostUsedFavoritePhrasesAdapter;

    public MostUsedPhrases_Recycler_View(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray() {
        array = json.getmJSonArrayFrasesFavoritas();
        arrayAux = json.getmJSonArrayFrasesFavoritas();
        createRecyclerLayoutManager();
        mostUsedFavoritePhrasesAdapter = new MostUsedFavoritePhrasesAdapter(mActivity, new ProgressBarListener() {
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
                mRecyclerView.setAdapter(mostUsedFavoritePhrasesAdapter);
            }
        });

    }

    @Override
    public void onPictosFiltrados() {


    }

    @Override
    public void onPictosNoFiltrados() {

    }

    @Override
    public void sincronizeData() {
        getPositionItem.updateSize(mostUsedFavoritePhrasesAdapter.getItemCount());
    }

    @Override
    public void scrollTo(boolean add) {
        if (createReturnPositionItem()) {
            if (add) {
                getPositionItem.add();
            } else {
                getPositionItem.subtract();
            }
            if (mRecyclerView.getAdapter().getItemCount() > 0)
                mRecyclerView.scrollToPosition(getPositionItem.getPosition());
        }
    }


    @Override
    protected void createRecyclerLayoutManager() {
        ScrollManager manager = new ScrollManager(mActivity, 1, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void talkAtPosition() {
        if (createReturnPositionItem()) {
            int value = getPositionItem.getPosition();
            if (validatePosition(value))
                mostUsedFavoritePhrasesAdapter.getMyTTs().hablar(mostUsedFavoritePhrasesAdapter.getmFavImagesArrayList().get(value).getTexto());
        }
    }

}
