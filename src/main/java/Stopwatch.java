import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Stopwatch extends JFrame {
    private long startTime;
    private long stopTime;
    private boolean running = false;
    private ArrayList<Long> lapTimes = new ArrayList<>();
    private DefaultListModel<String> lapListModel = new DefaultListModel<>();

    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton lapButton = new JButton("Mezičas");
    private JButton resetButton = new JButton("Reset");
    private JLabel timeLabel = new JLabel("00:00:00.000", JLabel.CENTER);
    private JList<String> lapList = new JList<>(lapListModel);
    private Timer timer;

    public Stopwatch() {
        setTitle("Stopwatch");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.add(startButton);
        topPanel.add(stopButton);
        topPanel.add(lapButton);
        topPanel.add(resetButton);

        add(topPanel, BorderLayout.NORTH);
        add(timeLabel, BorderLayout.CENTER);
        add(new JScrollPane(lapList), BorderLayout.SOUTH);

        stopButton.setEnabled(false);
        lapButton.setEnabled(false);
        resetButton.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        lapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lap();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
    }

    private void start() {
        startTime = System.currentTimeMillis();
        running = true;
        timer.start();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        lapButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private void stop() {
        stopTime = System.currentTimeMillis();
        running = false;
        timer.stop();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        lapButton.setEnabled(false);
    }

    private void lap() {
        long lapTime = System.currentTimeMillis() - startTime;
        lapTimes.add(lapTime);
        lapListModel.addElement(formatTime(lapTime));
    }

    private void reset() {
        stop();
        startTime = 0;
        stopTime = 0;
        timeLabel.setText("00:00:00.000");
        lapTimes.clear();
        lapListModel.clear();
        resetButton.setEnabled(false);
    }

    private void updateTime() {
        if (running) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            timeLabel.setText(formatTime(elapsedTime));
        }
    }

    private String formatTime(long time) {
        int hours = (int) (time / 3600000);
        int minutes = (int) ((time % 3600000) / 60000);
        int seconds = (int) ((time % 60000) / 1000);
        int milliseconds = (int) (time % 1000);
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Stopwatch().setVisible(true);
            }
        });
    }
}