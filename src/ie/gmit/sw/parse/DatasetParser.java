package ie.gmit.sw.parse;

import ie.gmit.sw.Database;
import ie.gmit.sw.Language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DatasetParser extends Parser {
    private Database db;

    public DatasetParser(String filePath, int k) {
        super(filePath, k);
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public Database getDatabase(Database db) {
        return this.db;
    }

    @Override
    public void run() {
        try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(getFilePath())))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] record = line.trim().split("@");
                if (record.length != 2)
                    continue;

                parse(record[0], record[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
