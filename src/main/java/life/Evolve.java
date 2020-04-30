package life;

import java.util.Random;

public final class Evolve {

    public static final String ALIVE = "O";
    public static final String DEAD = " ";

    private final String[][] uni;

    public Evolve(int uniSize, long seed) {
        this.uni = new String[uniSize][uniSize];
        // random init
        Random rnd = new Random(seed);
        for (int i = 0; i < uniSize; ++i) {
            for (int j = 0; j < uniSize; ++j) {
                uni[i][j] = rnd.nextBoolean() ? ALIVE : DEAD;
            }
        }
    }

    public void evolve(int maxGen, LoopCallback loopCallback, EndCallback endCallback) {
        NextGen gen = new NextGen();
        String[][] newUni = uni;
        for (int i = 0; i < maxGen && !Thread.currentThread().isInterrupted(); ++i) {
            newUni = gen.apply(newUni);
            loopCallback.call(newUni, i + 1, totalAlive(newUni));
        }
        endCallback.finish();
    }

    private int totalAlive(String[][] uni) {
        int total = 0;
        for (String[] strings : uni) {
            for (String string : strings) {
                if (ALIVE.equals(string))
                    ++total;
            }
        }
        return total;
    }

}
