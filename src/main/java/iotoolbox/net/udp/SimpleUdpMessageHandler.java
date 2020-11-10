package iotoolbox.net.udp;

import java.net.DatagramPacket;

public interface SimpleUdpMessageHandler {
    void handle( DatagramPacket datagramPacket);
}
