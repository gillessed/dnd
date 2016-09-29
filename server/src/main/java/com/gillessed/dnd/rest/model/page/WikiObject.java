package com.gillessed.dnd.rest.model.page;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.objects.WikiHeading;
import com.gillessed.dnd.rest.model.page.objects.WikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;
import com.gillessed.dnd.rest.model.page.objects.WikiText;
import com.gillessed.dnd.rest.model.page.objects.WikiTitle;

@JsonSerialize
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(WikiParagraph.class),
        @JsonSubTypes.Type(WikiTitle.class),
        @JsonSubTypes.Type(WikiSection.class),
        @JsonSubTypes.Type(WikiHeading.class),
        @JsonSubTypes.Type(WikiText.class)
})
public interface WikiObject {
}
