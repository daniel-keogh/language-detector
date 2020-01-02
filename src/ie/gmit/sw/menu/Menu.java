package ie.gmit.sw.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
    private String dataLoc;
    private String queryFileLoc;

    public String getDataLoc() {
        return dataLoc;
    }

    public void setDataLoc(String dataLoc) throws FileNotFoundException {
        if (isInvalidFile(dataLoc)) {
            throw new FileNotFoundException("Couldn't find file: \""+ dataLoc + "\"");
        }

        this.dataLoc = dataLoc;
    }

    public String getQueryFileLoc() {
        return queryFileLoc;
    }

    public void setQueryFileLoc(String queryFileLoc) throws FileNotFoundException {
        if (isInvalidFile(queryFileLoc)) {
            throw new FileNotFoundException("Couldn't find file: \""+ queryFileLoc + "\"");
        }

        this.queryFileLoc = queryFileLoc;
    }

    public void display() {
        System.out.println("=============================================================================");
        System.out.println("|                              Language Detector                            |");
        System.out.println("=============================================================================");

        String input;
        var console = new Scanner(System.in);

        do {
            try {
                if (dataLoc == null) {
                    System.out.print("$ Enter WiLi data location: ");
                    input = console.nextLine().trim();
                    setDataLoc(input);
                }

                if (queryFileLoc == null) {
                    System.out.print("$ Enter the query file location: ");
                    input = console.nextLine().trim();
                    setQueryFileLoc(input);
                }

                break;
            } catch (FileNotFoundException e) {
                System.out.println(OutColour.format(e.getMessage() +". Try again.\n", OutColour.ERROR));
            }
        } while (true);
    }

    private static boolean isInvalidFile(String path) {
        return !new File(path).isFile();
    }
}
