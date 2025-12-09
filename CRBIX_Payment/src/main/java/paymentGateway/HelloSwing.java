package paymentGateway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloSwing {
    public static void main(String[] args) {
        // Always create UI on EDT
        SwingUtilities.invokeLater(HelloSwing::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hello Swing Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // center

        // Top panel holds button and label stacked vertically
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btn = new JButton("Show Hello World & Shape");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // The label starts hidden
        JLabel helloLabel = new JLabel("Hello World");
        // light red / pink-ish; tweak RGB if you want
        helloLabel.setForeground(new Color(255, 153, 153));
        helloLabel.setFont(helloLabel.getFont().deriveFont(Font.BOLD, 18f));
        helloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        helloLabel.setVisible(false);

        topPanel.add(btn);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(helloLabel);

        // Custom drawing panel below; initially shape not shown
        DrawPanel drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(380, 150));
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Button action: show label and shape
        btn.addActionListener(new ActionListener() {
            private boolean shown = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                shown = true;
                helloLabel.setVisible(true);
                drawPanel.setShowShape(true);
                drawPanel.repaint();
                // optional: disable the button after first click
                btn.setEnabled(false);
            }
        });

        // Layout frame: topPanel NORTH, drawing center
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(drawPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    // Custom JPanel that draws a shape when showShape is true
    static class DrawPanel extends JPanel {
        private boolean showShape = false;

        public void setShowShape(boolean show) {
            this.showShape = show;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!showShape) return;
            // Use Graphics2D for better quality
            Graphics2D g2 = (Graphics2D) g.create();
            // enable anti-aliasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // choose a color for the shape (slightly darker red)
            g2.setColor(new Color(220, 80, 80));
            // draw a filled oval centered in the panel
            int w = getWidth();
            int h = getHeight();
            int ovalW = Math.min(w, h) - 40;
            int ovalH = ovalW / 2; // make it wider than tall
            int x = (w - ovalW) / 2;
            int y = (h - ovalH) / 2;
            g2.fillOval(x, y, ovalW, ovalH);

            g2.dispose();
        }
    }
}

