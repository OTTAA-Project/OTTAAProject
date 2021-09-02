package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import com.stonefacesoft.ottaa.GaleriaPictos3;

import androidx.annotation.NonNull;

public class ScrollFunctionGaleriaPictos extends ScrollFunction{
    private final GaleriaPictos3 galeriaPictos3;
    public ScrollFunctionGaleriaPictos(Context mContext,GaleriaPictos3 galeriaGrupos2) {
        super(mContext);
        this.galeriaPictos3=galeriaGrupos2;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case HACER_CLICK:
                galeriaPictos3.OnClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
