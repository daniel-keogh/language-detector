package ie.gmit.sw.parser;

import ie.gmit.sw.query.Query;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class QueryParser extends Parser<Query> {
    private Map<Integer, LanguageEntry> queryMap = new TreeMap<>();

    public QueryParser(Query query, int ... k) {
        super(query, k);
    }

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

    private void rank() {
        Set<LanguageEntry> entrySet = new TreeSet<>(queryMap.values());

        int rank = 1;
        for (var entry : entrySet) {
            queryMap.put(entry.getKmer(), entry.setRank(++rank));
        }
    }
}
