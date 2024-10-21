import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class AnalogClock extends JFrame {
    private LocalTime time = LocalTime.now();
    private ClockPanel clockPanel;

    public AnalogClock() {
        setTitle("Analog Clock");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //панель годинника
        clockPanel = new ClockPanel();
        add(clockPanel, BorderLayout.CENTER);

        //панель з кнопками
        JPanel buttonPanel = new JPanel();
        JButton addHourButton = new JButton("+ Hour");
        JButton subtractHourButton = new JButton("- Hour");
        JButton addMinuteButton = new JButton("+ Minute");
        JButton subtractMinuteButton = new JButton("- Minute");

        buttonPanel.add(addHourButton);
        buttonPanel.add(subtractHourButton);
        buttonPanel.add(addMinuteButton);
        buttonPanel.add(subtractMinuteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        //події для кнопок
        addHourButton.addActionListener(e -> changeTime(1, 0));
        subtractHourButton.addActionListener(e -> changeTime(-1, 0));
        addMinuteButton.addActionListener(e -> changeTime(0, 1));
        subtractMinuteButton.addActionListener(e -> changeTime(0, -1));
    }

    private void changeTime(int hours, int minutes) {
        time = time.plusHours(hours).plusMinutes(minutes);
        clockPanel.setTime(time);
        clockPanel.repaint();
    }

    //малювання годинника
    private class ClockPanel extends JPanel {
        private LocalTime time;

        public ClockPanel() {
            this.time = LocalTime.now();
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 20;

            //циферблат
            g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

            //цифри на циферблаті
            for (int i = 1; i <= 12; i++) {
                double angle = Math.toRadians(i * 30 - 90);
                int numX = centerX + (int) ((radius - 30) * Math.cos(angle));
                int numY = centerY + (int) ((radius - 30) * Math.sin(angle));
                g.drawString(String.valueOf(i), numX - 5, numY + 5);
            }

            //годинна стрілка
            double hourAngle = Math.toRadians((time.getHour() % 12 + time.getMinute() / 60.0) * 30 - 90);
            int hourLength = (int) (radius * 0.5);
            int hourX = centerX + (int) (Math.cos(hourAngle) * hourLength);
            int hourY = centerY + (int) (Math.sin(hourAngle) * hourLength);
            g.drawLine(centerX, centerY, hourX, hourY);

            //хвилинна стрілка
            double minuteAngle = Math.toRadians(time.getMinute() * 6 - 90);
            int minuteLength = (int) (radius * 0.8);
            int minuteX = centerX + (int) (Math.cos(minuteAngle) * minuteLength);
            int minuteY = centerY + (int) (Math.sin(minuteAngle) * minuteLength);
            g.drawLine(centerX, centerY, minuteX, minuteY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnalogClock clock = new AnalogClock();
            clock.setVisible(true);
        });
    }
}
