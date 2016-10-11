package com.gillessed.dnd.rest.model.page;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;
import com.gillessed.dnd.rest.model.page.objects.WikiTitle;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
public interface WikiPage {
    String getTitle();
    WikiTitle getTitleObject();
    List<WikiSection> getWikiSections();
}
