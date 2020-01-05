package ie.gmit.sw.parser;

/**
 * Object that represents a singe k-mer, along with its frequency and ranking.
 * Each k-mer is represented as an integer, rather than a String in order to reduce the amount of
 * memory used.
 *
 * This class implements {@link Comparable} which orders each LanguageEntry in descending order of frequency.
 */
public class LanguageEntry implements Comparable<LanguageEntry> {
    private int kmer;
    private int frequency;
    private int rank;

    public LanguageEntry(int kmer, int frequency) {
        this.kmer = kmer;
        this.frequency = frequency;
    }

    public int getKmer() {
        return kmer;
    }

    public LanguageEntry setKmer(int kmer) {
        this.kmer = kmer;

        return this;
    }

    public int getFrequency() {
        return frequency;
    }

    public LanguageEntry setFrequency(int frequency) {
        this.frequency = frequency;

        return this;
    }

    public int getRank() {
        return rank;
    }

    public LanguageEntry setRank(int rank) {
        this.rank = rank;

        return this;
    }

    /**
     * Increases the frequency of a given LanguageEntry by 1.
     *
     * @param kmer The kmer associated with the given LanguageEntry
     * @param langEntry The LanguageEntry to be incremented
     * @return A reference to the incremented LanguageEntry
     */
    public static LanguageEntry incrementFrequency(Integer kmer, LanguageEntry langEntry) {
        return langEntry.setFrequency(langEntry.frequency + 1);
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