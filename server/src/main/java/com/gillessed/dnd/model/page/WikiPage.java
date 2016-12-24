package com.gillessed.dnd.model.page;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.objects.WikiContent;
import com.gillessed.dnd.model.page.objects.WikiMeta;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonSerialize
public interface WikiPage {
    WikiMeta getMetadata();
    String getTitle();
    @Nullable WikiContent getPageContent();
    @Nullable WikiContent getDmContent();
    Target getTarget();
    String getPageSource();

    class Builder extends ImmutableWikiPage.Builder {}
}
