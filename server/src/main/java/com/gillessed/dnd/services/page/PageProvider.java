package com.gillessed.dnd.services.page;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.rest.api.response.status.StatusResponse;

import java.util.List;

public interface PageProvider {
    WikiPage getPageByTarget(Target target);
    List<Target> getChildrenForTarget(Target target);
    List<WikiPage> search(String query);
    StatusResponse getStatus();
    void start();
    void reloadPages();
    void addOrUpdatePage(WikiPage page, List<WikiLink> links);
    void deletePage(Target target);
    void addListener(Listener listener);
    void removeListener(Listener listener);

    interface Listener {
        void pageAddedOrUpdated(WikiPage page);
        void pageDeleted(Target target);
    }
}
