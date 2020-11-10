package iotoolbox.net.binarytool;

import java.util.ArrayList;
import java.util.Arrays;

public class SuperBinaryTool {

    public static byte[] transIntToBytes(int data, int maxLen) {
        byte[] result = transIntToBytes(data);
        if (result.length <= maxLen) {
            byte[] maxLenArr = new byte[maxLen];
            Arrays.fill(maxLenArr, (byte) 0);
            for (int i = 0; i < result.length; i++) {
                maxLenArr[maxLen - result.length + i] = result[i];
            }
            result = maxLenArr;
        }
        return result;
    }

    public static byte[] transIntToBytes(int data) {
        byte[] result = null;
        if (data <= 0xFF) {
            result = new byte[]{(byte) (data & 0xFF)};
        } else if (data <= 0xFFFF) {
            result = new byte[]{(byte) ((data >> 8) & 0xFF), (byte) (data & 0xFF)};
        } else if (data <= 0xFFFFFF) {
            result = new byte[]{(byte) ((data >> 16) & 0xFF), (byte) ((data >> 8) & 0xFF), (byte) (data & 0xFF)};
        } else {
            result = new byte[]{(byte) ((data >> 24) & 0xFF), (byte) ((data >> 16) & 0xFF), (byte) ((data >> 8) & 0xFF), (byte) (data & 0xFF)};
        }
        return result;
    }

    public static byte[] appendByteArr(byte[] bytes, byte[] appendBytes) {
        if (bytes == null || bytes.length == 0) {
            return appendBytes;
        }
        ArrayList<Byte> appendBytesList = new ArrayList<Byte>();
        if (appendBytes != null && appendBytes.length > 0) {
            for (byte b : appendBytes) {
                appendBytesList.add(b);
            }
        } else {
            return bytes;
        }
        byte[] resBytes = new byte[bytes.length + appendBytesList.size()];
        System.arraycopy(bytes, 0, resBytes, 0, bytes.length);
        int index = bytes.length;
        for (int i = 0; i < appendBytesList.size(); i++) {
            resBytes[index + i] = appendBytesList.get(i);
        }
        return resBytes;
    }

}
