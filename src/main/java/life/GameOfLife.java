package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;

public final class GameOfLife extends JFrame {

    private static final int SIZE = 800;
    private final JLabel generationLabel = new JLabel("Generation #0");
    private final JLabel aliveLabel = new JLabel("Alive: 0");

    private UniPanel uniPanel;
    private int uniSize;
    private int maxGen;

    private final Semaphore semaphore = new Semaphore(1);
    private final JToggleButton playBtn = new JToggleButton(new PlayAction());
    private final JButton resetBtn = new JButton(new ResetAction());
    private Thread th;

    public GameOfLife() {
        setTitle("Game Of Life");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(SIZE, SIZE + 100);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        generationLabel.setName("GenerationLabel");
        aliveLabel.setName("AliveLabel");
        playBtn.setName("PlayToggleButton");
        resetBtn.setName("ResetButton");

        northPanel.add(generationLabel);
        northPanel.add(aliveLabel);
        northPanel.add(playBtn);
        northPanel.add(resetBtn);

        add(northPanel, BorderLayout.NORTH);

        setLocationRelativeTo(null);

        setVisible(true);
    }

    public void setVisible(boolean visible, int uniSize, int maxGen) {
        setVisible(visible);
        if (visible) {
            this.uniSize = uniSize;
            this.maxGen = maxGen;
            uniPanel = new UniPanel(uniSize);
            uniPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            add(uniPanel, BorderLayout.CENTER);
        }
    }

    private final class PlayAction extends AbstractAction {

        private final Runnable work = new Runnable() {
            @Override
            public void run() {
                new Evolve(uniSize, System.currentTimeMillis()).evolve(
                        maxGen,
                        (uni, generation, alive) -> {
                            generationLabel.setText("Generation #" + generation);
                            aliveLabel.setText("Alive: " + alive);
                            uniPanel.updateUni(uni);
                            try {
                                semaphore.acquire();
                                Thread.sleep(100L);
                                semaphore.release();
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        },
                        () -> {
                            th = null;
                            resetBtn.setEnabled(true);
                        });
            }
        };


        public PlayAction() {
            super("Play");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton btn = (JToggleButton) e.getSource();
            if (btn.isSelected()) {
                resetBtn.setEnabled(false);
                btn.setText("Pause");
                if (th == null) {
                    th = new Thread(work);
                    th.start();
                } else {
                    semaphore.release();
                }
            } else {
                if (th != null && !th.isAlive()) {
                    btn.setText("Play");
                } else {
                    btn.setText("Resume");
                    resetBtn.setEnabled(true);
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private final class ResetAction extends AbstractAction {

        public ResetAction() {
            super("Reset");
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            if (th != null) {
                th.interrupt();
            }
            playBtn.setText("Play");
            uniPanel.updateUni(null);
            semaphore.release();
            generationLabel.setText("Generation #0");
            aliveLabel.setText("Alive: 0");
        }
    }

    public static void main(String[] args) {
        GameOfLife gl = new GameOfLife();
        gl.setVisible(true, 20, 20);
    }
}
