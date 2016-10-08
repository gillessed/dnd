package com.gillessed.dnd.rest.services.search.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gillessed.dnd.rest.services.PageService;
import com.gillessed.dnd.rest.services.search.index.Indexer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(WatchIndexerFactory.class)
})
public interface IndexerFactory {
    Indexer buildIndexer(PageService pageService, String root);
}
