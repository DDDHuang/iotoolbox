package iotoolbox.net.coap.message;

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

    private final byte code;
    private final byte detail;

    MessageCode(byte code, byte detail) {
        this.code = code;
        this.detail = detail;
    }

    public byte getCode() {
        return code;
    }

    public byte getDetail() {
        return detail;
    }
}
