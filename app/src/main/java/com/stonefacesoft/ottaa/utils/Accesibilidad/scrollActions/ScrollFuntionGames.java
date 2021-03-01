package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.Games.GameSelector;
import com.stonefacesoft.ottaa.Games.MatchPictograms;
import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.MainJuegos;

public class ScrollFuntionGames extends ScrollFunction{
    private MainJuegos mainJuegos;
    private WhichIsThePicto whichIsThePicto ;
    private MatchPictograms matchPictograms;
    private GameSelector gameSelector;


    public ScrollFuntionGames(MainJuegos mainJuegos) {
        super(mainJuegos);
        this.mainJuegos=mainJuegos;
    }
    public ScrollFuntionGames(GameSelector mainJuegos) {
        super(mainJuegos);
        this.gameSelector=mainJuegos;
    }
    public ScrollFuntionGames(WhichIsThePicto mainJuegos) {
        super(mainJuegos);
        this.whichIsThePicto=mainJuegos;
    }
    public ScrollFuntionGames(MatchPictograms mainJuegos) {
        super(mainJuegos);
        this.matchPictograms=mainJuegos;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case HACER_CLICK:
                    makeClickBarrido();
                break;
            default:
                super.handleMessage(msg);
        }
    }

    public void makeClickBarrido(){
        if(mainJuegos!=null)
            mainJuegos.OnClickBarrido();
        else if(gameSelector!=null)
            gameSelector.OnClickBarrido();
        else if(whichIsThePicto!=null)
            whichIsThePicto.OnClickBarrido();
        else if(matchPictograms!=null)
            matchPictograms.OnClickBarrido();
    }
}
