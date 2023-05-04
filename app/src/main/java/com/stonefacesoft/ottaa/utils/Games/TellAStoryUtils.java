package com.stonefacesoft.ottaa.utils.Games;

import android.content.Context;

public class TellAStoryUtils {
    private int pictoPosition;
    private static TellAStoryUtils _TellAStoryUtils;

    private Juego game;
    public static synchronized TellAStoryUtils getInstance(){
        if(_TellAStoryUtils == null)
            _TellAStoryUtils = new TellAStoryUtils();
        return _TellAStoryUtils;
    };

    public enum FilterGroups{
        Position0(new int[]{2,17,3,12}),Position1(new int[]{6,1,16,20}),Position2(new int[]{0,24}),Position3(new int[]{4,13,21});
        private final int[] options;
        FilterGroups(int[] list){
            this.options = list;
        }

        public int[] getOptions() {
            return options;
        }


    };

    public FilterGroups getItem(){
        if(pictoPosition>FilterGroups.values().length-1)
            pictoPosition = FilterGroups.values().length-1;
        return FilterGroups.values()[pictoPosition];
    }

    public void setPictoPosition(int pictoPosition) {
        this.pictoPosition = pictoPosition;
    }

    public int getPictoPosition() {
        return pictoPosition;
    }

    public int  getChildCounts(){
        int size = 0;
        for (int i = 0; i <FilterGroups.values().length ; i++) {
            size+= FilterGroups.values()[i].getOptions().length;
        }
        return size;
    }

    public void setGame(Context context,int option){
        game = new Juego(context,3,option);
        game.startUseTime();
        game.endUseTime();
        game.saveJsonObjects();
        game.uploadFirebaseGameData();
    }
}
