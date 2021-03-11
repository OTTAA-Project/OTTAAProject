package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BtControl {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private UUID mDeviceUuid;
    private BluetoothDevice mBluetoothDevice;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private Boolean mConnectedStarted;
    private BluetoothSocket mBluetoothSocket;
    private final String TAG="BtControl";


    public BtControl(Context mContext, BluetoothAdapter bluetoothAdapter) {
        this.mContext = mContext;
        mBluetoothAdapter = bluetoothAdapter;
    }

    //inicio cliente
    public void startClient(BluetoothDevice device, UUID uuid) {
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    //conecto
    public void connected(BluetoothSocket mBluethoothSocket) {
        mConnectedThread = new ConnectedThread(mBluethoothSocket);
        mConnectedThread.start();
    }

    public BluetoothSocket getmBluetoothSocket() {
        return mBluetoothSocket;
    }

    //escribe y envia al dispositivo bluetooth
    public boolean write(byte[] out) {
        return mConnectedThread.write(out);
    }


    //devuelve si esta conectado
    public boolean getConnected() {
        return mConnectedStarted;
    }


    //clase encargada de conectar el dispositivo
    private class ConnectThread extends Thread {
        //puerto al que se va a conectar

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            //dispositivo y id del dispositivo
            mBluetoothDevice = device;
            mDeviceUuid = uuid;
        }


        public void run() {
            //empiezo a correr el hilo
            BluetoothSocket temp = null;// el puerto esta vacio
            try {
                //le asigno el nuevo puerto
                temp = mBluetoothDevice.createRfcommSocketToServiceRecord(mDeviceUuid);
            } catch (IOException e) {
                e.printStackTrace();

            }
            // luego asigno el puerto
            mBluetoothSocket = temp;
            //cancelo la busqueda
            mBluetoothAdapter.cancelDiscovery();
            try {
                //lo conecto
                mBluetoothSocket.connect();
            } catch (IOException e) {
                try {
                    //no se puede conectar se cierra la conexion
                    Log.d(TAG, "bluetooth closed : yes");
                    mBluetoothSocket.close();
                } catch (IOException e1) {
                    Log.e(TAG, "log error : "+e1.getMessage() );
                }
            }
            //conecto el dispositivo
            connected(mBluetoothSocket);
        }

        //cierro la conexion
        public void cancel() {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mBlueethoothSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutputStream;
        private String txt;

        public ConnectedThread(BluetoothSocket bluetoothSocket) {
            mBlueethoothSocket = bluetoothSocket;
            InputStream tempInputStream = null;
            OutputStream tempOutputStream = null;

            try {
                Log.e("mBluetoothSocket", mBlueethoothSocket.toString());
                tempInputStream = mBlueethoothSocket.getInputStream();
                tempOutputStream = mBlueethoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("mBluetoothSocket", "no se conecto");
            }
            mInputStream = tempInputStream;
            mOutputStream = tempOutputStream;
            mConnectedStarted = true;


        }


        public void run() {
            byte[] buffer = new byte[1024];

            while (true) {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    int bytes = mInputStream.read(buffer);
                    String incomingMessages = new String(buffer, 0, bytes);
                    if (incomingMessages != null) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction("bluetooth_message");
                        Log.e("Broadcast_bluetooth1", incomingMessages);
                        if (!incomingMessages.equals("dd") || !incomingMessages.equals("aa"))
                            sendIntent.putExtra("mensaje", incomingMessages);
                        mContext.sendBroadcast(sendIntent);
                    }


                } catch (Exception e) {
                    Log.e("error bluetooth", e.getMessage());
                    break;
                }

            }

        }


        public boolean write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            try {
                mOutputStream.write(bytes);
                return true;
            } catch (IOException e) {
                e.printStackTrace();

            }
            mConnectedStarted = false;
            return false;
        }


        public void cancel() {
            try {
                mBlueethoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void recibir() {

        }


    }

}
