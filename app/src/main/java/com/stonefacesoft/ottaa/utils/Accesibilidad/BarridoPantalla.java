package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.Constants;

import java.util.ArrayList;
/**
 * @author Hector Costa
 * @author Gonzalo Juarez
 * @version  2.1
 * <h3>Objective</h3>
 * the screen scanning functions is show a floating button in a list of selected views
 * <h3>Implementation</h3>
 *<code>BarridoPantalla barridoPantalla=new BarridoPantalla(@param mContext,@param listadoObjectos,@param mActivity);</code>
 * <h3>How to use</h3>
 * <h4>How to get a position </h4>
 * <code>barridoPantalla.getPosicionBarrido();</code>
 * <h4>How to move to the next object </h4>
 * <code>barridoPantalla.avanzarBarrido();</code>
 *  <h4>How to move to the Previous object</h4>
 *  <code>barridoPantalla.volverAtrasBarrido();</code>
 * */
public class BarridoPantalla {
    private static final String TAG ="BarridoPantalla" ;
    private int tiempo, tiempo1;

    public void setmListadoVistas(ArrayList<View> mListadoVistas) {
        this.mListadoVistas = mListadoVistas;
    }

    private ArrayList<View> mListadoVistas;
    private int posicion=0;
    private boolean cambioPosicion;
    private final Context mContext;
    private boolean mEstaActivado;
    private boolean avanzarYAceptar;
    private boolean scrollMode;
    private boolean scrollModeClicker;
    private boolean isBarridoPantalla;
    private final boolean isSipAndPuff;

    private final SharedPreferences mDefaultSharedPreferences;

    private boolean pago;
    private final FloatingActionButton button;
    private Handler waitTime;//screenScanningRun,waitTime;
    private TimeToChange timeToChange;



    private final Runnable limpiarBarrido =new Runnable() {
        @Override
        public void run() {
            waitTime=new Handler();
            waitTime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activarBarrido();
                }
            }, tiempo * 1000);

        }
    };


    public BarridoPantalla(Context mContext, ArrayList<View> listadoObjetos, Activity mActivity) {
        this.mContext = mContext;
    //    screenScanningRun=new Handler();
        this.mListadoVistas = listadoObjetos;
        Activity activity = mActivity;
        button=activity.findViewById(R.id.floatting_button);
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEstaActivado = mDefaultSharedPreferences.getBoolean(Constants.BARRIDO_BOOL, false);
        tiempo = mDefaultSharedPreferences.getInt("velocidad_barrido", 5);
        isBarridoPantalla=mDefaultSharedPreferences.getBoolean("tipo_barrido_normal",false);
        avanzarYAceptar = mDefaultSharedPreferences.getBoolean("tipo_barrido", false);
        isSipAndPuff=mDefaultSharedPreferences.getBoolean("sip_and_puff",false);
        scrollMode=mDefaultSharedPreferences.getBoolean(this.mContext.getResources().getString(R.string.usar_scroll),false);
        scrollModeClicker =mDefaultSharedPreferences.getBoolean(this.mContext.getResources().getString(R.string.usar_scroll_click),false);
        posicion = 0;
        pago = consultarPago();
        cambiarEstadoBarrido();
        button.setSize(FloatingActionButton.SIZE_AUTO);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setAdjustViewBounds(true);
        button.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_touch_app_black_24dp));
        //   mPunteroPantalla = new PunteroEnPantalla(mContext, mActivity);
        //  cambiarMargin();


    }


    /*
     * Metodo encargado de dibujar los margenes de la vista
     * tiene que ser estatico para poder mantenerlo en la vista
     * */
    public static void setMargins(View v, int l, int t, int r, int b, int e, int s) {

        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        p.setMarginEnd(e);
        p.setMarginStart(s);
        p.setMargins(l, t, r, b);
        v.invalidate();

    }


    private void activarBarrido() {
        if (pago) {
            if (mEstaActivado) {
                if (devolverTipoBarrido()==0) {
                    recorrerBarridoAutomatico();
                } else {
                    recorrerBarridoManual();
                }
            }
        }
    }

    /*
     * Metodo encargado de cambiar la opcion del barrido de manera automatica
     * */
    public void recorrerBarridoAutomatico() {
        if(mListadoVistas.size()>0) {
            int color = mDefaultSharedPreferences.getInt("Color_Picker", Color.BLUE);
            Log.e("position", "position: " + posicion);
            posicion=devolverPosicion();
            if (posicion != -1) {
                moveCursor();
            }
            timeToChange.cambiarBoton();
        }
    }


    /*
     * Metodo encargado de cambiar la opcion de manera manual
     * */
    private void recorrerBarridoManual() {
        moveCursor();
      //  mListadoVistas.get(posicion).setBackgroundColor(color);

    }

    public boolean isBarridoPantalla() {
        return isBarridoPantalla;
    }

    public boolean isBarridoActivado() {
        return mEstaActivado;
    }

    public boolean isScrollMode(){
        return scrollMode;
    }

    public boolean isScrollModeClicker() {
        return scrollModeClicker;
    }


    public void setmEstaActivado(boolean mEstaActivado) {
        this.mEstaActivado = mEstaActivado;
    }

    public boolean isAvanzarYAceptar() {
        return avanzarYAceptar;
    }

    public void setAvanzarYAceptar(boolean avanzarYAceptar) {
        this.avanzarYAceptar = avanzarYAceptar;
    }

    public int getPosicionBarrido() {
        return posicion;
    }

    public void activarDesactivarBarrido() {
        posicion = 0;
        pago = consultarPago();
        if (consultarPago())
            activarBarrido();
        else {
            //limpiarBarrido();
            if(button.getVisibility()==View.VISIBLE)
            changeButtonVisibility();

        }

    }
    public boolean devolverpago(){
        return pago;
    }

    public void avanzarBarrido() {
        devolverPosicion(posicion,true);
        recorrerBarridoManual();

    }

    public void volverAtrasBarrido() {
        devolverPosicion(posicion,false);
        recorrerBarridoManual();
    }


    private boolean consultarPago() {
        return mDefaultSharedPreferences.getInt("premium", 0) == 1;
    }



    public ArrayList<View> getmListadoVistas() {
        return mListadoVistas;
    }


    public void cambiarEstadoBarrido() {
        mEstaActivado = mDefaultSharedPreferences.getBoolean(Constants.BARRIDO_BOOL, false);
        tiempo = mDefaultSharedPreferences.getInt("velocidad_barrido", 5);
        isBarridoPantalla=mDefaultSharedPreferences.getBoolean("tipo_barrido_normal",false);
        avanzarYAceptar = mDefaultSharedPreferences.getBoolean("tipo_barrido", false);
        scrollMode=mDefaultSharedPreferences.getBoolean(this.mContext.getResources().getString(R.string.usar_scroll),false);
        scrollModeClicker =mDefaultSharedPreferences.getBoolean(this.mContext.getResources().getString(R.string.usar_scroll_click),false);

        pago = consultarPago();

        // cambiarMargin();
      //  limpiarBarrido();
        posicion = 0;
        timeToChange=new TimeToChange(this,tiempo);
        activarDesactivarBarrido();

    }

    /*
     * Use this method to return the position of object that&acutes is visible
     * */
    private int devolverPosicion() {
        try{
            posicion++;
            if (posicion>mListadoVistas.size())
                posicion = 0;
            else if (posicion < 0)
                posicion = mListadoVistas.size() ;
            if (mListadoVistas.get(posicion).getVisibility() == View.VISIBLE) {
                return posicion;
            } else{
               devolverPosicion();
            }
        }catch (Exception ex){
            return 0;
        }
        return posicion;
    }

    private void devolverPosicion(int pos,boolean add) {

            if(add)
                pos++;
            else
                pos--;

            if (pos>=mListadoVistas.size())
                pos = 0;
            else if (pos < 0)
                pos = mListadoVistas.size() -1;
            posicion=pos;
            if (mListadoVistas.get(pos).getVisibility() != View.VISIBLE) {
                devolverPosicion(pos,add);
            }
    }




    public void addView(View v){
        mListadoVistas.add(v);
    }
    public void removieView(View v){
        mListadoVistas.remove(v);
    }

    /**
     * drag the floating button to an specific button
     * */
    private void moveCursor(){
        if(button.getVisibility()==View.INVISIBLE||button.getVisibility()==View.GONE)
       changeButtonVisibility();

        if(posicion!=-1) {
                getExactCenterPoint(mListadoVistas.get(posicion));
            }
//
    }

    public void changeButtonVisibility(){
        switch (button.getVisibility()){
            case View.GONE:
                button.setVisibility(View.VISIBLE);
                break;
            case View.INVISIBLE:
                button.setVisibility(View.VISIBLE);
                break;
            case View.VISIBLE  :
                button.setVisibility(View.GONE);
                break;
        }
    }
/**
 * return the visibility of the floating button
 * */
    public boolean isButtonVisible(){
        return button.getVisibility()==View.VISIBLE;
    }
    /**
     * Set the position of the floating button
     * @param to item to drag the floating button
     * */
    private void getExactCenterPoint(View to) {
        Rect rect=new Rect();
        to.getGlobalVisibleRect(rect);
        button.setX(rect.exactCenterX()-button.getWidth()/2);
        button.setY(rect.exactCenterY()-button.getHeight()/2);
    }

    /**
     * @return the kind of screen scanning
     * */
    private int devolverTipoBarrido(){
        if(isAvanzarYAceptar())
            return 1;
        else if(isScrollMode())
            return 2;
        else if(isScrollModeClicker())
            return 3;
        else if(isSipAndPuff())
            return 4;
        return 0;
    }

    public boolean isSipAndPuff() {
        return isSipAndPuff;
    }

    public FloatingActionButton getButton() {
        return button;
    }


}
