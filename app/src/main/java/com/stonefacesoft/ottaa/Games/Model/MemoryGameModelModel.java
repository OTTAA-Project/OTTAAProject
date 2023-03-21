package com.stonefacesoft.ottaa.Games.Model;

public class MemoryGameModelModel extends GameModel {
    private String selectedPictogramName;
    private int[][] matrixIdPictogram = new int[0][0];
    private int[] controlIndexSelect;
    private String[] history;


    public int[][] getMatrixIdPictogram() {
        return matrixIdPictogram;
    }


    public void refresMatrix() {
        for (int i = 0; i <matrixIdPictogram.length ; i++) {
            for (int j = 0; j <matrixIdPictogram[i].length ; j++) {
               matrixIdPictogram[i][j] = -1;
            }
        }
    }


    public void addRandomIndex() {
        for (int i = 0; i < matrixIdPictogram.length; i++) {// recorro la filas
            for (int j = 0; j < matrixIdPictogram[i].length; j++) { //recorro las columnas
                if (matrixIdPictogram[i][j] == -1) {
                    int index = elegirGanador(); // picto a seleccionar
                    if (controlIndexSelect[index] < 2) { //pregunto cuantas veces fue seleccionado el valor
                        matrixIdPictogram[i][j] = index;
                        controlIndexSelect[index]++; //le indico que ese valor fue seleccionado
                    } else {
                        addRandomIndex();
                    }

                }
            }
        }
    }

    public void resetMatrix(){
        for (int i = 0; i <controlIndexSelect.length ; i++) {
            controlIndexSelect[i] = 0;
        }
    }

    public void resetHistory(){
        for (int i = 0; i <history.length ; i++) {
            history[i] = "";
        }
    }

    @Override
    public void createArray() {
        super.createArray();
        controlIndexSelect = new int[size];
        history = new String[size];
        matrixIdPictogram = new int[2][size];
    }

    public void addHistoryValue(int index,String name){
        history[index] = name;
    }
    public boolean theViewHasBeenSelected(String text){
        for (int i = 0; i <history.length ; i++) {
            if(history[i].contentEquals(text))
                return true;
        }
        return false;
    }
}
