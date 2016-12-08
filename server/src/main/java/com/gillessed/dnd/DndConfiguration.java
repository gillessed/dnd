package com.gillessed.dnd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gillessed.dnd.services.search.config.IndexerFactory;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DndConfiguration extends Configuration {

    @NotNull
    private String root;
    @JsonProperty
    public String getRoot() {
        return root;
    }
    @JsonProperty
    public void setRoot(String root) {
        this.root = root;
    }

    @Valid
    @NotNull
    private IndexerFactory indexerFactory;
    @JsonProperty("indexer")
    public IndexerFactory getIndexerFactory() {
        return indexerFactory;
    }
    @JsonProperty("indexer")
    public void setIndexerFactory(IndexerFactory indexerFactory) {
        this.indexerFactory = indexerFactory;
    }

    @NotNull
    private String sessionStoreFile;
    @JsonProperty
    public String getSessionStoreFile() {
        return sessionStoreFile;
    }
    @JsonProperty
    public void setSessionStoreFile(String sessionStoreFile) {
        this.sessionStoreFile = sessionStoreFile;
    }

    @NotNull
    private long pageCacheTimeToLive = 60;
    @JsonProperty
    public long getPageCacheTimeToLive() {
        return pageCacheTimeToLive;
    }
    @JsonProperty
    public void setPageCacheTimeToLive(long pageCacheTimeToLive) {
        this.pageCacheTimeToLive = pageCacheTimeToLive;
    }
}
