package ie.gmit.sw.parser;

import ie.gmit.sw.Language;
import ie.gmit.sw.LanguageEntry;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Class that stores a mapping of n-grams for each {@link Language} that is parsed by the {@link SubjectParser}.
 */
public class SubjectDatabase {
    /**
     * Map each language to its n-grams and their frequency of occurrence.
     */
    private final ConcurrentMap<Language, ConcurrentMap<Integer, LanguageEntry>> db = new ConcurrentSkipListMap<>();

    /**
     * Get the total size of the subject database.
     *
     * @return The size of the database
     */
    public int size() {
        return db.size();
    }

    /**
     * Adds a given n-gram to the Subject Database.
     * If the n-gram already exists in the DB, its frequency of occurrence is instead incremented.
     *
     * @param s    The n-gram to be added
     * @param lang The language the given n-gram belongs to
     */
    public void add(CharSequence s, Language lang) {
        int kmer = s.hashCode();
        ConcurrentMap<Integer, LanguageEntry> langDb = getLanguageEntries(lang);

        if (langDb.containsKey(kmer)) {
            langDb.computeIfPresent(kmer, ($, entry) -> entry.incrementFrequency());
        } else {
            langDb.put(kmer, new LanguageEntry(kmer, 1));
        }
    }

    /**
     * Get the Map belonging to the given language.
     * If the Map doesn't exist, a new one is created.
     *
     * @param lang The Language whose Map is to be returned
     * @return The Map associated with <code>lang</code>
     */
    private ConcurrentMap<Integer, LanguageEntry> getLanguageEntries(Language lang) {
        ConcurrentMap<Integer, LanguageEntry> langDb;

        if (db.containsKey(lang)) {
            langDb = db.get(lang);
        } else {
            langDb = new ConcurrentSkipListMap<>();
            db.put(lang, langDb);
        }

        return langDb;
    }

    /**
     * Reduces the number of n-gram entries stored for each language to the most frequently occurring only.
     *
     * @param max The maximum number of n-grams to store for each language.
     */
    public void resize(int max) {
        Set<Language> keys = db.keySet();

        for (Language lang : keys) {
            ConcurrentMap<Integer, LanguageEntry> top = getTop(max, lang);
            db.put(lang, top);
        }
    }

    /**
     * Gets a Map containing the most frequently occurring n-grams for a given language.
     *
     * @param max  The maximum number of n-grams the new map should contain
     * @param lang The language the n-grams belong to
     * @return The most frequently occurring n-grams for the Language <code>lang</code>
     */
    public ConcurrentMap<Integer, LanguageEntry> getTop(int max, Language lang) {
        ConcurrentMap<Integer, LanguageEntry> temp = new ConcurrentSkipListMap<>();
        // Get the Set of frequencies for lang.
        Set<LanguageEntry> les = new TreeSet<>(db.get(lang).values());

        int rank = 1;
        for (LanguageEntry le : les) {
            le.setRank(rank);
            temp.put(le.getKmer(), le);

            if (rank == max)
                break;

            rank++;
        }

        return temp;
    }

    /**
     * Takes a map of n-grams and calculates which language in the DB is the most likely match.
     *
     * @param query The map that will be queried against the database
     * @return The language that best matches the query
     */
    public Language getLanguage(Map<Integer, LanguageEntry> query) {
        TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();

        Set<Language> langs = db.keySet();
        for (Language lang : langs) {
            oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
        }

        return oopm.first().getLanguage();
    }

    /**
     * Calculates the distance between a query and subject map.
     *
     * @param query   The query map
     * @param subject The map to compare the query against
     * @return The distance between a query and subject map
     */
    private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
        int distance = 0;

        Set<LanguageEntry> les = new TreeSet<>(query.values());
        for (LanguageEntry q : les) {
            LanguageEntry s = subject.get(q.getKmer());

            if (s == null) {
                distance += subject.size() + 1;
            } else {
                distance += s.getRank() - q.getRank();
            }
        }

        return distance;
    }

    private static class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric> {
        private final Language lang;
        private final int distance;

        public OutOfPlaceMetric(Language lang, int distance) {
            this.lang = lang;
            this.distance = distance;
        }

        public Language getLanguage() {
            return lang;
        }

        public int getAbsoluteDistance() {
            return Math.abs(distance);
        }

        @Override
        public int compareTo(OutOfPlaceMetric o) {
            return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
        }

        @Override
        public String toString() {
            return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int langCount = 0;
        int kmerCount = 0;
        Set<Language> keys = db.keySet();

        for (Language lang : keys) {
            langCount++;
            sb.append(lang.name()).append("->\n");

            Collection<LanguageEntry> m = new TreeSet<>(db.get(lang).values());
            kmerCount += m.size();

            for (LanguageEntry le : m) {
                sb.append("\t").append(le).append("\n");
            }
        }

        sb.append(kmerCount)
                .append(" total k-mers in ")
                .append(langCount).
                append(" languages");

        return sb.toString();
    }
}
