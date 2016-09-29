package com.gillessed.dnd.rest.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiHeading.type)
public interface WikiHeading extends WikiObject {
    String type = "heading";
    String getText();
    int getLevel();
}
