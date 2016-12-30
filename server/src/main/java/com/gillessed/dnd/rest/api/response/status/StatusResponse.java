package com.gillessed.dnd.rest.api.response.status;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.model.page.objects.WikiLink;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableStatusResponse.class)
@JsonDeserialize(as = ImmutableStatusResponse.class)
public interface StatusResponse {
    int getTotalPageCount();
    int getDraftPageCount();
    int getPublishedPageCount();
    int getLinkCount();
    int getBrokenLinkCount();
    List<WikiLink> getBrokenLinks();

    class Builder extends ImmutableStatusResponse.Builder {}
}
