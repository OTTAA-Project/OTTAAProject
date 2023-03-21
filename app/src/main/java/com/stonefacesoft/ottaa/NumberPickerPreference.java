package com.stonefacesoft.ottaa;

import android.app.Dialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.stonefacesoft.ottaa.utils.Custom_button;

/**
 * @author  Hector Costa  15/07/2015.
 * @version 1.1
 * @since 18/02/2020
 * this class has edited  by gonzalo juarez on 18/02/2020
 *<h3>Objective</h3>
 *Show an scrolleable dialog with a number picker
 * <h3>How to implements</h3>
 * <code>NumberPickerPreference numberPicker=new NumberPickerPreference(@param Context,@param string,@param key);</code>
 *<h3>How to show</h3>
 * numberPicker.createPicker();
 * <h3>How to set up the range of the values in the numberPicker</h3>
 * <code>int value1=0;</code><br>
 * <code>int value2=10;</code><br>
 * <code>numberPicker.setmPicker(value1,value2);</code>
 * */

public class NumberPickerPreference  implements View.OnClickListener , NumberPicker.OnScrollListener {
    private  NumberPicker mPicker;
    private final Integer mNumber = 10;
    private Integer mNumberMin=0;
    private Integer mNumberMax=10;
    private  Context mContext;
    private int tipo;
    private  String title;
    private  String  key;
    private  Dialog dialog;
    private  Custom_button positiveButton;
    private  Custom_button negativeButton;
    private  TextView titulo;


    public NumberPickerPreference(Context context,String title,String key){
        mContext=context;
        this.key=key;
        this.title=title;

    }

    public NumberPickerPreference(){

    }

    public void createDialog(){
        dialog=new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_number_picker_selector);
        titulo=dialog.findViewById(R.id.titulo);
        titulo.setText(title);
        positiveButton=dialog.findViewById(R.id.positive_button);
        negativeButton=dialog.findViewById(R.id.negative_button);
        positiveButton.setImageView(mContext,R.drawable.ic_check_circle_black_24dp);
        negativeButton.setImageView(mContext,R.drawable.ic_cancel_black_24dp);
        positiveButton.setTexto(mContext.getString(R.string.pref_OK));
        negativeButton.setTexto(mContext.getString(R.string.cancel));
        mPicker=dialog.findViewById(R.id.number_picker);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        mPicker.setOnScrollListener(this);
        mPicker.setClickable(false);
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal>picker.getMaxValue()){
                    newVal=picker.getMaxValue();
                }else if(newVal<picker.getMinValue()){
                    newVal=picker.getMinValue();
                }
                picker.setValue(newVal);
            }
        });

        dialog.setCancelable(true);
    }


    public void createPicker(){
       if(!dialog.isShowing())
        dialog.show();
    }



    public void setmPicker(int valueMin,int valueMax) {
        mNumberMax = valueMax;
        mNumberMin = valueMin;
        if (mContext != null) {
            mPicker.setMinValue(valueMin);
            mPicker.setMaxValue(valueMax);

        }
    }



    public void setTipo(int n)
    {
        tipo=n;
    }

    public int getTipo() {
        return tipo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positive_button:
                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(key,mPicker.getValue()).apply();
                dialog.dismiss();
                break;
            case R.id.negative_button:
                if(dialog.isShowing())
                    dialog.dismiss();
                    break;
            case R.id.number_picker:
                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(key,mPicker.getValue()).apply();
                    break;
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(key,mPicker.getValue()).apply();

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getmNumberMax() {
        return mNumberMax;
    }

    public Integer getmNumberMin() {
        return mNumberMin;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public void setmNumberMax(Integer mNumberMax) {
        this.mNumberMax = mNumberMax;
    }

    public void setmNumberMin(Integer mNumberMin) {
        this.mNumberMin = mNumberMin;
    }
}
