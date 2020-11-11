package iotoolbox.net.coap;

import iotoolbox.net.udp.SimpleUdpMessageHandler;
import iotoolbox.net.udp.UdpSimpleClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class CoAPClient implements SimpleUdpMessageHandler {
    private UdpSimpleClient udpSimpleClient;

    public CoAPClient(String hosts, int port) throws SocketException {
        udpSimpleClient = new UdpSimpleClient(this);
    }

    public void send(DatagramPacket datagramPacket) throws IOException {
        this.udpSimpleClient.send(datagramPacket);
    }

    public void close() {
        this.udpSimpleClient.close();
    }

    public void handle(DatagramPacket datagramPacket) {

    }
}

