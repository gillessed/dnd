package com.gillessed.dnd.model.page;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.objects.WikiSection;
import com.gillessed.dnd.model.page.objects.WikiTitle;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize
public interface WikiPage {
    String getTitle();
    WikiTitle getTitleObject();
    String getDescription();
    String getTarget();
    List<WikiSection> getWikiSections();
}
