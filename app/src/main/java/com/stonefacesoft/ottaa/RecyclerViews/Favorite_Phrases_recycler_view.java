package com.stonefacesoft.ottaa.RecyclerViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.CustomFavoritePhrasesAdapter;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

public class Favorite_Phrases_recycler_view extends Custom_recyclerView{
    private CustomFavoritePhrasesAdapter adapter;

    public Favorite_Phrases_recycler_view(AppCompatActivity mActivity, FirebaseAuth mAuth) {
        super(mActivity, mAuth);
        setArray();
    }

    public void setArray(){
        array=json.getmJSonArrayFrasesFavoritas();
        arrayAux = json.getmJSonArrayFrasesFavoritas();
        createRecyclerLayoutManager();
        adapter=new CustomFavoritePhrasesAdapter(mActivity);
        mRecyclerView.setAdapter(adapter);

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
