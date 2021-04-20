package com.stonefacesoft.ottaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;


//Source https://github.com/ArthurHub/Android-Image-Cropper
// https://github.com/ArthurHub/Android-Image-Cropper/wiki/Using-CropImageView-in-own-Activity

public class PictureCropper extends AppCompatActivity {
    private static final String TAG = "PictureCropper";
    CropImageView cropImageView;

    Uri croppedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO hacer la version para sw600
        setContentView(R.layout.activity_picture_cropper);

        cropImageView = findViewById(R.id.cropImageView);

        Log.d(TAG, "onCreate: Getting the URI for picture");
        final Uri imageUri = getIntent().getParcelableExtra("pickedImageUri");

        Log.d(TAG, "onCreate: Customizing the cropper");
        cropImageView.setImageUriAsync(imageUri);
        cropImageView.setCropShape(CropImageView.CropShape.OVAL);
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
            File tempFile = File.createTempFile("avatar","jpg");
            croppedImageUri = Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnTalk) {
            //Confirm crop and save
            Log.d(TAG, "onClick: saving file with uri: "+croppedImageUri.toString() + "Quality 400x400");
            cropImageView.saveCroppedImageAsync(croppedImageUri, Bitmap.CompressFormat.PNG,100,300,300);
            cropImageView.getCroppedImageAsync();
        } else if (id == R.id.left_button) {
            //Rotate the image to the left
            //cropImageView.rotateImage(-45);

        } else if (id == R.id.right_button) {
            //Rotate the image to the left
            //cropImageView.rotateImage(45);

        } else if (id == R.id.back_button) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("imageUri",croppedImageUri);
            setResult(CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE,resultIntent);
            finish();
        }
    }
}