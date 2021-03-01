package com.stonefacesoft.ottaa.Dialogos;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

public class dialogo_reglas_juego extends DialogUtils {
    private TextView title,description;
    private Button btn1,btn2;
    private int level=0;


    public dialogo_reglas_juego(Context context) {
        super(context, R.layout.rulesgame);
        prepareDataDialog();
        dialog.show();

    }
    @Override
    public void prepareDataDialog() {
        dialog.setCancelable(true);
    }

}
