package ie.gmit.sw.parser;

import ie.gmit.sw.LanguageEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class QueryParser extends Parser {
    private Map<Integer, LanguageEntry> queryMap = new TreeMap<>();
    private int queryLen = 400;

    public QueryParser(String filePath, int ... k) {
        super(filePath, k);
    }

    public Map<Integer, LanguageEntry> getQueryMap() {
        return new TreeMap<>(queryMap);
    }

    public int getQueryLen() {
        return queryLen;
    }

    public void setQueryLen(int queryLen) {
        this.queryLen = queryLen;
    }

    @Override
    public Void call() throws IOException {
        int kmer, freq = 1;
        String text = getQueryString();

        for (int i = 0; i < getK().length; i++) {
            for (int j = 0; j <= text.length() - getK()[i]; j++) {
                kmer = text.substring(j, j + getK()[i]).hashCode();

                if (queryMap.containsKey(kmer)) {
                    freq += queryMap.get(kmer).getFrequency();
                    queryMap.put(kmer, new LanguageEntry(kmer, freq));
                } else {
                    queryMap.put(kmer, new LanguageEntry(kmer, 1));
                }
            }
        }

        return null;
    }

    /**
     * Parse the first 400 or so characters from the query file into a query sentence.
     * @throws IOException if an IO error occurs when reading the query file
     */
    private String getQueryString() throws IOException {
        String content = Files.readString(Paths.get(getFilePath()));
        // Get rid of any extra whitespace
        content = content.replace("\r", " ")
                .replace("\n", " ")
                .replaceAll(" +", " ");

        if (content.length() > queryLen) {
            return content.substring(0, queryLen);
        }

        return content;
    }
}
