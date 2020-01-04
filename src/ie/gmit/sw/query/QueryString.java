package ie.gmit.sw.query;

public class QueryString implements Query {
    private String queryString;

    public QueryString(String queryString) {
        this.queryString = removeWhitespace(queryString);
    }

    public QueryString setQueryText(String queryString) {
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
