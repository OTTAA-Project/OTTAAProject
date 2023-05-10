package com.stonefacesoft.ottaa.utils.Handlers;

import android.os.Message;

import com.stonefacesoft.ottaa.Principal;
import com.stonefacesoft.ottaa.prefs;

public class HandlerComunicationClass extends HandlerUtils {
    private prefs preferences;
    private Principal principal;


    public static final int CARGARJSON=2;
    public static final int ERRORCARGARJSON=3;

    public static final int SHAREACTION = 7;

    private  int fallasLeerJson=0;
    public HandlerComunicationClass(prefs preferences){
        this.preferences=preferences;
    }
    public HandlerComunicationClass(Principal principal){
        this.principal=principal;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SHOWDIALOG:
                 if(preferences!=null){
                    preferences.mostrarDialogo();
                }
                break;
            case DISMISSDIALOG:
                if(preferences!=null){
                    preferences.cancelarDialogo();
                }
                break;
            case CARGARJSON :
                if(principal!=null)
                    principal.CargarJson();
                break;
            case ERRORCARGARJSON:
                if(principal!=null){
                    fallasLeerJson++;
                    if(fallasLeerJson==1)
                        principal.CargarJson();

                }
                break;
            case TRANSLATEDPHRASE:
                if (principal != null) {
                    principal.setOracion((String) msg.obj);
                    principal.speak();
                }
                break;
            case SHAREACTION:
                principal.setOracion((String) msg.obj);
                principal.shareText();
                break;
            case TEXTNOTTRANSLATED:
                if (principal != null) {
                    principal.mostrarMensajeSinConexionInternet();
                }
                break;
            case CHATGPTERROR:{
                if(principal!=null)
                    principal.chatGptError();
                }
            break;
            case TRYAGAIN:
                if (principal != null) {
                    principal.intentarDeNuevo();
                }
                break;
             default:
                 super.handleMessage(msg);
        }
    }

    public void setFallasLeerJson(int fallasLeerJson){
        this.fallasLeerJson=fallasLeerJson;
    }
}
