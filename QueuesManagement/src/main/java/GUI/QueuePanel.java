package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QueuePanel extends JPanel {
    private int queueId;
    private List<String> tasks;

    public QueuePanel(int queueId) {
        this.queueId = queueId;
        this.tasks = new ArrayList<>();
        setBorder(BorderFactory.createTitledBorder("Queue " + (queueId + 1)));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(2000, 100));
        setMinimumSize(new Dimension(2000, 100));
    }

    public void setTasks(List<String> tasks) {
        this.tasks = new ArrayList<>(tasks);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gd = (Graphics2D) g;

        int x = 10;
        int y = 30;
        int width = 100;
        int height = 30;
        int spacing = 10;

        for (String task : tasks) {
            gd.setColor(Color.PINK);
            gd.fillRect(x, y, width, height);
            gd.setColor(Color.BLACK);
            gd.drawRect(x, y, width, height);
            gd.drawString(task, x + 10, y + 20);
            x += width + spacing;
        }
    }
}
