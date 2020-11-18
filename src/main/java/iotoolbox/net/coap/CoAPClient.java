package iotoolbox.net.coap;

import iotoolbox.net.coap.exeception.CoAPException;
import iotoolbox.net.coap.message.CoAPMessage;
import iotoolbox.net.udp.SimpleUdpMessageHandler;
import iotoolbox.net.udp.UdpSimpleClient;

import java.io.IOException;
import java.net.*;

public class CoAPClient implements SimpleUdpMessageHandler {
    private UdpSimpleClient udpSimpleClient;
    private CoAPMessageHandler handler;

    public CoAPClient(CoAPMessageHandler handler) throws SocketException {
        udpSimpleClient = new UdpSimpleClient(this);
        this.handler = handler;
    }


    public void send(CoAPMessage message, String host, int port) throws IOException {
        byte[] data = message.getBytes();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
        this.udpSimpleClient.send(datagramPacket);
    }

    public void close() {
        this.udpSimpleClient.close();
    }

    public void handle(DatagramPacket datagramPacket) {
        try {
            handler.handle(new CoAPMessage(datagramPacket));
        } catch (CoAPException e) {
            // TODO: 2020/11/16 show message en de code error
            e.printStackTrace();
        }
    }
}

