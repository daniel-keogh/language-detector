package ie.gmit.sw;

/**
 * Object that represents a singe k-mer/n-gram, along with its frequency and ranking.
 * Each k-mer is represented as an integer, rather than a String in order to reduce the amount of
 * memory used. Therefore, in order to store a string of text you must first call that string's <code>hashCode()</code> method.
 * <p>
 * This class implements {@link Comparable} and each LanguageEntry is ordered in descending order of frequency.
 */
public class LanguageEntry implements Comparable<LanguageEntry> {
    private final int kmer;
    private int frequency;
    private int rank;

    public LanguageEntry(int kmer, int frequency) {
        this.kmer = kmer;
        this.frequency = frequency;
    }

    public int getKmer() {
        return kmer;
    }

    public int getFrequency() {
        return frequency;
    }

    public LanguageEntry setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public LanguageEntry incrementFrequency() {
        this.frequency++;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public LanguageEntry setRank(int rank) {
        this.rank = rank;
        return this;
    }

    @Override
    public int compareTo(LanguageEntry next) {
        return -Integer.compare(frequency, next.getFrequency());
    }

    @Override
    public String toString() {
        return "[" + kmer + "/" + frequency + "/" + rank + "]";
    }
}