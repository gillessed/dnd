package com.gillessed.dnd.rest.api.response.search;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableSearchResponse.class)
@JsonDeserialize(as = ImmutableSearchResponse.class)
public interface SearchResponse {
    List<SearchResult> getSearchResults();
}
