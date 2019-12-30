package ie.gmit.sw.parser;

import ie.gmit.sw.Database;
import ie.gmit.sw.Language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatasetParser extends Parser {
    private Database db = new Database();

    public DatasetParser(String filePath, int k) {
        super(filePath, k);
    }

    public Database getDatabase() {
        return this.db;
    }

    @Override
    public Void call() throws IOException {
        var br = new BufferedReader(new InputStreamReader(new FileInputStream(getFilePath())));
        String line;

        while ((line = br.readLine()) != null) {
            String[] record = line.trim().split("@");
            if (record.length != 2)
                continue;

            parseRecord(record[0], record[1]);
        }

        br.close();
        return null;
    }

    public void parseRecord(String text, String lang) {
        Language language = Language.valueOf(lang);

        for (int i = 0; i <= text.length() - getK(); i++) {
            CharSequence kmer = text.substring(i, i + getK());
            db.add(kmer, language);
        }
    }
}
