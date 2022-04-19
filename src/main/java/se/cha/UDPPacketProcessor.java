package se.cha;

import java.net.DatagramPacket;

public interface UDPPacketProcessor {
    void onPacketReceived(DatagramPacket receivedPacket);
}