package ie.gmit.sw;

import ie.gmit.sw.menu.Menu;

import java.io.IOException;

public class Runner {
    public static void main(String[] args) throws InterruptedException, IOException {
        Menu menu = new Menu();
        menu.display();

        QueryParser qp = new QueryParser(menu.getQueryFileLoc(), 4);
        qp.parse();

        Parser p = new Parser(menu.getDataLoc(), 4);

        Database db = new Database();
        p.setDatabase(db);

        Thread t = new Thread(p);
        t.start();
        t.join();

        db.resize(300);

        System.out.println(db.getLanguage(qp.getQueryMap()));
    }
}
