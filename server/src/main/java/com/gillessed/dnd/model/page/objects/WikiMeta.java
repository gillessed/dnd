package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiMeta.type)
public interface WikiMeta extends WikiObject {
    String type = "meta";
    Status getStatus();
    String getDescription();

    enum Status {
        DRAFT,
        PUBLISHED
    }

    class Builder extends ImmutableWikiMeta.Builder {}
}
