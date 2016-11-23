package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiParagraph.type)
public interface WikiParagraph extends WikiObject {
    String type = "paragraph";
    List<WikiObject> getWikiObjects();
}
