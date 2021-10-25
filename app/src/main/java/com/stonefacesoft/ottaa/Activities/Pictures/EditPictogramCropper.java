package com.stonefacesoft.ottaa.Activities.Pictures;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class EditPictogramCropper extends PictureCropper{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "EditPictogramCropper";
        initComponents();
    }

    @Override
    protected void initComponents() {
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setAspectRatio(1,1);
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                Log.d(TAG, "onCropImageComplete: Creating the intent with uri: "+croppedImageUri.toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imageUri",croppedImageUri);
                setResult(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE,resultIntent);
                finish();
            }
        });

        try {
            File tempFile = File.createTempFile("pictograms","jpg");
            croppedImageUri = Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
