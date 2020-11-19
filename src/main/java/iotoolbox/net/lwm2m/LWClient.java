package iotoolbox.net.lwm2m;

import iotoolbox.net.coap.CoAPClient;

import java.net.SocketException;

public class LWClient {
    private CoAPClient coAPClient;

    public LWClient() throws SocketException {
        coAPClient = new CoAPClient();
    }

    public void send() {

    }
}
