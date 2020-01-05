package ie.gmit.sw.parser;

import ie.gmit.sw.query.Query;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class parses a query.
 */
public class QueryParser extends Parser<Query> {
    private Map<Integer, LanguageEntry> queryMap = new TreeMap<>();

    /**
     * Constructs a new QueryParser object.
     * @param query The query to be parsed
     * @param k the list of k-mers to parse from the query text
     */
    public QueryParser(Query query, int ... k) {
        super(query, k);
    }

    /**
     * Get the result of the parsed query.
     * @return A map containing the result of the parsed query.
     */
    public Map<Integer, LanguageEntry> getQueryMapping() {
        return new TreeMap<>(queryMap);
    }

    @Override
    public Void call() throws Exception {
        String text = getParsable().getQueryString();

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

    /**
     * Rank each n-gram in the query map in order of its frequency.
     */
    private void rank() {
        Set<LanguageEntry> entrySet = new TreeSet<>(queryMap.values());

        int rank = 1;
        for (var entry : entrySet) {
            queryMap.put(entry.getKmer(), entry.setRank(++rank));
        }
    }
}
