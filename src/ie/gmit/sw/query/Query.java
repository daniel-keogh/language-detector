package ie.gmit.sw.query;

public interface Query {
    int QUERY_LENGTH = 400;

    String getQueryString() throws Exception;

    default String removeWhitespace(String qs) {
        return qs.replace("\r", " ")
                 .replace("\n", " ")
                 .replaceAll(" +", " ");
    }
}
