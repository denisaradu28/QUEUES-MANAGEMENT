package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationFrame extends JFrame {
    private List<QueuePanel> queuePanels;
    private JPanel queueContainer;

    public AnimationFrame(int clients, int queues) {
        this.queuePanels = new ArrayList<>();

        setTitle("Animation Frame");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        queueContainer = new JPanel();
        queueContainer.setLayout(new BoxLayout(queueContainer, BoxLayout.Y_AXIS));
        queueContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < queues; i++) {
            QueuePanel queuePanel = new QueuePanel(i);
            queuePanels.add(queuePanel);

            JScrollPane queueScroll = new JScrollPane(queuePanel);
            queueScroll.setPreferredSize(new Dimension(950, 130));
            queueScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            queueScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

            queueContainer.add(queueScroll);
            queueContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane globalScrollPane = new JScrollPane(queueContainer);
        globalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        globalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(globalScrollPane, BorderLayout.CENTER);
    }

    public void updateQueue(int queueIndex, List<String> taskLabels) {
        if (queueIndex >= 0 && queueIndex < queuePanels.size()) {
            queuePanels.get(queueIndex).setTasks(taskLabels);
        }
    }

    public void refresh() {
        for (QueuePanel queuePanel : queuePanels) {
            queuePanel.repaint();
        }
    }
}
