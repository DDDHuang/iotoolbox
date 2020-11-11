package iotoolbox.net.binarytool;

import java.util.Arrays;

public class SuperBinaryTool {

    public static byte[] transIntToBytes(int data, int maxLen) {
        return fillZero(transIntToBytes(data), maxLen);
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

    public static byte[] transShortToBytes(short data, int maxLen) {
        return fillZero(transShortToBytes(data), maxLen);
    }

    public static byte[] transShortToBytes(short data) {
        if (data <= 0xFF) {
            return new byte[]{(byte) (data & 0xFF)};
        } else {
            return new byte[]{(byte) ((data >> 8) & 0xFF), (byte) (data & 0xFF)};
        }
    }

    public static byte[] appendByteArr(byte[]... appendArr) {
        byte[] result = null;
        for (byte[] bytes : appendArr) {
            result = appendByteArr(result, bytes);
        }
        return result;
    }

    public static byte[] appendByteArr(byte[] begin, byte[] appendArr) {
        if ((begin == null || begin.length == 0) && appendArr != null) return appendArr;
        if (appendArr == null || appendArr.length == 0) return begin;
        byte[] result = new byte[begin.length + appendArr.length];
        System.arraycopy(begin, 0, result, 0, begin.length);
        System.arraycopy(appendArr, 0, result, begin.length, appendArr.length);
        return result;
    }

    public static byte[] fillZero(byte[] data, int maxLen) {
        if (data.length <= maxLen) {
            byte[] maxLenArr = new byte[maxLen];
            Arrays.fill(maxLenArr, (byte) 0);
            for (int i = 0; i < data.length; i++) {
                maxLenArr[maxLen - data.length + i] = data[i];
            }
            return maxLenArr;
        }
        return data;
    }

}
