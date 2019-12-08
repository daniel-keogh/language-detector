package ie.gmit.sw;

import ie.gmit.sw.menu.Menu;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.display();

        Parser p = new Parser(menu.getDataLoc(), 4);

        Database db = new Database();
        p.setDatabase(db);

        Thread t = new Thread(p);
        t.start();
        t.join();

        db.resize(300);
    }
}
