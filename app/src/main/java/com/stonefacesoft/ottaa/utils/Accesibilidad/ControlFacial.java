package com.stonefacesoft.ottaa.utils.Accesibilidad;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.stonefacesoft.ottaa.utils.verificarPaqueteInstalado;
/**
 *<h3>Objective</h3>
 * <p>Implements the facial control application</p>
 * <h3>How to declare</h3>
 * <code>ControlFacial facialControl=new ControlFacial(mContext);</code>
 * <h3>Examples of implementation</h3>
 * <h4>How to get the status</h4>
 * <code>facialControl.ConsultarEstado();</code>
 * */
public class ControlFacial {
    private final Context mContext;
    private final SharedPreferences mDefaultSharedPreferences;
    private static final Intent sSettingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    private final String slaveMode="com.crea_si.eviacam.slavemode.SlaveModeService";
    private final String packageName="com.crea_si.eviacam.service";
    public ControlFacial(Context context)
    {
        mContext=context;
        mDefaultSharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        consultarServicio();
    }
    /**
     * */
    private boolean consultarEstado()
    {
        return mDefaultSharedPreferences.getBoolean("control_facial",false);
    }
    private void consultarServicio()
    {
        if(consultarEstado())
        {
            iniciarReconocimiento(true);
            Log.d("controlFacial","1");
        }
        else {
            iniciarReconocimiento(false);
            Log.d("controlFacial","2");

        }

    }
/**
 *
 * */
    private void iniciarReconocimiento(boolean instalar)
    {
        boolean instalado=new verificarPaqueteInstalado(mContext).estaInstalado("com.crea_si.eviacam.service");
        PackageManager packageM=mContext.getPackageManager();
        if(instalado)
        {

            Intent intent= new Intent();
            intent.setPackage(packageName);
            mContext.startActivity(intent);

        }
        else {
            if(instalar)
            descargarReconocimiento();
        }
    }
    /**
     *
     * */
    private void descargarReconocimiento()
    {
        try {
            Intent goToMarketCa = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("market://details?id=com.crea_si.eviacam.service"));
            mContext.startActivity(goToMarketCa);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.crea_si.eviacam.service".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




}
