package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stonefacesoft.ottaa.R;

public class Dialog_options_level_game extends DialogUtils {
    private TextView title,description;
    private Button btn1,btn2;
    private int level=0;


    public Dialog_options_level_game(Context context) {
        super(context,R.layout.game_option);
        prepareDataDialog();
        dialog.show();

    }

    @Override
    public void prepareDataDialog() {
        title=dialog.findViewById(R.id.dialog_title);
        description=dialog.findViewById(R.id.text_descripcion);
        btn1=dialog.findViewById(R.id.action_1);
        btn2=dialog.findViewById(R.id.action_2);
        this.title.setText("Nivel de dificultad");
        this.description.setText("Por favor seleccione el nivel de dificultad del juego");
        this.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=0;
                dialog.dismiss();
            }
        });
        this.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level=1;
                dialog.dismiss();
            }
        });
    }

    public int getLevel() {
        return level;
    }
}
