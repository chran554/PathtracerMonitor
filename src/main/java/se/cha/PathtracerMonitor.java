package se.cha;

public class PathtracerMonitor {

    public static void main(String[] args) {
        System.out.println("Starting render monitor");

        System.out.println("Listening to messages...");
        final RenderMessageProcessor renderMessageProcessor = new RenderMessageProcessor();
        // final UDPMessageListener dataListener = new UDPMessageListener(renderMessageProcessor, 8088);
        final UdpMulticastMessageListener dataListener = new UdpMulticastMessageListener(renderMessageProcessor, "230.0.0.0", 9999);

        final Thread messageThread = new Thread(dataListener);
        messageThread.start();

/*
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Stopping message listener");
            dataListener.stop();
        }
*/

        // System.out.println("Ending render monitor main thread.");
    }

}
