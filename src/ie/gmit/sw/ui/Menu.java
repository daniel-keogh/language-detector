package ie.gmit.sw.ui;

import ie.gmit.sw.Language;
import ie.gmit.sw.parser.QueryParser;
import ie.gmit.sw.parser.SubjectParser;
import ie.gmit.sw.query.Query;
import ie.gmit.sw.query.QueryFile;
import ie.gmit.sw.query.QueryString;
import ie.gmit.sw.utils.Script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class for displaying a simple console based menu to the user.
 * By calling the <code>display()</code> method, the user will be prompted to enter the location of the benchmark
 * file, as well as their query.
 *
 * @see Query
 * @see Path
 */
public class Menu {
    private Path dataPath;
    private Query query;

    private static final String DEFAULT_DATA_PATH = "datasets/wili-2018-Large-117500-Edited.txt";

    public Path getDataPath() {
        return dataPath;
    }

    public Menu setDataPath(Path dataPath) {
        this.dataPath = dataPath;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public Menu setQuery(Query query) {
        this.query = query;
        return this;
    }

    /**
     * Displays a simple console-based menu to the user. They will be prompted to enter the location of the benchmark file,
     * as well as their query. The query may be either file or a string.
     * <p>
     * If an invalid benchmark file is entered, the user will continue to be prompted until they enter a valid file.
     *
     * @see QueryFile
     * @see QueryString
     */
    public void display() {
        System.out.println("=============================================================================");
        System.out.println("|                              Language Detector                            |");
        System.out.println("=============================================================================");

        try (var console = new Scanner(System.in)) {
            String input;

            while (true) {
                try {
                    if (dataPath == null) {
                        System.out.print("$ Enter WiLi data location (or press enter to use the default): ");
                        input = console.nextLine().trim();

                        if (input.isEmpty()) {
                            input = DEFAULT_DATA_PATH;
                        }

                        if (isFile(input)) {
                            this.dataPath = Path.of(input);
                        } else {
                            throw new FileNotFoundException("The dataset file could not be found");
                        }
                    }

                    if (query == null) {
                        System.out.print("$ Enter the query text/file: ");
                        input = console.nextLine().trim();

                        if (input.isEmpty()) {
                            continue;
                        }

                        if (isFile(input)) {
                            this.query = new QueryFile(Path.of(input));
                        } else {
                            this.query = new QueryString(input);
                        }
                    }

                    break;
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage() + ". Try again.\n");
                }
            }
        }

        detect();
    }

    private void detect() {
        double startTime = System.currentTimeMillis();

        QueryParser qParser = new QueryParser(query, 1, 2, 3, 4);

        Character.UnicodeScript script;

        try {
            script = Script.of(qParser.getContent().getQuery());
        } catch (Exception e) {
            script = null;
        }

        SubjectParser subParser = new SubjectParser(dataPath, script, 1, 2, 3, 4);

        ExecutorService ex = Executors.newFixedThreadPool(2);

        Future<Void> query = ex.submit(qParser);
        System.out.println("Processing query...");

        Future<Void> benchmark = ex.submit(subParser);
        System.out.println("Building subject database...");

        try {
            query.get();
            System.out.println("Finished processing query.");

            benchmark.get();
            System.out.println("Finished building subject database.");
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("[Error] " + e.getMessage());
            ex.shutdownNow();
            System.exit(1);
        }

        subParser.resize();

        ex.shutdown();

        try {
            Language result = subParser.detect(qParser.getQueryMapping());
            System.out.printf("\nThe text appears to be written in %s.\n", result.toString());
        } catch (IllegalStateException e) {
            System.err.println("\n[Error] Failed to detect the language: " + e.getMessage());
        }

        System.out.println("\nTime: " + calcDuration(startTime) + " (s)");
    }

    private static boolean isFile(String filename) {
        return new File(filename).isFile();
    }

    private static double calcDuration(double startTime) {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
