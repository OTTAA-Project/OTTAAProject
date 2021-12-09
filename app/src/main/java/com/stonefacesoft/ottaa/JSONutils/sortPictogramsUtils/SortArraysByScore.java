package com.stonefacesoft.ottaa.JSONutils.sortPictogramsUtils;

import com.stonefacesoft.ottaa.JSONutils.Json;

import org.json.JSONArray;
import org.json.JSONException;

public class SortArraysByScore extends SortObjects {
    @Override
    protected int partition(JSONArray arr, int low, int high) throws JSONException {
        int pivot = Json.getInstance().getScore(arr.getJSONObject(high),false);
        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);
        for(int j = low; j <= high - 1; j++)
        {
            if (Json.getInstance().getScore(arr.getJSONObject(j),false)< pivot)
            {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    /*
    @Override
     public  int partition(JSONArray arr, int low, int high) throws JSONException {
        // pivot

    }*/

}
