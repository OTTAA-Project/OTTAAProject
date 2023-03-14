package com.stonefacesoft.ottaa;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class BuscarArasaacTest  {
    public BuscarArasaac buscarArasaac = new BuscarArasaac();

    @Test
    public void testHacerBusqueda() {
        JSONObject jsonObjectExpected;
        String response = "{ \"symbols\": [{ \"imagePNGURL\": \"http://old.arasaac.org/repositorio/originales/34567.png\",\"name\": \"hola\", \"wordTYPE\": \"5\",\"CreationDate\": \"2019-03-05 21:14:37\", \"ModificationDate\": \"2019-03-05 21:14:37\",\"thumbnailURL\": \"http://old.arasaac.org/classes/img/thumbnail.php?i=c2l6ZT0xNTAmcnV0YT0uLi8uLi9yZXBvc2l0b3Jpby9vcmlnaW5hbGVzLzM0NTY3LnBuZw==\" }, { \"imagePNGURL\": \"http://old.arasaac.org/repositorio/originales/6522.png\",\"name\": \"hola\", \"wordTYPE\": \"5\",\"CreationDate\": \"2008-12-02 10:08:06\", \"ModificationDate\": \"2008-12-02 10:08:06\",\"thumbnailURL\": \"http://old.arasaac.org/classes/img/thumbnail.php?i=c2l6ZT0xNTAmcnV0YT0uLi8uLi9yZXBvc2l0b3Jpby9vcmlnaW5hbGVzLzY1MjIucG5n\" }, { \"imagePNGURL\": \"http://old.arasaac.org/repositorio/originales/6009.png\",\"name\": \"hola\", \"wordTYPE\": \"5\",\"CreationDate\": \"2008-11-06 11:47:11\", \"ModificationDate\": \"2008-11-06 11:47:11\",\"thumbnailURL\": \"http://old.arasaac.org/classes/img/thumbnail.php?i=c2l6ZT0xNTAmcnV0YT0uLi8uLi9yZXBvc2l0b3Jpby9vcmlnaW5hbGVzLzYwMDkucG5n\" } ], \"itemCount\": 1000, \"page\": 0, \"totalItemCount\": 3, \"pageCount\": 0 }";
        try {
            jsonObjectExpected = new JSONObject(response);
            //TODO ver si hay q poner una interfaz o que hacer

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}