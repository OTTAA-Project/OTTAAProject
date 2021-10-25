package com.stonefacesoft.ottaa.Activities.Pictures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.stonefacesoft.ottaa.R;


//Source https://github.com/ArthurHub/Android-Image-Cropper
// https://github.com/ArthurHub/Android-Image-Cropper/wiki/Using-CropImageView-in-own-Activity

public class PictureCropper extends AppCompatActivity {
    protected String TAG = "PictureCropper";
    protected CropImageView cropImageView;
    protected Uri croppedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_cropper);
        cropImageView = findViewById(R.id.cropImageView);
        final Uri imageUri = getIntent().getParcelableExtra("pickedImageUri");
        Log.d(TAG, "onCreate: Customizing the cropper");
        cropImageView.setImageUriAsync(imageUri);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnTalk) {
            //Confirm crop and save
            Log.d(TAG, "onClick: saving file with uri: "+croppedImageUri.toString() + "Quality 400x400");
            cropImageView.startCropWorkerTask(300,300, CropImageView.RequestSizeOptions.RESIZE_INSIDE, Bitmap.CompressFormat.PNG,100,croppedImageUri);
          //  cropImageView.saveCroppedImageAsync(croppedImageUri,Bitmap.CompressFormat.PNG, 100,300,300, CropImageView.RequestSizeOptions.RESIZE_INSIDE);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    protected void initComponents(){

    }
}