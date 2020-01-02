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

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(LanguageEntry next) {
        return -Integer.compare(frequency, next.getFrequency());
    }

    @Override
    public String toString() {
        return "LanguageEntry{" +
                "kmer=" + kmer +
                ", frequency=" + frequency +
                ", rank=" + rank +
                '}';
    }
}