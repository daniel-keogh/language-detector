package ie.gmit.sw.parser;

import ie.gmit.sw.LanguageEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class QueryParser extends Parser {
    private Map<Integer, LanguageEntry> queryMap = new TreeMap<>();
    private int queryLength = 400;

    public QueryParser(String filePath, int ... k) {
        super(filePath, k);
    }

    public Map<Integer, LanguageEntry> getQueryMapping() {
        return new TreeMap<>(queryMap);
    }

    public int getQueryLength() {
        return queryLength;
    }

    public void setQueryLength(int queryLength) {
        this.queryLength = queryLength;
    }

    @Override
    public Void call() throws IOException {
        String text = getQueryString();

        for (int i = 0; i < getK().length; i++) {
            for (int j = 0; j <= text.length() - getK()[i]; j++) {
                int kmer = text.substring(j, j + getK()[i]).hashCode();

                if (queryMap.containsKey(kmer)) {
                    queryMap.computeIfPresent(kmer, LanguageEntry::incrementFrequency);
                } else {
                    queryMap.put(kmer, new LanguageEntry(kmer, 1));
                }
            }
        }

        rank();

        return null;
    }

    private void rank() {
        Set<LanguageEntry> entrySet = new TreeSet<>(queryMap.values());

        int rank = 1;
        for (var entry : entrySet) {
            queryMap.put(entry.getKmer(), entry.setRank(++rank));
        }
    }

    /**
     * Parse the first 400 or so characters from the query file into a query sentence.
     * @throws IOException if an IO error occurs when reading the query file
     */
    private String getQueryString() throws IOException {
        // Read string and get rid of any extra whitespace
        String queryString = Files.readString(Path.of(getFilePath()))
                .replace("\r", " ")
                .replace("\n", " ")
                .replaceAll(" +", " ");

        if (queryString.length() > queryLength) {
            return queryString.substring(0, queryLength);
        }

        return queryString;
    }
}
