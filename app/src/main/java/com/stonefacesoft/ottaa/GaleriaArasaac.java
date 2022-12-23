package com.stonefacesoft.ottaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.firebase.perf.metrics.AddTrace;
import com.stonefacesoft.ottaa.Interfaces.SearchAraasacPictogram;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.Pictures.DownloadArasaac;
import com.stonefacesoft.ottaa.utils.Pictures.DownloadTask;
import com.stonefacesoft.ottaa.utils.StringFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


//import android.widget.SearchView;

/**
 * Created by morro on 22/6/2016.
 * Edited by Gonzalo Juareez on 2/10/2020
 */
public class GaleriaArasaac extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //Shared prefs
    SharedPreferences sharedPrefsDefault;
    int boton;
    ArrayList<JSONObject> pictosDelGrupo;
    private BuscarArasaac buscarArasaac;
    private JSONObject arasaac;
    private JSONArray pictogramsResult;
    private String text = "";
    private int tipo = 0;
    ImageView ImagenArasaac;
    TextView TextoArasaac;
    private Json json;
    private gridViewAdapter gridAdapter;

    @AddTrace(name = "GaleriaArasaac", enabled = true /* optional */)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intento = getIntent();
        boolean status_bar = intento.getBooleanExtra("status_bar",false);
        if (!status_bar) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_pictos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pictosDelGrupo = new ArrayList<>();
        Json.getInstance().setmContext(this);
        json = Json.getInstance();
        //Implemento el manejador de preferencias
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        buscarArasaac = new BuscarArasaac();
        ImagenArasaac = findViewById(R.id.ImageArasaac);
        ImagenArasaac.setVisibility(View.VISIBLE);
        TextoArasaac = findViewById(R.id.TextoArasaac);
        TextoArasaac.setVisibility(View.VISIBLE);
        TextoArasaac.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeria_pictos, menu);
        MenuItem item;
        item = menu.findItem(R.id.vincular);
        item.setVisible(false);
        item = menu.findItem(R.id.nuevo);
        item.setVisible(false);
//        item = menu.findItem(R.id.listo);
        item.setVisible(false);
        item = menu.findItem(R.id.bajarFotos);
        item.setVisible(false);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getResources().getString(R.string.HintSearch));

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent databack = new Intent();
        databack.putExtra("ID", 0);
        databack.putExtra("Boton", boton);
        setResult(3, databack);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openIntent()
    {
        GridView gridView = findViewById(R.id.gridView);
        try {
            if(gridAdapter ==null)
                gridAdapter = new gridViewAdapter(gridView.getContext(), R.layout.grid_item_layout, pictosDelGrupo, json);
            else{
                gridAdapter.setData1(pictosDelGrupo);
                gridAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        gridView.setAdapter(gridAdapter);
        gridView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.regular_margin));
        gridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.regular_margin));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    String url = JSONutils.getUriByApi(pictosDelGrupo.get(position));
                    text = JSONutils.getStringByApi(pictosDelGrupo.get(position));
                    tipo = JSONutils.getTypeAsInteger(pictosDelGrupo.get(position));
                    final DownloadArasaac downloadTask = new DownloadArasaac(GaleriaArasaac.this,text,tipo);
                    downloadTask.execute(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.EDITARPICTO.getCode()) {
            openIntent();
        }
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, getString(R.string.searching_araasac) +" "+ query, Toast.LENGTH_LONG).show();
        query = query.trim();
        if (ConnectionDetector.isNetworkAvailable(this))
            new HTTPRequest(query).execute();
        else
            Toast.makeText(this, "No existe una conexion de internet activa, vuelva a intentarlo mas tarde", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class HTTPRequest extends AsyncTask<Void, Void, Void> implements SearchAraasacPictogram {

        private ProgressDialog progressDialog = new ProgressDialog(GaleriaArasaac.this);
        String texto;

        public HTTPRequest(String texto)
        {
            this.texto = texto;
            buscarArasaac.setSearchAraasacPictogram(this);
        }
        // can use UI thread here
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(GaleriaArasaac.this, "", "Saving changes...",
                    true,false);
            progressDialog.setMessage(getResources().getString(R.string.pref_cargando_DB));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            buscarArasaac.HacerBusqueda(texto, sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma),"en"),GaleriaArasaac.this);
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            progressDialog.dismiss();
            ImagenArasaac.setVisibility(View.GONE);
            TextoArasaac.setVisibility(View.GONE);
            try {
                if (pictogramsResult != null) {
                    pictosDelGrupo.clear();
                    for (int i = 0; i < pictogramsResult.length(); i++) {
                        pictosDelGrupo.add(pictogramsResult.getJSONObject(i));
                    }
                    openIntent();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GaleriaArasaac.this, R.string.problema_inet, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void findPictogramsJsonObject(JSONObject value) {
            arasaac = value;
            try {
                pictogramsResult = arasaac.getJSONArray("symbols");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onPostExecute(null);
        }

        @Override
        public void findPictogramsJsonArray(JSONArray value) {
            pictogramsResult = value;
            onPostExecute(null);
        }

    }


    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Json.getInstance().setmContext(this);
    }

}
