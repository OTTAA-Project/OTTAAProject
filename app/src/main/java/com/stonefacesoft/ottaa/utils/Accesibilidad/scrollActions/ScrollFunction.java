package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Accesibilidad.BarridoPantalla;


public class ScrollFunction  extends android.os.Handler {
    private int cantMovimientos;
    private Activity mActivity;
    private MotionEvent event;
    private BarridoPantalla barridoPantalla;
    private final int position=0;
    private boolean clickEnabled;
    private long tiempo;
    private final Context mContext;
    private final SharedPreferences mDefaultSharedPreferences;
    private GaleriaGrupos2 galeriaGrupos2;
    public static  final int HACER_CLICK=0;
    public static final int SLEEP=2;
    public static final int WAKE_UP=1;



    public ScrollFunction(Context mContext){
        this.mContext=mContext;
        mDefaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        wakeUp();
    }

    public void HacerClickEnTiempo(){
        tiempo=500*mDefaultSharedPreferences.getInt(mContext.getResources().getString(R.string.scroll_speed),5);
         hacerClick();
    }



    public void descansar(){
        removeAllMessages();
        super.sendMessageDelayed(getHandler().obtainMessage(SLEEP),tiempo);
    }
    public void wakeUp(){
        removeAllMessages();
        super.sendMessageDelayed(getHandler().obtainMessage(WAKE_UP),tiempo);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

    }

    public void removeClickBarrido(){
        removeAllMessages();
    }

    public  int getPosition(){
        return position;
    }
    private void hacerClick(){
        removeAllMessages();
        super.sendMessageDelayed(getHandler().obtainMessage(HACER_CLICK),tiempo);
    }
    public android.os.Handler getHandler()
    {
        return this;
    }

    private void removeCreatedMessages(int value){
        if(super.hasMessages(value))
        super.removeMessages(value);
    }
    private void removeAllMessages(){
        removeCreatedMessages(HACER_CLICK);
    }

    public boolean isClickEnabled() {
        return this.clickEnabled;
    }
}
