import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class stacktest {
    private stacktest() {

    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(stacktest::doRun);
    }

    private static void doRun() {
        final int[] x = new int[] {0, 40, 80, 120, 160, 200, 240, 280, 320, 360};
        final int[] y = new int[] {100, 200, 100, 50, 200, 0, 300, 200, 100, 300};

        final
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().setLayout(new BorderLayout());
        jFrame.getContentPane().add(new JPanelImpl(x, y), BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);
    }

    private static final class JPanelImpl extends JPanel {
        private final int[] x;
        private final int[] y;

        public JPanelImpl(final int[] x, final int[] y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void paintComponent(final Graphics graphics) {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, getWidth(), getHeight());

            for(int i = 0; i < this.x.length; i++) {
                if(i + 1 < this.x.length) {
                    final int x0 = this.x[i];
                    final int y0 = this.y[i];
                    final int x1 = this.x[i + 1];
                    final int y1 = this.y[i + 1];

                    graphics.setColor(Color.BLACK);
                    graphics.drawLine(x0, y0, x1, y1);
                    graphics.fillOval(x1 - 3, y1 - 3, 6, 6);
                }
            }
        }
    }
}