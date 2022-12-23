package com.stonefacesoft.ottaa.utils.Pictures;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;

public class DownloadArasaac extends DownloadTask{
    public DownloadArasaac(AppCompatActivity context, String text, int tipo) {
        super(context, text, tipo);
    }

    @Override
    protected void makeAction(String result) {
        Intent databack = new Intent();
        if (mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            Toast.makeText(appCompatActivity, appCompatActivity.getString(R.string.error_download) + result, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(appCompatActivity, R.string.file_download, Toast.LENGTH_SHORT).show();
            databack.putExtra("Path", path);
            databack.putExtra("Type", tipo);
            databack.putExtra("Text", text);

        }
        appCompatActivity.setResult(IntentCode.ARASAAC.getCode(), databack);
        appCompatActivity.finish();
    }
}
