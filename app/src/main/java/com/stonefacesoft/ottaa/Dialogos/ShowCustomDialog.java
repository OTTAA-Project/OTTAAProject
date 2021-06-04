package com.stonefacesoft.ottaa.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

public class ShowCustomDialog {
    private final Context mContext;
    private final SharedPreferences sharedPreferences;

    public ShowCustomDialog(Context context) {
        this.mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public void showCustomDialog(boolean isCancelable, String titulo, String descripcion, int imageResource) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_new_features);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(isCancelable);
        TextView mTxtViewTitulo = dialog.findViewById(R.id.titulo);
        mTxtViewTitulo.setText(titulo);
        TextView mTxtViewDescripcion = dialog.findViewById(R.id.text_descripcion);
        mTxtViewDescripcion.setText(descripcion);
        ImageView mImgViewDialog = dialog.findViewById(R.id.flecha_cambio);
        mImgViewDialog.setImageResource(imageResource);
        Button mBtnDialog = dialog.findViewById(R.id.btn_dialog);
        mBtnDialog.setOnClickListener(v -> {

            dialog.dismiss();
        });
        dialog.show();


    }


    private void animarVista(View v, int x) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.animate().setStartDelay(100);
//                v.animate().rotationYBy(x * 180);
                v.animate().rotationBy(x * 45).setDuration(100);
                v.animate().rotationBy(-x * 45).setDuration(100);
                v.animate().setDuration(1500);
                v.animate().alpha(1f);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (x == 1)
                            animarVista(v, -1);
                        else
                            animarVista(v, 1);
                    }
                }, 1600);
            }
        }, 100);


    }

    private Rect getRect(View view) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        Rect rect = new Rect(l[0], l[1], l[0] + view.getWidth(), l[1] + view.getHeight());
        return rect;
    }

}
