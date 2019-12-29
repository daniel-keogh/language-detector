package ie.gmit.sw.parse;

import ie.gmit.sw.Database;
import ie.gmit.sw.Language;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatasetParser extends Parser {
    private Database db;

    public DatasetParser(String filePath, Database db, int k) {
        super(filePath, k);
        this.db = db;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public Database getDatabase(Database db) {
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

            parse(record[0], record[1]);
        }

        br.close();
        return null;
    }

    public void parse(String text, String lang, int ... ks) {
        Language language = Language.valueOf(lang);
        CharSequence kmer;

        for (int i = 0; i <= text.length() - getK(); i++) {
            kmer = text.substring(i, i + getK());
            db.add(kmer, language);
        }
    }
}
