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
        return buildRequest(url.getHost(), url.getPort(), url.getPath(), MessageType.CON, MessageCode.POST, (short) 0, null, null, payload);
    }

    public static Request buildRequest(String host, int port, String path, MessageType type,
                                       MessageCode code, short messageId, Integer token,
                                       ContentFormat contentFormat, byte[] payload) {
        Request request = new Request();
        CoAPUrl coAPUrl = new CoAPUrl(host, port, path);
        request.coAPUrl = coAPUrl;
        request.setMessageType(type);
        request.setMessageCode(code);
        if (messageId != 0) request.setMessageId(messageId);
        if (token != null) request.setToken(token);
        if (coAPUrl.getPath() != null && coAPUrl.getPath().trim().length() != 0)
            request.addOption(CoAPOption.URI_PATH, coAPUrl.getPath());
        if (coAPUrl.getQueryParamMap().keySet().size() > 0) {
            for (String queryKey : coAPUrl.getQueryParamMap().keySet()) {
                request.addOption(CoAPOption.URI_QUERY, queryKey + "=" + coAPUrl.getQueryParamMap().get(queryKey));
            }
        }
        request.setContentFormat(contentFormat);
        request.setPayLoad(payload);
        return request;
    }

    public void setContentFormat(ContentFormat contentFormat) {
        if (contentFormat != null) this.addOption(CoAPOption.CONTENT_FORMATE, "" + contentFormat.code);
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
