package ie.gmit.sw.query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Object that creates a query file that can then be parsed by a {@link ie.gmit.sw.parser.QueryParser}.
 *
 * @see Query
 */
public class QueryFile implements Query {
    private final Path filepath;
    private String queryString;

    /**
     * Constructs a new QueryFile using the given filepath
     *
     * @param filepath The path to the query file
     */
    public QueryFile(Path filepath) {
        this.filepath = filepath;
    }

    /**
     * Read the file and gets the text that is to be parsed.
     * The parsed text will be at most the first 400 characters of the file, excluding any extra whitespace.
     *
     * @return The part of the file that will be parsed
     * @throws IOException If an I/O error occurs reading from the file
     */
    @Override
    public String getQuery() throws IOException {
        if (this.queryString == null) {
            String queryString = removeWhitespace(Files.readString(filepath));

            if (queryString.length() > MAX_QUERY_LENGTH) {
                this.queryString = queryString.substring(0, MAX_QUERY_LENGTH);
            } else {
                this.queryString = queryString;
            }
        }

        return this.queryString;
    }
}
