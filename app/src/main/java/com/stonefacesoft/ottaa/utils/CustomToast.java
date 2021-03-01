package com.stonefacesoft.ottaa.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;

/**
 * Created by gonzalo on 1/8/18.
 */

public class CustomToast extends Application {

    private static final String TAG = "CustomToast";
    LayoutInflater inflater;
    private final SharedPreferences sharedPrefsDefault;
    private final TextView tv;
    private final Context mContext;
    private final View layout;
    private final Toast toast;

    public CustomToast(Context context) {

        //Retrieve the layout inflator
        this.mContext = context;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());

        //Assign the custom layout to view
        //Parameter 1 - Custom layout XML
        //Parameter 2 - Custom layout ID present in linearlayout tag of XML


        LayoutInflater inflater = LayoutInflater.from(mContext.getApplicationContext());

        View layout1 = inflater.inflate(R.layout.custom_toast,null,true);
        this.layout = inflater.inflate(R.layout.custom_toast, layout1.findViewById(R.id.toast_layout_root));

        this.layout.setAlpha((float) 0.85);
        this.tv = layout.findViewById(R.id.text);
        this.toast = new Toast(mContext);
    }

    public void mostrarFrase(CharSequence texto, AnalyticsFirebase mTracker) {
        setMayus(texto);
        personalizarToast();
        mTracker.customEvents("Talk","Principal","Created Phrase");
    }


    public void mostrarFrase(CharSequence texto) {
        setMayus(texto);
        personalizarToast();
    }

    private void personalizarToast() {

        if (sharedPrefsDefault.getBoolean(mContext.getResources().getString(R.string.bool_subtitulo), false)) {
            try {
                this.tv.setTextSize(sharedPrefsDefault.getInt(this.mContext.getResources().getString(R.string.str_subtitulo_tamano), 25));
            } catch (Exception ex) {
                this.tv.setTextSize(Integer.parseInt(sharedPrefsDefault.getString(this.mContext.getResources().getString(R.string.str_subtitulo_tamano), "25")));
            }
        } else {
            this.tv.setTextSize(20);
        }

        //Set custom_toast gravity to bottom
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        //Set custom_toast duration
        toast.setDuration(Toast.LENGTH_SHORT);
        //Set the custom layout to Toast
        toast.setView(layout);
        //Display custom_toast
        toast.show();
    }

    private void setMayus(CharSequence Testo) {
        if (sharedPrefsDefault.getBoolean(this.mContext.getResources().getString(R.string.bool_subtitulo_mayuscula), false))
            Testo = Testo.toString().toUpperCase();
        this.tv.setText(Testo);
    }
}
