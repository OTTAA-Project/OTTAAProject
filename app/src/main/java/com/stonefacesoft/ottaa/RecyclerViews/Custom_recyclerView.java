package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.FirebaseRequests.SubirArchivosFirebase;
import com.stonefacesoft.ottaa.Helper.SimpleItemTouchHelperCallback;
import com.stonefacesoft.ottaa.Interfaces.interface_search;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.LicenciaExpirada;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Gonzalo Juarez
 * @since 23/06/2020
 * Recycler view class
 * */
public abstract class Custom_recyclerView implements SearchView.OnQueryTextListener, interface_search {
    protected static final String TAG ="Custom_recyclerView" ;
    protected AppCompatActivity mActivity;
    protected RecyclerView mRecyclerView;
    private int cantColumnnas;
    protected Json json;
    protected FirebaseAuth mAuth;
    protected textToSpeech myTTS;
    protected SharedPreferences sharedPrefsDefault;
    protected PopupMenu popupMenu;
    protected SubirArchivosFirebase uploadFirebaseFile;
    protected PopupMenu.OnMenuItemClickListener menuClickListener;
    protected SimpleItemTouchHelperCallback itemTouchHelperCallback;
    protected ReturnPositionItem getPositionItem;

    protected SearchView mSearchView;
    protected JSONArray array;
    protected JSONArray arrayAux;
    protected JSONArray arrayWithoutRelation;

    protected  int posItem;
    protected  int cantColumnas;
    protected  int cantFilas;
    protected AnalyticsFirebase analyticsFirebase;
    protected boolean scrollVertical = true;




    public Custom_recyclerView(AppCompatActivity mActivity, FirebaseAuth mAuth){
        this.mActivity=mActivity;
        mRecyclerView = this.mActivity.findViewById(R.id.recyclerView);
        this.json=Json.getInstance();
        this.json.setmContext(this.mActivity);
        this.mAuth=mAuth;
        analyticsFirebase=new AnalyticsFirebase(this.mActivity);
        mRecyclerView.setVisibility(View.VISIBLE);
        cantColumnas=calcularCantidadColumnas();
    }

    protected int calcularCantidadColumnas() {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = mActivity.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / 250);
//        return mActivity.getResources().getConfiguration().screenWidthDp / convertToDp(150);
    }

    public void setPopupMenu(PopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void setSharedPrefsDefault(SharedPreferences sharedPrefsDefault) {
        this.sharedPrefsDefault = sharedPrefsDefault;
    }

    public void setUploadFirebaseFile(SubirArchivosFirebase uploadFirebaseFile) {
        this.uploadFirebaseFile = uploadFirebaseFile;
    }

    public void setMenuClickListener(PopupMenu.OnMenuItemClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    public void setMyTTS(textToSpeech myTTS) {
        this.myTTS = myTTS;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    /*
    * Show the recycler view
    * @param show
    * true  : View.Gone
    * false : View.Visible
    */
    public void showRecyclerView(boolean show){
        if(!show){
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else
            mRecyclerView.setVisibility(View.GONE);
    }
    protected void createRecyclerLayoutManager(){
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mActivity, calcularCantidadColumnas());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public void setSearchView(SearchView searchView) {
        this.mSearchView = searchView;
        this.mSearchView.setOnQueryTextListener(this);
    }





    @Override
    public boolean onQueryTextSubmit(String query) {
        analyticsFirebase.customEvents("Touch","Editar Grupos","Search Pictogram");
        if (mSearchView.getQuery().length() > 0) {
            mSearchView.setIconified(true);
            arrayAux=new JSONArray();
            int cant = array.length() / 200;
            int cont = 0;
            do {

                if ((cont * 200 + 200) < array.length())
                    recorrerListado(cont * 200, (cont * 200 + 200), query);
                else
                    recorrerListado(cont * 200, array.length(), query);
                cont++;
            } while (cont <= cant);


            //Nos aseguramos que es vincular o no, si lo es seteamos el array nuevo de pictos filtrados
            //para que el adapter trabaje con ese array.
            onPictosFiltrados();
            mSearchView.setQuery(query,false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (mSearchView.getQuery().length() == 0||mSearchView.getQuery().equals(" ")) {
            onPictosNoFiltrados();
            return true;
        }
        return false;
    }
    /**
     * Analize the array and looking for the word
     * */
    protected void recorrerListado(int k, int tam, String query) {
        for (int i = k; i < tam; i++) {

            try {
                if (array.getJSONObject(i).getJSONObject("texto").getString(sharedPrefsDefault.getString(mActivity.getString(R.string.str_idioma), "en")).toLowerCase().
                        replace(mActivity.getString(R.string.chart_a), "a").
                        replace(mActivity.getString(R.string.chart_e), "e").
                        replace(mActivity.getString(R.string.chart_i), "i").
                        replace(mActivity.getString(R.string.chart_o), "o").
                        replace(mActivity.getString(R.string.chart_u), "u").
                        contains(query)) {
                    arrayAux.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   Log.e("positionBusqueda",i+"");
        }

    }
    /*
    * Upload the group
    * */
    public void subirGrupos(){
        uploadFirebaseFile.subirGruposFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.Grupos), uploadFirebaseFile.getmStorageRef(mAuth, Constants.Grupos));

    }

    public void subirPictos(){
        uploadFirebaseFile.subirPictosFirebase(uploadFirebaseFile.getmDatabase(mAuth, Constants.PICTOS), uploadFirebaseFile.getmStorageRef(mAuth, Constants.PICTOS));

    }

    /**
     * return the main JsonArray
     * */
    public JSONArray getArray() {
        return array;
    }
    /**
     * return the auxiliar JsonArray
     * */
    public JSONArray getArrayAux() {
        return arrayAux;
    }

    /**
     * Recycler view page scroll
     * @param add
     * <item>
     *  <li>true  : increment </li>
     *  <li>false : decrement</li>
     * </item>
     * */
    public void scrollTo(boolean add){
        float items = 0;
        if(existRecyclerViewAndAdapter()){
            items = mRecyclerView.getAdapter().getItemCount();
            cantFilas = getNumberOfCells(cantFilas,(int) Math.ceil(items/cantColumnas));
            if(getPositionItem!=null){
                getPositionItem.updateSize(cantFilas);
            }
        }
        if(getPositionItem==null && existRecyclerViewAndAdapter()){
            getPositionItem = new ReturnPositionItem(cantFilas);
        }
        try{
            if(add){
                posItem = getPositionItem.add();
            }
            else{
                posItem = getPositionItem.subtract();
            }
            int positionNavigate=posItem*cantColumnas;
            mRecyclerView.scrollToPosition(positionNavigate);
        }catch (Exception ex){

        }
    }

    private boolean existRecyclerViewAndAdapter(){
        if(mRecyclerView!= null)
            return mRecyclerView.getAdapter() != null;
        return false;
    }

    protected void AlertBorrar(){

    }

    public void changeData(){
        if(existRecyclerViewAndAdapter())
            mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void sincronizeData(){

    }

    public boolean createReturnPositionItem(){
        if(getPositionItem==null && existRecyclerViewAndAdapter())
            getPositionItem = new ReturnPositionItem(mRecyclerView.getAdapter().getItemCount());
        return getPositionItem != null;
    }

    public void setOnClickListener(){

    }
    protected class ScrollManager extends GridLayoutManager{
        public ScrollManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
        public ScrollManager(Context context, int spanCount,
                             @RecyclerView.Orientation int orientation, boolean reverseLayout) {
            super(context, spanCount,orientation, reverseLayout);
        }


        @Override
        public boolean canScrollVertically() {
            return scrollVertical;
        }

        @Override
        public boolean canScrollHorizontally() {
            return super.canScrollHorizontally();
        }
    }

    public void setScrollVertical(boolean scrollVertical) {
        this.scrollVertical = scrollVertical;
    }

    public void talkAtPosition(){

    }
    public boolean validatePosition(int value){
        return value != -1;
    }


    protected Intent startExpiredLicense(){
        return new Intent(mActivity, LicenciaExpirada.class);
    }

    protected Intent startEditAction(){
        return null;
    }
    protected Intent startEditAction(int position){
        return null;
    }

    protected int getNumberOfCells(int value,int result){
        return result;
    }
    protected void saidPhrase(int position){
        try {
            if (myTTS != null && array.getJSONObject(position).getJSONObject("texto").getString((sharedPrefsDefault.getString(mActivity.getString(R.string
                    .str_idioma), "en"))) != null) {
                myTTS.hablar(array.getJSONObject(position).getJSONObject("texto").getString((sharedPrefsDefault.getString(mActivity.getString(R.string
                        .str_idioma), "en"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
