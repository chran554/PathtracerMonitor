package se.cha.frame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RenderFrame extends ImageFrame {

    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.00");

    private JProgressBar progressBar;
    private JLabel timeElapsedInfoLabel;
    private JLabel timeRemainingInfoLabel;
    private JLabel timeFinishedInfoLabel;
    // private JLabel programInfoLabel;
    private JButton resizeButton;

    private RepaintTimer repaintTimer = new RepaintTimer(this, 500);
    private boolean pause = false;

    private int amountPixelsRendered = 0;
    private long startTime = -1;

    public RenderFrame(String title, int width, int height) {
        super(title);

        initialize(width, height);

        final Thread repaintThread = new Thread(repaintTimer);
        repaintThread.start();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                repaintTimer.quit();
                try {
                    repaintThread.join();
                } catch (InterruptedException e1) {
                    // Do nothing by intention...
                }
                // System.exit(0);
            }
        });

        pack();

        resizeFrame();
    }

    private void initialize(int width, int height) {
        setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));

        final Graphics graphics = getImage().getGraphics();
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
    }

    public void reset(final int width, final int height) {
        initialize(width, height);

        startTime = -1;

        pause = false;
        amountPixelsRendered = 0;

        resizeFrame();
    }

    private void updateInfo() {
        final int amountPixelsToRender = getImage().getWidth() * getImage().getHeight();
        final double percentageFinished = amountPixelsRendered / (double) amountPixelsToRender;

        final String progressString = PERCENT_FORMAT.format(100.0 * percentageFinished);
        progressBar.setValue((int) (100.0 * percentageFinished));
        progressBar.setStringPainted(true);
        progressBar.setString(progressString + "%");

        if (startTime != -1) {
            final long time = System.currentTimeMillis();
            final long timeElapsed = time - startTime;
            final long predictedTotalTime = (long) (timeElapsed / percentageFinished);
            final long predictedTimeLeft = predictedTotalTime - timeElapsed;

            final int amountSecondsElapsed = (int) (timeElapsed / 1000);
            final int secondsElapsed = amountSecondsElapsed % 60;
            final int minutesElapsed = ((amountSecondsElapsed - secondsElapsed) / 60) % 60;
            final int hoursElapsed = amountSecondsElapsed / 3600;

            final int amountSecondsLeft = (int) (predictedTimeLeft / 1000);
            final int secondsLeft = amountSecondsLeft % 60;
            final int minutesLeft = ((amountSecondsLeft - secondsLeft) / 60) % 60;
            final int hoursLeft = amountSecondsLeft / 3600;

            final Date finishTime = new Date(time + predictedTimeLeft);

            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            final boolean finishToday = finishTime.before(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            final boolean finishTomorrow = !finishToday && finishTime.before(calendar.getTime());

            String finishDateText = "";
            if (finishToday) {
                finishDateText = "";
            } else if (finishTomorrow) {
                finishDateText = ", tomorrow";
            } else {
                finishDateText = ", " + SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(finishTime);
            }

            timeElapsedInfoLabel.setText("Elapsed: " + hoursElapsed + "h " + minutesElapsed + "m " + secondsElapsed + "s");
            timeRemainingInfoLabel.setText("Remaining: " + hoursLeft + "h " + minutesLeft + "m " + secondsLeft + "s");
            timeFinishedInfoLabel.setText("Finished: " + SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(finishTime) + finishDateText);
        } else {
            timeElapsedInfoLabel.setText("Elapsed:");
            timeRemainingInfoLabel.setText("Remaining:");
            timeFinishedInfoLabel.setText("Finished:");
        }

    }

    @Override
    protected void initUI() {
        super.initUI();
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

        timeRemainingInfoLabel = new JLabel(" ");
        timeElapsedInfoLabel = new JLabel(" ");
        timeFinishedInfoLabel = new JLabel(" ");
        // programInfoLabel = new JLabel(" ");

        resizeButton = new JButton("Resize");
        resizeButton.addActionListener(e -> pack());

        layoutComponents();
    }

    private void layoutComponents() {
        final JPanel infoPanel = new JPanel(new GridBagLayout());
        final Insets insets = new Insets(2, 4, 2, 4);
        GridBagConstraints gbc;

        gbc = new GridBagConstraints(0, 0, 3, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets, 0, 0);
        infoPanel.add(progressBar, gbc);

        gbc = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets, 0, 0);
        infoPanel.add(timeElapsedInfoLabel, gbc);

        gbc = new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets, 0, 0);
        infoPanel.add(timeRemainingInfoLabel, gbc);

        gbc = new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets, 10, 0);
        infoPanel.add(timeFinishedInfoLabel, gbc);

        gbc = new GridBagConstraints(2, 1, 1, 2, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, insets, 0, 0);
        infoPanel.add(resizeButton, gbc);

        //gbc = new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, insets, 0, 0);
        //infoPanel.add(programInfoLabel, gbc);

        final Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        infoPanel.setBorder(border);

        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public boolean isPause() {
        return pause;
    }

    public void start() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
    }

    public synchronized void fillArea(int x, int y, int x2, int y2, Color color) {
        start();

        final Graphics graphics = getImage().getGraphics();
        graphics.setColor(new java.awt.Color(color.getIntColor()));

        final int width = x2 - x + 1;
        final int height = y2 - y + 1;

        graphics.fillRect(x, y, width, height);

        repaintTimer.repaint();
    }

    public synchronized void setPixel(int x, int y, Color color) {
        start();

        if ((x >= 0) && (x < getImage().getWidth()) && (y >= 0) && (y < getImage().getHeight())) {
            getImage().setRGB(x, y, color.getIntColor());
            repaintTimer.repaint();
        }
    }

    public synchronized void setPixelRendered() {
        amountPixelsRendered++;
    }

    public synchronized void setPixelRendered(int amount) {
        amountPixelsRendered += amount;
    }

    public void setIterativePixel(int x, int y, Color color, int passage) {
        if (passage == 0) {
            setPixel(x + 0, y + 0, color);
            setPixel(x + 1, y + 0, color);
            setPixel(x + 0, y + 1, color);
            setPixel(x + 1, y + 1, color);

        } else if (passage == 1) {
            setPixel(x - 1, y + 0, color);
            setPixel(x + 0, y + 0, color);

        } else if (passage == 2) {
            setPixel(x + 0, y + 0, color);

        } else if (passage == 3) {
            setPixel(x + 0, y + 0, color);
        }
    }

    class RepaintTimer implements Runnable {

        private RenderFrame repaintable;
        private final long delay;
        private long requestTimeStamp = System.currentTimeMillis();
        private boolean repaintPerformed = false;
        private boolean quit = false;

        public RepaintTimer(RenderFrame repaintable, long delay) {
            this.repaintable = repaintable;
            this.delay = delay;
        }

        public void run() {
            while (!quit) {

                if (!repaintPerformed && ((requestTimeStamp + delay) < System.currentTimeMillis())) {
                    try {
                        EventQueue.invokeAndWait(() -> {
                            repaintable.updateInfo();
                            repaintable.repaint();
                            repaintPerformed = true;
                        });
                    } catch (InterruptedException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(200); // Delay between repaints
                } catch (InterruptedException e) {
                    // Empty by intention
                }
            }
            System.out.println("Ending repaint thread.");
        }

        public void repaint() {
            if (repaintPerformed) {
                requestTimeStamp = System.currentTimeMillis();
                repaintPerformed = false;
            }
        }

        public void quit() {
            quit = true;
        }
    }

}
