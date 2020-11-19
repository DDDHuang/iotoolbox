package iotoolbox.net.coap;

import iotoolbox.net.coap.exeception.CoAPException;
import iotoolbox.net.udp.SimpleUdpMessageHandler;
import iotoolbox.net.udp.UdpSimpleClient;

import java.io.IOException;
import java.net.*;

public class CoAPClient {
    private CoAPMessageHandler handler;
    private UdpSimpleClient udpSimpleClient;

    public CoAPClient() throws SocketException {
        handler = new SimpleCoAPMessageHandler();
        udpSimpleClient = new UdpSimpleClient((SimpleUdpMessageHandler) handler);
    }

    public Response empty(Request request) {
        return null;
    }

    public Response get() {
        return null;
    }

    public Response post() {
        return null;
    }

    public Response put() {
        return null;
    }

    public Response delete() {
        return null;
    }

    public void send(CoAPMessage message, String host, int port) throws IOException {
        byte[] data = message.getBytes();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
        this.udpSimpleClient.send(datagramPacket);
    }

    public void setHandler(CoAPMessageHandler handler) {
        this.handler = handler;
    }

    public void close() {
        this.udpSimpleClient.close();
    }
}

class SimpleCoAPMessageHandler implements CoAPMessageHandler, SimpleUdpMessageHandler {
    @Override
    public void handle(CoAPMessage message) {
        System.out.println(message);
    }

    @Override
    public void handle(DatagramPacket datagramPacket) {
        try {
            handle(new CoAPMessage(datagramPacket));
        } catch (CoAPException e) {
            e.printStackTrace();
        }
    }
}

