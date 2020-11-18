package iotoolbox.net.coap.message;

import iotoolbox.net.binarytool.SuperBinaryTool;

import java.util.ArrayList;
import java.util.Arrays;

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

    public final int option;
    public final byte[] optionValue;

    public CoAPOption(int option, String optionValue) {
        this(option, optionValue.getBytes());
    }

    public CoAPOption(int option, byte[] optionValue) {
        this.option = option;
        this.optionValue = optionValue;
    }

    public CoAPOption(byte[] messageData, int decodeIndex, CoAPOption last) {
        int delta = ((messageData[decodeIndex] & 0xff) >> 4);
        delta = (delta == 13) ? SuperBinaryTool.transBytesToInt(messageData[++decodeIndex]) + delta : delta;
        delta = (delta == 14) ? SuperBinaryTool.transBytesToInt(messageData[++decodeIndex], messageData[++decodeIndex]) + 269 : delta;
        this.option = last == null ? delta : delta + last.option;
        int optLen = messageData[++decodeIndex] & 0xf;
        optLen = (optLen == 13) ? SuperBinaryTool.transBytesToInt(messageData[++decodeIndex]) + optLen : optLen;
        optLen = (optLen == 14) ? SuperBinaryTool.transBytesToInt(messageData[++decodeIndex], messageData[++decodeIndex]) + 269 : optLen;
        optionValue = Arrays.copyOfRange(messageData, ++decodeIndex, decodeIndex + optLen);
    }

    @Override
    public int compareTo(Object o) {
        return this.option - ((CoAPOption) o).option;
    }

    public byte[] getBytes(CoAPOption last) {
        int delta = last == null ? this.option : this.option - last.option;
        return SuperBinaryTool.appendByteArr(
                new byte[]{(byte) ((calculateDeltaOrLen(delta) << 4) + calculateDeltaOrLen(optionValue.length))},
                reloadDeltaOrLen(delta), reloadDeltaOrLen(optionValue.length),
                optionValue);
    }

    public static CoAPOption[] parseOptions(byte[] messageData, int decodeIndex) {
        ArrayList<CoAPOption> options = new ArrayList<>();
        CoAPOption last = null;
        while (messageData[++decodeIndex] != CoAPMessage.PAYLOAD_MARK && messageData[++decodeIndex] != 0) {
            CoAPOption option = new CoAPOption(messageData, decodeIndex, last);
            options.add(option);
            last = option;
        }
        return (CoAPOption[]) options.toArray();
    }


    private static byte calculateDeltaOrLen(int deltaOrLen) {
        if (deltaOrLen < 13) return (byte) deltaOrLen;
        if (deltaOrLen < 269) return 13;
        return 14;
    }

    private static byte[] reloadDeltaOrLen(int deltaOrLen) {
        if (deltaOrLen < 13) return null;
        if (deltaOrLen < 269) return SuperBinaryTool.transIntToBytes(deltaOrLen - 13, 1);
        return SuperBinaryTool.transIntToBytes(deltaOrLen - 269, 2);
    }

    @Override
    public String toString() {
        return "CoAPOption{" +
                "option=" + option +
                ", optionValue=" + Arrays.toString(optionValue) +
                '}';
    }
}