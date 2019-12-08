package ie.gmit.sw.menu;

/**
 * Enumeration of ANSI escape codes for providing coloured console output.
 * @see <a href="https://stackoverflow.com/a/5762502">https://stackoverflow.com/a/5762502</a>
 */
public enum OutColour {
    ERROR("\u001B[31m"),
    SUCCESS("\u001B[32m"),
    RESULT("\u001B[34m"),
    RESET("\u001B[0m");

    private final String colour;

    OutColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    /**
     * Returns a String coloured with the specified OutColour, including the reset at the end of the String.
     *
     * @param text String to colour.
     * @param colour Specifies which OutColor to use.
     * @return The coloured String.
     */
    public static String format(String text, OutColour colour) {
        return colour.getColour() + text + RESET.getColour();
    }
}
