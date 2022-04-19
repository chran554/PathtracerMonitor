package se.cha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPMessageListener implements Runnable {

    // 56 bytes = 7 * 64bit values
    // Content:
    //    * 1 byte: data type (uint). Legal values {0: int32, 1: int64, 2: float32, 3: float:64}
    //    * 4 bytes: x position (int32)
    //    * 4 bytes: y position (int32)
    //    * 4-8 bytes: value 1 (red)
    //    * 4-8 bytes: value 1 (green)
    //    * 4-8 bytes: value 1 (blue)
    private static final int BYTE_BUFFER_SIZE = 1 * 1024; // 1kb read buffer

    int port;
    byte[] receiveData;
    DatagramSocket udpListeningSocket;
    UDPPacketProcessor processor;
    boolean continueListen = true;

    public UDPMessageListener(UDPPacketProcessor listener, int localPort) {
        port = localPort;
        processor = listener;
        receiveData = new byte[BYTE_BUFFER_SIZE];
        try {
            udpListeningSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Socket bind error in port: " + port);
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
            final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
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