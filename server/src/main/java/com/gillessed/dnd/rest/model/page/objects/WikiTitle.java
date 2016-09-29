package com.gillessed.dnd.rest.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiTitle.type)
public interface WikiTitle extends WikiObject {
    String type = "title";
    String getText();
}
