package ie.gmit.sw.query;

public interface Query {
    int MAX_QUERY_LENGTH = 400;

    String getQueryString() throws Exception;

    default String removeWhitespace(String qs) {
        return qs.replace("\r", " ")
                 .replace("\n", " ")
                 .replaceAll(" +", " ");
    }
}
