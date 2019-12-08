package ie.gmit.sw.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class Menu {
    private String dataLoc;

    public Menu() { }

    public Menu(String dataLoc) throws FileNotFoundException {
        setDataLoc(dataLoc);
    }

    public String getDataLoc() {
        return dataLoc;
    }

    public void setDataLoc(String dataLoc) throws FileNotFoundException {
        // Verify that the file exists
        if (!new File(dataLoc).isFile()) {
            throw new FileNotFoundException("Couldn't find file: "+ dataLoc);
        }

        this.dataLoc = dataLoc;
    }

    public void display() {
        System.out.println("=============================================================================");
        System.out.println("|                              Language Detector                            |");
        System.out.println("=============================================================================");

        if (dataLoc == null) {
            String input;
            try (var console = new Scanner(System.in)) {
                do {
                    System.out.print("$ Enter WiLi data location: ");
                    try {
                        input = console.nextLine().trim();
                        setDataLoc(input);
                        break;
                    } catch (FileNotFoundException e) {
                        System.out.println(OutColour.format("File not found. Try again.\n", OutColour.ERROR));
                    }
                } while (true);
            }
        }
    }
}
