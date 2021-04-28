package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.view.MotionEvent;

import com.stonefacesoft.ottaa.Principal;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AccessibillityDevicesConfig  extends Handler {
    private final MotionEvent mouseEvent;
    private Principal principal;

    public AccessibillityDevicesConfig(MotionEvent motionEvent,Principal principal){
        this.mouseEvent=motionEvent;
    }
    public void OTTAADevicesV1(){

    }
    public  void OTTAADevicesV2(){

    }

    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
