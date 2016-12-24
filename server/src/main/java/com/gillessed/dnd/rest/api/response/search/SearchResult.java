package com.gillessed.dnd.rest.api.response.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.Target;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableSearchResult.class)
@JsonDeserialize(as = ImmutableSearchResult.class)
public interface SearchResult {
    String getTitle();
    String getDescription();
    Target getTarget();
}
