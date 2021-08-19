package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

public class Devices_Version_Dialog extends DialogUtils {
    private ImageView w1,w2,w3;
    private TextView tv1,tv2,tv3;
    public Devices_Version_Dialog(Context mContext,boolean isV2) {
        super(mContext, R.layout.dialog_devices_version);
        if(isV2)
            initComponents();
        dialog.show();
        dialog.setCancelable(true);
    }
    public void initComponents(){
        w1=dialog.findViewById(R.id.w1);
        w2=dialog.findViewById(R.id.w2);
        w3=dialog.findViewById(R.id.w3);
        tv1=dialog.findViewById(R.id.text1);
        tv2=dialog.findViewById(R.id.text2);
        tv3=dialog.findViewById(R.id.text3);

        w1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.press20));
        w2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.worm1));
        w3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sippandpuff));
        tv1.setText(mContext.getResources().getText(R.string.str_avanzar_aceptar));
        tv2.setText("Worms");
        tv3.setText("Sip & puff");
    }

}
