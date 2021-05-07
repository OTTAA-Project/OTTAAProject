package com.stonefacesoft.ottaa.Games.Model;

public class GameModel {
    protected String name;
    protected int level;
    protected int accuracy;
    protected int valueIndex[] = new int[]{};
    protected int size;

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean changeLevelGame(int value,int maxValue){
        return accuracy > maxValue;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void createArray(){
        valueIndex = new int[size];
        refreshValueIndex();
    }

    public void refreshValueIndex(){
        for (int i = 0; i <valueIndex.length ; i++) {
            valueIndex[i] = -1;
        }
    }

    public int[] getValueIndex() {
        return valueIndex;
    }

    public void loadValue(){

    }

    public int elegirGanador(){
        return (int) Math.floor(Math.random()*size+0);
    }

}
