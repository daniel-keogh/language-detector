package ie.gmit.sw;

import ie.gmit.sw.menu.Menu;
import ie.gmit.sw.menu.OutColour;
import ie.gmit.sw.parse.DatasetParser;
import ie.gmit.sw.parse.QueryParser;

import java.io.IOException;
import java.util.concurrent.*;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.display();

        Database db = new Database();

        QueryParser qParser = new QueryParser(menu.getQueryFileLoc(), 4);
        DatasetParser dsParser = new DatasetParser(menu.getDataLoc(), db, 4);

        ExecutorService ex = Executors.newFixedThreadPool(2);

        Future<Void> query = ex.submit(qParser);
        System.out.println("Processing query...");

        Future<Void> benchmark = ex.submit(dsParser);
        System.out.println("Building subject database...");

        try {
            query.get();
            System.out.println("Finished processing query...");
        } catch (ExecutionException e) {
            var cause = e.getCause();
            System.out.println(cause.getMessage());
        }

        try {
            benchmark.get();
            System.out.println("Finished building subject database...");
        } catch (ExecutionException e) {
            var cause = e.getCause();
            System.out.println(cause.getMessage());
        }

        ex.shutdown();

        menu.printResult(db.getLanguage(qParser.getQueryMap()));
    }
}
