package iotoolbox.net.coap;

import iotoolbox.net.coap.exeception.CoAPException;
import iotoolbox.net.udp.SimpleUdpMessageHandler;
import iotoolbox.net.udp.UdpSimpleClient;

import java.io.IOException;
import java.net.*;

public class CoAPClient implements SimpleUdpMessageHandler {
    private UdpSimpleClient udpSimpleClient;

    public CoAPClient() throws SocketException {
        udpSimpleClient = new UdpSimpleClient(this);
    }

    public Response post(String url, String payload) throws IOException, CoAPException {
        return post(Request.postRequest(url, payload));
    }

    public Response post(Request request) throws IOException {
        send(request);
        return null;
    }


    public Response empty(Request request) {
        return null;
    }

    public Response get(Request request) {
        return null;
    }


    public Response put(Request request) {
        return null;
    }

    public Response delete(Request request) {
        return null;
    }

    public void send(Request request) throws IOException {
        send(request, request.getCoAPUrl().getHost(), request.getCoAPUrl().getPort());
    }

    public void send(CoAPMessage message, String host, int port) throws IOException {
        byte[] data = message.getBytes();
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, socketAddress);
        this.udpSimpleClient.send(datagramPacket);
    }

    private void handle(CoAPMessage message) {
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

    public void close() {
        this.udpSimpleClient.close();
    }
}

