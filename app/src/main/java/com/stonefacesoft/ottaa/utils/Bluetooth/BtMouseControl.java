package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class BtMouseControl {
    private static final UUID UID_Arduino = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mBluetoothAdapter;
    private BtControl mBluetoothService;
    private Boolean activeBluetoothFragment;
    private int timeout;
    private AppCompatActivity activity;
    private final String TAG="MouseControl";
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() == null || device.getName().equals("null") || device.getName().startsWith("HC")) {
                    bondAndConnect(device);


                } else {
                    Log.w(TAG, "FOUND_DEV: " + device.getName());
                }
            }

        }
    };

    public BtMouseControl(AppCompatActivity mActivity) {
        this.activity = mActivity;

    }

    public void conectarDispositivo(BluetoothAdapter adapter) {
        mBluetoothAdapter = adapter;
        activeBluetoothFragment = true;
        timeout = 0;
        enableBluetooth();
        Thread discoverThreadt = new Thread(new Runnable() {
            @Override
            public void run() {
                while (activeBluetoothFragment) {

                    try {
                        discover();
                        Thread.sleep(4000);
                        timeout++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        discoverThreadt.start();
    }

    public void discover() {


        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(receiver, discoverDevicesIntent);


        }
        if (!mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(receiver, discoverDevicesIntent);

        }
    }

    public void enableBluetooth() {
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Device dont have bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableBTIntent);
        }
    }

    private void bondAndConnect(BluetoothDevice device) {
        mBluetoothService = new BtControl(activity, mBluetoothAdapter);
        mBluetoothService.startClient(device, UID_Arduino);
        activeBluetoothFragment = false;
    }

    public BtControl getmBluetoothService() {
        return mBluetoothService;
    }
}
