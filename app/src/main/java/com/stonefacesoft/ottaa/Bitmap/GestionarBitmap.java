package com.stonefacesoft.ottaa.Bitmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import com.google.android.datatransport.cct.internal.LogEvent;
import com.stonefacesoft.ottaa.Interfaces.LoadOnlinePictograms;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.R;

import org.json.JSONArray;
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
    private LoadOnlinePictograms loadOnlinePictograms;



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
    private void storeImage(Bitmap image,LoadOnlinePictograms loadOnlinePictograms)
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

        try{
            loadOnlinePictograms.loadPictograms(image);
            loadOnlinePictograms.FileIsCreated();
        }catch (Exception ex){
            Log.e(TAG, "storeImage: "+ex.getMessage() );
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
                imgs = File.createTempFile(nombre, ".png", mContext.getCacheDir());
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
            Bitmap nube=drawableToBitmap(mContext.getResources().getDrawable(R.drawable.ic_cloud_download_orange));
            Bitmap bitmap = nube;
            try {
                bitmap = imagenes.get(0);
            }catch (Exception ex){
                bitmap = nube;
            }
            Bitmap mBufferPictos = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
            Bitmap logo=drawableToBitmap(mContext.getResources().getDrawable(R.drawable.logo_ottaa_dev));
            logo = Bitmap.createScaledBitmap(logo,75,75,true);

            mImagenFinalWidth = (mBufferPictos.getWidth()+mDeltax) * imagenes.size()+mDeltax;
            mImagenFinalHeight = 3*mDeltay+mBufferPictos.getHeight()+logo.getHeight();
            //tamano = 0;
            mImagenFinal = Bitmap.createBitmap(mImagenFinalWidth, mImagenFinalHeight, Bitmap.Config.ARGB_8888);

            Canvas comboImage = new Canvas(mImagenFinal);
            Paint pintura = preparePaint(20,0,0,0,0,false,true) ;
            pintura.setColor(mContext.getResources().getColor(color));
            comboImage.drawRect(0,0,mImagenFinal.getWidth(),mImagenFinal.getHeight(),pintura);
            for (int j = 0; j < imagenes.size(); j++) {
                int despx =j*((mDeltax)+mBufferPictos.getWidth())+mDeltax;
                if(imagenes.get(j)!=null)
                    comboImage.drawBitmap(redimensionarImagenMaximo(imagenes.get(j), mBufferPictos.getWidth(), mBufferPictos.getHeight()), despx, mDeltay, null);
                else
                    comboImage.drawBitmap(redimensionarImagenMaximo(nube, mBufferPictos.getWidth(), mBufferPictos.getHeight()), despx, mDeltay, null);
            }
            Paint pinturas= preparePaint(20,200,255,255,255,false,true);
            int mPosicionLogoX,mPosicionLogoY;
            mPosicionLogoX =mImagenFinalWidth-logo.getWidth()- mDeltax;
            mPosicionLogoY = mImagenFinalHeight-mDeltay-logo.getHeight();
            comboImage.drawBitmap(logo,mPosicionLogoX,mPosicionLogoY,pinturas);
        }
        return mImagenFinal;
    }


    private Paint preparePaint(int stroke,int alpha, int red,int green,int blue,boolean useARGB,boolean useStroke){
        Paint pintura = new Paint();
        if(useStroke)
            pintura.setStrokeWidth(stroke);
        if(useARGB){
            pintura.setAlpha(alpha);
            pintura.setARGB(alpha,red,green,blue);
        }
        return pintura;
    }

    /**
     *  this method transform the drawables on bitmaps
     */
    public int getSize(Bitmap bitmap,boolean isWidth){
        if(bitmap != null){
            if(isWidth)
                return bitmap.getWidth();
            return bitmap.getHeight();}

        return 100;
    }

    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;
        try{
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
        }catch (Exception ex){
            Log.e(TAG, "drawableToBitmap: "+ ex.getMessage() );
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
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
    }

    //metodo para armar las imagenes
    public void createImage(LoadOnlinePictograms loadOnlinePictograms) {
        storeImage(combineImages(25,25),loadOnlinePictograms);
    }



    public File getImgs() {
        imgs = getOutputMediaFile();
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
    public void getBitmapDeFrase(JSONObject frase,LoadOnlinePictograms loadOnlinePictograms) {
        preparePhrase(frase,loadOnlinePictograms);
        Bitmap bitmap = null;
        if(combineImages(20, 20) != null ) {
            bitmap = getRoundedCornerBitmap(combineImages(20, 2), 20);
            loadOnlinePictograms.loadPictograms(bitmap);
        }else{
            Log.d(TAG, "getBitmapDeFrase: "+ "Combine Images Error");
        }
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



    public JSONArray getJsonArray(JSONObject frase) throws JSONException {
        if(frase.has("complejidad")&&frase.getJSONObject("complejidad") != null){
            return frase.getJSONObject("complejidad").getJSONArray("pictos componentes");
        }
        return  frase.getJSONArray("picto_componentes");
    }

    public void  preparePhrase(JSONObject phrase,LoadOnlinePictograms loadOnlinePictograms){
        imagenes = new ArrayList<>();
        CombineImages mCombineImages = new CombineImages(mContext);
        JSONObject picto = null;
        JSONArray array = null;

        try {
            array = getJsonArray(phrase);
            for (int i = 0; i <= array.length()-1; i++) {
                picto = json.getPictoFromId2(array.getJSONObject(i).getInt("id"));
                Drawable drawable = mCombineImages.loadPictogram(json,picto);
                if(drawable == null){
                    drawable = mCombineImages.loadPictogramsLogo(picto);
                }
                if(drawable !=null)
                    imagenes.add(drawableToBitmap(drawable));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setLoadOnlinePictograms(LoadOnlinePictograms loadOnlinePictograms) {
        this.loadOnlinePictograms = loadOnlinePictograms;
    }
}
