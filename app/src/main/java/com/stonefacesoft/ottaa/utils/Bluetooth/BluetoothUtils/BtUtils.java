package com.stonefacesoft.ottaa.utils.Bluetooth.BluetoothUtils;

import android.os.ParcelUuid;

import java.util.UUID;

public class BtUtils  {

    public static UUID getUuid(ParcelUuid[] uuids){
        if(uuids==null)
            return null;
         else
            return uuids[0].getUuid();
    }

    public static String getUuidString(ParcelUuid[] uuids) {
        StringBuilder stringBuilder = new StringBuilder();
        if (uuids == null)
            return "";
        ParcelUuid uuid = uuids[0];
        stringBuilder.append(uuid.getUuid().toString());
        return stringBuilder.toString();
    }
}
