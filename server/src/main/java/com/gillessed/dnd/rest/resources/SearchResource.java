package com.gillessed.dnd.rest.resources;

import com.gillessed.dnd.rest.api.request.SearchRequest;
import com.gillessed.dnd.rest.api.response.search.ImmutableSearchResponse;
import com.gillessed.dnd.rest.api.response.search.SearchResponse;
import com.gillessed.dnd.rest.api.response.search.SearchResult;
import com.gillessed.dnd.services.search.SearchService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/search")
public class SearchResource {
    private static final Logger log = LoggerFactory.getLogger(SearchResource.class);

    private final SearchService searchService;

    @Inject
    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }

    @POST
    public SearchResponse search(SearchRequest searchRequest) {
        List<SearchResult> results = searchService.performSearch(searchRequest.getQuery());
        return ImmutableSearchResponse.builder()
                .searchResults(results)
                .build();
    }
}
