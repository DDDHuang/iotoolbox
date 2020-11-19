package iotoolbox.net.coap;

public class Request extends CoAPMessage {
    public Request() {
    }

    public static Request getRequest(CoAPUrl url) {
        Request request = new Request();
        return request;
    }

    public static Request postRequest(CoAPUrl url, ContentFormat contentFormat, byte[] payload) {
        return null;
    }

}
