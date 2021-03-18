package com.stonefacesoft.ottaa.utils.Bluetooth;

import android.os.ParcelUuid;

import com.stonefacesoft.ottaa.utils.Bluetooth.BluetoothUtils.BtUtils;

public class BtDeviceItem {
    private String deviceName;
    private String address;
    private String uuid;


    public String getAddress() {
        return address;
    }

    public String getDeviceName() {
        return deviceName;
    }



    public void setAddress(String address) {
        this.address = address;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public BtDeviceItem(String name, String address, ParcelUuid[] uuids) {
        this.deviceName = name;
        this.address = address;
        this.uuid = BtUtils.getUuidString(uuids);
    }

    public BtDeviceItem(String name, String address) {
        this.deviceName = name;
        this.address = address;
        this.uuid = "";
    }

    public String getUuid() {
        return uuid;
    }
}
