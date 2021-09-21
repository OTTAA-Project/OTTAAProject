package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.view.View;

import com.stonefacesoft.ottaa.R;

public class Yes_noDialogs extends DialogUtils {


    public Yes_noDialogs(Context mContext) {
        super(mContext);
        ocultarElementos();

    }

    public Yes_noDialogs(Context mContext, String title, String message) {
        super(mContext, title, message);
        ocultarElementos();
    }

    @Override
    protected void ocultarElementos() {
        changeVisibilityView(R.id.progressBar2,View.GONE);
        changeVisibilityView(R.id.unknow_Button,View.GONE);
    }

}
