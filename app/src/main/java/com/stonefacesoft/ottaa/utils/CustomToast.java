package com.stonefacesoft.ottaa.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.AvatarPackage.AvatarUtils;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;

/**
 * Created by gonzalo on 1/8/18.
 */

public class CustomToast extends Application {

    //TODO animate entrance

    private static final String TAG = "CustomToast";
    LayoutInflater inflater;
    private final SharedPreferences sharedPrefsDefault;
    private final TextView tv;
    private final ImageView imageView;
    private final Context mContext;
    private final View layout;
    private final Toast toast;
    private static CustomToast customToast;
    private AvatarUtils avatarUtils;
    private SetUpToast setUpToast;

    public static CustomToast getInstance(Context mContext){
        if(customToast == null)
            customToast = new CustomToast(mContext);
        return customToast;
    }

    private CustomToast(Context context) {
        this.mContext = context;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        this.layout = inflater.inflate(R.layout.custom_toast, null);
        this.layout.setAlpha((float) 0.85);
        this.tv = layout.findViewById(R.id.text);
        this.imageView = layout.findViewById(R.id.imageViewAvatarToast);
        this.toast = new Toast(mContext);
        setUpToast = new SetUpToast();
        setUpToast.setToast(toast);
        setUpToast.prepareToast();
        setUpToast.setUseUpperCase();
        updateToastMessageLetters();
    }

    public void updateToastIcon(){
        avatarUtils = new AvatarUtils(mContext,imageView,FirebaseAuth.getInstance());
        avatarUtils.getFirebaseAvatar();
    }

    public void updateToastMessageLetters(){
        setUpToast.setUseUpperCase();
        if (sharedPrefsDefault.getBoolean(mContext.getResources().getString(R.string.bool_subtitulo), false)) {
            try {
                setUpToast.updateUserSize(sharedPrefsDefault.getInt(mContext.getResources().getString(R.string.str_subtitulo_tamano), 25));
            } catch (Exception ex) {
                setUpToast.updateUserSize(Integer.parseInt(sharedPrefsDefault.getString(mContext.getResources().getString(R.string.str_subtitulo_tamano), "25")));
            }
        } else {
            setUpToast.updateUserSize(20);
        }
    }






    public void mostrarFrase(CharSequence texto) {
        setUpToast.setMayus(texto).prepareCustomSubtitle().getToast().show();
        //personalizarToast();
    }

    private void setMayus(CharSequence Testo) {
        if (sharedPrefsDefault.getBoolean(this.mContext.getResources().getString(R.string.bool_subtitulo_mayuscula), false))
            Testo = Testo.toString().toUpperCase();
        this.tv.setText(Testo);
    }

    private class SetUpToast{
        private Toast toast;
        private boolean useUpperCase;
        private int size = 20;

        public void setToast(Toast toast) {
            this.toast = toast;
        }

        public SetUpToast prepareCustomSubtitle(){
            tv.setTextSize(size);
            return this;
        }

        public SetUpToast setMayus(CharSequence Testo) {
            if (useUpperCase)
                Testo = Testo.toString().toUpperCase();
            tv.setText(Testo);
            return this;
        }

        public void prepareToast(){
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            //Set custom_toast duration
            toast.setDuration(Toast.LENGTH_LONG);
            //Set the custom layout to Toast
            toast.setView(layout);
        }

        public Toast getToast() {
            return toast;
        }

        public void setUseUpperCase(){
            useUpperCase = sharedPrefsDefault.getBoolean(mContext.getResources().getString(R.string.bool_subtitulo_mayuscula), false);
        }
        public void updateUserSize(int size){
            this.size = size;
        }

    }


}
