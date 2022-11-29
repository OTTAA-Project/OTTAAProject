package com.stonefacesoft.ottaa;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.stonefacesoft.ottaa.Interfaces.DrawableInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by morro on 22/6/2016.
 */
public class DrawableManager {
    private final Map<String, Drawable> drawableMap;
    private final String TAG="DrawableManager";
    private ImageLoader imageLoader;

    public DrawableManager() {
        drawableMap = new HashMap<String, Drawable>();
    }

    public Drawable fetchDrawable(String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");
            if (drawable != null) {
                drawableMap.put(urlString, drawable);
                Log.d(TAG, "got a thumbnail drawable: " + drawable.getBounds() + ", "
                        + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                        + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            } else {
                Log.d(TAG, "could not get thumbnail");
            }

            return drawable;
        } catch (IOException e) {
            Log.e(TAG, "fetchDrawable failed", e);
            return null;
        }
    }

    public Drawable fetchDrawable(String urlString, DrawableInterface drawableInterface) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");
            if (drawable != null) {
                drawableMap.put(urlString, drawable);
                drawableInterface.getDrawable(drawable);
                drawableInterface.fetchDrawable(drawable);
            } else {
                Log.d(TAG, "could not get thumbnail");
            }


        } catch (IOException e) {
            Log.e(TAG, "fetchDrawable failed", e);

        }
        return null;
    }

    public Drawable fetchDrawable(String urlString,int position, DrawableInterface drawableInterface) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = Drawable.createFromStream(is, "src");
            if (drawable != null) {
                drawableMap.put(urlString, drawable);
                Log.d(TAG, "got a thumbnail drawable: " + drawable.getBounds() + ", "
                        + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                        + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
                drawableInterface.getDrawable(drawable);
                drawableInterface.fetchDrawable(drawable);
            } else {
                Log.d(TAG, "could not get thumbnail");
            }


        } catch (IOException e) {
            Log.e(TAG, "fetchDrawable failed", e);

        }
        return null;
    }



    private InputStream fetch(String urlString) throws IOException {
        Log.d(TAG, "url saved : "+ urlString);
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        if(urlConnection.getAllowUserInteraction())
            return urlConnection.getInputStream();
        return  null;
    }
}
