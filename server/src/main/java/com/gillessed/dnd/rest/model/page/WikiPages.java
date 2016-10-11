package com.gillessed.dnd.rest.model.page;

import com.gillessed.dnd.rest.model.page.objects.WikiParagraph;
import com.gillessed.dnd.rest.model.page.objects.WikiSection;

import java.util.ArrayList;
import java.util.List;

public final class WikiPages {
    /**
     * Returns a list of all wiki objects in a page as a flat list.
     */
    public static List<WikiObject> flatten(WikiPage page) {
        List<WikiObject> objects = new ArrayList<>();
        page.getWikiSections().forEach((WikiSection section) -> objects.addAll(flattenSection(section)));
        return objects;
    }

    private static List<WikiObject> flattenSection(WikiSection section) {
        List<WikiObject> objects = new ArrayList<>();
        for (WikiObject object : section.getWikiObjects()) {
            objects.add(object);
            if (WikiParagraph.class.isAssignableFrom(object.getClass())) {
                WikiParagraph paragraph = (WikiParagraph) object;
                objects.addAll(paragraph.getWikiObjects());
            }
        }
        return objects;
    }

    private WikiPages() {
        throw new UnsupportedOperationException();
    }
}
