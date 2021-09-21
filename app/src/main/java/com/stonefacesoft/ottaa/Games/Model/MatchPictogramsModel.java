package com.stonefacesoft.ottaa.Games.Model;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class MatchPictogramsModel extends GameModel {
    private int value;
    public void setCorrectValue(int index,int value){
        valueIndex[index] = value;
    }
    public boolean restartValue(){
        for (int i = 0; i <valueIndex.length ; i++) {
            if(valueIndex[i]==-1||valueIndex[i]==0)
                return false;
        }
        return true;
    }



    public void selectRandomPictogram(ArrayList numbers){
        while (numbers.size()<size){
            value = elegirGanador();
            for (int i = 0; i <numbers.size() ; i++) {
                if((int)numbers.get(i)==value)
                    selectRandomPictogram(numbers);
            }
             numbers.add(value);
            Log.d("MatchPictogramMod", "selectRandomPictogram: "+value);
            selectRandomPictogram(numbers);
        }
    }

    public void selectRandomChild(ArrayList number, JSONArray object){

    }


}
