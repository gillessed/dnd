package com.gillessed.dnd.rest.api.response;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDndError.class)
@JsonDeserialize(as = ImmutableDndError.class)
public interface DndError {
    enum Type {
        WIKI_PAGE_NOT_FOUND
    };

    Type getErrorType();
    String getErrorMessage();
}
