package com.stonefacesoft.ottaa.JSONutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SortPictograms {

    static void swap(JSONArray array, int i, int j) throws JSONException {
        JSONObject temp = array.getJSONObject(i);
        array.put(i,array.getJSONObject(j));
        array.put(j,temp);
    }

    /* This function takes last element as pivot, places
       the pivot element at its correct position in sorted
       array, and places all smaller (smaller than pivot)
       to left of pivot and all greater elements to right
       of pivot */
    static int partition(JSONArray arr, int low, int high) throws JSONException {

        // pivot
        int pivot = Json.getInstance().getScore(arr.getJSONObject(high),false);

        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {

            // If current element is smaller
            // than the pivot
            if (Json.getInstance().getScore(arr.getJSONObject(j),false) < pivot)
            {

                // Increment index of
                // smaller element
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    /* The main function that implements QuickSort
              arr[] --> Array to be sorted,
              low --> Starting index,
              high --> Ending index
     */
    public static void quickSort(JSONArray array, int low, int high) throws JSONException {
        if (low < high)
        {

            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = partition(array, low, high);

            // Separately sort elements before
            // partition and after partition
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

}
