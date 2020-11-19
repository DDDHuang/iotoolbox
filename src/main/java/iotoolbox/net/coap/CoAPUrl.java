package iotoolbox.net.coap;

public class CoAPUrl {
    public final String url;
    private Protocol protocol;

    public CoAPUrl(String url) {
        this.url = url;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getHost() {
        return "";
    }

    public String getPath() {
        return "";
    }

    public String getQuery() {
        return "";
    }

    public enum Protocol {
        coap;
    }
}
