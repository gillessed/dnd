package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiMeta.type)
public interface WikiMeta extends WikiObject {
    String type = "meta";
    Status getStatus();
    String getDescription();
    Type getType();
    @Nullable
    Object getObject();

    enum Status {
        DRAFT,
        PUBLISHED
    }

    enum Type {
        DOCUMENT,
        SPELL,
        SKILL,
        FEAT,
        ITEM,
        CREATURE,
    }

    class Builder extends ImmutableWikiMeta.Builder {}
}
