package iotoolbox.net.coap.exeception;

public enum CoAPError {
    UN_DEFINED(-1),
    UN_SUPPORT_VERSION(0),
    ;

    private int code;

    CoAPError(int code) {
        this.code = code;
    }
}
