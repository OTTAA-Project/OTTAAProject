package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.preferences.PreferencesUtil;

/**
 * This class was created as to reduce code in the prefs class
 * In order to select one item when I try
 */
public class Item_adapter extends RecyclerView.Adapter<Item_adapter.ItemAdapterViewHolder> {

    private final int mLayoutResourceId;
    private  Context mContext;
    private String[] mArrayListItemsnames;
    private String[] mArrayListValues;

    private final Dialog dialogDismiss;
    private int position = 0;

    private View.OnClickListener listener;

    private PreferencesUtil sharedPreferencesUtil;

    private String key, defaultValue;

    /**
     * El sistema pone el valor en el sistema de acuerdo a los siguientes casos :
     * cambio de idioma, el sistema tiene que bajar el idioma.
     * Cambio de opcion para el joystick el sistema tiene que dar opcion 1 0 2
     * Cambio de sexo
     * Cambio de edad
     */


    public Item_adapter(int mLayoutResourceId, Context mContext, Dialog dialogToDismiss) {
        this.mLayoutResourceId = mLayoutResourceId;
        this.mContext = mContext;
        this.dialogDismiss = dialogToDismiss;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        sharedPreferencesUtil = new PreferencesUtil(preferences);
    }

    public Item_adapter(int mLayoutResourceId,Dialog dialogToDismiss){
        this.mLayoutResourceId = mLayoutResourceId;
        this.dialogDismiss = dialogToDismiss;
    }

    public Item_adapter setmArrayListItemsNames(String[] mArrayListItemsNames) {
        this.mArrayListItemsnames = mArrayListItemsNames;
        return this;
    }


    public Item_adapter setmArrayListValues(String[] mArrayListValues) {
        this.mArrayListValues = mArrayListValues;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    @Override
    public Item_adapter.ItemAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext)
                .inflate(mLayoutResourceId, parent, false);
        return new Item_adapter.ItemAdapterViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(Item_adapter.ItemAdapterViewHolder holder, int position) {
        String model = mArrayListItemsnames[position];
        holder.textView.setText(model);
        selecItem(holder, position);
    }

    public void selecItem(Item_adapter.ItemAdapterViewHolder holder, int position) {
        if (defaultValue != null) {
            try {
                holder.radioButton.setChecked(sharedPreferencesUtil.getStringValue(key, defaultValue).equalsIgnoreCase(mArrayListValues[position]));
            } catch (Exception ex) {

                if (!sharedPreferencesUtil.getBooleanKey(key, false)) {
                    switch (position) {
                        case 0:
                            holder.radioButton.setChecked(true);
                            break;
                        case 1:
                            holder.radioButton.setChecked(false);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            holder.radioButton.setChecked(false);
                            break;
                        case 1:
                            holder.radioButton.setChecked(true);
                            break;
                    }
                }

            }
        } else {
            holder.radioButton.setChecked(false);
        }
    }

    public String getBooleanValue(boolean value) {
        if (value == false)
            return "false";
        return "true";
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mArrayListItemsnames != null)
            return mArrayListItemsnames.length;
        else
            return 0;
    }


    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RadioButton radioButton;
        private final TextView textView;


        public ItemAdapterViewHolder(View itemView) {
            super(itemView);//this class extends from recycler view
            radioButton = itemView.findViewById(R.id.checkbox);

            textView = itemView.findViewById(R.id.text_description);
            if (listener != null)
                radioButton.setOnClickListener(this);
        }


        public RadioButton getRadioButton() {
            return radioButton;
        }

        @Override
        public void onClick(View v) {
            position = getPosition();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onClick(v);
                }
            }, 100);

        }
    }


    public String[] getmArrayListItemsnames() {
        return mArrayListItemsnames;
    }

    public String[] getmArrayListValues() {
        return mArrayListValues;
    }

    public int devolverPosition() {
        return position;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getKey() {
        return key;
    }
}
