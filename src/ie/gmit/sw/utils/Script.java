package ie.gmit.sw.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

/**
 * Utility class that can determine which Unicode block a given String belongs to.
 *
 * @see java.lang.Character.UnicodeBlock
 */
public final class Script {
    private Script() {
    }

    /**
     * Attempt to determine which script a given string is most likely written in.
     * <p>
     * This is done by finding the Unicode block of each 1-gram in the text.
     * <p>
     * Since the text may contain characters in multiple scripts, this method will try to return the closest match.
     *
     * @param text The text to process
     * @return The {@link java.lang.Character.UnicodeBlock} that was the closest match, or null if there was no match
     */
    public static Character.UnicodeBlock of(String text) {
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

    private static Map<Character.UnicodeBlock, Integer> findMatchingScripts(String text) {
        Map<Character.UnicodeBlock, Integer> matchingScripts = new HashMap<>();

        text = stripWhitespace(text);

        for (int i = 0; i < text.length(); i++) {
            var block = Character.UnicodeBlock.of(text.charAt(i));

            if (matchingScripts.containsKey(block)) {
                matchingScripts.put(block, matchingScripts.get(block) + 1);
            } else {
                matchingScripts.put(block, 1);
            }
        }

        return matchingScripts;
    }

    private static String stripWhitespace(String text) {
        return text.replaceAll("\\p{Punct}|\\p{Space}", "");
    }
}
