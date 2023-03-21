package com.stonefacesoft.ottaa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import com.stonefacesoft.ottaa.Interfaces.DialogInterfaceTags;
import com.stonefacesoft.ottaa.Interfaces.TagInterfazJson;
import com.stonefacesoft.ottaa.JSONutils.Json;
import com.stonefacesoft.ottaa.utils.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.mockito.Mockito;

import java.util.ArrayList;

public class AsignTagsTest implements DialogInterfaceTags, TagInterfazJson {
    private AsignTags asignTags;
    private Context context = Mockito.mock(Context.class);

    @Before
    public void setUp(){
        asignTags = new AsignTags(context);
    }
    @Test
    public void testSetInterfaz() {
        asignTags.setInterfaz(this::onTagAsignado);
        assertNotNull(asignTags.getmDialogInterface());
    }
    @Test
    public void testSetInterfazTag() {
        asignTags.setInterfazTag(this::refrescarJsonTags);
        assertNotNull(asignTags.getmTagInterface());
    }

    @Test
    public void testSetExtras() {
        try {
            asignTags.setExtras(createPictogramJSONArray().getJSONObject(0),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertNotNull(asignTags.getPicto());
    }
    @Test
    public void testGetTagsAdapter() {

    }

    @Test
    public void testCargarTags() {
        try {
            asignTags.setExtras(createPictogramJSONArray().getJSONObject(0),false);
            asignTags.setTagsToPicto(createPictogramJSONArray().getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        asignTags.cargarTags(Constants.EDAD);
        System.out.println(asignTags.getArrayListTodosLosTags().toString());
        try {
            System.out.println(tags().toString());
            assertEquals(tags().getJSONObject(0).toString(),asignTags.getArrayListTodosLosTags().get(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testAsignarTags() {
        try {
            JSONObject object = createPictogramJSONArray().getJSONObject(2);
            asignTags.setExtras(object,false);
            ArrayList<JSONObject> selectedTags = new ArrayList<>();
            asignTags.cargarTags(Constants.EDAD);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.SEXO);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(2));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.UBICACION);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(3));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(1));
            asignTags.cargarTags(Constants.HORA);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(3));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(1));
            asignTags.setmArrayListSelectedTAGS(selectedTags);
            asignTags.setTagsToPicto(object);
            Json json =Json.getInstance();
            assertTrue(object.has(Constants.SEXO)&&object.has(Constants.EDAD)&&object.has(Constants.UBICACION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRemoveTagToPicto() {
        JSONObject object = null;
        try {
            object = createPictogramJSONArray().getJSONObject(2);
            asignTags.setExtras(object,false);
            ArrayList<JSONObject> selectedTags = new ArrayList<>();
            asignTags.cargarTags(Constants.EDAD);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.SEXO);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(2));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.UBICACION);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(3));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(1));
            asignTags.setmArrayListSelectedTAGS(selectedTags);
            asignTags.setTagsToPicto(object);
            object.remove(Constants.UBICACION);
            assertTrue(object.has(Constants.SEXO)&&object.has(Constants.EDAD)&&!object.has(Constants.UBICACION));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testSetTagsToPictogram() {
        JSONObject object = null;
        try {
            object = createPictogramJSONArray().getJSONObject(2);
            asignTags.setExtras(object,false);
            ArrayList<JSONObject> selectedTags = new ArrayList<>();
            asignTags.cargarTags(Constants.EDAD);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.SEXO);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(2));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            asignTags.cargarTags(Constants.UBICACION);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(3));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(1));
            asignTags.cargarTags(Constants.HORA);
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(0));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(1));
            selectedTags.add(asignTags.getmArrayListTagsPorTipo().get(2));
            asignTags.setmArrayListSelectedTAGS(selectedTags);
            asignTags.setTagsToPicto(object);
            assertTrue(object.has("hora"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTagAsignado(String jsonStringTags) {

    }

    @Override
    public void refrescarJsonTags(JSONObject jsonTags) {

    }

    private JSONArray createPictogramJSONArray() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(createPictograms(643, "es", "Yo", "I", 1));
        jsonArray.put(createPictograms(22, "es", "quiero", "want", 3));
        jsonArray.put(createPictograms(118, "es", "comer", "eat", 3));
        jsonArray.put(createPictograms(474, "es", "manzana", "apple", 2));
        return jsonArray;
    }

    public JSONObject createPictograms(int id, String locale, String localeName, String englishName, int tipo) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("tipo", tipo);
            JSONObject texto = new JSONObject();
            texto.put("en", englishName);
            texto.put(locale, localeName);
            jsonObject.put("texto", texto);
            JSONObject imagen = new JSONObject();
            imagen.put("picto", "ic_action_previous");
            jsonObject.put("imagen", imagen);
            JSONArray jsonArray = new JSONArray();
            JSONObject relatedId1 = new JSONObject();
            relatedId1.put("id", 22).put("frec", 10);
            JSONObject relatedId2 = new JSONObject();
            relatedId2.put("id", 118);
            JSONObject relatedId3 = new JSONObject();
            relatedId3.put("id", 474);
            jsonArray.put(relatedId1).put(relatedId2).put(relatedId3);
            jsonObject.put("relacion", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    
    public JSONArray tags(){
        try {
            return new JSONArray("[{\"texto\":{\"en\":\"Morning\",\"es\":\"MANANA\"},\"tipo\":\"hora\",\"imagen\":{\"picto\":\"ic_buenos_dias\"},\"id\":379}, {\"texto\":{\"en\":\"Noon\",\"es\":\"MEDIODIA\"},\"tipo\":\"hora\",\"imagen\":{\"picto\":\"verano\"},\"id\":818}, {\"texto\":{\"en\":\"Afternoon\",\"es\":\"TARDE\"},\"tipo\":\"hora\",\"imagen\":{\"picto\":\"ic_buenas_tardes\"},\"id\":380}, {\"texto\":{\"en\":\"Night\",\"es\":\"NOCHE\"},\"tipo\":\"hora\",\"imagen\":{\"picto\":\"ic_buenas_noches\"},\"id\":381}, {\"texto\":{\"en\":\"grandpa\",\"es\":\"ADULTO\"},\"tipo\":\"edad\",\"imagen\":{\"picto\":\"ic_abuelo\"},\"id\":614}, {\"texto\":{\"en\":\"young\",\"es\":\"JOVEN\"},\"tipo\":\"edad\",\"imagen\":{\"picto\":\"joven\"},\"id\":195}, {\"texto\":{\"en\":\"boy\",\"es\":\"NINO\"},\"tipo\":\"edad\",\"imagen\":{\"picto\":\"nino\"},\"id\":630}, {\"texto\":{\"en\":\"field\",\"es\":\"ESTADIO\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"cancha\"},\"id\":651}, {\"texto\":{\"en\":\"square\",\"es\":\"PARQUE\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"plaza\"},\"id\":653}, {\"texto\":{\"en\":\"movie theater\",\"es\":\"CINE\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"cine\"},\"id\":1013}, {\"texto\":{\"en\":\"computer store\",\"es\":\"TIENDA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"tienda_de_informatica\"},\"id\":654}, {\"texto\":{\"en\":\"Bar\",\"es\":\"BAR\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"bar\"},\"id\":687}, {\"texto\":{\"en\":\"coffee\",\"es\":\"CAFE\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"cafe\"},\"id\":496}, {\"texto\":{\"en\":\"Restaurant\",\"es\":\"RESTAURANT\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"restaurante\"},\"id\":685}, {\"texto\":{\"en\":\"bakery\",\"es\":\"PANADERIA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"pan_blanco\"},\"id\":672}, {\"texto\":{\"en\":\"butchery\",\"es\":\"CARNICERIA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"carniceria\"},\"id\":652}, {\"texto\":{\"en\":\"greengrocery\",\"es\":\"VERDULERIA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"verduleria\"},\"id\":698}, {\"texto\":{\"en\":\"farmacy\",\"es\":\"FARMACIA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"ic_farmacia\"},\"id\":655}, {\"texto\":{\"en\":\"hospital\",\"es\":\"HOSPITAL\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"hospital\"},\"id\":668}, {\"texto\":{\"en\":\"school\",\"es\":\"ESCUELA\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"ic_escuela\"},\"id\":662}, {\"texto\":{\"en\":\"transportation\",\"es\":\"ESTACIONDEBUS\"},\"tipo\":\"ubicacion\",\"imagen\":{\"picto\":\"transportes\"},\"id\":611}, {\"texto\":{\"en\":\"woman\",\"es\":\"FEMENINO\"},\"tipo\":\"sexo\",\"imagen\":{\"picto\":\"mujer\"},\"id\":1033}, {\"texto\":{\"en\":\"man\",\"es\":\"MASCULINO\"},\"tipo\":\"sexo\",\"imagen\":{\"picto\":\"hombre\"},\"id\":1035}, {\"texto\":{\"en\":\"binary\",\"es\":\"BINARIO\"},\"tipo\":\"sexo\",\"imagen\":{\"picto\":\"hombre\"},\"id\":1036}, {\"texto\":{\"en\":\"fluid\",\"es\":\"FLUIDO\"},\"tipo\":\"sexo\",\"imagen\":{\"picto\":\"hombre\"},\"id\":1037}, {\"texto\":{\"en\":\"others\",\"es\":\"OTRO\"},\"tipo\":\"sexo\",\"imagen\":{\"picto\":\"hombre\"},\"id\":1038}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

}