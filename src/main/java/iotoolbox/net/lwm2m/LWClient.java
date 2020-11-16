package iotoolbox.net.lwm2m;

import iotoolbox.net.coap.CoAPClient;
import iotoolbox.net.coap.CoAPMessageHandler;
import iotoolbox.net.coap.message.CoAPMessage;

import java.net.SocketException;

public class LWClient implements CoAPMessageHandler {
    private CoAPClient coAPClient;

    public LWClient() throws SocketException {
        coAPClient = new CoAPClient(this);
    }

    public void send() {

    }

    @Override
    public void handle(CoAPMessage message) {

    }
}
