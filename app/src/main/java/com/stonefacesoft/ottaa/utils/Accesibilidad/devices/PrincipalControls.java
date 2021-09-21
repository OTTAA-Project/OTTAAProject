package com.stonefacesoft.ottaa.utils.Accesibilidad.devices;

import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;

import com.stonefacesoft.ottaa.Principal;

import androidx.core.view.MotionEventCompat;

public class PrincipalControls extends Controls {
    private final Principal principal;
    private final String TAG="PrincipalControls";
    public PrincipalControls(Principal principal) {
        super(principal);
        this.principal=principal;
    }

    @Override
    public boolean makeClick(MotionEvent event) {
        Log.e(TAG, "makeClick: "+event.getAction() );

        if (event.getSource() == InputDevice.SOURCE_MOUSE&&(principal.getBarridoPantalla().isAvanzarYAceptar()||principal.getBarridoPantalla().isSipAndPuff())) {
            switch (event.getButtonState()) {
                case MotionEvent.BUTTON_PRIMARY:
                    Log.e(TAG, "onTouchEvent: Primary Button");
                    makePrimaryClick(event);
                    return true;
                case MotionEvent.BUTTON_SECONDARY:
                    Log.e(TAG, "onTouchEvent: Secundary Button");
                    makeSecondaryClick(event);
                    return true;
                case MotionEvent.BUTTON_TERTIARY:
                    Log.e(TAG, "onTouchEvent: Tertiary Button");
                    makeTertiaryClick(event);
                    return true;
                case MotionEvent.AXIS_LTRIGGER:
                        principal.getBarridoPantalla().volverAtrasBarrido();
                    return true;
                case MotionEvent.AXIS_RTRIGGER:
                    principal.getBarridoPantalla().avanzarBarrido();
                case MotionEvent.AXIS_HAT_Y :
                    return true;


            }

        }else if(event.getSource()==InputDevice.SOURCE_MOUSE&&principal.getBarridoPantalla().isScrollModeClicker()){
            switch (event.getButtonState()){
                case  MotionEvent.BUTTON_PRIMARY:
                    principal.OnClickBarrido();
                    return true;
            }
        }else{
            final int action = MotionEventCompat.getActionMasked(event);
            switch (action){
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        }

        return false;
    }

    @Override
    public boolean makePrimaryClick(MotionEvent event) {
        principal.getBarridoPantalla().avanzarBarrido();
        switch (preferences.getInt("deviceId",0)){
            case 0:
                if(principal.getBarridoPantalla().isSipAndPuff())
                    principal.getFunction_scroll().HacerClickEnTiempo();
                break;
        }
        return true;
    }

    @Override
    public boolean makeSecondaryClick(MotionEvent event) {
        switch (preferences.getInt("deviceId",0)){
            case 0:
                principal.getBarridoPantalla().volverAtrasBarrido();
                if(principal.getBarridoPantalla().isSipAndPuff())
                    principal.getFunction_scroll().HacerClickEnTiempo();
                break;
            case 1:
                principal.onClick(principal.getBarridoPantalla().getmListadoVistas().get(principal.getBarridoPantalla().getPosicionBarrido()));
                break;
        }
        return true;
    }

    @Override
    public boolean makeTertiaryClick(MotionEvent event) {
        principal.onClick(principal.getBarridoPantalla().getmListadoVistas().get(principal.getBarridoPantalla().getPosicionBarrido()));
        return true;
    }

    @Override
    public boolean pressBackButton() {

        principal.getBarridoPantalla().getmListadoVistas().get(principal.getBarridoPantalla().getPosicionBarrido()).callOnClick();
        return true;
    }
}
