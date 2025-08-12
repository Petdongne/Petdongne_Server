package org.songeun.petdongne_server.compare.domain;

import java.util.List;

public class ProcessedSearchText {

    private final boolean empty;
    private final List<String> query;

    public boolean isEmpty() {
        return empty;
    }

    public boolean isSingleCharacter() {
        return query.size() == 1;
    }

    public String toQueryWhenSingleChar() {
        return query.getFirst();
    }

    public List<String> toQueryWhenMulti() {
        return query;
    }

    public static ProcessedSearchText ofEmpty() {
        return new ProcessedSearchText(true, null);
    }

    public static ProcessedSearchText createForMulti(List<String> query) {
        return new ProcessedSearchText(false, query);
    }

    public static ProcessedSearchText createForSingle(String query) {
        return new ProcessedSearchText(false, List.of(query));
    }

    private ProcessedSearchText(boolean empty, List<String> query) {
        this.empty = empty;
        this.query = query;
    }

}
