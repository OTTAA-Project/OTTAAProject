package com.stonefacesoft.ottaa.utils.Phrases;

import android.content.Context;

import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.pictogramslibrary.JsonUtils.JSONObjectManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class CustomFavoritePhrases {
    private Context mContext;
    private Json json;
    private JSONArray favoritePhrases;

    /**
     * Posibles ideas
     *  El sistema debe permitir seleccionar cuales son las frases favoritas del usuario
     *  El sistema debe mostrar las mismas en un dialogo
     *  El sistema esta limitado a 10 frases favoritas
     *  El sistema debe indicar cuales son las frases favoritas por uso y cuales son las favoritas personalizadas
     *  El sistema debe permitir modificar la posicion de las mismas
     *  El sistema debe permitir eliminar cualquiera de las frases
     *  Cada una de las frases debe estar filtrada por idioma
     * */

     public CustomFavoritePhrases(Context mContext){
         this.mContext=mContext;
         json=Json.getInstance();
         json.setmContext(this.mContext);
         favoritePhrases=json.getmJSonArrayFrasesFavoritas();
         if(favoritePhrases==null)
             favoritePhrases=new JSONArray();
     }

     public boolean addFavoritePhrase(JSONObject object){
         boolean existPhrase=isExist(object);
         if(!existPhrase){
            if(favoritePhrases.length()<10){
                favoritePhrases.put(object);
                return true;
            }
         }
            return false;
     }

     public boolean isExist(JSONObject object){
         boolean existPhrase=false;
         JSONObjectManager manager=new JSONObjectManager();
         for (int i = 0; i <favoritePhrases.length() ; i++) {
             try {
                 String phrase=manager.JsonObjectGetString(object,"frase","CustomFavoritePhrases");
                 if(manager.JsonObjectGetString(favoritePhrases.getJSONObject(i),"frase","CustomFavoritePhrases").contentEquals(phrase))
                     existPhrase=true;
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         return existPhrase;
     }

     public boolean removeFavoritePhrase(JSONObject phrase){
         for (int i = 0; i <favoritePhrases.length() ; i++) {
             try {
                 if(favoritePhrases.getJSONObject(i).getString("frase").contentEquals(phrase.getString("frase"))) {
                     favoritePhrases.remove(i);
                     return true;
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         return false;
     }


     public JSONArray getPhrases(){
         return favoritePhrases;
     }

    public Json getJson() {
        return json;
    }

    public void saveFavoritePhrases(){
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput(Constants.ARCHIVO_FRASES_FAVORITAS, Context.MODE_PRIVATE);
            outputStream.write(favoritePhrases.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }



    
}
