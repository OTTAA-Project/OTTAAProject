package com.stonefacesoft.ottaa.utils.Pictures;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.Bitmap.UriFiles;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadTask extends AsyncTask<String, Integer, String> {
   protected AppCompatActivity appCompatActivity;
   protected PowerManager.WakeLock mWakeLock;
   protected ProgressDialog mProgressDialog;
   protected String path;
   protected  String text;
   protected int tipo;

   public DownloadTask(AppCompatActivity context,String text,int tipo) {
      this.appCompatActivity = context;
      this.text =text;
      this.tipo = tipo;
      mProgressDialog = new ProgressDialog(this.appCompatActivity);
   }

   @Override
   protected void onPreExecute() {
      mProgressDialog = ProgressDialog.show(appCompatActivity, "", appCompatActivity.getResources().getString( R.string.downloadingFoto),
              true,false);
      super.onPreExecute();
      // take CPU lock to prevent CPU from going off if the user
      // presses the power button during download
      PowerManager pm = (PowerManager) appCompatActivity.getSystemService(Context.POWER_SERVICE);
      mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
              getClass().getName());
      mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
   }

   public void execute1(){
      Executor executor = Executors.newSingleThreadExecutor();
      Handler handler = new Handler(Looper.getMainLooper());

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
         String mImageName="/MI_"+timeStamp+".jpg";
         input = connection.getInputStream();
         String pathAux="";
         UriFiles files=new UriFiles(appCompatActivity);
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


   @Override
   protected void onPostExecute(String result) {
      mWakeLock.release();
      makeAction(result);

   }

   protected void makeAction(String result){

   }



}

