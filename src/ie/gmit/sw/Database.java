package ie.gmit.sw;

import java.util.*;

public class Database {
    // Map each Language to its n-gram and frequency of occurrence.
    private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();

    /**
     * Adds a given n-gram to the DB.
     * If the n-gram already exists, its frequency of occurrence is instead incremented.
     * @param s
     * @param lang
     */
    public void add(CharSequence s, Language lang) {
        int kmer = s.hashCode();
        Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);

        int frequency = 1;
        if (langDb.containsKey(kmer)) {
            frequency += langDb.get(kmer).getFrequency();
        }

        langDb.put(kmer, new LanguageEntry(kmer, frequency));
    }

    /**
     * Get the Map belonging to the Language <code>lang</code>.
     * If the Map doesn't exist, a new one is created.
     * @param lang The Language whose Map is to be returned
     * @return The Map associated with <code>lang</code>
     */
    private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
        Map<Integer, LanguageEntry> langDb;

        if (db.containsKey(lang)) {
            langDb = db.get(lang);
        } else {
            langDb = new TreeMap<>();
            db.put(lang, langDb);
        }

        return langDb;
    }

    /**
     * Reduces the number of n-gram entries stored for each language to the most frequently occurring only.
     * @param max The maximum number of n-grams to store for each language.
     */
    public void resize(int max) {
        Set<Language> keys = db.keySet();

        for (Language lang : keys) {
            Map<Integer, LanguageEntry> top = getTop(max, lang);
            db.put(lang, top);
        }
    }

    /**
     * Gets a Map containing the most frequently occurring n-grams for a given language.
     * @param max The maximum number of n-grams the new map should contain
     * @param lang The language the n-grams belong to
     * @return The most frequently occurring n-grams for the Language <code>lang</code>
     */
    public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
        Map<Integer, LanguageEntry> temp = new TreeMap<>();
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
     * @param query
     * @return
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
     * @param query
     * @param subject
     * @return
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

    private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric> {
        private Language lang;
        private int distance;

        public OutOfPlaceMetric(Language lang, int distance) {
            super();
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
}
