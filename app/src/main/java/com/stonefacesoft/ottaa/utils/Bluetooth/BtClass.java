package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.stonefacesoft.ottaa.utils.Bluetooth.BluetoothUtils.BluetoothService;
import com.stonefacesoft.ottaa.utils.Bluetooth.BluetoothUtils.DeviceList;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;

import java.util.ArrayList;

public class BtClass extends Handler {
    private final String TAG = "BtClass";
    private BluetoothAdapter adapter;
    private BluetoothManager manager;
    private Context mContext;
    private Activity appCompatActivity;
    private int mState = Constants.STATE_NONE;

    private static BtClass adapterDevices;
    private ArrayList<BtDeviceItem> devices;
    private BtDeviceItem btDeviceItem;
    private BluetoothDevice device;
    private String mConnectedDeviceName = null;
    private BluetoothService service;


    public synchronized static BtClass getInstance() {
        if (adapterDevices == null) {

            synchronized (BtClass.class) {
                if (adapterDevices == null) {
                    adapterDevices = new BtClass();
                }
            }
        }
        return adapterDevices;
    }

    private BtClass() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }
    public void setUpBluetooth(){
        service=new BluetoothService(mContext,this);
    }

    public BtClass setupActivity(Activity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        return setUpContext(this.appCompatActivity);
    }

    public BtClass setUpContext(Context mContext) {
        this.mContext = mContext;
        if (devices == null)
            devices = new ArrayList<>();
        return this;
    }

    public void enableBluetooth() {
        if (adapter != null) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            appCompatActivity.startActivityForResult(enableBT, IntentCode.REQUEST_ENABLE_BT.getCode());
        }
    }

    public boolean isBluetoothEnabled() {
        return (adapter != null && adapter.isEnabled());
    }

    public void startBtScanner() {
        if (adapter != null) {
            adapter.startDiscovery();
            mState = Constants.STATE_LISTEN;
        }
    }

    public void stopBtScanner() {
        if (adapter != null) {
            adapter.cancelDiscovery();
        }
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }


    public ArrayList<BtDeviceItem> getDevices() {
        return devices;
    }

    public synchronized void connectDevices() {
        BluetoothService service=new BluetoothService(mContext,this);
        service.start();

    }



    public BluetoothDevice getDevice() {
        return device;
    }


    public Context getmContext() {
        return mContext;
    }



    public void connectDevice(Intent data, boolean secure) {
        //Get the device MAC address
        if(data!=null&&data.hasExtra(Constants.EXTRA_DEVICE_ADDRESS)){
        String address = data.getExtras().getString(Constants.EXTRA_DEVICE_ADDRESS);

        //Get the BluetoothDevice object
        if(adapter!=null){
            BluetoothDevice device = adapter.getRemoteDevice(address);

            //Attempt to connect to the device
            if (service == null)
                Toast.makeText(appCompatActivity,"BtService = null",Toast.LENGTH_SHORT).show();
            else
                service.connect(device, secure);
        }
        }
    }



    @Override
    public void handleMessage(@NonNull Message msg) {

        switch (msg.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case BluetoothService.STATE_CONNECTED:
                        setStatus(BluetoothService.STATE_CONNECTED, mConnectedDeviceName);
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        setStatus(BluetoothService.STATE_CONNECTING);
                        break;
                    case BluetoothService.STATE_LISTEN:

                    case BluetoothService.STATE_NONE:
                        setStatus(BluetoothService.STATE_NONE);
                        break;
                }
                break;
            case Constants.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);

                break;
            case Constants.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.d(TAG, "handleMessage: "+readMessage);
                break;
            case Constants.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                if (null != appCompatActivity) {
                    Toast.makeText(appCompatActivity,  "Connected"
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.MESSAGE_TOAST:
                if (null != appCompatActivity) {
                    Toast.makeText(appCompatActivity, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @NonNull
    @Override
    public String getMessageName(@NonNull Message message) {

        return super.getMessageName(message);
    }



    public synchronized int getState() {
        return mState;
    }

    public synchronized void setUpState(int mState) {
        this.mState = mState;
    }
    private void setStatus(int status, String connectedDeviceName){
        switch (status){
            case BluetoothService.STATE_CONNECTED:
             //   btnBluetooth.setImageDrawable(getDrawable(R.drawable.ic_bt_connected));
                //TODO agregar un connecteddevicename
                Log.d(TAG, "setStatus: Connected");
                break;
            case BluetoothService.STATE_CONNECTING:
                Log.d(TAG, "setStatus: Connecting");
                //TODO agregar un estado conectando
                break;
            case BluetoothService.STATE_LISTEN:
                Log.d(TAG, "setStatus: listen");
            case BluetoothService.STATE_NONE:
                Log.d(TAG, "setStatus: None");
                break;
        }
    }
    private void setStatus(int status){
        switch (status){
            case BluetoothService.STATE_CONNECTED:
                //   btnBluetooth.setImageDrawable(getDrawable(R.drawable.ic_bt_connected));
                //TODO agregar un connecteddevicename
                Log.d(TAG, "setStatus: Connected");
                break;
            case BluetoothService.STATE_CONNECTING:
                Log.d(TAG, "setStatus: Connecting");
                //TODO agregar un estado conectando
                break;
            case BluetoothService.STATE_LISTEN:
                Log.d(TAG, "setStatus: listen");
            case BluetoothService.STATE_NONE:
                Log.d(TAG, "setStatus: None");
                break;
        }
    }

    public void InitSecureDiscover(){
        Intent serverIntent = new Intent(appCompatActivity, DeviceList.class);
        appCompatActivity.startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_SECURE);
    }
    public void InitInsecureScan(){
        Intent serverIntent = new Intent(appCompatActivity, DeviceList.class);
        appCompatActivity.startActivityForResult(serverIntent, Constants.REQUEST_CONNECT_DEVICE_SECURE);
    }

    public BluetoothService getService() {
        return service;
    }
}