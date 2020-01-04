package ie.gmit.sw;

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