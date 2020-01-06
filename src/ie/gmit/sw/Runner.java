package ie.gmit.sw;

import ie.gmit.sw.parser.SubjectParser;
import ie.gmit.sw.parser.QueryParser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Entry Point to the application.
 */
public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.display();

        double startTime = System.currentTimeMillis();

        QueryParser qParser = new QueryParser(menu.getQuery(), 1, 2, 3, 4);
        SubjectParser subParser = new SubjectParser(menu.getDataPath(), 1, 2, 3, 4);

        ExecutorService ex = Executors.newFixedThreadPool(2);

        Future<Void> query = ex.submit(qParser);
        System.out.println("Processing query...");

        Future<Void> benchmark = ex.submit(subParser);
        System.out.println("Building subject database...");
        
        try {
            try {
                query.get();
                System.out.println("Finished processing query.");
            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException) {
                    System.err.println("[Error] There was an issue with reading: "+ menu.getQuery());
                } else {
                    System.err.println(e.getMessage());
                }
                throw e;
            }

            try {
                benchmark.get();
                System.out.println("Finished building subject database.");
            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException) {
                    System.err.println("[Error] There was an issue with reading: "+ menu.getDataPath());
                } else {
                    System.err.println(e.getMessage());
                }
                throw e;
            }
        } catch (ExecutionException e) {
            ex.shutdownNow();
            return;
        }

        subParser.resize();

        ex.shutdown();

        try {
            Language result = subParser.detect(qParser.getQueryMapping());
            System.out.printf("\nThe text appears to be written in %s.\n", result.toString());
        } catch (IllegalStateException e) {
            System.err.println("\nFailed to detect the language: "+ e.getMessage());
        }

        System.out.println("\nTime: "+ (System.currentTimeMillis() - startTime) / 1000 +" (s)");
    }
}
