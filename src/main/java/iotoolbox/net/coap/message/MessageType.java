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
}
