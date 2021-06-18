package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.MostUsedFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.Interfaces.ProgressBarListener;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

public class MostUsedPhrases_Recycler_View extends Custom_recyclerView {
    private MostUsedFavoritePhrasesAdapter mostUsedFavoritePhrasesAdapter;

    public MostUsedPhrases_Recycler_View(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray(){
        array=json.getmJSonArrayFrasesFavoritas();
        arrayAux = json.getmJSonArrayFrasesFavoritas();
        createRecyclerLayoutManager();
        mostUsedFavoritePhrasesAdapter =new MostUsedFavoritePhrasesAdapter(mActivity, new ProgressBarListener() {
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
    public void scrollTo(boolean add) {
        if(getPositionItem==null)
            getPositionItem = new ReturnPositionItem(mRecyclerView.getAdapter().getItemCount());
        if(add){
            getPositionItem.add();
        }
        else{
            getPositionItem.subtract();
        }
        mRecyclerView.scrollToPosition(getPositionItem.getPosition());
    }

    @Override
    protected void createRecyclerLayoutManager() {
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
    }

}
