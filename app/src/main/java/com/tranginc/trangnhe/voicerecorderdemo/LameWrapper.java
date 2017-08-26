package com.tranginc.trangnhe.voicerecorderdemo;

/**
 * Created by myema on 22/02/2017.
 */

public class LameWrapper {

    static {
        System.loadLibrary("WrapperLAME");
    }

    private native void initEncoder(int numChannels, int sampleRate, int bitRate, int mode, int quality);

    private native void destroyEncoder();

    private native int encodeFile(String sourcePath, String targetPath);
}
