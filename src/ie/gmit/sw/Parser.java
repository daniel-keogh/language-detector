package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Parser implements Runnable {
    private Database db;
    private String file;
    private int k;

    public Parser(String file, int k) {
        this.file = file;
        this.k = k;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;

            while ((line = br.readLine()) != null) {
                String[] record = line.trim().split("@");
                if (record.length != 2)
                    continue;

                parse(record[0], record[1]);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse(String text, String lang, int ... ks) {
        Language language = Language.valueOf(lang);

        for (int i = 0; i <= text.length() - k; i++) {
            CharSequence kmer = text.substring(i, i + k);
            db.add(kmer, language);
        }
    }

    public void analyseQuery(CharSequence query) {

    }
}
