package com.stonefacesoft.ottaa.RecyclerViews;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.Adapters.FindAllPictogramsAdapter;
import com.stonefacesoft.ottaa.BuscarArasaac;
import com.stonefacesoft.ottaa.Helper.ItemTouchHelperAdapter;
import com.stonefacesoft.ottaa.Helper.RecyclerItemClickListener;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.pictogramslibrary.Classes.Pictogram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindAllPictograms_Recycler_View extends Custom_recyclerView implements ItemTouchHelperAdapter {

    private BuscarArasaac buscarArasaac;
    private FindAllPictogramsAdapter findAllPictogramsAdapter;
    private JSONObject arasaac;
    private int id;


    public FindAllPictograms_Recycler_View(AppCompatActivity appCompatActivity, FirebaseAuth mAuth){
        super(appCompatActivity,mAuth);
        buscarArasaac=new BuscarArasaac();
    }

    public void setArray(){
        try {
            array=json.getHijosGrupo2(json.getGrupoFromId(24));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FiveMbException mbException) {
            mbException.printStackTrace();
        }
        createRecyclerLayoutManager();
        findAllPictogramsAdapter=new FindAllPictogramsAdapter(mActivity, R.layout.pictogram,array,true);
        mRecyclerView.setAdapter(findAllPictogramsAdapter);
        mRecyclerView.addOnItemTouchListener(listener());
    }


    @Override
    public void onPictosFiltrados() {
        filtrarPictogramas();
    }

    @Override
    public void onPictosNoFiltrados() {
        findAllPictogramsAdapter.setmVincularArray(array);
        findAllPictogramsAdapter.setEsFiltrado(false);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void filtrarPictogramas(){
        findAllPictogramsAdapter.setmVincularArray(arrayAux);
        findAllPictogramsAdapter.setEsFiltrado(true);
        mRecyclerView.getAdapter().notifyDataSetChanged();
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
            new HTTPRequest(query).execute();
            mSearchView.setQuery(query,false);
            return true;
        }
        return false;
    }

    @Override
    public void onItemMove(int fromIndex, int toIndex) {

    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onDropItem() {

    }

    private class HTTPRequest extends AsyncTask<Void, Void, Void> {

        //        private ProgressDialog progressDialog = new ProgressDialog(GaleriaArasaac.this);
        String texto;

        public HTTPRequest(String texto)
        {
            this.texto = texto;
        }
        // can use UI thread here
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(GaleriaArasaac.this, "", "Saving changes...",
//                    true,false);
//            progressDialog.setMessage(getResources().getString(R.string.pref_cargando_DB));
//            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            arasaac = buscarArasaac.HacerBusqueda(mActivity,texto);
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            try {
                if (arasaac != null && arasaac.getJSONArray("symbols") != null) {
                    JSONArray jsonArray = arasaac.getJSONArray("symbols");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pictogram=jsonArray.getJSONObject(i);
                        Pictogram picto=new Pictogram();
                        String url = pictogram.getString("imagePNGURL");
                        String text = pictogram.getString("name");
                        int tipo = pictogram.getInt("wordTYPE");
                        picto.setLocale(sharedPrefsDefault.getString(mActivity.getResources().getString(R.string.str_idioma),"en"));
                        picto.setName_en(text);
                        picto.setName(text);
                        picto.setUrl(url);
                        picto.setEditedPictogram(url);
                        picto.setType(tipo);
                        picto.setId((int)System.currentTimeMillis());
                        arrayAux.put(arrayAux.length(),picto.toJsonObject());
                      //  array.put(array.length(),(JSONObject) jsonArray.get(i));
                    }
                    onPictosFiltrados();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mActivity, R.string.problema_inet, Toast.LENGTH_LONG).show();
                onPictosFiltrados();
            }
        }
    }

    private RecyclerItemClickListener listener(){
        return new RecyclerItemClickListener(mRecyclerView,mActivity,new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                try {
                    JSONObject object=findAllPictogramsAdapter.getmArrayPictos().getJSONObject(position);
                    Log.d("FindAllPictograms", "onItemClick: "+object.toString());
                    int idPicto=json.getId(findAllPictogramsAdapter.getmArrayPictos().getJSONObject(position));
                    if(id!=idPicto){
                        id=idPicto;
                        myTTS.hablar(json.getNombre(findAllPictogramsAdapter.getmArrayPictos().getJSONObject(position)));
                    }
                    else {
                        Intent databack = new Intent();
                        databack.putExtra("ID", id);
                      //  databack.putExtra("Boton", button);
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
                try {
                    idPicto = json.getId(findAllPictogramsAdapter.getmArrayPictos().getJSONObject(position));
                    if(id!=idPicto){
                        id=idPicto;
                        myTTS.hablar(json.getNombre(findAllPictogramsAdapter.getmArrayPictos().getJSONObject(position)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
    }
}
