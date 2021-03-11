package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BtReceiver {
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("bluetooth_message")){
                if (intent.getExtras() != null) {
                    String comando = intent.getExtras().getString("mensaje", "");
                    Log.d("TAG", "onReceive: "+ comando);
                }
            }
        }
    };

    public BtReceiver(AppCompatActivity appCompatActivity){
        IntentFilter filter = new IntentFilter();
        filter.addAction("bluetooth_message");
        appCompatActivity.registerReceiver(receiver,filter);

    }
}
