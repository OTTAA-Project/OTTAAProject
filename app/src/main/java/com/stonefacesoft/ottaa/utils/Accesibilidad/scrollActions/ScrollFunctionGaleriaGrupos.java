package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import com.stonefacesoft.ottaa.GaleriaGrupos2;
import com.stonefacesoft.ottaa.Views.GroupGalleryNavigator;

import androidx.annotation.NonNull;

public class ScrollFunctionGaleriaGrupos extends ScrollFunction {
    private final GroupGalleryNavigator navigator;
    public ScrollFunctionGaleriaGrupos(Context mContext,GroupGalleryNavigator navigator) {
        super(mContext);
        this.navigator= navigator;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case HACER_CLICK:
                navigator.OnClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
