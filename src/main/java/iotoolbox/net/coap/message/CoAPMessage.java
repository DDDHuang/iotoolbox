package iotoolbox.net.coap.message;

import iotoolbox.net.binarytool.SuperBinaryTool;
import iotoolbox.net.coap.exeception.CoAPError;
import iotoolbox.net.coap.exeception.CoAPException;

import java.net.DatagramPacket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;

public class CoAPMessage {

    public static final int VERSION = 1;
    public static final byte PAYLOAD_MARK = (byte) 0xff;

    public final MessageType messageType;
    public final MessageCode messageCode;
    public final short messageId;
    public Integer token;
    public final ArrayList<SimpleEntry<CoAPOption, byte[]>> options = new ArrayList<>();
    public byte[] payLoad;

    public CoAPMessage(DatagramPacket datagramPacket) throws CoAPException {
        this(datagramPacket.getData());
    }

    // TODO: 2020/11/19 分包 沾包  处理
    public CoAPMessage(byte[] messageData) throws CoAPException {
        int decodeIndex = 0;
        byte v_t_tkl = messageData[decodeIndex];
        if ((v_t_tkl >> 6) != 1) throw new CoAPException(CoAPError.UN_SUPPORT_VERSION);
        int type_code = ((v_t_tkl - (VERSION << 6)) >> 4);
        this.messageType = MessageType.findByCode(type_code);
        int tkl = v_t_tkl & 0xF;
        this.messageCode = MessageCode.findByCode(messageData[++decodeIndex]);
        this.messageId = SuperBinaryTool.transBytesToShort(messageData[++decodeIndex], messageData[++decodeIndex]);
        if(tkl>0){
            this.token =  SuperBinaryTool.transBytesToInt(Arrays.copyOfRange(messageData, ++decodeIndex, decodeIndex += tkl));
        }else {
            decodeIndex++;
        }
        CoAPOption lastOption = null;
        while (messageData[decodeIndex] != CoAPMessage.PAYLOAD_MARK && messageData[decodeIndex] != 0) {
            int delta = ((messageData[decodeIndex] & 0xff) >> 4);
            int optLen = messageData[decodeIndex] & 0xf;
            if (delta == 13) delta = SuperBinaryTool.transBytesToInt(messageData[++decodeIndex]) + 13;
            if (delta == 14)
                delta = SuperBinaryTool.transBytesToInt(messageData[++decodeIndex], messageData[++decodeIndex]) + 269;
            if (optLen == 13) optLen = SuperBinaryTool.transBytesToInt(messageData[++decodeIndex]) + 13;
            if (optLen == 14)
                optLen = SuperBinaryTool.transBytesToInt(messageData[++decodeIndex], messageData[++decodeIndex]) + 269;
            CoAPOption newOption = lastOption == null ? CoAPOption.findByCode(delta) : CoAPOption.findByCode(lastOption.optionCode + delta);
            this.addOption(newOption, Arrays.copyOfRange(messageData, ++decodeIndex, decodeIndex += optLen));
            lastOption = newOption;
        }
        this.payLoad = messageData[decodeIndex] == PAYLOAD_MARK ? Arrays.copyOfRange(messageData, ++decodeIndex, messageData.length) : null;
    }

    public CoAPMessage(MessageType messageType, MessageCode messageCode, short messageId) {
        this.messageType = messageType;
        this.messageCode = messageCode;
        this.messageId = messageId;
    }

    public CoAPMessage(MessageType messageType, MessageCode messageCode, short messageId, int token, byte[] payLoad) {
        this.messageType = messageType;
        this.messageCode = messageCode;
        this.messageId = messageId;
        this.token = token;
        this.payLoad = payLoad;
    }

    public void addOption(CoAPOption option, byte[] data) {
        this.options.add(new SimpleEntry<>(option, data));
    }

    public void addOption(CoAPOption option, String data) {
        addOption(option, data.getBytes());
    }

    public byte[] getBytes() {
        byte v_t_tkl = (VERSION << 6);
        v_t_tkl += (messageType.getCode() << 4);
        byte code = (byte) (messageCode.code << 5);
        code += messageCode.detail;
        byte[] message = SuperBinaryTool.appendByteArr(new byte[]{v_t_tkl, code});
        message = SuperBinaryTool.appendByteArr(message, SuperBinaryTool.transShortToBytes(messageId, 2));
        if (token != null) {
            byte[] tokenBytes = SuperBinaryTool.transIntToBytes(token);
            message[0] += tokenBytes.length;
            message = SuperBinaryTool.appendByteArr(message, tokenBytes);
        }
        CoAPOption last = null;
        for (SimpleEntry<CoAPOption, byte[]> optionEntry : options) {
            message = SuperBinaryTool.appendByteArr(message, buildOption(last, optionEntry.getKey(), optionEntry.getValue()));
            last = optionEntry.getKey();
        }
        if (payLoad != null) {
            message = SuperBinaryTool.appendByteArr(message, new byte[]{PAYLOAD_MARK}, payLoad);
        }
        return message;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad.getBytes();
    }

    private static byte[] buildOption(CoAPOption last, CoAPOption newOpt, byte[] data) {
        int delta = last == null ? newOpt.optionCode : newOpt.optionCode - last.optionCode;
        return SuperBinaryTool.appendByteArr(
                new byte[]{(byte) ((calculateDeltaOrLen(delta) << 4) + calculateDeltaOrLen(data.length))},
                reloadDeltaOrLen(delta), reloadDeltaOrLen(data.length),
                data);
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CoAPMessage{ messageType=").
                append(messageType).append(", messageCode=")
                .append(messageCode).append(", messageId=")
                .append(messageId).append(", token=")
                .append(token)
                .append(", optionLinkedHashMap={");
        for (SimpleEntry<CoAPOption, byte[]> optionEntry : options) {
            stringBuilder.append(optionEntry.getKey());
            stringBuilder.append(":");
            stringBuilder.append(new String(optionEntry.getValue()));
            stringBuilder.append("  ");
        }
        stringBuilder.append("}, payLoad=").append(Arrays.toString(payLoad)).append('}');
        return stringBuilder.toString();
    }
}