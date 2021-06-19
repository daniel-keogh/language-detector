package ie.gmit.sw.utils;

import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;
import static java.lang.Character.*;

/**
 * Utility class that can determine which {@link UnicodeScript} a given String belongs to.
 */
public final class Script {
    private Script() {
    }

    /**
     * Attempt to determine which script a given string is most likely written in.
     * <p>
     * This is done by finding the {@link UnicodeScript} of each 1-gram in the text.
     * <p>
     * Since the text may contain characters in multiple scripts, this method will try to return the closest match.
     *
     * @param text The text to process
     * @return The {@link UnicodeScript} that was the closest match, or null if there was no match
     */
    public static UnicodeScript of(String text) {
        // Find the highest Integer value in the map since that is most likely the script.
        // Reference: https://codereview.stackexchange.com/a/153400
        return findMatchingScripts(text)
                .entrySet()
                .stream()
                .collect(groupingBy(Map.Entry::getValue, TreeMap::new, toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .lastEntry()
                .getValue()
                .keySet()
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a mapping of each {@link UnicodeScript} found in a given String along with its frequency of occurrence.
     */
    private static Map<UnicodeScript, Integer> findMatchingScripts(String text) {
        Map<UnicodeScript, Integer> matchingScripts = new TreeMap<>();

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
