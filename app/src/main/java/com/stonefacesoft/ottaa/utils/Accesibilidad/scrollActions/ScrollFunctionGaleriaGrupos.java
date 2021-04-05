package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.GaleriaGrupos2;

public class ScrollFunctionGaleriaGrupos extends ScrollFunction {
    private final GaleriaGrupos2 galeriaGrupos2;

    public ScrollFunctionGaleriaGrupos(Context mContext, GaleriaGrupos2 galeriaGrupos2) {
        super(mContext);
        this.galeriaGrupos2 = galeriaGrupos2;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case HACER_CLICK:
                galeriaGrupos2.OnClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
