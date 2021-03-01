package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hector on 09/03/2016.
 *
 */
public class ArchivoImagenes {

    private String mCurrentPhotoPath;
    private static final String TAG = "ArchivoImagenes";

    public Bitmap getBitmapFromUri(Context c, Intent data) {
        Uri imageUri = data.getData();
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), imageUri);
        } catch (IOException e) {
            bitmap = null;
            Log.e(TAG, "getBitmapFromUri: Error:" + e.getMessage());
        }
        return bitmap;
    }


    public Bitmap decodeUri(Uri uri, Context context) {
        ParcelFileDescriptor parcelFD = null;
        Bitmap bitmap = null;
        try {
            parcelFD = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 200;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

        } catch (IOException e) {
            Log.e(TAG, "decodeUri: Error:" + e.getMessage());
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
        return bitmap;
    }

    public void storeImage(Bitmap image, Context c) {
        //AI_Foto:ArchivoImagenes_Foto
        File pictureFile = getOutputMediaFile(c);
        if (pictureFile == null) {
            Log.d(TAG,"AI_FOTO : "+"Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image = Bitmap.createScaledBitmap(image, 500, 500, false);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG,"AI_FOTO : "+"File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,"AI_FOTO : "+"Error accessing file: " + e.getMessage());
        }
        Log.d(TAG,"AI_FOTO : "+ "PATH>> " + mCurrentPhotoPath);
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(Context c) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + c.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mCurrentPhotoPath = (mediaStorageDir.getPath() + File.separator + mImageName);
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        return mediaFile;
    }

}
