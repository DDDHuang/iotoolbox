package iotoolbox.net.coap.exeception;

public class CoAPException extends Exception {

    public static final int UN_DEFINED = -1;
    public static final int UN_SUPPORT_VERSION = 0;

    public CoAPError error ;

    public CoAPException(CoAPError error) {
        this.error = error;
    }

    public CoAPException() {
    }

    public CoAPException(String message) {
        super(message);
    }
}
