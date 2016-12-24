package com.gillessed.dnd;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class DndConfiguration extends Configuration {
    private static final int DEFAULT_SEARCH_RESULT_LIMIT = 25;

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

    private int searchResultLimit = DEFAULT_SEARCH_RESULT_LIMIT;
    @JsonProperty
    public int getSearchResultLimit() {
        return searchResultLimit;
    }
    @JsonProperty
    public void setSearchResultLimit(int searchResultLimit) {
        this.searchResultLimit = searchResultLimit;
    }
}
