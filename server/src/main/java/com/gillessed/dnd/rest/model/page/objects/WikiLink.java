package com.gillessed.dnd.rest.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiLink.type)
public interface WikiLink extends WikiObject {
    String type = "link";
    String getText();
    String getTarget();
    boolean isBroken();
}
