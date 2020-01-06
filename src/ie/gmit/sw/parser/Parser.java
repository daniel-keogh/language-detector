package ie.gmit.sw.parser;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * This class serves as the root class of all parser classes.
 * The class contains a generic "parsable" type as well as an integer array listing all the k-mer sizes that will be parsed.
 * For instance, k[1,2,3] means each 1-gram, 2-gram and 3-gram of the text will be parsed. This array cannot be empty.
 * 
 * This class implements the {@link Callable} interface and all subclasses are required to provide a <code>call()</code> method.
 *
 * @param <T> The type of the object that is to be parsed
 * 
 * @see QueryParser
 * @see SubjectParser
 */
public abstract class Parser<T> implements Callable<Void> {
    private T parsable;
    private int[] k;

    /**
     * Constructs a Parser object.
     */
    public Parser() { }
    
    /**
     * Constructs a Parser object.
     * @param parsable The object that will be parsed
     * @param k the list of k-mer sizes (eg. 1, 2, 3)
     * @throws IllegalArgumentException if k is an empty array
     */
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

    /**
     * Set the list of k-mer sizes
     * @param k The list of k-mer sizes (eg. 1, 2, 3)
     * @throws IllegalArgumentException if k is an empty array
     */
    public void setK(int ... k) {
        if (k.length == 0) {
            throw new IllegalArgumentException("k[] cannot be empty.");
        }
        this.k = k;
    }
}
