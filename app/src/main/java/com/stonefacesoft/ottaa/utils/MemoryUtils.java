package com.stonefacesoft.ottaa.utils;
/*
* this code is a copy from https://github.com/nalitzis/TestMemoryViews/blob/master/app/src/main/java/com/nalitzis/testmemoryviews/app/MemUtils.java
*
* That code check the available memory
* */
public class MemoryUtils {
    public static final float BYTES_MB = 1024.0f*1024.0f;

    public static float freeMegabytes(){
        final Runtime runtime = Runtime.getRuntime();
        final float usedBytes = runtime.totalMemory();
        final float usedMb = usedBytes/BYTES_MB;
        final float freeMb = availableMemory()-usedMb;
        return freeMb;
    }

    public static float availableMemory(){
        final Runtime runtime = Runtime.getRuntime();
        final float bytesAvailable = runtime.maxMemory();
        return bytesAvailable/ BYTES_MB;
    }


}
