package com.gillessed.dnd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gillessed.dnd.services.search.config.IndexerFactory;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DndConfiguration extends Configuration {

    @NotNull
    private String root;

    @Valid
    @NotNull
    private IndexerFactory indexerFactory;

    @JsonProperty
    public String getRoot() {
        return root;
    }

    @JsonProperty
    public void setRoot(String root) {
        this.root = root;
    }

    @JsonProperty("indexer")
    public IndexerFactory getIndexerFactory() {
        return indexerFactory;
    }

    @JsonProperty("indexer")
    public void setIndexerFactory(IndexerFactory indexerFactory) {
        this.indexerFactory = indexerFactory;
    }
}
