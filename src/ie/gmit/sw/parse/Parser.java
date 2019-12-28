package ie.gmit.sw.parse;

public abstract class Parser implements Runnable {
    private final String filePath;
    private int k;

    public Parser(String filePath, int k) {
        this.filePath = filePath;
        this.k = k;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public abstract void run();
}
