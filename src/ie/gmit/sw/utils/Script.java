package ie.gmit.sw.utils;

import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Character.*;

/**
 * Utility class that can determine which {@link UnicodeScript} a given String belongs to.
 */
public final class Script {
    private Script() {
    }

    /**
     * Determines which script a given string is most likely written in.
     * <p>
     * This is done by finding the {@link UnicodeScript} of each 1-gram in the text.
     * <p>
     * Since the text may contain characters in multiple scripts, this method will return the closest match.
     *
     * @param text The text to process
     * @return The {@link UnicodeScript} that was the closest match
     */
    public static UnicodeScript of(String text) {
        // Returns the script with the highest value/frequency in the map
        return findMatchingScripts(text).lastKey();
    }

    /**
     * Gets a mapping of each {@link UnicodeScript} found in a given String along with its frequency of
     * occurrence in ascending order.
     */
    private static SortedMap<UnicodeScript, Integer> findMatchingScripts(String text) {
        SortedMap<UnicodeScript, Integer> matchingScripts = new TreeMap<>();

        text = stripWhitespace(text);

        for (int i = 0; i < text.length(); i++) {
            UnicodeScript script = UnicodeScript.of(text.charAt(i));

            if (matchingScripts.containsKey(script)) {
                matchingScripts.put(script, matchingScripts.get(script) + 1);
            } else {
                matchingScripts.put(script, 1);
            }
        }

        return matchingScripts;
    }

    private static String stripWhitespace(String text) {
        return text.replaceAll("\\p{Punct}|\\p{Space}", "");
    }
}
