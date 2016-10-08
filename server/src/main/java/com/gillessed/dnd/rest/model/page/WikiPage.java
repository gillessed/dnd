package com.gillessed.dnd.rest.model.page;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
public interface WikiPage {
    String getTitle();
    List<WikiObject> getWikiObjects();
}
