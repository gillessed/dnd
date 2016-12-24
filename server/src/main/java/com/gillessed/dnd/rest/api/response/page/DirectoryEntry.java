package com.gillessed.dnd.rest.api.response.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.Target;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDirectoryEntry.class)
@JsonDeserialize(as = ImmutableDirectoryEntry.class)
public interface DirectoryEntry {
    Target getTarget();
    String getTitle();
    String getDescription();

    class Builder extends ImmutableDirectoryEntry.Builder {}
}
