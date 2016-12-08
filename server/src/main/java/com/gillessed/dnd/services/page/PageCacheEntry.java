package com.gillessed.dnd.services.page;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiPage;
import org.immutables.value.Value;

import java.nio.file.Path;

@Value.Immutable
@JsonSerialize
public interface PageCacheEntry {
    WikiPage getPage();
    Path getPath();
    long getTimestamp();

    class Builder extends ImmutablePageCacheEntry.Builder {}
}
