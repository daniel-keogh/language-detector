package ie.gmit.sw.parser;

import ie.gmit.sw.Database;
import ie.gmit.sw.Language;
import ie.gmit.sw.LanguageEntry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatasetParser extends Parser {
    private Database db = new Database();

    public DatasetParser(String filePath, int ... k) {
        super(filePath, k);
    }

    public Language detect(Map<Integer, LanguageEntry> query) {
        return db.getLanguage(query);
    }

    public void resize(int max) {
        db.resize(max);
    }

    @Override
    public Void call() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var br = new BufferedReader(new InputStreamReader(new FileInputStream(getFilePath())));
        String line;

        while ((line = br.readLine()) != null) {
            String[] record = line.trim().split("@");
            if (record.length != 2)
                continue;

            executor.execute(() -> parseRecord(record[0], record[1]));
        }

        br.close();

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            executor.shutdownNow();
        }

        return null;
    }

    private void parseRecord(String text, String lang) {
        Language language = Language.valueOf(lang);

        for (int i = 0; i < getK().length; i++) {
            for (int j = 0; j <= text.length() - getK()[i]; j++) {
                CharSequence kmer = text.substring(j, j + getK()[i]);
                db.add(kmer, language);
            }
        }
    }
}
