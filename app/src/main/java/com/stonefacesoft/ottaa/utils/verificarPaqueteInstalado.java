package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class verificarPaqueteInstalado {
    Context mContext;

    public verificarPaqueteInstalado(Context context) {
        this.mContext = context;
    }

    public boolean estaInstalado(String texto) {
        PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packInfo : list) {
            if (packInfo.packageName.equals(texto)) {
                return true;
            }
        }
        return false;
    }
}
