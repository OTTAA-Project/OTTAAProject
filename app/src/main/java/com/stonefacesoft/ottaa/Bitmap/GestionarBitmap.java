package com.stonefacesoft.ottaa.Bitmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Gonzalo Juarez
 * @version 2.0
 * Created by gonzalo on 1/29/18.
 * edited on 5/5/2021
 */

public class GestionarBitmap  {
    private ArrayList<Bitmap> imagenes;//Arreglo para obtener todos los bitmaps
    private ArrayList<JSONObject> idjson;//arreglo para obtener el json
    private int tamano;//permite determinar donde se va a ubicar la imagen
    private File imgs;//archivo que se crea
    private final Context mContext;//contexto de la aplicacion
    private String mCurrentPhotoPath;//direccion donde se guarda el archivo
    private  String nombre;//nombre de la imagen
    private  String texto;
    private SharedPreferences sharedPrefs;//me va a permitir obtener el idioma del texto
    private boolean noTemp;//bandera que permite saber si el archivo es temporal o no
    private boolean escribirTexto;
    private final Json json;
    private int color=R.color.FondoApp;
    private final String TAG="GestionarBitmap";


    public GestionarBitmap(Context context) {

        mContext =context;
        imagenes=new ArrayList<>();
        idjson=new ArrayList<>();
        Json.getInstance().setmContext(mContext);
        json = Json.getInstance();


    }


    public void setTexto(String texto) {
        this.texto=texto;
    }

    public String getTexto() {
        return texto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Bitmap> getImagenes() {
        return imagenes;
    }

    public boolean isNoTemp() {
        return noTemp;
    }

    public void setNoTemp(boolean noTemp) {
        this.noTemp = noTemp;
    }

    public void setImagenes() {

        this.imagenes =new ArrayList<>();
        //this.imagenes=imagenes;
    }
    //metodo para almacenar archivo
    private void storeImage(Bitmap image)
    {
        try {
            imgs = getOutputMediaFile();
             if(imgs == null)
                 imgs = File.createTempFile("imagen",".png",mContext.getExternalCacheDir());

                FileOutputStream fos = new FileOutputStream(imgs);
                int width = 0;
                int height = 0;
                if (image == null) {
                    width = 200;
                    height = 200;
                } else {
                    width = image.getWidth();
                    height = image.getHeight();
                }
                image = Bitmap.createScaledBitmap(image, width, height, false);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch(FileNotFoundException e){
                Log.e("errorGB_StoreImage_Foto", "File not found: " + e.getMessage());
            } catch(IOException e){
                Log.e("errorGB_StoreImage_Foto", "Error accessing file: " + e.getMessage());
            }
            Log.d("GB_StoreImage_Foto", "PATH>> " + mCurrentPhotoPath);
    }
    //metodo para obtener la imagen
    public File getOutputMediaFile()
    {
        UriFiles filesUtils = new UriFiles(mContext);
        String path="";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            path=filesUtils.downloadAfterAndroidQ();
        }else
            path=filesUtils.downloadBeforeAndroidQ();
        File mediaStorageDir= new File(path);
        //direccionn real donde se va a ubicar el archivo
        mCurrentPhotoPath = (mediaStorageDir.getPath() + File.separator + nombre);
        if(!noTemp)
        {
            try {
                //si es temporal creo un archivo en la cache
                imgs = File.createTempFile(nombre, ".jpg", mContext.getExternalCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {//si no lo es lo genero en una direccion real para poder compartirlo
            imgs = new File(mediaStorageDir.getPath() + File.separator + nombre);
        }

        if (! mediaStorageDir.exists())
        {
            if (! mediaStorageDir.mkdirs())
            {
                return null;
            }
        }
        return imgs;
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * Combine image method
     * */
    public Bitmap combineImages(int mDeltax,int mDeltay) {
        Bitmap mImagenFinal = null;

        int mImagenFinalWidth, mImagenFinalHeight = 0;

        if (imagenes.size() > 0 && imagenes.get(0) != null) {

            Bitmap mBufferPictos = Bitmap.createScaledBitmap(imagenes.get(0), 250, 250, false);
            Bitmap logo=drawableToBitmap(mContext.getResources().getDrawable(R.drawable.logo_ottaa));

            logo=redimensionarImagenMaximo(logo,mBufferPictos.getWidth(),mBufferPictos.getHeight()/4);
            mImagenFinalWidth = (mBufferPictos.getWidth()+mDeltax) * imagenes.size()+mDeltax;
            mImagenFinalHeight = 3*mDeltay+mBufferPictos.getHeight()+logo.getHeight();
            //tamano = 0;
            mImagenFinal = Bitmap.createBitmap(mImagenFinalWidth, mImagenFinalHeight, Bitmap.Config.ARGB_8888);

            Canvas comboImage = new Canvas(mImagenFinal);
            Paint pintura=new Paint();
            pintura.setColor(mContext.getResources().getColor(R.color.FondoApp));
            pintura.setStrokeWidth(20);

            comboImage.drawRect(0,0,mImagenFinal.getWidth(),mImagenFinal.getHeight(),pintura);
            for (int j = 0; j < imagenes.size(); j++) {
                int despx =j*((mDeltax)+mBufferPictos.getWidth())+mDeltax;
                //Bitmap imgRedimensionada=redimensionarImagenMaximo(imagenes.get(j),mBufferPictos.getWidth(), mBufferPictos.getHeight());
                //comboImage.drawLine(despx,0,despx,mImagenFinal.getHeight(),pintura);
                comboImage.drawBitmap(redimensionarImagenMaximo(imagenes.get(j), mBufferPictos.getWidth(), mBufferPictos.getHeight()), despx, mDeltay, null);
            }
            Paint pinturas=new Paint();
            pinturas.setAlpha(200);
            pinturas.setARGB(200,255,255,255);

            int mPosicionLogoX,mPosicionLogoY;

            mPosicionLogoX = mImagenFinalWidth-mDeltax-logo.getWidth();
            mPosicionLogoY = mImagenFinalHeight-mDeltay-logo.getHeight();
            comboImage.drawBitmap(logo,mPosicionLogoX,mPosicionLogoY,pinturas);
        }
        return mImagenFinal;
    }
    /**
     *  this method transform the drawables on bitmaps
     */
    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable != null) {
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }




    //metodo para redimensionar los bitmaps
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeigth / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    //metodo para armar las imagenes
    public void createImage() {
        storeImage(combineImages(25,25));
    }



    public File getImgs() {
        imgs=getOutputMediaFile();
        return imgs;
    }

    public void setImgs(File imgs) {
        imgs = imgs;
    }
    public void resetearImagen()
    {
        imagenes=new ArrayList<>();
    }

    private Integer cargarColor (int tipo)
    {
        switch (tipo){
            case 1:
                return  mContext.getResources().getColor(R.color.Yellow);
            case 2:
                return  mContext.getResources().getColor(R.color.Orange);
            case 3:
                return  mContext.getResources().getColor(R.color.YellowGreen);
            case 4:
                return  mContext.getResources().getColor(R.color.DodgerBlue);
            case 5:
                return  mContext.getResources().getColor(R.color.Magenta);
            case 6:
                return  mContext.getResources().getColor(R.color.Black);
            default:
                return  mContext.getResources().getColor(R.color.White);
        }
    }



    public ArrayList<JSONObject> getIdjson() {
        return idjson;
    }

    public void setIdjson(ArrayList<JSONObject> idjson) {
        this.idjson = idjson;
    }


    //Devolvemos un bitmap con todos los pictos componentes de una frase
    public Bitmap getBitmapDeFrase(JSONObject frase) {
        try {
            if (frase.has("complejidad")&&frase.getJSONObject("complejidad") != null) {
                imagenes = new ArrayList<>();
                for (int i = 0; i < frase.getJSONObject("complejidad").getJSONArray("pictos componentes").length(); i++) {
                    JSONObject picto = json.getPictoFromId2(frase.getJSONObject("complejidad")
                            .getJSONArray("pictos componentes").getJSONObject(i).getInt("id"));
                    Bitmap logo = drawableToBitmap(AppCompatResources.getDrawable(mContext, R.drawable.logo_ottaa));
                    Bitmap nube = drawableToBitmap(AppCompatResources.getDrawable(mContext, R.drawable.ic_baseline_cloud_download_24_big));//para evitar que no funcionen las frases mas usadas se pone el icono de la nube
                    Bitmap imagen = drawableToBitmap(json.getIcono(picto));

                    if (imagen != null) {
                        imagenes.add(imagen);
                    } else {
                        imagenes.add(nube);
                    }
                }
            } else {
                imagenes = new ArrayList<>();
                for (int i = 0; i < frase.getJSONArray("picto_componentes").length(); i++) {
                    JSONObject picto = json.getPictoFromId2(frase.getJSONArray("picto_componentes").getJSONObject(i).getInt("id"));
                    Bitmap logo = drawableToBitmap(AppCompatResources.getDrawable(mContext, R.drawable.logo_ottaa));
                    Bitmap nube = drawableToBitmap(AppCompatResources.getDrawable(mContext, R.drawable.ic_baseline_cloud_download_24_big));//para evitar que no funcionen las frases mas usadas se pone el icono de la nube
                    Bitmap imagen = drawableToBitmap(json.getIcono(picto));

                    if (imagen != null) {
                        imagenes.add(imagen);
                    } else {
                        imagenes.add(nube);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (combineImages(20, 20) != null)
        return getRoundedCornerBitmap(combineImages(20, 20), 20);
        else
            return null;
    }


    public void setEscribirTexto(boolean escribirTexto) {
        this.escribirTexto = escribirTexto;
    }
    public void setColor(int color){
        this.color=color;
    }
    public Bitmap setUpLogo(Bitmap mBufferPictos){
        Bitmap logo=drawableToBitmap(mContext.getResources().getDrawable(R.drawable.logo_ottaa));
        logo=redimensionarImagenMaximo(logo,mBufferPictos.getWidth()/4,mBufferPictos.getHeight()/6);
        return logo;
    }
}
