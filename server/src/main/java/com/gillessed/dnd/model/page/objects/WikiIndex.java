package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

/**
 * This is a special compound item that will simple display as a list of links
 * to children of the current page.
 */
@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiIndex.type)
public interface WikiIndex extends WikiObject {
    String type = "index";

    String getTitle();

    class Builder extends ImmutableWikiIndex.Builder {}
}
