package com.stonefacesoft.ottaa.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stonefacesoft.ottaa.Adapters.Item_adapter;
import com.stonefacesoft.ottaa.R;

public class custom_dialog_option_item  {
    private final Context mContext;
    private RecyclerView Recycler;
    private TextView titulo;
    private final Dialog dialog;
    private SharedPreferences mDefaultSharedPreferences;
    public custom_dialog_option_item(Context mActivity){
        this.mContext=mActivity;
        dialog=new Dialog(this.mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tags_selector);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


    public custom_dialog_option_item prepareDialog(String titulo, int arrayName, int arrayValue, View.OnClickListener listener){
        Recycler=dialog.findViewById(R.id.recyclerViewTags);
        this.titulo=dialog.findViewById(R.id.textTiulo);
        this.titulo.setText(titulo.toUpperCase());
        Button btn=dialog.findViewById(R.id.btn_dialogTag);
        btn.setVisibility(View.GONE);
        Item_adapter adapter=new Item_adapter(R.layout.custom_item_select,mContext,dialog);
        adapter.setmArrayListItemsNames(mContext.getResources().getStringArray(arrayName));
        adapter.setmArrayListValues(mContext.getResources().getStringArray(arrayValue));

        adapter.setOnClickListener(listener);
        Recycler.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        Recycler.setLayoutManager(mLayoutManager);
        return this;
    }

    public custom_dialog_option_item prepareDialog(String titulo,String key,String selectedValue, int arrayName, int arrayValue, View.OnClickListener listener){
        Recycler=dialog.findViewById(R.id.recyclerViewTags);
        this.titulo=dialog.findViewById(R.id.textTiulo);
        this.titulo.setText(titulo.toUpperCase());
        Button btn=dialog.findViewById(R.id.btn_dialogTag);
        btn.setVisibility(View.GONE);
        Item_adapter adapter=new Item_adapter(R.layout.custom_item_select,mContext,dialog);
        adapter.setmArrayListItemsNames(mContext.getResources().getStringArray(arrayName));
        adapter.setmArrayListValues(mContext.getResources().getStringArray(arrayValue));
        adapter.setKey(key);
        adapter.setDefaultValue(selectedValue);
        adapter.setOnClickListener(listener);
        Recycler.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        Recycler.setLayoutManager(mLayoutManager);
        return this;
    }

    public void ShowDialog(){
        dialog.show();
    }

    public Dialog getDialog(){
        return dialog;
    }
    public RecyclerView getRecycler(){
        return Recycler;
    }

    public void DissmisDialog(){
        if(dialog.isShowing())
        dialog.dismiss();
    }





}
