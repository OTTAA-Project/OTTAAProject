package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.os.Message;

import androidx.annotation.NonNull;

public class TimeToChange extends android.os.Handler {
    public static final int CHANGE_BUTTON=0;
    private final BarridoPantalla barridoPantalla;
    private final int tiempo;

    public TimeToChange(BarridoPantalla barridoPantalla,int tiempo){
        this.barridoPantalla=barridoPantalla;
        this.tiempo=tiempo;
    }

    private void removeCreatedMessages(int value){
        if(super.hasMessages(value))
            super.removeMessages(value);
    }
    private void removeAllMessages(){
        removeCreatedMessages(CHANGE_BUTTON);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case CHANGE_BUTTON:
                barridoPantalla.recorrerBarridoAutomatico();
                break;
        }
    }

    public void cambiarBoton(){
        removeAllMessages();
        if(barridoPantalla.isBarridoPantalla())
        super.sendMessageDelayed(getHandler().obtainMessage(CHANGE_BUTTON),tiempo*1000);
    }
    public android.os.Handler getHandler()
    {
        return this;
    }
}
