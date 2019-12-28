package ie.gmit.sw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class QueryParser {
    private final String filePath;
    private int k;
    private Map<Integer, LanguageEntry> queryMap = new TreeMap<>();

    private static final int QUERY_LEN = 400;

    public QueryParser(String filePath, int k) {
        this.filePath = filePath;
        this.k = k;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Map<Integer, LanguageEntry> getQueryMap() {
        return new TreeMap<>(queryMap);
    }

    public void parse() throws IOException {
        int kmer, freq = 1;
        String text = getQueryString();

        for (int i = 0; i < text.length() - k; i++) {
            kmer = text.substring(i, i + k).hashCode();

            if (queryMap.containsKey(kmer)) {
                freq += queryMap.get(kmer).getFrequency();
                queryMap.put(kmer, new LanguageEntry(kmer, freq));
            } else {
                queryMap.put(kmer, new LanguageEntry(kmer, 1));
            }
        }
    }

    /**
     * Parse the first 400 characters from the query file into a query sentence.
     *
     * @throws IOException if an IO error occurs when reading the query file
     */
    public String getQueryString() throws IOException {
        String content = Files.readString(Paths.get(filePath));
        // Get rid of any extra whitespace
        content = content.replace("\r", " ")
                .replace("\n", " ")
                .replaceAll(" +", " ");

        if (content.length() > QUERY_LEN) {
            return content.substring(0, QUERY_LEN);
        }

        return content;
    }
}
