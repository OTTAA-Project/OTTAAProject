package com.stonefacesoft.ottaa.Dialogos;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stonefacesoft.ottaa.utils.Custom_button;

public abstract class Dialog_abstract_class {
    /*
    *This is an abstract class that bring the diferent methods  of the dialog constructor
    * */
    public abstract void setDialog(Context mContext);
    public abstract void setDialog(Context mContext,int layout);
    public abstract Dialog_abstract_class changeVisibilityView(int v, int visibility);
    public abstract Dialog_abstract_class changeIcon(Custom_button button, int drawable);
    public abstract Dialog_abstract_class setTitle(String title);
    public abstract Dialog_abstract_class setMessage(String message);
    public abstract Dialog_abstract_class setTextButton(int v,String text);
    public abstract Dialog_abstract_class setLayout(int layout);
    public abstract Dialog_abstract_class settingButton( ProgressBar v, int layout);
    public abstract Dialog_abstract_class settingButton(Custom_button v, int layout);
    public abstract Dialog_abstract_class settingButton(TextView v, int layout);
    public abstract Dialog_abstract_class setOnClick(View v,View.OnClickListener clickListener);
    public abstract Dialog_abstract_class setCancelable(boolean isCancelable);
    public abstract View getObject(int view);

}
