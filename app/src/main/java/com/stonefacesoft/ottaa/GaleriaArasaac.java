package com.stonefacesoft.ottaa;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.IntentCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private String text = "";
    private int tipo = 0;
    ImageView ImagenArasaac;
    TextView TextoArasaac;
    private Json json;

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
        new cargarImg().execute();

        ImagenArasaac = findViewById(R.id.ImageArasaac);
        ImagenArasaac.setVisibility(View.VISIBLE);
        TextoArasaac = findViewById(R.id.TextoArasaac);
        TextoArasaac.setVisibility(View.VISIBLE);
        TextoArasaac.setTextColor(getResources().getColor(R.color.colorWhite));





//        openIntent();

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
//        Intent intent = getIntent();
//        boton = intent.getIntExtra("Boton", 0);

//        pictosDelGrupo = Json.getHijosGrupo(Json.getmArrayListTodosLosGruposJson().get(boton));
        GridView gridView = findViewById(R.id.gridView);

        gridViewAdapter gridAdapter = null;
        try {
            gridAdapter = new gridViewAdapter(this, R.layout.grid_item_layout, pictosDelGrupo, json, true);




        } catch (Exception e) {
//            WeeklyBackup wb = new WeeklyBackup(this);
//            wb.weeklyBackupDialog(false, R.string.pref_summary_backup_principal, false);
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
                    String url = pictosDelGrupo.get(position).getString("imagePNGURL");
                    text = pictosDelGrupo.get(position).getString("name");
                    tipo = pictosDelGrupo.get(position).getInt("wordTYPE");
                    //                    if (isDownloadManagerAvailable(this)) { descargarImagen(url);}
                    // execute this when the downloader must be fired
                    final DownloadTask downloadTask = new DownloadTask(GaleriaArasaac.this);
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

    ///////////////////////////////  Descargar Archivo  ////////////////////////////////////////////

    private void descargarImagen(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Se esta descargando la imagen deseada para el pictograma seleccionado.");
        request.setTitle("Imagen OTTAA Project");
        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= 21) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".png";
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files", mImageName);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void showCustomAlert(CharSequence Testo) {
        //Retrieve the layout inflator
        LayoutInflater inflater = getLayoutInflater();
        //Assign the custom layout to view
        //Parameter 1 - Custom layout XML
        //Parameter 2 - Custom layout ID present in linearlayout tag of XML
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));
        TextView tv = layout.findViewById(R.id.text);
        tv.setTextColor(getResources().getColor(R.color.colorWhite));
        tv.setTextSize(sharedPrefsDefault.getInt("subtitulo_tamanio", 25));
        tv.setText(Testo);
        //Return the application mContext
        Toast toast = new Toast(getApplicationContext());
        //Set custom_toast gravity to bottom
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        //Set custom_toast duration
        toast.setDuration(Toast.LENGTH_SHORT);
        //Set the custom layout to Toast
        toast.setView(layout);
        //Display custom_toast
        toast.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.EDITARPICTO.getCode()) {
            openIntent();
        }
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private final Context context;
        private PowerManager.WakeLock mWakeLock;
        private ProgressDialog mProgressDialog = new ProgressDialog(GaleriaArasaac.this);
        String path;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(GaleriaArasaac.this, "", context.getResources().getString( R.string.downloadingFoto),
                    true,false);
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                String mImageName="/MI_"+timeStamp+".png";
                input = connection.getInputStream();
                String pathAux="";
                UriFiles files=new UriFiles(context);
                files.dir();


                path =files.getPath() + mImageName;
                output = new FileOutputStream(path);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {

                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            Intent databack = new Intent();
            if (mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (result != null) {
                Toast.makeText(context, getString(R.string.error_download) + result, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, R.string.file_download, Toast.LENGTH_SHORT).show();
                databack.putExtra("Path", path);
                databack.putExtra("Type", tipo);
                databack.putExtra("Text", text);

            }
            setResult(IntentCode.ARASAAC.getCode(), databack);
            finish();
        }

    }

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
//        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        return false;
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
            arasaac = buscarArasaac.HacerBusqueda(texto, sharedPrefsDefault.getString(getApplicationContext().getString(R.string.str_idioma),"en"));
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
            ImagenArasaac.setVisibility(View.GONE);
            TextoArasaac.setVisibility(View.GONE);
            try {
                if (arasaac != null && arasaac.getJSONArray("symbols") != null) {
                    JSONArray jsonArray = arasaac.getJSONArray("symbols");
                    pictosDelGrupo.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pictosDelGrupo.add((JSONObject) jsonArray.get(i));
                        openIntent();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GaleriaArasaac.this, R.string.problema_inet, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class cargarImg extends AsyncTask<Void, Void, Void> {

        Drawable drawable;
        @Override
        protected Void doInBackground(Void... voids) {
            DrawableManager drawableManager = new DrawableManager();
            drawable = drawableManager.fetchDrawable("http://arasaac.org/repositorio/originales/30482.png");
            return null;
        }

        /**
         * Una vez que paso el tiempo de espera ilumino la parte adecuando
         * @param unused
         */
        protected void onPostExecute(final Void unused) {
//            Opcion1.setCustom_Img(drawable);
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

    private int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }
}
