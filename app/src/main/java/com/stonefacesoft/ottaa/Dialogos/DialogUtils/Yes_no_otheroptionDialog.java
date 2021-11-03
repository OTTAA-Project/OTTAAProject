package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.view.View;

import com.stonefacesoft.ottaa.R;

public class Yes_no_otheroptionDialog extends DialogUtils {

    public Yes_no_otheroptionDialog(Context mContext, String title, String message) {
        super(mContext, title, message);
        ocultarElementos();

    }

    public Yes_no_otheroptionDialog(Context mContext) {
        super(mContext);
        ocultarElementos();
    }


    @Override
    protected void ocultarElementos() {
        changeVisibilityView(R.id.progressBar2, View.GONE);
    }


}
