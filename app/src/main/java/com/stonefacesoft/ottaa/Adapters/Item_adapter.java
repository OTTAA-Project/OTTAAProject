package com.stonefacesoft.ottaa.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.FavModel;
import com.stonefacesoft.ottaa.FirebaseRequests.BajarJsonFirebase;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.DatosDeUso;
import com.stonefacesoft.ottaa.utils.exceptions.FiveMbException;
import com.stonefacesoft.ottaa.utils.textToSpeech;

import java.util.ArrayList;

public class Item_adapter extends RecyclerView.Adapter<Item_adapter.ItemAdapterViewHolder>  {

    private int mLayoutResourceId;
    private Context mContext;
    private String[] mArrayListItemsnames;
    private String[] mArrayListValues;

    private Dialog dialogDismiss;
    private int position=0;

    private View.OnClickListener listener;

    /**
     * El sistema pone el valor en el sistema de acuerdo a los siguientes casos :
     * cambio de idioma, el sistema tiene que bajar el idioma.
     * Cambio de opcion para el joystick el sistema tiene que dar opcion 1 0 2
     * Cambio de sexo
     * Cambio de edad
     * */




    public Item_adapter(int mLayoutResourceId, Context mContext, Dialog dialogToDismiss)  {
        this.mLayoutResourceId = mLayoutResourceId;
        this.mContext = mContext;
        this.dialogDismiss = dialogToDismiss;
    }

    public Item_adapter setmArrayListItemsNames(String[] mArrayListItemsNames){
        this.mArrayListItemsnames=mArrayListItemsNames;
        return this;
    }


    public Item_adapter setmArrayListValues(String[] mArrayListValues){
        this.mArrayListValues=mArrayListValues;
        return this;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
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
        holder.textView.setText( model);

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

        private RadioButton radioButton;
        private TextView textView;



        public ItemAdapterViewHolder(View itemView) {
            super(itemView);//this class extends from recycler view
            radioButton = itemView.findViewById(R.id.checkbox);
            textView=itemView.findViewById(R.id.text_description);
            if(listener!=null)
            radioButton.setOnClickListener(this);

        }




        public RadioButton getRadioButton() {
            return radioButton;
        }

        @Override
        public void onClick(View v) {
            position=getPosition();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onClick(v);
                }
            },100);

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
}
