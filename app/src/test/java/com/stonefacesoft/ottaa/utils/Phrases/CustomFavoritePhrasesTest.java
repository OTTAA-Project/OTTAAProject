package com.stonefacesoft.ottaa.utils.Phrases;

import com.stonefacesoft.ottaa.Principal;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CustomFavoritePhrasesTest {
    CustomFavoritePhrases customFavoritePhrases = new CustomFavoritePhrases();

    @Test
    public void addFavoritePhrase() {
        JSONArray phrase = Phrases();
        try {
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(0));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(1));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(2));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(phrase.getJSONObject(0).toString(), customFavoritePhrases.getPhrases().getJSONObject(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void isExist() {
        JSONArray phrase = Phrases();
        try {
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(0));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(1));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(2));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(customFavoritePhrases.isExist(phrase.getJSONObject(1)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeFavoritePhrase() {
        JSONArray phrase = Phrases();
        try {
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(0));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(1));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(2));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            customFavoritePhrases.removeFavoritePhrase(phrase.getJSONObject(3));
            assertTrue(!customFavoritePhrases.isExist(phrase.getJSONObject(3)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPhrases() {
        JSONArray phrase = Phrases();
        try {
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(0));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(1));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(2));
            customFavoritePhrases.addFavoritePhrase(phrase.getJSONObject(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(4, customFavoritePhrases.getPhrases().length());
    }

    @Test
    public void getJson() {
        assertNotNull(customFavoritePhrases.getJson());
    }

    public JSONArray Phrases() {
        try {
            return new JSONArray("[{\"frase\":\"yo quiero ir a casa \",\"frecuencia\":1,\"complejidad\":{\"valor\":0,\"pictos componentes\":[{\"id\":643,\"esSugerencia\":false},{\"id\":44,\"esSugerencia\":false},{\"id\":30,\"esSugerencia\":false},{\"id\":653,\"esSugerencia\":false}]},\"fecha\":1620650383499,\"locale\":\"es\",\"id\":0},{\"frase\":\"Hola ¿Cómo estás? estoy aburrido quiero \",\"frecuencia\":1,\"complejidad\":{\"valor\":0,\"pictos componentes\":[{\"id\":377,\"esSugerencia\":false},{\"id\":378,\"esSugerencia\":false},{\"id\":22,\"esSugerencia\":false},{\"id\":119,\"esSugerencia\":false},{\"id\":44,\"esSugerencia\":false}]},\"fecha\":1620652671707,\"locale\":\"es\",\"id\":2},{\"frase\":\"acompañar \",\"frecuencia\":4,\"complejidad\":{\"valor\":0,\"pictos componentes\":[{\"id\":1,\"hora\":[\"TARDE\",\"MEDIODIA\"],\"edad\":[\"JOVEN\"],\"sexo\":[\"MASCULINO\"],\"ubicacion\":[\"CAFE\",\"BAR\"]}]},\"fecha\":[1620848827861,1620848833313,1620848876219,1620848936363],\"locale\":\"es\",\"id\":5},{\"frase\":\"Hola Buenas tardes \",\"frecuencia\":3,\"complejidad\":{\"valor\":0,\"pictos componentes\":[{\"id\":377,\"esSugerencia\":false},{\"id\":380,\"horario\":[\"MEDIODIA\",\"TARDE\"],\"esSugerencia\":false,\"hora\":[\"TARDE\"]}]},\"fecha\":[1620673767927,1621017780404,1621017791290],\"locale\":\"es\",\"id\":4}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}