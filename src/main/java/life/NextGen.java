package life;

public final class NextGen {

    private String[][] copy(String[][] src) {
        int length = src.length;
        String[][] target = new String[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private int neighboursAlive(String[][] uni, int i, int j) {
        // i - 1
        int a = (i - 1 < 0 ? uni.length - 1 : i - 1);
        // j - 1
        int b = (j - 1 < 0 ? uni[i].length - 1 : j - 1);
        // i + 1
        int c = (i + 1 >= uni.length ? 0 : i + 1);
        // j + 1
        int d = (j + 1 >= uni[i].length ? 0 : j + 1);
        String[] neighbours = new String[]{
                uni[a][b], uni[a][j], uni[a][d],
                uni[i][b], uni[i][d],
                uni[c][b], uni[c][j], uni[c][d]
        };
        int alives = 0;
        for (String neighbour : neighbours) {
            if (Evolve.ALIVE.equals(neighbour))
                ++alives;
        }
        return alives;
    }

    public String[][] apply(String[][] currentUni) {
        String[][] newUni = copy(currentUni);
        for (int i = 0; i < currentUni.length; ++i) {
            for (int j = 0; j < currentUni[i].length; ++j) {
                int neighboursAlive = neighboursAlive(currentUni, i, j);
                if (Evolve.ALIVE.equals(currentUni[i][j])) {
                    if (neighboursAlive < 2 || neighboursAlive > 3)
                        newUni[i][j] = Evolve.DEAD;
                } else {
                    if (neighboursAlive == 3)
                        newUni[i][j] = Evolve.ALIVE;
                }
            }
        }
        return newUni;
    }
}