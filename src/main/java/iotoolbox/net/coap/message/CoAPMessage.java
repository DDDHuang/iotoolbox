package iotoolbox.net.coap.message;

import iotoolbox.net.binarytool.SuperBinaryTool;
import iotoolbox.net.coap.exeception.CoAPError;
import iotoolbox.net.coap.exeception.CoAPException;

import java.net.DatagramPacket;
import java.util.Arrays;

public class CoAPMessage {

    public static final int VERSION = 1;

    private MessageType messageType;
    private MessageCode messageCode;
    private int token;
    private short messageId;
    private CoAPOption[] coAPOptions;
    private byte[] payLoad;

    private boolean useToken = true;


    public CoAPMessage(DatagramPacket datagramPacket) throws CoAPException {
        byte[] messageData = datagramPacket.getData();
        byte v_t_tkl = messageData[0];
        //version
        if ((v_t_tkl >> 6) != 1) throw new CoAPException(CoAPError.UN_SUPPORT_VERSION);
        //type

    }

    public CoAPMessage(MessageType messageType, MessageCode messageCode, int token, short messageId, CoAPOption[] coAPOptions, byte[] payLoad) {
        this.messageType = messageType;
        this.messageCode = messageCode;
        this.token = token;
        this.messageId = messageId;
        this.coAPOptions = coAPOptions;
        this.payLoad = payLoad;
    }

    public byte[] getBytes() {
        byte v_t_tkl = (VERSION << 6);
        v_t_tkl += (messageType.getCode() << 4);
        byte code = (byte) (messageCode.getCode() << 5);
        code += messageCode.getDetail();
        byte[] message = SuperBinaryTool.appendByteArr(new byte[]{v_t_tkl, code});
        message = SuperBinaryTool.appendByteArr(message, SuperBinaryTool.transShortToBytes(messageId, 2));
        if (useToken) {
            byte[] tokenBytes = SuperBinaryTool.transIntToBytes(token);
            message[0] += tokenBytes.length;
            message = SuperBinaryTool.appendByteArr(message, tokenBytes);
        }
        if (coAPOptions != null && coAPOptions.length != 0) {
            Arrays.sort(coAPOptions);
            CoAPOption last = null;
            for (CoAPOption coAPOption : coAPOptions) {
                message = SuperBinaryTool.appendByteArr(message, coAPOption.getBytes(last));
                last = coAPOption;
            }
        }
        if (payLoad != null) {
            message = SuperBinaryTool.appendByteArr(message, new byte[]{(byte) 0xff}, payLoad);
        }
        return message;
    }

    public void setCoAPOptions(CoAPOption[] coAPOptions) {
        this.coAPOptions = coAPOptions;
    }

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public void setMessageId(short messageId) {
        this.messageId = messageId;
    }

    public void setToken(int token) {
        this.token = token;
    }
}