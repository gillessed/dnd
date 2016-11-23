package com.gillessed.dnd.services.search.index;

import org.immutables.value.Value;

@Value.Immutable
public interface IndexEntry {
    enum Level {
        TITLE,
        HEADING,
        LINK_TO
    }
    String getTitle();
    String getDescription();
    String getKey();
    String getTarget();
    String getSource();
    Level getLevel();
}
