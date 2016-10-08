package com.gillessed.dnd.rest.services.search;

import com.gillessed.dnd.rest.api.response.search.SearchResult;
import com.gillessed.dnd.rest.services.search.index.Indexer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class SearchService {
    private final int DEFAULT_LIMIT = 25;

    private final Indexer indexer;

    @Inject
    public SearchService(Indexer indexer) {
        this.indexer = indexer;
    }

    public List<SearchResult> performSearch(String searchQuery) {
        return performSearch(searchQuery, DEFAULT_LIMIT);
    }

    public List<SearchResult> performSearch(String searchQuery, int limit) {
        List<SearchResult> results = indexer.getIndex().getResultsForQuery(searchQuery);
        if (results.size() > limit) {
            results = results.subList(0, DEFAULT_LIMIT);
        }
        return results;
    }
}
