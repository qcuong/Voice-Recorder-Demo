package com.tranginc.trangnhe.voicerecorderdemo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by myema on 19/02/2017.
 */

public class MyData {
    public static ArrayList<short[]> recordData = new ArrayList<>();
    public static int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    public static int BytesPerElement = 2; // 2 bytes in 16bit format


}
