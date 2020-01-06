package ie.gmit.sw.query;

/**
 * Object that creates a query string that can then be parsed by a {@link ie.gmit.sw.parser.QueryParser}.
 *
 * @see Query
 */
public class QueryString implements Query {
    private String queryString;

    /**
     * Constructs a new QueryString object using the given String.
     * @param queryString The string to query
     */
    public QueryString(String queryString) {
        this.queryString = removeWhitespace(queryString);
    }

    public QueryString setQueryString(String queryString) {
        this.queryString = removeWhitespace(queryString);

        return this;
    }

    @Override
    public String getQueryString() {
        if (queryString.length() > MAX_QUERY_LENGTH) {
            return queryString.substring(0, MAX_QUERY_LENGTH);
        }

        return queryString;
    }
}
