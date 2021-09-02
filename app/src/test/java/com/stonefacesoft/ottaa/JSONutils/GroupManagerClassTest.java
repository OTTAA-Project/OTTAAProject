package com.stonefacesoft.ottaa.JSONutils;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class GroupManagerClassTest {

    @Test
    public void testGetInstance() {
        GroupManagerClass groupManagerClass = GroupManagerClass.getInstance();
        Assert.assertTrue(groupManagerClass != null);
    }
    @Test
    public void testSetmGroup() {
        GroupManagerClass groupManagerClass = GroupManagerClass.getInstance();
        JSONArray group = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("prueba","hola");
            group.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        groupManagerClass.setmGroup(group);
        Assert.assertTrue(groupManagerClass.getmGroup().length() == 1);
    }
    @Test
    public void testGetmGroup() {
    }
}