package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiTitle.type)
public interface WikiTitle extends WikiObject {
    String type = "title";
    String getText();
    String getDescription();
}
