package ie.gmit.sw.query;

public class QueryText implements Query {
    private String queryText;

    public QueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryText() {
        return queryText;
    }

    public QueryText setQueryText(String queryText) {
        this.queryText = queryText;

        return this;
    }

    @Override
    public String getQueryString() {
        String queryString = removeWhitespace(queryText);

        if (queryString.length() > QUERY_LENGTH) {
            return queryString.substring(0, QUERY_LENGTH);
        }

        return queryString;
    }
}
