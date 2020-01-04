package ie.gmit.sw.query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class QueryFile implements Query {
    private Path filepath;

    public QueryFile(Path filepath) {
        this.filepath = filepath;
    }

    public Path getFilepath() {
        return filepath;
    }

    public QueryFile setFilepath(Path filepath) {
        this.filepath = filepath;

        return this;
    }

    @Override
    public String getQueryString() throws IOException {
        // Read string and get rid of any extra whitespace
        String queryString = removeWhitespace(Files.readString(filepath));

        if (queryString.length() > QUERY_LENGTH) {
            return queryString.substring(0, QUERY_LENGTH);
        }

        return queryString;
    }
}
