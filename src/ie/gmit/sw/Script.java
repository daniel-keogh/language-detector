package ie.gmit.sw;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Enumeration of the Unicode range for language scripts. This is not a complete list.
 */
public enum Script {
    Arabic (0x0600, 0x06FF),
    Armenian (0x0530, 0x058F),
    Basic_Latin (0x0020, 0x007F), // Basic Latin A-Z, excludes accented characters etc.
    Bengali (0x0980, 0x09FF),
    Cherokee (0x13A0, 0x13FF),
    CJK (0x4E00, 0x9FFF), // Chinese, Japanese, Korean unified Ideographs
    Devanagari (0x0900, 0x097F),
    Ethiopic (0x1200, 0x137F),
    Gujarati (0x0A80, 0x0AFF),
    Greek_And_Coptic (0x0370, 0x03FF),
    Cyrillic (0x0400, 0x04FF),
    Georgian (0x10A0, 0x10FF),
    Hebrew (0x0590, 0x05FF),
    Hiragana (3040, 0x309F),
    Katakana (0x30A0, 0x30FF),
    Kannada (0x0C80, 0x0CFF),
    Khmer (0x1780, 0x17FF),
    Lao (0x0E80, 0x0EFF),
    Malayalam (0x0D00, 0x0D7F),
    Mongolian (0x1800, 0x18AF),
    Myanmar (0x1000, 0x109F),
    Oriya (0x0B00, 0x0B7F),
    Sinhala (0x0D80, 0x0DFF),
    Tamil (0x0B80, 0x0BFF),
    Telugu (0x0C00, 0x0C7F),
    Thanna (0x0780, 0x07BF),
    Thai (0x0E00, 0x0E7F),
    Tibeten (0x0F00, 0x0FFF);

    int lower, upper;

    Script(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Check if a given integer value is within the Unicode range for this script.
     *
     * @param num The integer value to test
     * @return True if num represents a character in this script
     */
    public boolean test(int num) {
        return num <= upper && num >= lower;
    }

    /**
     * Attempt to determine which script a given string is most likely written in.
     *
     * This is done by checking the Unicode value of each 1-gram in the text and from that, determining which script that
     * Unicode value belongs to.
     *
     * Since the text may contain characters in multiple scripts, this method will try to return the closest match.
     *
     * @param text The text to process
     * @return The Script that was the closest match
     */
    public static Script determine(String text) {
        Map<Script, Integer> matchingScripts = new TreeMap<>();

        text = text.replace(" ", "");

        for (int i = 0; i < text.length(); i++) {
            for (Script s : Script.values()) {
                if (s.test(text.charAt(i))) {
                    if (matchingScripts.containsKey(s)) {
                        matchingScripts.put(s, matchingScripts.get(s) + 1);
                    } else {
                        matchingScripts.put(s, 1);
                    }
                }
            }
        }

        // Find the highest Integer value in the map since that is most likely the script.
        Map.Entry<Script, Integer> maxEntry = Collections.max(matchingScripts.entrySet(), Map.Entry.comparingByValue());
        return maxEntry.getKey();
    }
}
