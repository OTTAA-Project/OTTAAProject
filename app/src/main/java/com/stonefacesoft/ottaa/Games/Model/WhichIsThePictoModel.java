package com.stonefacesoft.ottaa.Games.Model;

public class WhichIsThePictoModel extends GameModel {
    private String winnerPictogramName;
    private int pictogramId;


    public void selectRandomPictogram(){

    }
    public int elegirGanador(){
        return (int) Math.floor(Math.random()*size+0);
    }

    public String getWinnerPictogramName(){
        return winnerPictogramName;
    }

    public boolean isTheWinnerPictogram(String name){
        return winnerPictogramName.equalsIgnoreCase(name);
    }

    public void loadValue(int index,int value){
        valueIndex[index] = value;
    }

    public int getValue(int index){
        return valueIndex[index];
    }

    public boolean isTheValue(int index,int value){
        return valueIndex[index] == value;
    }

    public boolean hasTheValue(int value){
        for (int i = 0; i <valueIndex.length ; i++) {
            if(valueIndex[i] == value)
                return true;
        }
        return false;
    }

    public boolean validatePosition(int index){
        return index<valueIndex.length;
    }





}
