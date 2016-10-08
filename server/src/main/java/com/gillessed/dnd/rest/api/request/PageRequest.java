package com.gillessed.dnd.rest.api.request;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutablePageRequest.class)
@JsonDeserialize(as = ImmutablePageRequest.class)
public interface PageRequest {
    String getPage();
}
