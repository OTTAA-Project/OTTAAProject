package com.stonefacesoft.ottaa.Interfaces;

public  interface interface_show_dialog_options {
    void showLanguajeDialog(String name, int value, int options);
    void showDialogOptionsDownloadFile(String name, int value, int options, String preferences);
    void showDialogOptionsSettings(String name, int value, int options, String preferences);
    void showDialogOptionsSkilledHand(String name, int value, int options, String preferences);
    void showDialogOptionsSkilledHand(String name, boolean value, int values, int options, String preferences);
    void showDialogOptionsDownloadFile(String name, String key, String value, int values, int options, String preference);


}
