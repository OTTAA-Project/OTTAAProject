package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.UUID;

public class BtConnector {
    private static final UUID UID_Arduino = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String NAME_SECURE = "HC-05";
    private static final String NAME_INSECURE = "HC-05";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBlueethoothDevice;
    private BtControl mBluetoothService;
    private Boolean activeBluetoothFragment;
    private int timeout;
    private Context mContext;
    private final String TAG="";
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
              //  if (device.getName() == null || device.getName().equals("null") || device.getName().startsWith("HC")) {
                    bondAndConnect(device);

                //} else {
                  //  Log.d(TAG, "FOUND_DEV: " + device.getName());
                //}
            }

        }
    };

    public BtConnector(Context context) {
        this.mContext = context;
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
                        if (timeout == 5 && activeBluetoothFragment) {
                            Log.d(TAG, "run: please turn on the bluetooth device");
                            activeBluetoothFragment=false;
                        } else {
                            Log.d(TAG, "run: searching" );
                        }
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
            mContext.registerReceiver(receiver, discoverDevicesIntent);


        }
        if (!mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            mContext.registerReceiver(receiver, discoverDevicesIntent);

        }
    }

    public void enableBluetooth() {
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Device dont have bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mContext.startActivity(enableBTIntent);
        }
    }

    private void bondAndConnect(BluetoothDevice device) {
        mBluetoothService = new BtControl(mContext, mBluetoothAdapter);
        mBluetoothService.startClient(device, UID_Arduino);
        activeBluetoothFragment = false;
    }

    public BtControl getmBluetoothService() {
        return mBluetoothService;
    }


}
