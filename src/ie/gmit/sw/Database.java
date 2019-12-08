package ie.gmit.sw;

import java.util.*;

public class Database {
    // Map each Language to its n-gram and frequency of occurrence.
    private Map<Language, Map<Integer, LanguageEntry>> db = new TreeMap<>();

    public void add(CharSequence s, Language lang) {
        int kmer = s.hashCode();
        // Get a handle on the Map for this particular language.
        Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);

        int frequency = 1;
        // If the kmer is already present, increment the frequency.
        if (langDb.containsKey(kmer)) {
            frequency += langDb.get(kmer).getFrequency();
        }
        // Overwrite existing kmer with new LanguageEntry.
        langDb.put(kmer, new LanguageEntry(kmer, frequency));
    }

    private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
        Map<Integer, LanguageEntry> langDb = null;

        // Create the Map if it doesn't exist, return it if it does.
        if (db.containsKey(lang)) {
            langDb = db.get(lang);
        } else {
            langDb = new TreeMap<Integer, LanguageEntry>();
            db.put(lang, langDb);
        }
        return langDb;
    }

    /**
     * Reduce the number of entries to a given max size.
     * @param max Maximum number of entries.
     */
    public void resize(int max) {
        Set<Language> keys = db.keySet(); // Get a Set of all Languages.

        for (Language lang : keys) {
            Map<Integer, LanguageEntry> top = getTop(max, lang);
            db.put(lang, top);
        }
    }

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

    public Language getLanguage(Map<Integer, LanguageEntry> query) {
        TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();

        Set<Language> langs = db.keySet();
        for (Language lang : langs) {
            oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
        }
        return oopm.first().getLanguage();
    }

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
