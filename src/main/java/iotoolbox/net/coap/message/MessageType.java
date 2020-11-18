package iotoolbox.net.coap.message;

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
