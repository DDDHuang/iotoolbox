package iotoolbox.net.coap;

import iotoolbox.net.binarytool.SuperBinaryTool;
import iotoolbox.net.coap.exeception.CoAPException;

import java.net.DatagramPacket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;

public class CoAPMessage {
    private MessageType messageType;
    private MessageCode messageCode;
    private short messageId;
    private Integer token;
    private final ArrayList<SimpleEntry<CoAPOption, byte[]>> options = new ArrayList<>();
    private byte[] payLoad;

    public CoAPMessage(DatagramPacket datagramPacket) throws CoAPException {
        this(datagramPacket.getData());
    }

    public CoAPMessage() {
    }

    // TODO: 2020/11/19 分包 沾包  处理
    public CoAPMessage(byte[] messageData) throws CoAPException {
        int decodeIndex = 0;
        byte v_t_tkl = messageData[decodeIndex];
        if ((v_t_tkl >> 6) != 1) throw new CoAPException(CoAPException.UN_SUPPORT_VERSION);
        int type_code = ((v_t_tkl - (CoAP.VERSION << 6)) >> 4);
        this.messageType = MessageType.findByCode(type_code);
        int tkl = v_t_tkl & 0xF;
        this.messageCode = MessageCode.findByCode(messageData[++decodeIndex]);
        this.messageId = SuperBinaryTool.transBytesToShort(messageData[++decodeIndex], messageData[++decodeIndex]);
        if (tkl > 0) {
            this.token = SuperBinaryTool.transBytesToInt(Arrays.copyOfRange(messageData, ++decodeIndex, decodeIndex += tkl));
        } else {
            decodeIndex++;
        }
        CoAPOption lastOption = null;
        while (messageData[decodeIndex] != CoAP.PAYLOAD_MARK && messageData[decodeIndex] != 0) {
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
        this.payLoad = messageData[decodeIndex] == CoAP.PAYLOAD_MARK ? Arrays.copyOfRange(messageData, ++decodeIndex, messageData.length) : null;
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
        byte v_t_tkl = (CoAP.VERSION << 6);
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
            message = SuperBinaryTool.appendByteArr(message, new byte[]{CoAP.PAYLOAD_MARK}, payLoad);
        }
        return message;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setMessageCode(MessageCode messageCode) {
        this.messageCode = messageCode;
    }

    public void setMessageId(short messageId) {
        this.messageId = messageId;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.setPayLoad(payLoad.getBytes());
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

    public enum MessageType {
        CON((byte) 0), NON((byte) 1), ACK((byte) 2), RESET((byte) 3);
        private final byte code;

        MessageType(byte code) {
            this.code = code;
        }

        public byte getCode() {
            return code;
        }

        public static MessageType findByCode(int code) {
            for (MessageType value : MessageType.values()) {
                if (value.getCode() == code) return value;
            }
            return null;
        }
    }

    public enum MessageCode {
        //REQUEST
        EMPTY((byte) 0, (byte) 0),
        GET((byte) 0, (byte) 1),
        POST((byte) 0, (byte) 2),
        PUT((byte) 0, (byte) 3),
        DELETE((byte) 0, (byte) 4),

        //SUCCESS
        CREATED((byte) 2, (byte) 1),
        DELETED((byte) 2, (byte) 2),
        VALID((byte) 2, (byte) 3),
        CHANGED((byte) 2, (byte) 4),
        CONTENT((byte) 2, (byte) 5),

        //CLIENT_ERROR
        BAD_REQUEST((byte) 4, (byte) 0),
        UNAUTHORIZED((byte) 4, (byte) 1),
        BAD_OPTION((byte) 4, (byte) 2),
        FORBIDDEN((byte) 4, (byte) 3),
        NOT_FOUND((byte) 4, (byte) 4),
        METHOD_NOT_ALLOWED((byte) 4, (byte) 5),
        NOT_ACCEPTABLE((byte) 4, (byte) 6),
        PRECONDITION_FAILED((byte) 4, (byte) 12),
        REQUEST_ENTITY_TOO_LARGE((byte) 4, (byte) 13),
        UNSUPPORTED_CONTENT_FORMAT((byte) 4, (byte) 15),

        //SERVER_ERROR
        INTERNAL_SERVER_ERROR((byte) 5, (byte) 0),
        NOT_IMPLEMENTED((byte) 5, (byte) 1),
        BAD_GATEWAY((byte) 5, (byte) 2),
        SERVICE_UNAVAILABLE((byte) 5, (byte) 3),
        GATEWAY_TIMEOUT((byte) 5, (byte) 4),
        PROXYING_NOT_SUPPORTED((byte) 5, (byte) 5);

        public final byte code;
        public final byte detail;

        MessageCode(byte code, byte detail) {
            this.code = code;
            this.detail = detail;
        }

        public static MessageCode findByCode(byte data) {
            byte code = (byte) ((data & 0xFF) >> 5);
            byte detail = (byte) (data & 0x1f);
            return findByCode(code, detail);
        }

        public static MessageCode findByCode(byte code, byte detail) {
            for (MessageCode value : MessageCode.values()) {
                if (value.code == code && value.detail == detail) return value;
            }
            return null;
        }

        @Override
        public String toString() {
            return "MessageCode{" + this.name() +
                    ", code=" + code +
                    ", detail=" + detail +
                    '}';
        }
    }

    public enum CoAPOption {
        RESERVED(0),
        IF_MATCH(1),
        URI_HOST(3),
        ETAG(4),
        IF_NOT_MATCH(5),
        URI_OBSERVE(6),
        OBJECT_19(19),
        URI_PORT(7),
        LOCATION_PATH(8),
        URI_PATH(11),
        CONTENT_FORMATE(12),
        MAX_AGE(14),
        URI_QUERY(15),
        ACCEPT(17),
        LOCATION_QYEERY(20),
        PROXY_URI(35),
        PROXY_SCHEME(39),
        SIZEL(60);

        public final int optionCode;

        CoAPOption(int optionCode) {
            this.optionCode = optionCode;
        }

        public static CoAPOption findByCode(int optionCode) {
            for (CoAPOption option : CoAPOption.values()) {
                if (option.optionCode == optionCode) return option;
            }
            return null;
        }
    }

    public enum ContentFormat {
        TEXT_PLAIN(0),
        APPLICATION_LINK_FORMATE(40),
        APPLICATION_OCTET_STREAM(42),
        APPLICATION_VNDOMALWM2M_TLV(11542),
        APPLICATION_VNDOMALWM2M_JSON(11543);

        public final int code;

        ContentFormat(int code) {
            this.code = code;
        }
    }
}
