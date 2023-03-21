package com.stonefacesoft.ottaa.Dialogos.DialogUtils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stonefacesoft.ottaa.Dialogos.Dialog_abstract_class;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ActivityUtilsEstatus;
import com.stonefacesoft.ottaa.utils.Custom_button;
import com.stonefacesoft.pictogramslibrary.utils.ValidateContext;

public class DialogUtils extends Dialog_abstract_class {
    protected Context mContext;
    protected Dialog dialog;
    protected ActivityUtilsEstatus activityUtilsEstatus;
    protected int max;
    protected Custom_button unknow_option,yes_button,no_button;
    protected ProgressBar bar_progress;
    protected TextView title,message;
    private final String TAG="Dialog Utils";

    public DialogUtils(Context context){
        this.mContext=context;
        setDialog(mContext);
    }

    public DialogUtils(Context mContext,String title,String message){
        setDialog(mContext);
        setTitle(title);
        setMessage(message);
    }

    public  DialogUtils(Context mContext,int layout){
        this.mContext=mContext;
        setDialog(mContext,layout);
    }


    public void mostrarDialogo(){
        if(ValidateContext.isValidContext(mContext) &&dialog!=null&&!dialog.isShowing()){
            dialog.show();
        }

    }

    public void destruirDialogo(){
        if(activityUtilsEstatus.isValidContext(mContext)&&dialog.isShowing()&&dialog!=null){
            dialog.dismiss();
        }
    }
    public void cancelarDialogo(){
        if(activityUtilsEstatus.isValidContext(mContext)&&dialog!=null){
            dialog.cancel();
        }

    }



    public boolean isShowing(){
        if(dialog!=null&&activityUtilsEstatus.isValidContext(mContext))
            return dialog.isShowing();
        return false;
    }

    public void setProgressStyle(int progressStyle){
        if(activityUtilsEstatus.isValidContext(mContext)&&dialog!=null&&bar_progress!=null)
           bar_progress.setProgress(progressStyle);
    }

    public void setMax(int value){
        this.max=value;
    }

    public void setProgress(int value){
        if(activityUtilsEstatus.isValidContext(mContext)){
            if(dialog!=null&&bar_progress.getProgress()<100){
                if(max!=0){
                    int value1=(value*100)/max;
                    bar_progress.setProgress(value1);
                }else{
                    bar_progress.setMax(value);
                    bar_progress.setProgress(value);
                }
            }else{
            destruirDialogo();
            }
        }
    }



    public void prepareDataDialog(){
        settingButton(title,R.id.textTitulo);
        settingButton(message,R.id.text_descripcion);
        settingButton(bar_progress,R.id.progressBar2);
        settingButton(unknow_option,R.id.unknow_Button);
        settingButton(yes_button,R.id.yes_button);
        settingButton(no_button,R.id.no_button);
        setTextButton(R.id.yes_button,mContext.getResources().getString(R.string.pref_yes_alert));
        setTextButton(R.id.no_button,mContext.getResources().getString(R.string.pref_no_alert));
        setTextButton(R.id.unknow_Button,mContext.getResources().getString(R.string.pref_volver_editor));
        changeIcon((Custom_button) getObject(R.id.no_button),R.drawable.ic_cancel_black_24dp);
    }

    protected void ocultarElementos(){

    }

    @Override
    public Dialog_abstract_class changeIcon(Custom_button v, int drawable) {
        if(mContext!=null){
            v.setImageView(mContext,drawable);
        }
        return this;
    }

    @Override
    public Dialog_abstract_class setTitle(String titulo) {
        if(getObject(R.id.textTitulo)!=null){
           title= (TextView) getObject(R.id.textTitulo);
           title.setText(titulo);
        }
        return this;
    }

    @Override
    public Dialog_abstract_class setMessage(String mensaje) {
        if(getObject(R.id.text_descripcion)!=null){
            message= (TextView) getObject(R.id.text_descripcion);
            message.setText(mensaje);
        }
        return this;
    }

    @Override
    public Dialog_abstract_class setTextButton(int v,String text) {
        if(getObject(v)!=null){
            Custom_button btn=(Custom_button) getObject(v);
            btn.setTexto(text);
        }
        return this;
    }

    @Override
    public Dialog_abstract_class setLayout(int layout) {
        if(dialog!=null){

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(layout);
        }
        return this;
    }

    @Override
    public Dialog_abstract_class settingButton( ProgressBar v, int view) {
       if(dialog!=null)
           v=(ProgressBar) getObject(view);
        return this;
    }


    @Override
    public Dialog_abstract_class settingButton( Custom_button v, int layout) {
        if(dialog!=null)
            v= dialog.findViewById(layout);
        return this;
    }

    @Override
    public Dialog_abstract_class settingButton( TextView v, int layout) {
        if(dialog!=null)
            v= dialog.findViewById(layout);
        return this;
    }

    @Override
    public Dialog_abstract_class setOnClick( View v,View.OnClickListener clickListener) {
        if(v!=null)
        v.setOnClickListener(clickListener);
        return this;
    }


    @Override
    public void setDialog(Context mContext) {
        this.mContext=mContext;
        dialog=new Dialog(mContext);
        prepareDialog(R.layout.dialog_yes_no_cancel);

    }

    @Override
    public void setDialog(Context mContext,int layout) {
        this.mContext=mContext;
        dialog=new Dialog(mContext);
        prepareDialog(layout);

    }


    private void prepareDialog(int layout){
        setLayout(layout);
        if(layout==R.layout.dialog_yes_no_cancel)
        prepareDataDialog();
        activityUtilsEstatus=new ActivityUtilsEstatus();
    }

    @Override
    public Dialog_abstract_class changeVisibilityView(int view, int visibility) {
        if(getObject(view)!=null)
            getObject(view).setVisibility(visibility);
        return this;
    }


    @Override
    public Dialog_abstract_class  setCancelable(boolean isCancelable){
        if(dialog!=null&&activityUtilsEstatus.isValidContext(mContext))
            dialog.setCancelable(isCancelable);
        return this;
    }



    @Override
    public View getObject(int view) {
        if(dialog!=null){
            switch (view){
                case R.id.textTitulo:
                    Log.d(TAG, "getObject: Titulo");
                    if(title==null)
                        title= dialog.findViewById(R.id.textTitulo);
                    return  title;
                case  R.id.text_descripcion :
                    Log.d(TAG, "getObject: Mensaje");
                    if(message==null)
                       message=dialog.findViewById(R.id.text_descripcion);
                    return message;
                case  R.id.progressBar2 :
                    Log.d(TAG, "getObject: ProgressBar");
                    if(bar_progress==null)
                         bar_progress=dialog.findViewById(R.id.progressBar2);
                    return bar_progress;
                case  R.id.unknow_Button:
                    Log.d(TAG, "getObject: Boton 3");
                    if(unknow_option==null)
                        unknow_option=dialog.findViewById(R.id.unknow_Button);
                    return unknow_option;
                case  R.id.yes_button :
                    Log.d(TAG, "getObject: Boton si");
                    if(yes_button==null)
                        yes_button=dialog.findViewById(R.id.yes_button);
                    return yes_button;
                case R.id.no_button :
                    Log.d(TAG, "getObject: Boton no");
                    if(no_button==null)
                        no_button=dialog.findViewById(R.id.no_button);
                    return no_button;
            }
        }
        return null;
    }


}
