package ie.gmit.sw.parser;

import java.util.concurrent.Callable;

public abstract class Parser implements Callable<Void> {
    private final String filePath;
    private int[] k;

    public Parser(String filePath, int ... k) {
        this.filePath = filePath;
        this.k = k;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getK() {
        return k;
    }

    public void setK(int ... k) {
        this.k = k;
    }
}
