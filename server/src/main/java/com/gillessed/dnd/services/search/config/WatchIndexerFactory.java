package com.gillessed.dnd.services.search.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gillessed.dnd.services.PageService;
import com.gillessed.dnd.services.search.index.Indexer;
import com.gillessed.dnd.services.search.index.WatchIndexer;

@JsonTypeName("watch")
public class WatchIndexerFactory implements IndexerFactory {

    @Override
    public Indexer buildIndexer(PageService pageService, String root) {
        return new WatchIndexer(pageService, root);
    }
}
