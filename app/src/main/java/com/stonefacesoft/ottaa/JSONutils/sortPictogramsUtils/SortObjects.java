package com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SortObjects {
    protected int partition(JSONArray arr, int low, int high) throws JSONException {
        // pivot
        return -1;
    }

    protected void swap(JSONArray array, int i, int j) throws JSONException {
        JSONObject temp = array.getJSONObject(i);
        array.put(i,array.getJSONObject(j));
        array.put(j,temp);
    }

    public void quickSort(JSONArray array, int low, int high) throws JSONException {
        if (low < high)
        {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }
}
