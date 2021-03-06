package iotoolbox.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpSimpleClient implements Runnable {
    private final DatagramSocket datagramSocket;
    private final SimpleUdpMessageHandler messageHandler;
    private final int bufferSize;
    private final Thread selfThread;

    public UdpSimpleClient(SimpleUdpMessageHandler messageHandler) throws SocketException {
        this(messageHandler, -1, -1);
    }

    public UdpSimpleClient(SimpleUdpMessageHandler messageHandler, int localPort, int bufferSize) throws SocketException {
        datagramSocket = localPort > 0 && localPort < 65535 ? new DatagramSocket(localPort) : new DatagramSocket();
        bufferSize = bufferSize > 0 ? bufferSize : 2048;
        this.messageHandler = messageHandler;
        this.bufferSize = bufferSize;
        selfThread = new Thread(this);
        selfThread.start();
    }

    public void send(DatagramPacket datagramPacket) throws IOException {
        datagramSocket.send(datagramPacket);
    }

    public void close() {
        selfThread.interrupt();
        datagramSocket.close();
    }

    public void run() {
        while (true) {
            byte[] bytes = new byte[bufferSize];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            try {
                this.datagramSocket.receive(datagramPacket);
                messageHandler.handle(datagramPacket);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                break;
            }
        }
    }
}