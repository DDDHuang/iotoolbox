package iotoolbox.net.coap.message;

import iotoolbox.net.binarytool.SuperBinaryTool;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class CoAPMessage {

    public static final int VERSION = 1;

    private MessageType messageType;
    private MessageCode messageCode;
    private int token;
    private int messageId;
    private CoAPOption[] coAPOptions;
    private byte[] payLoad;

    private DatagramPacket datagramPacket;

    public CoAPMessage(
            MessageType messageType,
            MessageCode messageCode,
            int token,
            int messageId,
            CoAPOption[] coAPOptions,
            byte[] payLoad,
            String host, String port) {
        this.messageType = messageType;
        this.messageCode = messageCode;
        this.token = token;
        this.messageId = messageId;
        this.coAPOptions = coAPOptions;
        this.payLoad = payLoad;
        datagramPacket = build();
    }

    private DatagramPacket build() {
        byte[] tokenBytes = SuperBinaryTool.transIntToBytes(token);
        byte part1 = (byte)(0x80 & messageType<<7);

        return null;
    }

    public CoAPMessage(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public int getOffset() {
        return datagramPacket.getOffset();
    }

    public int getLength() {
        return datagramPacket.getLength();
    }

    public InetAddress getAddress() {
        return datagramPacket.getAddress();
    }

    public int getPort() {
        return datagramPacket.getPort();
    }
}

enum MessageType {
    CON(0), NON(1), ACK(2), RESET(3);
    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

enum MessageCode {
    //REQUEST
    EMPTY(0, 0),
    GET(0, 1),
    POST(0, 2),
    PUT(0, 3),
    DELETE(0, 4),

    //SUCCESS
    CREATED(2, 1),
    DELETED(2, 2),
    VALID(2, 3),
    CHANGED(2, 4),
    CONTENT(2, 5),

    //CLIENT_ERROR
    BAD_REQUEST(4, 0),
    UNAUTHORIZED(4, 1),
    BAD_OPTION(4, 2),
    FORBIDDEN(4, 3),
    NOT_FOUND(4, 4),
    METHOD_NOT_ALLOWED(4, 5),
    NOT_ACCEPTABLE(4, 6),
    PRECONDITION_FAILED(4, 12),
    REQUEST_ENTITY_TOO_LARGE(4, 13),
    UNSUPPORTED_CONTENT_FORMAT(4, 15),

    //SERVER_ERROR
    INTERNAL_SERVER_ERROR(5, 0),
    NOT_IMPLEMENTED(5, 1),
    BAD_GATEWAY(5, 2),
    SERVICE_UNAVAILABLE(5, 3),
    GATEWAY_TIMEOUT(5, 4),
    PROXYING_NOT_SUPPORTED(5, 5);

    private final int code;
    private final int detail;

    MessageCode(int code, int detail) {
        this.code = code;
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public int getDetail() {
        return detail;
    }
}

class CoAPOption {
    enum Option {
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
        public int code;

        Option(int code) {
            this.code = code;
        }
    }

    private Option option;
    private byte[] optionValue;

    public CoAPOption(Option option, byte[] optionValue) {
        this.option = option;
        this.optionValue = optionValue;
    }

    public Option getOption() {
        return option;
    }

    public byte[] getOptionValue() {
        return optionValue;
    }
}