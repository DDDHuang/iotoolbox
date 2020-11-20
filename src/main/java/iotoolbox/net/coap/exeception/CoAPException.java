package iotoolbox.net.coap.exeception;

import java.util.HashMap;
import java.util.Map;

public class CoAPException extends Exception {

    public static final int UN_DEFINED = -1;
    public static final int UN_SUPPORT_VERSION = 0;

    public static final Map<Integer, String> errorMessageMap = new HashMap<Integer, String>() {{
        put(UN_DEFINED, "UN_DEFINED");
        put(UN_SUPPORT_VERSION, "UN_SUPPORT_VERSION");
    }};


    public int errorCode;

    public CoAPException(int error) {
        this(errorMessageMap.get(error));
        this.errorCode = error;
    }

    public CoAPException() {
    }

    public CoAPException(String message) {
        super(message);
    }
}
