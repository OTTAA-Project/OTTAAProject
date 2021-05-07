package com.stonefacesoft.ottaa.Games.Model;

public class MatchPictogramsModel extends GameModel {
    public void setCorrectValue(int index,int value){
        valueIndex[index] = index;
    }
    public boolean restartValue(){
        for (int i = 0; i <valueIndex.length ; i++) {
            if(valueIndex[i]==-1||valueIndex[i]==0)
                return false;
        }
        return true;
    }
}
