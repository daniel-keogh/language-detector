package ie.gmit.sw;

import ie.gmit.sw.menu.Menu;
import ie.gmit.sw.menu.OutColour;
import ie.gmit.sw.parse.DatasetParser;
import ie.gmit.sw.parse.QueryParser;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.display();

        QueryParser qp = new QueryParser(menu.getQueryFileLoc(), 4);
        DatasetParser dp = new DatasetParser(menu.getDataLoc(), 4);

        Database db = new Database();
        dp.setDatabase(db);

        Thread query = new Thread(qp);
        Thread benchmark = new Thread(dp);

        query.start();
        System.out.println("Processing query...");
        benchmark.start();
        System.out.println("Building subject database...");

        query.join();
        System.out.println("Finished processing query...");
        benchmark.join();
        System.out.println("Finished building subject database...");

        String result = db.getLanguage(qp.getQueryMap()).toString();
        System.out.println("\nThe text appears to be written in "+ OutColour.format(result, OutColour.RESULT) +".");
    }
}
