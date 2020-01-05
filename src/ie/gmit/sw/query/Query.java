package ie.gmit.sw.query;

/**
 * An object that will be passed to {@link ie.gmit.sw.parser.QueryParser} for parsing.
 *
 * @see QueryFile
 * @see QueryString
 */
public interface Query {
    /**
     * The default length of the query string. Heuristically, only the first ~400 characters are needed to determine
     * the query's language.
     */
    int MAX_QUERY_LENGTH = 400;

    /**
     * Method used by the Parser for retrieving the text that will be parsed.
     *
     * Typically this need only be the first ~400 characters of content, since that is all that is
     * required in order to generate a sufficient set of n-grams for correctly identifying a language.
     *
     * @return The string that will be parsed by the QueryParser
     * @throws Exception This method can throw a checked exception
     */
    String getQueryString() throws Exception;

    /**
     * Removes any newlines, carriage returns and extra spaces from the query string.
     *
     * @param queryString The query string
     * @return The query string sans whitespace
     */
    default String removeWhitespace(String queryString) {
        return queryString.replace("\r", " ")
                .replace("\n", " ")
                .replaceAll(" +", " ");
    }
}
