package iotoolbox.net.coap.message;

import iotoolbox.net.binarytool.SuperBinaryTool;

public class CoAPOption implements Comparable {

    public static final int RESERVED = 0;
    public static final int IF_MATCH = 1;
    public static final int URI_HOST = 3;
    public static final int ETAG = 4;
    public static final int IF_NOT_MATCH = 5;
    public static final int URI_OBSERVE = 6;
    public static final int OBJECT_19 = 19;
    public static final int URI_PORT = 7;
    public static final int LOCATION_PATH = 8;
    public static final int URI_PATH = 11;
    public static final int CONTENT_FORMATE = 12;
    public static final int MAX_AGE = 14;
    public static final int URI_QUERY = 15;
    public static final int ACCEPT = 17;
    public static final int LOCATION_QYEERY = 20;
    public static final int PROXY_URI = 35;
    public static final int PROXY_SCHEME = 39;
    public static final int SIZEL = 60;

    private final int option;
    private final byte[] optionValue;

    public CoAPOption(int option, String optionValue) {
        this(option, optionValue.getBytes());
    }

    public CoAPOption(int option, byte[] optionValue) {
        this.option = option;
        this.optionValue = optionValue;
    }

    public int getOption() {
        return option;
    }

    public byte[] getOptionValue() {
        return optionValue;
    }

    @Override
    public int compareTo(Object o) {
        return this.option - ((CoAPOption) o).getOption();
    }

    public byte[] getBytes(CoAPOption last) {
        int delta = last == null ? 0 : last.getOption() - this.getOption();
        return SuperBinaryTool.appendByteArr(
                new byte[]{(byte) ((calculateDeltaOrLen(delta) << 4) + calculateDeltaOrLen(optionValue.length))},
                reloadDeltaOrLen(delta),
                reloadDeltaOrLen(optionValue.length), optionValue);
    }

    private static byte calculateDeltaOrLen(int deltaOrLen) {
        if (deltaOrLen < 13) return (byte) deltaOrLen;
        if (deltaOrLen < 269) return 14;
        return 15;
    }

    private static byte[] reloadDeltaOrLen(int deltaOrLen) {
        if (deltaOrLen < 13) return null;
        if (deltaOrLen < 269) return SuperBinaryTool.transIntToBytes(deltaOrLen - 13, 2);
        return SuperBinaryTool.transIntToBytes(deltaOrLen - 269, 4);
    }
}