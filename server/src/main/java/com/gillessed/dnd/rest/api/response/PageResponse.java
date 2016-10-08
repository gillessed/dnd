package com.gillessed.dnd.rest.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.WikiPage;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePageResponse.class)
@JsonDeserialize(as = ImmutablePageResponse.class)
public interface PageResponse {
    WikiPage getPage();
}
