package com.stonefacesoft.ottaa.utils.Games;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

//test merge rejected
public class CalculaPuntos {

    private final String TAG = "CalculaPuntos";

    private int aciertos = 0;
    private int desaciertos = 0;
    private int total;
    private float score = 0;


    public CalculaPuntos() {

    }

    public void sumarCantidadVecesCorrectas(){
        this.aciertos++;
    }

    public void sumarCantidVecesIncorretas(){
        this.desaciertos++;
    }

    private int calcularTotal(){
        return total= aciertos + desaciertos;
    }

    public float calcularValor(){
        score=0;
        if(calcularTotal()==0) {
            return 0;
        }
        else
            score=(aciertos/(float) calcularTotal()*100);
        return score;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public void setDesaciertos(int desaciertos) {
        Log.d(TAG, "setDesaciertos: " + desaciertos);
        this.desaciertos = desaciertos;
    }

    public void setIntentos(int total) {
        this.total = total;
    }
    public int getIntentos(){
        calcularTotal();
        return total;
    }

    public float getScore() {
        return score;
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getDesaciertos() {
        return desaciertos;
    }

    public JSONObject getPuntaje(){
        JSONObject object=new JSONObject();
        try {
            object.put("aciertos",aciertos);
            object.put("desaciertos",desaciertos);
            object.put("score",calcularValor());
            object.put("intentos",getIntentos());
        } catch (JSONException e) {
            Log.e(TAG, "getPuntaje: " + e.getMessage());
        }
        return object;
    }

}
