package iotoolbox.net.coap;

import iotoolbox.net.coap.message.CoAPMessage;

public interface CoAPMessageHandler {
    void handle(CoAPMessage message);
}
