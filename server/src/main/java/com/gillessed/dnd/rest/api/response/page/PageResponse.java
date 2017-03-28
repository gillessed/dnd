package com.gillessed.dnd.rest.api.response.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiPage;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutablePageResponse.class)
@JsonDeserialize(as = ImmutablePageResponse.class)
public interface PageResponse {
    WikiPage getPage();
    List<DirectoryEntry> getChildren();
    List<DirectoryEntry> getDirectoryEntries();
    List<DirectoryEntry> getParentPaths();

    class Builder extends ImmutablePageResponse.Builder {}
}
