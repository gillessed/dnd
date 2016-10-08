package com.gillessed.dnd.rest.services.search.index;

import java.io.IOException;
import java.nio.file.Path;

public interface Indexer {
    void start();
    void stop();
    Index getIndex();
    void indexPage(Path pageFile, boolean deleted) throws IOException;
}
