package com.stonefacesoft.ottaa.utils.Accesibilidad.scrollActions;

import android.os.Message;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.Games.GameSelector;
import com.stonefacesoft.ottaa.Games.TellAStory;
import com.stonefacesoft.ottaa.Views.MatchPictograms;
import com.stonefacesoft.ottaa.Games.WhichIsThePicto;
import com.stonefacesoft.ottaa.MainJuegos;
import com.stonefacesoft.ottaa.Views.Games.GameViewSelectPictograms;

public class ScrollFuntionGames extends ScrollFunction{
    private MainJuegos mainJuegos;
    private WhichIsThePicto whichIsThePicto ;

    private TellAStory tellAStory;
    private MatchPictograms matchPictograms;
    private GameSelector gameSelector;
    private GameViewSelectPictograms gameViewSelectPictograms;


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
    public ScrollFuntionGames(GameViewSelectPictograms mainJuegos) {
        super(mainJuegos);
        this.gameViewSelectPictograms=mainJuegos;
    }

    public ScrollFuntionGames(TellAStory mainJuegos) {
        super(mainJuegos);
        this.tellAStory=mainJuegos;
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
        else if(gameViewSelectPictograms!=null)
            gameViewSelectPictograms.OnClickBarrido();
        else if(tellAStory!=null)
            tellAStory.OnClickBarrido();
    }
}
