package se.cha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class UdpMulticastMessageListener implements Runnable {

    // 56 bytes = 7 * 64bit values
    // Content:
    //    * 1 byte: data type (uint). Legal values {0: int32, 1: int64, 2: float32, 3: float:64}
    //    * 4 bytes: x position (int32)
    //    * 4 bytes: y position (int32)
    //    * 4-8 bytes: value 1 (red)
    //    * 4-8 bytes: value 1 (green)
    //    * 4-8 bytes: value 1 (blue)
    private static final int BYTE_BUFFER_SIZE = 2 * 1024; // 1kb read buffer

    int port;
    byte[] receiveData;
    MulticastSocket udpListeningSocket;
    UDPPacketProcessor processor;
    boolean continueListen = true;

    /**
     * In IPv4, any address between 224.0.0.0 to 239.255.255.255 can be used as a multicast address.
     */
    public UdpMulticastMessageListener(UDPPacketProcessor listener, String multicastAddress, int localPort) {
        port = localPort;
        processor = listener;
        receiveData = new byte[BYTE_BUFFER_SIZE];
        try {
            udpListeningSocket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName(multicastAddress);
            udpListeningSocket.joinGroup(group);
        } catch (IOException e) {
            System.err.println("Multicast socket setup: " + port);
            e.printStackTrace();
        }
    }

    public void stop() {
        continueListen = false;
        //udpListeningSocket.disconnect();
        udpListeningSocket.close();
    }

    @Override
    public void run() {
        while (continueListen) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                udpListeningSocket.receive(receivePacket);
                processor.onPacketReceived(receivePacket);
            } catch (IOException e) {
                if (continueListen) {
                    System.out.println("UDP Listener end up with an exception:");
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Ending UDP listener thread.");
    }
}