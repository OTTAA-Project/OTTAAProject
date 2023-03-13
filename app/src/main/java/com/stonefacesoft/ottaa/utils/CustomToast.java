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

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.AvatarPackage.AvatarUtils;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

/**
 * Created by gonzalo on 1/8/18.
 *
 */

public class CustomToast extends Application {

    //TODO animate entrance

    private static final String TAG = "CustomToast";
    LayoutInflater inflater;
    private static SharedPreferences sharedPrefsDefault;
    private static TextView tv;
    private final ImageView imageView;
    private static Context mContext;
    private static View layout;
    private Toast toast;
    private static CustomToast customToast;
    private AvatarUtils avatarUtils;
    private SetUpToast setUpToast;

    public static CustomToast getInstance(Context mContext){
        if(customToast == null)
            customToast = new CustomToast(mContext);
        else{
            customToast.prepareToast(mContext);
        }
        return customToast;
    }

    private CustomToast(Context context) {
        mContext = context;
        sharedPrefsDefault = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = inflater.inflate(R.layout.custom_toast, null);
        layout.setAlpha((float) 0.85);
        tv = layout.findViewById(R.id.text);
        this.imageView = layout.findViewById(R.id.imageViewAvatarToast);
        prepareToast(mContext);
    }

    public CustomToast prepareToast(Context mContext){
        this.toast = new Toast(mContext);
        setUpToast = new SetUpToast();
        setUpToast.setToast(toast);
        setUpToast.prepareToast();
        setUpToast.setUseUpperCase();
        updateToastMessageLetters();
        return this;
    }


    public CustomToast updateToastIcon(Context mContext){
        avatarUtils = new AvatarUtils(mContext,imageView);
        avatarUtils.getFirebaseAvatar();
        return this;
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
        setUpToast.setTexto(texto.toString()).prepareCustomSubtitle().showToast();
        //personalizarToast();
    }

    private static class SetUpToast{
        private Toast toast;
        private boolean useUpperCase;
        private int size = 20;
        private String texto;

        public void setToast(Toast toast) {
            this.toast = toast;
        }

        public SetUpToast prepareCustomSubtitle(){
            tv.setTextSize(size);
            if(useUpperCase)
                texto = texto.toUpperCase();
            tv.setText(texto);
            return this;
        }

        public SetUpToast setTexto(String texto) {
            this.texto = texto;
            return this;
        }

        public void prepareToast(){
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.setDuration(Toast.LENGTH_LONG);
            if(toast.getView()== null|| toast.getView().getId()!= layout.getId())
                toast.setView(layout);
        }

        public void showToast() {
            try {
                toast.show();
            }catch (Exception ex){
                if(ValidateContext.isValidContext(mContext))
                    customToast = new CustomToast(mContext);
            }
        }

        public void setUseUpperCase(){
            useUpperCase = sharedPrefsDefault.getBoolean(mContext.getResources().getString(R.string.bool_subtitulo_mayuscula), false);
        }
        public void updateUserSize(int size){
            this.size = size;
        }


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        toast.cancel();

    }
}
