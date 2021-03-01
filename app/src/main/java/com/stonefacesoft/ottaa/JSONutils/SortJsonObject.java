package com.stonefacesoft.ottaa.JSONutils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SortJsonObject {
   private JSONArray Array;
   private static boolean isSorted;
    private static final String TAG = "SortJsonObj";

    public SortJsonObject SortArray(JSONArray Array,Json json){
      //  cargarDatos(Array,json);
        this.Array=new JSONArray();
        this.Array=Array;
        //sort(this.Array,0,this.Array.length()-1,json);
       // if(isSorted)
        sort(Array,json);
       // countingSort(this.Array,json);

        return this;
    }

    public JSONArray getArray(Json json) {
       JSONArray ArrayAux=Array;
        return ArrayAux;
    }
    public JSONObject getObject(JSONArray array,int i)  {
        try{
            return array.getJSONObject(i);
        }catch (JSONException ex){
            Log.e(TAG, "getObject: Error: " + ex.getMessage());
            return null;
        }
    }


    public void sort(JSONArray arr, Json json) {
        int n = arr.length();
        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i, json);
        // One by one extract an element from heap
        for (int i = n - 1; i >= 0; i--) {
            // Move current root to end
            JSONObject temp, temp1 = null;
            try {
                temp = arr.getJSONObject(0);
                temp1 = arr.getJSONObject(i);
                arr.put(0, temp1);
                arr.put(i, temp);
            } catch (JSONException e) {
                Log.e(TAG, "sort: Error: " + e.getMessage());
            }
            heapify(arr, i, 0,json);
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    void heapify(JSONArray arr, int n, int i, Json json) {
        int largest = i;  // Initialize largest as root
        int l = 2 * i + 1;  // left = 2*i + 1
        int r = 2 * i + 2;  // right = 2*i + 2

        // If left child is larger than root
        if (l < n && json.getScore(getObject(arr, l), false) < json.getScore(getObject(arr, largest), false))
            largest = l;

        // If right child is larger than largest so far
        if (r < n && json.getScore(getObject(arr, r), false) < json.getScore(getObject(arr, largest), false))
            largest = r;

        // If largest is not root
        if (largest != i) {
            JSONObject Swap = getObject(arr, i);
            JSONObject Swap1 = getObject(arr, largest);
            try {
                arr.put(largest, Swap);
                arr.put(i, Swap1);
            } catch (JSONException e) {
                Log.e(TAG, "heapify: Error: " + e.getMessage());
            }
            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest, json);
        }
    }

    public static void setIsSorted(boolean isSorted) {
        SortJsonObject.isSorted = isSorted;
    }

    public  void countingSort(JSONArray input,Json json) {
        // create buckets
         JSONArray aux=input;
         // fill buckets

         // sort array
         int ndx = 0;
         for (int i = 0; i < aux.length(); i++)
              { while (0 <json.getScore(getObject(aux,i),false))
                {// input[ndx++] = i; counter[i]--;
                    try {
                        input.put(ndx++,getObject(aux,i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }



}
