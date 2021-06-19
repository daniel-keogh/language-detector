package ie.gmit.sw.parser;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * This class serves as the root class of all parser classes.
 * The class contains a generic "content" type as well as an integer array listing all the k-mer sizes that will be parsed.
 * For instance, k[1,2,3] means each 1-gram, 2-gram and 3-gram of the text will be parsed.
 * <p>
 * This class implements the {@link Callable} interface and all subclasses are required to provide a <code>call()</code> method.
 *
 * @param <T> The type of the object that is to be parsed
 * @see QueryParser
 * @see SubjectParser
 */
public abstract class Parser<T> implements Callable<Void> {
    private final T content;
    private final int[] k;

    /**
     * Constructs a Parser object.
     *
     * @param content The object that will be parsed
     * @param k       the list of k-mer sizes (eg. 1, 2, 3)
     * @throws IllegalArgumentException if k is an empty array
     */
    public Parser(T content, int... k) {
        if (k.length == 0) {
            throw new IllegalArgumentException("k[] cannot be empty.");
        }
        this.content = content;
        this.k = k;
    }

    public T getContent() {
        return content;
    }

    public int[] getK() {
        return Arrays.copyOf(k, k.length);
    }
}
