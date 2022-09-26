package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonefacesoft.ottaa.Adapters.ScoreListItem;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Games.Juego;

import androidx.recyclerview.widget.RecyclerView;

public class DialogGameProgressInform extends DialogUtils {
    private RecyclerView mRecyclerView;
    private TextView Title,title_correct,title_wrong,title_cantUso,Title_racha,Correct,Wrong,cant_uso,racha;
    private ImageView imageView;
    private final Context mContext;
    private ScoreListItem scoreListItem;
    private final Juego game;
     //NOTA recordar que el layout de este dialogo es R.layout.
    public DialogGameProgressInform(Context mContext, int layout, Juego game) {
        super(mContext, layout);
        this.mContext=mContext;
        this.game=game;
    }


    public DialogGameProgressInform cargarDatosJuego(){
        prepareDataDialog();
        dialog.setCancelable(true);
        return this;
    }

    @Override
    public void prepareDataDialog() {
        Title=dialog.findViewById(R.id.dialog_title);
        title_correct=dialog.findViewById(R.id.text_descripcion);
        title_wrong=dialog.findViewById(R.id.title_wrong);
        title_cantUso=dialog.findViewById(R.id.cant_uso);
        Title_racha=dialog.findViewById(R.id.Racha_maxima);
        Correct=dialog.findViewById(R.id.value_correct);
        Wrong=dialog.findViewById(R.id.wrong_value);
        cant_uso=dialog.findViewById(R.id.cantidad);
        racha=dialog.findViewById(R.id.racha_max);
        Correct.setText(game.getScoreClass().getAciertos()+"");
        Wrong.setText(game.getScoreClass().getDesaciertos()+"");
        cant_uso.setText(game.getChronometer().getTimeInMinutes()+"");
        racha.setText(game.getBestStreak()+"");
        imageView=dialog.findViewById(R.id.status_face);
        imageView.setImageDrawable(game.getSmiley());
    }

    public void showDialog(){
        if(dialog!=null||!dialog.isShowing())
            dialog.show();
    }
    public void dismissDialog(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }
}
