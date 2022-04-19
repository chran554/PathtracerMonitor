package se.cha;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.cha.frame.Color;
import se.cha.frame.RenderFrame;

import java.io.IOException;
import java.net.DatagramPacket;

public class RenderMessageProcessor implements UDPPacketProcessor {

    @Override
    public void onPacketReceived(DatagramPacket receivedPacket) {
        try {
            //System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength(), StandardCharsets.UTF_8));

            final PixelData pixelData = new ObjectMapper().readValue(receivedPacket.getData(), 0, receivedPacket.getLength(), PixelData.class);
            final Color color = new Color(pixelData.getColor());

            final RenderFrame renderFrame = RenderFrameLibrary.getOrCreateRenderFrame(pixelData.imageGroup, pixelData.imageName, pixelData.getImageWidth(), pixelData.getImageHeight());

            if ((pixelData.getX() != -1) && (pixelData.getY() != -1)) {

                for (int yw = 0; yw < Math.abs(pixelData.getPixelHeight()); yw++) {
                    for (int xw = 0; xw < Math.abs(pixelData.getPixelWidth()); xw++) {
                        renderFrame.setPixel(pixelData.getX() + xw * sign(pixelData.getPixelWidth()), pixelData.getY() + yw * sign(pixelData.getPixelHeight()), color);
                    }
                }

                renderFrame.setPixelRendered();
            } else {
                // Initialize
                renderFrame.reset(pixelData.getImageWidth(), pixelData.getImageHeight());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int sign(int value) {
        return value >= 0 ? 1 : -1;
    }
}
