package com.stonefacesoft.ottaa.test;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.stonefacesoft.ottaa.Prediction.Clima;
import com.stonefacesoft.ottaa.Prediction.Edad;
import com.stonefacesoft.ottaa.Prediction.Horario;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.Prediction.Sexo;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

public class JSONutilsTest {

    @Test
    public void getIDfromNombreTest() {
        JSONArray jsonArray = createPictogramJSONArray();
        try {
            assertEquals(22,JSONutils.getIDfromNombre("want",jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stringToArrayListTest() {
        String string = "[{\"id\":0,\"tipo\":111},{\"id\":1,\"tipo\":222},{\"id\":2,\"tipo\":333},{\"id\":3,\"tipo\":444},{\"id\":4,\"tipo\":555},{\"id\":5,\"tipo\":666}]";
        ArrayList<JSONObject> arrayList = JSONutils.stringToArrayList(string);

        try {
            assertEquals(0,arrayList.get(0).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNombreTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        assertEquals("apple",JSONutils.getNombre(jsonObject,"en"));
        assertEquals("manzana",JSONutils.getNombre(jsonObject,"es"));
        assertEquals("",JSONutils.getNombre(jsonObject,"ec"));

    }

    @Test
    public void getTipoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        assertEquals(2,JSONutils.getTipo(jsonObject));
    }

    @Test
    public void getWordTypeTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        try {
            jsonObject.put("wordTYPE",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals(1,JSONutils.getWordType(jsonObject));
    }

    @Test
    public void setHijosGrupo2Test() {
        JSONArray arrayPadre = createPictogramJSONArray();
        JSONArray arrayHijo = createPictogramJSONArray();
        JSONutils.setHijosGrupo2(arrayPadre,arrayHijo,1);
        try {
            assertEquals(arrayHijo,arrayPadre.getJSONObject(1).getJSONArray("relacion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setPosicionTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setPosicion(jsonObject, Posicion.BANCO);

        try {
            assertEquals(Posicion.BANCO,jsonObject.getJSONArray(Constants.UBICACION).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONutils.setPosicion(jsonObject, Posicion.PANADERIA);

        try {
            assertEquals(Posicion.PANADERIA,jsonObject.getJSONArray(Constants.UBICACION).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void setNombreTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setNombre(jsonObject,"Manzana Rica","Mela rica","es","it");

        assertEquals("Mela rica",JSONutils.getNombre(jsonObject,"it"));
    }

    @Test
    public void setTipoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setTipo(jsonObject,6);

        assertEquals(6,JSONutils.getTipo(jsonObject));
    }

    @Test
    public void setImagenTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setImagen(jsonObject,"img","url","pushKey");

        try {
            assertEquals("url",jsonObject.getJSONObject("imagen").getString("urlFoto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getImagenTest(){
        JSONObject jsonObjectActual = createPictograms(474,"es","manzana","apple",2);
        //Test picto original
        try {
            JSONObject jsonObjectExpected = new JSONObject().put("type", 2).put("picto", "ic_action_previous");
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Test picto editado
        try {
            JSONutils.setImagen(jsonObjectActual,"img","url","pushKey");
            JSONObject jsonObjectExpected = new JSONObject().put("type", 1).put("pictoEditado", "img");
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Test null
        try {
            jsonObjectActual.remove("imagen");
            JSONObject jsonObjectExpected = new JSONObject().put("type", 3);
            assertEquals(jsonObjectExpected.getInt("type"),JSONutils.getImagen(jsonObjectActual).getInt("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void desvincularJsonTest() {
        JSONObject jsonObject = createPictograms(643,"es","Yo","I",1);
        try {
            int lenghtExpected = jsonObject.getJSONArray("relacion").length();
            JSONutils.desvincularJson(jsonObject,118);
            assertEquals(lenghtExpected-1,jsonObject.getJSONArray("relacion").length());

            //Test without relacion
            jsonObject.remove("relacion");
            JSONutils.desvincularJson(jsonObject,1);
            assertNull(jsonObject.getJSONObject("relacion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setHorarioTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setHorario(jsonObject, Horario.MANANA);
        try {
            assertEquals(Horario.MANANA,jsonObject.getJSONArray(Constants.HORA).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setHorario(jsonObject, Horario.MEDIODIA);
        try {
            assertEquals(Horario.MEDIODIA,jsonObject.getJSONArray(Constants.HORA).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setSexoTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setSexo(jsonObject, Sexo.MASCULINO);
        try {
            assertEquals(Sexo.MASCULINO,jsonObject.getJSONArray(Constants.SEXO).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setSexo(jsonObject, Sexo.BINARIO);
        try {
            assertEquals(Sexo.BINARIO,jsonObject.getJSONArray(Constants.SEXO).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setEdadTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setEdad(jsonObject, Edad.ADULTO);
        try {
            assertEquals(Edad.ADULTO,jsonObject.getJSONArray(Constants.EDAD).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setEdad(jsonObject, Edad.NINO);
        try {
            assertEquals(Edad.NINO,jsonObject.getJSONArray(Constants.EDAD).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setClimaTest() {
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        JSONutils.setClima(jsonObject, Clima.LLUVIA);
        try {
            assertEquals(Clima.LLUVIA,jsonObject.getJSONArray(Constants.CLIMA).get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONutils.setClima(jsonObject, Clima.DESPEJADO);
        try {
            assertEquals(Clima.DESPEJADO,jsonObject.getJSONArray(Constants.CLIMA).get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aumentarFrecTest() {
        JSONObject jsonObject = createPictograms(643,"es","Yo","I",1);
        JSONObject jsonObjectRelated = createPictograms(22,"es","quiero","want",3);
        JSONObject jsonObjectNotRelated = createPictograms(555,"es","nuevo","new",2);

        JSONutils.aumentarFrec(jsonObject,jsonObjectRelated);

        //TODO encontrar el JSONobject por id y ver la frecuencia
        try {
            assertEquals(11,jsonObject.getJSONArray("relacion").getJSONObject(0).getInt("frec"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONutils.aumentarFrec(jsonObject,jsonObjectNotRelated);

        try {
            assertEquals(4,jsonObject.getJSONArray("relacion").length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crearRelacionTest() {
        JSONObject jsonObject = JSONutils.crearJson(1,"es","PictoPadre","Picto father",new JSONArray(),"img",1);

        try {
            JSONutils.crearRelacion(jsonObject.getJSONArray("relacion"),999);
            assertEquals(1,jsonObject.getJSONArray("relacion").length());

            JSONutils.crearRelacion(jsonObject.getJSONArray("relacion"),888);
            assertEquals(2,jsonObject.getJSONArray("relacion").length());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void crearJSONTest() {
        JSONObject jsonObject = JSONutils.crearJson(1,"es","Espanol","English",new JSONArray(),"img",1);

        assertNotNull(jsonObject);

        try {
            assertEquals(1,jsonObject.getInt("id"));
            assertEquals("img",jsonObject.getJSONObject("imagen").getString("picto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void relacionarConGrupoTest() {
        JSONArray jsonArray = createPictogramJSONArray();
        try {
            JSONutils.relacionarConGrupo2(jsonArray,3,643);
            assertEquals(4,jsonArray.getJSONObject(3).getJSONArray("relacion").length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO no entiendo que hace este metodo ni como funciona
    @Test
    public void add2allTest() {
        JSONArray jsonArrayGrupo = createGrupoJSONArrayAll();

        JSONutils.addToAllRelacion2(jsonArrayGrupo,1);

        assertEquals(2,jsonArrayGrupo.length());

    }
    @Test
    public void add2allTestNull() {
        JSONArray jsonArrayGrupo = createGrupoJSONArrayAll();

        JSONutils.addToAllRelacion2(null,1);

        assertEquals(2,jsonArrayGrupo.length());

    }

    @Test
    public void crearPictoTest() {
        JSONArray jsonArrayGrupo = createGrupoJSONArray();
        JSONArray arrayTodosLosPicto = createPictogramJSONArray();
        arrayTodosLosPicto = JSONutils.crearPicto(jsonArrayGrupo, arrayTodosLosPicto,"es", 1, "Espanol", "English","img",1,"url","pushKey");

        assertEquals(6,arrayTodosLosPicto.length());
    }

    @Test
    public void crearGrupoTest() {
        JSONArray jsonArrayGrupo = createGrupoJSONArray();
        try {
            jsonArrayGrupo = JSONutils.crearGrupo(jsonArrayGrupo,"es","Nuevo","New","img",1,"url","pushKey");
            assertEquals(3,jsonArrayGrupo.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHijosGrupo2Test() {
        JSONArray jsonArrayGrupos = createGrupoJSONArray();
        JSONArray jsonArrayPictos = createPictogramJSONArray();

        try {
            JSONArray hijos = JSONutils.getHijosGrupo2(jsonArrayPictos,jsonArrayGrupos.getJSONObject(1));
            assertEquals(3,hijos.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPictoFromId2Test() {
        JSONArray jsonArrayPictos = createPictogramJSONArray();

        try {
            assertEquals(118,JSONutils.getPictoFromId2(jsonArrayPictos,118).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO hacer grupo que tenga los pictos y hacer test cuando no lo tenga
    @Test
    public void getHijosGrupos2Test() {
        JSONArray jsonArrayGrupos = createGrupoJSONArray();
        JSONArray jsonArrayPictos = createPictogramJSONArray();
        try {
            JSONArray jsonArrayHijos = JSONutils.getHijosGrupo2(jsonArrayGrupos.getJSONObject(1),jsonArrayPictos);
            assertEquals(1,jsonArrayHijos.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getHijosGrupos2EmptyTest() {
        JSONArray jsonArrayGrupos = createGrupoJSONArray();
        JSONArray jsonArrayPictos = createPictogramJSONArrayEmpty();
        try {
            JSONArray jsonArrayHijos = JSONutils.getHijosGrupo2(jsonArrayGrupos.getJSONObject(1),jsonArrayPictos);
            assertEquals(0,jsonArrayHijos.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setJsonEditadoTest() {
        JSONArray jsonArrayPictos = createPictogramJSONArray();
        JSONObject jsonObject = createPictograms(22,"es","Editado","Edited",2);

        try {
            JSONutils.setJsonEditado2(jsonArrayPictos,jsonObject);
            assertEquals("Editado",jsonArrayPictos.getJSONObject(1).getJSONObject("texto").getString("es"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tieneAgendaTest(){
        JSONArray jsonArray = createGrupoJSONArray();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(4);
            assertEquals(0,JSONutils.tieneAgenda(jsonObject,"Agenda"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tieneSexoTest(){
        JSONArray jsonArray = createGrupoJSONArray();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(4);
            assertEquals(1,JSONutils.tieneSexo(jsonObject,Sexo.MASCULINO.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tieneHoraTest(){
        JSONArray jsonArray = createGrupoJSONArray();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(4);
            assertEquals(1,JSONutils.tieneHora(jsonObject,Horario.TARDE.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tieneEdadTest(){
        JSONArray jsonArray = createGrupoJSONArray();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(4);
            assertEquals(1,JSONutils.tieneEdad(jsonObject,Edad.JOVEN.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tienePosicionTest(){
        JSONArray jsonArray = createGrupoJSONArray();
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(4);
            assertEquals(1,JSONutils.tienePosicion(jsonObject,Posicion.BAR.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void scoreTest() {
        //
        JSONArray jsonArray = createGrupoJSONArray();
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        try {
            jsonObject.put("frec",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        double scoreBase, scoreTAG;
        scoreBase = JSONutils.score(jsonObject,false,"none",Sexo.MASCULINO.toString(),Horario.MEDIODIA.toString(),Edad.ADULTO.toString(),Posicion.PARQUE.toString());
        System.out.println(scoreBase);
        assertEquals(false,false);
        //

        //
    }
    @Test
    public void scoreTestSuggestedPictogram() {
        //
        JSONArray jsonArray = createGrupoJSONArray();
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        try {
            jsonObject.put("frec",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        double scoreBase, scoreTAG;
        scoreBase = JSONutils.score(jsonObject,true,"none",Sexo.MASCULINO.toString(),Horario.MEDIODIA.toString(),Edad.ADULTO.toString(),Posicion.PARQUE.toString());
        System.out.println(scoreBase);
        assertEquals(4.0d,scoreBase,0);
        //

        //
    }
    @Test
    public void scoreTestNullPictogram() {
        //
        JSONArray jsonArray = createGrupoJSONArray();
        JSONObject jsonObject = createPictograms(474,"es","manzana","apple",2);
        try {
            jsonObject.put("frec",10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        double scoreBase, scoreTAG;
        scoreBase = JSONutils.score(jsonObject,true,"none",Sexo.MASCULINO.toString(),Horario.MEDIODIA.toString(),Edad.ADULTO.toString(),Posicion.PARQUE.toString());
        System.out.println(scoreBase);
        assertEquals(20.0d,scoreBase,0);
        //

        //
    }



    public JSONObject createPictograms(int id, String locale, String localeName, String englishName, int tipo) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("tipo",tipo);
            JSONObject texto=new JSONObject();
            texto.put("en",englishName);
            texto.put(locale,localeName);
            jsonObject.put("texto",texto);
            JSONObject imagen=new JSONObject();
            imagen.put("picto","ic_action_previous");
            jsonObject.put("imagen",imagen);
            JSONArray jsonArray = new JSONArray();
            JSONObject relatedId1 = new JSONObject();
            relatedId1.put("id",22).put("frec",10);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id",118);
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id",474);
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray createPictogramJSONArray(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643,"es","Yo","I",1));
        jsonArray.put(createPictograms(22,"es","quiero","want",3));
        jsonArray.put(createPictograms(118,"es","comer","eat",3));
        jsonArray.put(createPictograms(474,"es","manzana","apple",2));
        String pictoTag = "{\"id\":1,\"texto\":{\"en\":\"accompany\",\"es\":\"acompañar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_acompanar\"},\"relacion\":[],\"agenda\":0,\"gps\":0,\"esSugerencia\":false,\"hora\":[\"TARDE\"],\"edad\":[\"JOVEN\"],\"sexo\":[\"MASCULINO\"],\"ubicacion\":[\"BAR\"]}";
        try {
            jsonArray.put(new JSONObject(pictoTag));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    private JSONArray createPictogramJSONArrayEmpty(){
        JSONArray jsonArray = new JSONArray();
        return jsonArray;
    }

    private JSONArray createGrupoJSONArray(){
        try {
            return new JSONArray("[{\"id\":0,\"texto\":{\"en\":\"Actions\",\"es\":\"ACCIONES\"},\"tipo\":0,\"imagen\":{\"picto\":\"verbos\"},\"relacion\":[{\"id\":1,\"texto\":{\"en\":\"escort\",\"es\":\"acompañar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_acompanar\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":2,\"texto\":{\"en\":\"turn off\",\"es\":\"apagar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_apagar_television\"},\"relacion\":[{\"id\":1016,\"frec\":2},{\"id\":1019,\"frec\":1},{\"id\":773,\"frec\":2},{\"id\":774,\"frec\":2}],\"agenda\":0,\"gps\":0},{\"id\":3,\"texto\":{\"en\":\"turn the volume down\",\"es\":\"bajar volumen\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_volumen_menos\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":4,\"texto\":{\"en\":\"erase\",\"es\":\"borrar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_borrar\"},\"relacion\":[],\"agenda\":0,\"gps\":0}],\"frecuencia\":1},{\"id\":1,\"texto\":{\"en\":\"Adjectives\",\"es\":\"ADJETIVOS\"},\"tipo\":0,\"imagen\":{\"picto\":\"descripcion\"},\"relacion\":[{\"id\":118,\"frec\":1},{\"id\":121,\"frec\":1},{\"id\":122,\"frec\":1}],\"frecuencia\":1}]");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private JSONArray createGrupoJSONArrayAll(){
        try {
            return new JSONArray("[{\"id\":0,\"texto\":{\"en\":\"ALL\",\"es\":\"Todos\"},\"tipo\":0,\"imagen\":{\"picto\":\"verbos\"},\"relacion\":[{\"id\":1,\"texto\":{\"en\":\"escort\",\"es\":\"acompañar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_acompanar\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":2,\"texto\":{\"en\":\"turn off\",\"es\":\"apagar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_apagar_television\"},\"relacion\":[{\"id\":1016,\"frec\":2},{\"id\":1019,\"frec\":1},{\"id\":773,\"frec\":2},{\"id\":774,\"frec\":2}],\"agenda\":0,\"gps\":0},{\"id\":3,\"texto\":{\"en\":\"turn the volume down\",\"es\":\"bajar volumen\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_volumen_menos\"},\"relacion\":[],\"agenda\":0,\"gps\":0},{\"id\":4,\"texto\":{\"en\":\"erase\",\"es\":\"borrar\"},\"tipo\":3,\"imagen\":{\"picto\":\"ic_borrar\"},\"relacion\":[],\"agenda\":0,\"gps\":0}],\"frecuencia\":1},{\"id\":1,\"texto\":{\"en\":\"Adjectives\",\"es\":\"ADJETIVOS\"},\"tipo\":0,\"imagen\":{\"picto\":\"descripcion\"},\"relacion\":[{\"id\":118,\"frec\":1},{\"id\":121,\"frec\":1},{\"id\":122,\"frec\":1}],\"frecuencia\":1}]");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}