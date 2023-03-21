package com.stonefacesoft.ottaa.Games.Model;

import java.util.Random;

public class GameModel {
    protected String name;
    protected int level;
    protected int accuracy;
    protected int[] valueIndex = new int[]{};
    protected int size;
    private Random random;

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
        if(random == null)
            createRandom();
        return  random.nextInt(size);
    }

    public void createRandom(){
        random = new Random();

    }

    public int getSize() {
        return size;
    }
    public boolean reiniciarJuego(int value){
        return value==size;
    }
}
