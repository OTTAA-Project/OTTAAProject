package com.stonefacesoft.ottaa.utils;

import com.stonefacesoft.ottaa.utils.constants.Constants;

public class PictogramPositionCounter {
    private int posChild;
    private  int limit= 5;

    private boolean useLimit;

    private static PictogramPositionCounter _pictogramPositionCounter;
    public static PictogramPositionCounter getInstance(){
        if(_pictogramPositionCounter==null)
            _pictogramPositionCounter = new PictogramPositionCounter();
        return _pictogramPositionCounter;
    }

    public void incrementPosition(){
        posChild++;
    }

    public void decrementPosition(){
        posChild--;
    }

    public void resetPosition(){
        posChild = 0;
    }

    public void setlessone(){
        posChild = -1;
    }

    public int getPosChild() {
        return posChild;
    }

    public void setLimit(int value){
        if(!useLimit)
            this.limit =  value;
        else
            this.limit = Constants.VUELTAS_CARRETE;
    }

    public int getLimit() {
        return limit;
    }
}
