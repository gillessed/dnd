package com.gillessed.dnd.model.page.objects;


import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiSectionHeader.type)
public interface WikiSectionHeader extends WikiObject {
    String type = "section";
    String getText();
}
