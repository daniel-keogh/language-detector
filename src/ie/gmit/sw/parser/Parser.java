package ie.gmit.sw.parser;

import java.util.Arrays;
import java.util.concurrent.Callable;

public abstract class Parser<T> implements Callable<Void> {
    private T parsable;
    private int[] k;

    public Parser(T parsable, int ... k) {
        if (k.length == 0) {
            throw new IllegalArgumentException("k[] cannot be empty.");
        }
        this.parsable = parsable;
        this.k = k;
    }

    public T getParsable() {
        return parsable;
    }

    public Parser<T> setParsable(T parsable) {
        this.parsable = parsable;

        return this;
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
