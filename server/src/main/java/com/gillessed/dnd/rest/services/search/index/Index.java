package com.gillessed.dnd.rest.services.search.index;

import com.gillessed.dnd.rest.api.response.search.SearchResult;

import javax.annotation.Nonnull;
import java.util.List;

public interface Index {
    List<SearchResult> getResultsForQuery(String query);
    void updateSource(@Nonnull String source, @Nonnull List<IndexEntry> entries);
    void deleteSource(@Nonnull String source);
}
