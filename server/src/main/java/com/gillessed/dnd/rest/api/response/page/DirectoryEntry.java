package com.gillessed.dnd.rest.api.response.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDirectoryEntry.class)
@JsonDeserialize(as = ImmutableDirectoryEntry.class)
public interface DirectoryEntry {
    String getPath();
    String getTitle();
    String getDescription();

    class Builder extends ImmutableDirectoryEntry.Builder {}
}
