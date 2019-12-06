package ie.gmit.sw;

/**
 * List of ANSI escape codes intended to allow coloured console output.
 *
 * @see <a href="https://stackoverflow.com/a/5762502">https://stackoverflow.com/a/5762502</a>
 */
public enum OutColour {
    SUCCESS("\u001B[32m"),
    ERROR("\u001B[31m"),
    RESULT("\u001B[34m"),
    RESET("\u001B[0m");

    private final String colour;

    private OutColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    /**
     * Returns a String coloured with the specified OutColour, including the reset at the end of said String.
     */
    public static String colourText(String text, OutColour colour) {
        return colour.getColour() + text + RESET.getColour();
    }
}
