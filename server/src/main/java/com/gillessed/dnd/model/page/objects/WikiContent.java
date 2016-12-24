package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.WikiObject;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
@JsonTypeName(WikiContent.type)
public interface WikiContent extends WikiObject {
    String type = "content";
    ContentType getContentType();
    List<WikiSection> getContent();

    class Builder extends ImmutableWikiContent.Builder {}

    enum ContentType {
        PAGE,
        DM;

        public static ContentType fromString(String str) {
            switch(str) {
                case "content":
                    return PAGE;
                case "dm":
                    return DM;
                default:
                    throw new IllegalStateException("Cannot parse " + str);
            }
        }
    }
}
