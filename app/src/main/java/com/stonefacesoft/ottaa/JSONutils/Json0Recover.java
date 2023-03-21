package com.stonefacesoft.ottaa.JSONutils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Interfaces.FailReadPictogramOrigin;
import com.stonefacesoft.ottaa.idioma.ConfigurarIdioma;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.JSONutils;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;
import com.stonefacesoft.ottaa.utils.preferences.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.grpc.internal.JsonUtil;

public class Json0Recover {
    private String TAG = "JSONRECOVER";
    private JSONObject parent;
    public JSONObject createJson(){
        parent = new JSONObject();
        JSONArray array = new JSONArray();
        loadRelationShip(array);
        parent = JSONutils.crearJson(0, ConfigurarIdioma.getLanguaje(),"","",array,"ic_perro",0);
        return parent;
    }
    private void preparePictograms(JSONArray array,int id,int frec){
        JSONObject object = new JSONObject();
        try {
            object.put("id",id);
            object.put("frec",frec);
        } catch (JSONException e) {
        }
        array.put(object);
    }
    private void loadRelationShip(JSONArray array){
        preparePictograms(array,44,51);
        preparePictograms(array,377,73);
        preparePictograms(array,382,2);
        preparePictograms(array,384,1);
        preparePictograms(array,388,11);
        preparePictograms(array,389,13);
        preparePictograms(array,614,14);
        preparePictograms(array,623,10);
        preparePictograms(array,628,35);
        preparePictograms(array,632,16);
        preparePictograms(array,643,37);
    }

    public void backupPictogram0(Context context,JSONObject parent,FailReadPictogramOrigin failReadPictogramOrigin){
        JSONObject object = parent;
        String uid = User.getInstance(context).getUserUid();
        saveFile(context,parent);
        if(!uid.isEmpty()){
            FirebaseUtils.getInstance().getmDatabase().child("backupPictogramOrigin").child(uid).child(ConfigurarIdioma.getLanguaje()).setValue(object.toString());
            failReadPictogramOrigin.loadDialog();
        }
    }
    public void restorePictogram0(Context mContext, FailReadPictogramOrigin failReadPictogramOrigin){
        if(ConnectionDetector.isNetworkAvailable(mContext))
        FirebaseUtils.getInstance().getmDatabase().child("backupPictogramOrigin").child(User.getInstance(mContext).getUserUid()).child(ConfigurarIdioma.getLanguaje()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JSONObject object = null;
                try {
                    String value = snapshot.getValue(String.class);
                    object = new JSONObject(snapshot.getValue(String.class));
                    failReadPictogramOrigin.setParent(object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        else{
            JSONObject aux = recoverFile(mContext);
            failReadPictogramOrigin.setParent(aux);
        }

    }

    public void saveFile(Context mContext,JSONObject object){
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput("backupPictogram0.txt", Context.MODE_PRIVATE);
            outputStream.write(object.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
           Log.e(TAG,"IOException"+ e.getMessage());
        }
    }

    public JSONObject recoverFile(Context mContext){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = null;
        File archivo = new File(mContext.getFilesDir(), "backupPictogram0.txt");
        try {
            fis = new FileInputStream(archivo);
            reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not exist : "+ e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG,"File not readen :"+ e.getMessage());
        }


        JSONObject object = null;
        try {
            object = new JSONObject(builder.toString());
        } catch (JSONException e) {
           object = createJson();
        }
        return object;
    }

}
