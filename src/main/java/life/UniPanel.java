package life;

import javax.swing.*;
import java.awt.*;

public final class UniPanel extends JPanel {

    private final int uniSize;
    private String[][] uni;

    public UniPanel(int uniSize) {
        this.uniSize = uniSize;
    }

    public void updateUni(String[][] uni) {
        this.uni = uni;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (uni != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            int size = Math.min(getWidth(), getHeight());
            int step = size / uniSize;
            for (int i = 0; i < size; i += step) {
                g2.drawLine(i, 0, i, size - (size % step));
                g2.drawLine(0, i, size - (size % step), i);
            }
            g2.setColor(Color.DARK_GRAY);
            for (int i = 0; i < uni.length; ++i) {
                for (int j = 0; j < uni[i].length; ++j) {
                    if (Evolve.ALIVE.equals(uni[i][j]))
                        g2.fillRect(j * step, i * step, step, step);
                }
            }
        }
    }
}
