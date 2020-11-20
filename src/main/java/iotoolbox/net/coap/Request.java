package iotoolbox.net.coap;

import iotoolbox.net.coap.exeception.CoAPException;

public class Request extends CoAPMessage {
    private CoAPUrl coAPUrl;

    public Request() {
    }

    public static Request getRequest(CoAPUrl url) {
        Request request = new Request();
        return request;
    }

    public static Request postRequest(String url, String payload) throws CoAPException {
        return postRequest(new CoAPUrl(url), payload.getBytes());
    }

    public static Request postRequest(CoAPUrl url, String payload) {
        return postRequest(url, payload.getBytes());
    }

    public static Request postRequest(CoAPUrl url, byte[] payload) {
        Request request = new Request();
        request.coAPUrl = url;
        request.setMessageType(MessageType.CON);
        request.setMessageCode(MessageCode.POST);
        request.addOption(CoAPOption.URI_PATH, url.getPath());
        for (String s : url.getQueryParamMap().keySet()) {
            request.addOption(CoAPOption.URI_QUERY, s + "=" + url.getQueryParamMap().get(s));
        }
        request.setPayLoad(payload);
        return request;
    }

    public void setContentFormat(ContentFormat contentFormat) {
        this.addOption(CoAPOption.CONTENT_FORMATE, "" + contentFormat.code);
    }

    public CoAPUrl getCoAPUrl() {
        return coAPUrl;
    }

    @Override
    public String toString() {
        return "Request{" +
                "coAPUrl=" + coAPUrl +
                "CoAP Message=" + super.toString() +
                '}';
    }
}
