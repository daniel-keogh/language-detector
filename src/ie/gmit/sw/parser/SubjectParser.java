package ie.gmit.sw.parser;

import ie.gmit.sw.Language;
import ie.gmit.sw.LanguageEntry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for parsing the subject dataset file.
 */
public class SubjectParser extends Parser<Path> {
    private final SubjectDatabase db = new SubjectDatabase();
    private Character.UnicodeBlock queryScript;

    /**
     * Constructs a new SubjectParser object.
     *
     * @param filePath The path to the dataset file
     * @param k        the list of k-mers to parse from the query text
     * @throws IllegalArgumentException if k is an empty array
     */
    public SubjectParser(Path filePath, int... k) {
        super(filePath, k);
    }

    /**
     * Constructs a new SubjectParser object.
     *
     * @param filePath    The path to the dataset file
     * @param queryScript The {@link java.lang.Character.UnicodeBlock} the query belongs to. Languages not in this block are ignored.
     * @param k           the list of k-mers to parse from the query text
     * @throws IllegalArgumentException if k is an empty array
     */
    public SubjectParser(Path filePath, Character.UnicodeBlock queryScript, int... k) {
        super(filePath, k);
        this.queryScript = queryScript;
    }

    /**
     * Detects a language from a given query.
     *
     * @param query A map of k-mers to query against the subject database
     * @return The detected language
     * @throws IllegalStateException If the subject database is empty. This may happen if the dataset file wasn't parsed properly.
     */
    public Language detect(Map<Integer, LanguageEntry> query) {
        if (db.size() == 0) {
            throw new IllegalStateException("The Subject Database is empty.");
        }

        return db.getLanguage(query);
    }

    /**
     * Resize the subject database leaving only the most frequently occurring 300 entries.
     * <p>
     * Empirical evidence shows that the most frequently occurring ~300 n-grams are highly
     * correlated to a language, with the next 300-400 n-grams more correlated to the subject that a
     * text relates to.
     */
    public void resize() {
        final int MAX = 300;
        db.resize(MAX);
    }

    /**
     * Resize the subject database leaving only the most frequently occurring entries. The number of remaining
     * entries is specified by <code>max</code>.
     *
     * @param max The maximum number of entries to keep
     */
    public void resize(int max) {
        db.resize(max);
    }

    @Override
    public Void call() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(getContent().toFile())))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] record = line.trim().split("@");
                if (record.length != 2)
                    continue;

                executor.execute(() -> parseRecord(record[0], record[1]));
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        return null;
    }

    private void parseRecord(String text, String lang) {
        try {
            Language language = Language.valueOf(lang);

            // Don't parse the record if the query is not written in the same script
            if (!Arrays.asList(language.scripts()).contains(queryScript)) {
                return;
            }

            for (int i = 0; i < getK().length; i++) {
                for (int j = 0; j <= text.length() - getK()[i]; j++) {
                    CharSequence kmer = text.substring(j, j + getK()[i]);
                    db.add(kmer, language);
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}
