package com.stonefacesoft.ottaa;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Cristian on 05/06/2015.
 * Un PictoHora es un Picto pero que cambia la imagen y el texto segun la hora del dia (Ej. 8am Buenos dias 8pm Buenas Noches)
 * The class has been edited by gonzalo Juarez on  09/02/2021
 */
public class PictoHora extends Picto {

    private Drawable icono1, icono2, icono3, icono4;
    private String oracion1, oracion2, oracion3, oracion4;
    private final String TAG=this.getClass().getName();

    public PictoHora(int ID, int HoraDelDia, Drawable icono1, Drawable icono2, Drawable icono3, Drawable icono4, String oracion1,String oracion2,String oracion3, String oracion4, String nombre, int color) {
        super(ID, icono1, oracion1, nombre, color);
        this.icono1 = icono1;
        this.icono2 = icono2;
        this.icono3 = icono3;
        this.icono4 = icono4;
        this.oracion1 = oracion1;
        this.oracion2 = oracion2;
        this.oracion3 = oracion3;
        this.oracion4 = oracion4;
        Log.d(TAG,"PictoHora_PictoHora"+ "Hora = " + HoraDelDia);
        Refresh(HoraDelDia);
    }

    public void Refresh(int HoraDelDia)
    {
        if (HoraDelDia>=5 && HoraDelDia<=11) {
            setIcono(icono1);
            setOracion(oracion1);
            setNombre(oracion1);
            Log.d(TAG,"PictoHora_refresh_Hora :"+" Hora del dia= "+HoraDelDia+" "+oracion1);

        }
        else if (HoraDelDia>11 && HoraDelDia<=15) {
            setIcono(icono2);
            setOracion(oracion2);
            setNombre(oracion2);
            Log.d(TAG,"PictoHora_refresh_Hora :"+" Hora de la tarde= "+HoraDelDia+" "+oracion2);
        }
        else if (HoraDelDia>15 && HoraDelDia<20) {
            setIcono(icono3);
            setOracion(oracion3);
            setNombre(oracion3);
            Log.d(TAG,"PictoHora_refresh_Hora : "+"Hora de noche= "+HoraDelDia+" "+oracion3);
        }
        else{
            setIcono(icono4);
            setOracion(oracion4);
            setNombre(oracion4);
            Log.d(TAG,"PictoHora_refresh_Hora :"+" Hora de noche= "+HoraDelDia+" "+oracion4);
        }
    }
}
