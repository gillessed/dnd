package com.gillessed.dnd.services.search;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.rest.api.response.search.ImmutableSearchResult;
import com.gillessed.dnd.rest.api.response.search.SearchResult;
import com.gillessed.dnd.services.page.PageProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SearchService {
    private final int searchResultLimit;
    private final PageProvider pageProvider;

    @Inject
    public SearchService(
            DndConfiguration dndConfiguration,
            PageProvider pageProvider) {
        this.searchResultLimit = dndConfiguration.getSearchResultLimit();
        this.pageProvider = pageProvider;
    }

    public List<SearchResult> performSearch(String searchQuery) {
        return performSearch(searchQuery, searchResultLimit);
    }

    public List<SearchResult> performSearch(String searchQuery, int limit) {
        List<WikiPage> pages = pageProvider.search(searchQuery);

        List<SearchResult> results = pages.stream()
                .map((WikiPage page) -> {
                    SearchResult result = ImmutableSearchResult.builder()
                            .title(page.getTitle())
                            .description(page.getMetadata().getDescription())
                            .target(page.getTarget())
                            .build();
                    return result;
                })
                .collect(Collectors.toList());
        if (results.size() > searchResultLimit) {
            results = results.subList(0, searchResultLimit);
        }
        return results;
    }
}
