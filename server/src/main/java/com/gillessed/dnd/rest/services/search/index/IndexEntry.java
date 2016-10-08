package com.gillessed.dnd.rest.services.search.index;

import org.immutables.value.Value;

@Value.Immutable
public interface IndexEntry {
    enum Level {
        TITLE,
        HEADING,
        LINK_TO
    }
    String getTitle();
    String getKey();
    String getTarget();
    String getSource();
    Level getLevel();
}
