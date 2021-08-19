package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.view.View;

import com.stonefacesoft.ottaa.Dialogos.DialogUtils.DialogUtils;
import com.stonefacesoft.ottaa.R;
/*
* @Author gonzalo Juarez
* */
public class Progress_dialog_options extends DialogUtils {
    public Progress_dialog_options(Context mContext) {
        super(mContext);
        ocultarElementos();
    }

    public Progress_dialog_options(Context mContext, String title, String message) {
        super(mContext, title, message);
       ocultarElementos();
    }

    @Override
    protected void ocultarElementos() {
        changeVisibilityView(R.id.unknow_Button, View.INVISIBLE);
        changeVisibilityView(R.id.no_button,View.INVISIBLE);
        changeVisibilityView(R.id.yes_button,View.INVISIBLE);
    }


}
