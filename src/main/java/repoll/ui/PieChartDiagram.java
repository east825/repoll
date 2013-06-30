package repoll.ui;

import repoll.Repoll;
import repoll.core.Answer;
import repoll.mappers.MapperException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PieChartDiagram extends JPanel {
    static {
        Logger rootLogger = Repoll.LOG;
    }

    private static final Logger LOG = Logger.getLogger(PieChartDiagram.class.getName());
    private final List<Answer> answers;
    private Answer selectedAnswer;
    private final DefaultListModel<Answer> legendListModel;
    private Map<Answer, Color> colorMap = new IdentityHashMap<>();
    private final JPanel chartPanel;

    public PieChartDiagram(List<Answer> answers) {
        this.answers = answers;
        setLayout(new GridLayout(1, 2));
        add(new JLabel(), BorderLayout.CENTER);
        legendListModel = new DefaultListModel<>();
        if (answers.isEmpty()) {
            legendListModel.addElement(null);
        }

        for (Answer answer : answers) {
            legendListModel.addElement(answer);
        }
        final JList<Answer> legendList = new JList<>(legendListModel);
        legendList.setFixedCellWidth(150);
        legendList.setCellRenderer(new ListCellRenderer<Answer>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Answer> list, Answer value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    return new JLabel("No answers");
                }
                String description = null;
                try {
                    description = String.format("%s: %d votes", value.getDescription(), value.getVotesNumber());
                } catch (MapperException e) {
                    LOG.throwing("PieChartDiagram", "getListCellRendererComponent", e);
                }
                return new JLabel(description, new ColorFilledIcon(16, 16, colorMap.get(value)), SwingConstants.LEFT);
            }
        });
        prepareColorMap(answers);
        final JLabel chartLabel = new JLabel();
        legendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        legendList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                selectedAnswer = legendList.getModel().getElementAt(e.getFirstIndex());
////                chartPanel.revalidate();
////                chartPanel.repaint();
////                chartLabel.revalidate();
//                chartLabel.repaint();
//            }
//        });
        legendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = legendList.locationToIndex(e.getPoint());
                selectedAnswer = legendListModel.elementAt(index);
                chartLabel.repaint();
            }
        });
        chartPanel = new JPanel(new BorderLayout());
//        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        chartLabel.setIcon(new PieChartIcon());
        chartPanel.add(chartLabel);
//        add(chartPanel);
        add(chartLabel);
        add(new JScrollPane(legendList));
        setMaximumSize(new Dimension(
                (int)getMaximumSize().getWidth(),
                (int)Math.max(legendList.getPreferredSize().getHeight(),
                        chartPanel.getPreferredSize().getHeight())));
        LOG.fine("" + getPreferredSize());
    }

    private static class ColorFilledIcon implements Icon {
        private final int width, height;
        private final Color color;

        private ColorFilledIcon(int width, int height, Color color) {
            this.width = width;
            this.height = height;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(rect);
            g2d.setPaint(color);
            g2d.fill(rect);
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }

    private class PieChartIcon implements Icon {

        public static final int SIZE = 100;

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(Color.GRAY);
            int xCenter = x + SIZE / 2;
            int yCenter = y + SIZE / 2;
            Arc2D fullCircle = new Arc2D.Double(xCenter, yCenter, SIZE, SIZE, 0, 360, Arc2D.PIE);
            g2d.fill(fullCircle);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(fullCircle);
            try {
                int totalVotes = 0;
                for (Answer answer : answers) {
                    totalVotes += answer.getVotesNumber();
                }
                double startAngle = 0;
                for (Answer answer : answers) {
                    g2d.setPaint(answer == selectedAnswer ? Color.RED : colorMap.get(answer));
                    double extent = 360.0 * answer.getVotesNumber() / totalVotes;
                    g2d.fill(new Arc2D.Double(xCenter, yCenter, SIZE, SIZE, startAngle, extent, Arc2D.PIE));
                    startAngle += extent;
                }
            } catch (MapperException e) {
                LOG.throwing("PieChartIcon", "paintIcon", e);
            }
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }

    private void prepareColorMap(List<Answer> answers) {
        if (answers.isEmpty()) {
            return;
        }
        int step = 256 / answers.size();
        for (int i = 0; i < answers.size(); i++) {
            int colorComponent = 255 - i * step;
            colorMap.put(answers.get(i), new Color(colorComponent, colorComponent, colorComponent));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new JFrame("Sample");
//                mainFrame.setLayout(new GridLayout(0, 16));
//                for (int i = 0; i < 256; i++) {
//                    Color color = new Color(i, i, i);
//                    mainFrame.add(new JLabel(new ColorFilledIcon(16, 16, color)));
//                }
//                try {
//                    mainFrame.add(new PieChartDiagram(Poll.getMapper().loadById(1).getAnswers()));
                mainFrame.add(new PieChartDiagram(Collections.<Answer>emptyList()));
//                } catch (MapperException e) {
//                    throw new RuntimeException(e);
//                }
                mainFrame.pack();
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }


}
