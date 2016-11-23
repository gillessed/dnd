package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiUnorderedList.type)
public interface WikiUnorderedList extends WikiObject {
    String type = "unorderedList";
    List<WikiObject> getListItems();
}
