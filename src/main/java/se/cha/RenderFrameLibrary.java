package se.cha;

import se.cha.frame.RenderFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class RenderFrameLibrary {

    private static final Map<String, RenderFrame> renderFramesLookup = new HashMap<>();

    public synchronized static RenderFrame getOrCreateRenderFrame(String imageGroup, String imageName, int imageWidth, int imageHeight) {
        final String renderFrameKey = imageGroup + ":" + imageName;

        RenderFrame renderFrame = renderFramesLookup.get(renderFrameKey);

        if (renderFrame == null) {
            renderFrame = new RenderFrame(renderFrameKey, imageWidth, imageHeight);
            renderFrame.setVisible(true);
            renderFrame.centerFrame();

            renderFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    final RenderFrame closingRenderFrame = (RenderFrame)e.getSource();
                    renderFramesLookup.remove(closingRenderFrame.getTitle());

                    if (renderFramesLookup.isEmpty()) {
                        //notifyLastFrameClosed();
                    }
                }
            });

            renderFramesLookup.put(renderFrameKey, renderFrame);
        }

        return renderFrame;
    }

    public synchronized static void deleteRenderFrame(String imageGroup, String imageName) {
        final String renderFrameKey = imageGroup + ":" + imageName;
        renderFramesLookup.remove(renderFrameKey);
    }

    public synchronized static int getRenderFrameCount() {
        return renderFramesLookup.size();
    }
}
