package iotoolbox.net.binarytool;

import java.util.Arrays;

public class SuperBinaryObject {
    private int data;
    private int dataSize;

    public SuperBinaryObject(int data, int dataSize) {
        this.data = data;
        this.dataSize = dataSize;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getData() {
        return data;
    }

    public static byte[] build(SuperBinaryObject[] superBinaryObjects) {
        byte[] result = new byte[2];
        Arrays.fill(result, (byte) 0);
        int byteIndex = 0;
        int appendIndex = 0;
        for (SuperBinaryObject superBinaryObject : superBinaryObjects) {

        }
        return result;
    }
//    private static int get(){
//
//    }
}
