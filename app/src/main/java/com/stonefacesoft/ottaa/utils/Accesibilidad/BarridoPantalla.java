package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.app.Activity;
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
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.ReturnPositionItem;

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
   // private int posicion=0;
    private Activity mActivity;


    private SharedPreferences mDefaultSharedPreferences;

    private boolean pago;
    private FloatingActionButton button;
    private Handler waitTime;//screenScanningRun,waitTime;
    private TimeToChange timeToChange;
    private ReturnPositionItem returnPositionItem;
    private final ScreenScanningSettings settings;



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

    public BarridoPantalla(){
        settings = new ScreenScanningSettings();
    }


    public BarridoPantalla(Activity mActivity, ArrayList<View> listadoObjetos) {
        this.mActivity = mActivity;
    //    screenScanningRun=new Handler();
        this.mListadoVistas = listadoObjetos;
        button=mActivity.findViewById(R.id.floatting_button);
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        settings = new ScreenScanningSettings();
        tiempo = mDefaultSharedPreferences.getInt("velocidad_barrido", 5);
        enableDisableScreenScanning(mDefaultSharedPreferences.getBoolean(Constants.BARRIDO_BOOL, false),mDefaultSharedPreferences.getBoolean("tipo_barrido_normal",false),mDefaultSharedPreferences.getBoolean("tipo_barrido", false),mDefaultSharedPreferences.getBoolean("sip_and_puff",false),mDefaultSharedPreferences.getBoolean(this.mActivity.getResources().getString(R.string.usar_scroll),false),mDefaultSharedPreferences.getBoolean(this.mActivity.getResources().getString(R.string.usar_scroll_click),false));
        returnPositionItem = new ReturnPositionItem(listadoObjetos.size());
        pago = consultarPago();
        cambiarEstadoBarrido();
        button.setSize(FloatingActionButton.SIZE_AUTO);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setAdjustViewBounds(true);
        button.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_touch_app_black_24dp));
        //   mPunteroPantalla = new PunteroEnPantalla(mContext, mActivity);
        //  cambiarMargin();


    }

    public void enableDisableScreenScanning(boolean isEnabled,boolean isBarrido,boolean isAvanzarYAceptar,boolean isSipAndPuff,boolean isScroll,boolean setScrollWithClick){
        settings.setEnabled(isEnabled);
        settings.setBarrido(isBarrido);
        settings.setAvanzarYAceptar(isAvanzarYAceptar);
        settings.setSipAndPuff(isSipAndPuff);
        settings.setScroll(isScroll);
        settings.setScrollWithClick(setScrollWithClick);
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
            if (settings.isEnabled()) {
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
            Log.e("position", "position: " + returnPositionItem.getPosition());
            changeSelectedChild();
            moveCursor();
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
        return settings.isBarrido();
    }

    public boolean isBarridoActivado() {
        if(settings != null)
            return settings.isEnabled();
        return false;
    }

    public boolean isScrollMode(){
        return settings.isScroll();
    }

    public boolean isScrollModeClicker() {
        return settings.isScrollWithClick();
    }


    public void setmEstaActivado(boolean mEstaActivado) {
        settings.setEnabled(mEstaActivado);
    }

    public boolean isAvanzarYAceptar() {
        return settings.isAvanzarYAceptar();
    }

    public void setAvanzarYAceptar(boolean avanzarYAceptar) {
        settings.setAvanzarYAceptar(avanzarYAceptar);
    }

    public int getPosicionBarrido() {
        return this.returnPositionItem.getPosition();
    }

    public void activarDesactivarBarrido() {
        pago = consultarPago();
        if (pago)
            activarBarrido();
        else {
            if(button.getVisibility()==View.VISIBLE)
                changeButtonVisibility();
        }

    }
    public boolean devolverpago(){
        return pago;
    }

    public void avanzarBarrido() {
        changeSelectedChild(true);
        recorrerBarridoManual();

    }

    public void volverAtrasBarrido() {
        changeSelectedChild(false);
        recorrerBarridoManual();
    }


    private boolean consultarPago() {
        return mDefaultSharedPreferences.getInt("premium", 0) == 1;
    }



    public ArrayList<View> getmListadoVistas() {
        return mListadoVistas;
    }


    public void cambiarEstadoBarrido() {

        tiempo = mDefaultSharedPreferences.getInt("velocidad_barrido", 5);
        enableDisableScreenScanning(mDefaultSharedPreferences.getBoolean(Constants.BARRIDO_BOOL, false),mDefaultSharedPreferences.getBoolean("tipo_barrido_normal",false),mDefaultSharedPreferences.getBoolean("tipo_barrido", false),mDefaultSharedPreferences.getBoolean("sip_and_puff",false),mDefaultSharedPreferences.getBoolean(this.mActivity.getResources().getString(R.string.usar_scroll),false),mDefaultSharedPreferences.getBoolean(this.mActivity.getResources().getString(R.string.usar_scroll_click),false));
        timeToChange=new TimeToChange(this,tiempo);
        activarDesactivarBarrido();
    }

    public BarridoPantalla updateSharePrefs(SharedPreferences sharedPreferences){
        this.mDefaultSharedPreferences = sharedPreferences;
        return this;
    }

    /*
     * Use this method to return the position of object that&acutes is visible
     * */
    private int changeSelectedChild() {
        this.returnPositionItem.add();
        try{
            if (mListadoVistas.get(this.returnPositionItem.getPosition()).getVisibility() == View.VISIBLE) {
                return this.returnPositionItem.getPosition();
            } else{
               changeSelectedChild();
            }
        }catch (Exception ex){
            return 0;
        }
        return this.returnPositionItem.getPosition();
    }

    private void changeSelectedChild(boolean add) {
            if(add)
                this.returnPositionItem.add();
            else
                this.returnPositionItem.subtract();
            if (mListadoVistas.get(this.returnPositionItem.getPosition()).getVisibility() != View.VISIBLE) {
                changeSelectedChild(add);
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
        getExactCenterPoint(mListadoVistas.get(this.returnPositionItem.getPosition()));
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
        return settings.isSipAndPuff();
    }

    public FloatingActionButton getButton() {
        return button;
    }

    public void createPositionItem(int size){
        returnPositionItem = new ReturnPositionItem(size);
    }

    public FloatingActionButton findButton(Activity mActivity){
        return mActivity.findViewById(R.id.floatting_button);
    }


}
