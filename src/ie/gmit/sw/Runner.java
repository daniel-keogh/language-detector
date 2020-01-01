package ie.gmit.sw;

import ie.gmit.sw.menu.Menu;
import ie.gmit.sw.menu.OutColour;
import ie.gmit.sw.parser.DatasetParser;
import ie.gmit.sw.parser.QueryParser;

import java.util.concurrent.*;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.display();

        double startTime = System.currentTimeMillis();

        QueryParser qParser = new QueryParser(menu.getQueryFileLoc(), 1, 2, 3, 4);
        DatasetParser dsParser = new DatasetParser(menu.getDataLoc(), 1, 2, 3, 4);

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

        System.out.printf("\nThe text appears to be written in %s.\n",
                OutColour.format(dsParser.detect(qParser.getQueryMap()).toString(), OutColour.RESULT));

        System.out.println("\nTime: "+ (System.currentTimeMillis() - startTime) / 1000 +" (s)");
    }
}
