package com.gillessed.dnd.rest.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDirectoryContentsRequest.class)
@JsonDeserialize(as = ImmutableDirectoryContentsRequest.class)
public interface DirectoryContentsRequest {
    String getPath();
}
