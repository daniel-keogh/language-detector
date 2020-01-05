package ie.gmit.sw;

import ie.gmit.sw.query.Query;
import ie.gmit.sw.query.QueryFile;
import ie.gmit.sw.query.QueryString;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Class for displaying a console based menu to the user.
 * By calling the <code>display()</code> method, the user will be prompted to enter the location of the benchmark
 * file, as well as their query.
 *
 * The query must be an instance of a class that implements the Query interface.
 * 
 * @see Query
 * @see Path
 */
public class Menu {
    private Path dataPath;
    private Query query;

    public Path getDataPath() {
        return dataPath;
    }

    public void setDataPath(Path dataPath) {
        this.dataPath = dataPath;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * Displays a console-based menu to the user. They will be prompted to enter the location of the benchmark file, as well as
     * their query text/file. If an invalid file is entered, the user will continue to be prompted until they enter a valid file.
     * 
     * @see QueryFile
     * @see QueryString
     */
    public void display() {
        System.out.println("=============================================================================");
        System.out.println("|                              Language Detector                            |");
        System.out.println("=============================================================================");

        String input;
        var console = new Scanner(System.in);

        do {
            try {
                if (dataPath == null) {
                    System.out.print("$ Enter WiLi data location: ");
                    input = console.nextLine().trim();

                    if (isFile(input)) {
                        setDataPath(Path.of(input));
                    } else {
                        throw new FileNotFoundException("That file does not exist");
                    }
                }

                if (query == null) {
                    System.out.print("$ Enter the query text/file: ");
                    input = console.nextLine().trim();

                    if (isFile(input)) {
                        setQuery(new QueryFile(Path.of(input)));
                    } else {
                        setQuery(new QueryString(input));
                    }
                }

                break;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage() +". Try again.\n");
            }
        } while (true);
    }

    private static boolean isFile(String filename) {
        return new File(filename).isFile();
    }
}
