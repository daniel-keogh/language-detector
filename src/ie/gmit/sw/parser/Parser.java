package ie.gmit.sw.parser;

import java.util.Arrays;
import java.util.concurrent.Callable;

public abstract class Parser implements Callable<Void> {
    private final String filePath;
    private int[] k;

    public Parser(String filePath, int ... k) {
        if (k.length == 0) {
            throw new IllegalArgumentException("k[] cannot be empty.");
        }
        this.filePath = filePath;
        this.k = k;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getK() {
        return Arrays.copyOf(k, k.length);
    }

    public void setK(int ... k) {
        if (k.length == 0) {
            throw new IllegalArgumentException("k[] cannot be empty.");
        }
        this.k = k;
    }
}
