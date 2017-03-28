package com.gillessed.dnd.model.page.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiObject;

/**
 * The link class is not entirely immutable because its state depends not
 * only on its page's content, but also the presence of other pages.
 */
public class WikiLink implements WikiObject {
    private final String type = "link";
    private final String text;
    private final String targetString;
    private Target target;
    private String displayText;

    @JsonCreator
    public WikiLink(
            @JsonProperty("text") String text,
            @JsonProperty("targetString") String targetString,
            @JsonProperty("target") Target target) {
        this.text = text;
        this.targetString = targetString;
        this.target = target;
    }

    @JsonProperty
    public String getText() {
        return text;
    }

    @JsonProperty
    public String getTargetString() {
        return targetString;
    }

    @JsonProperty
    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @JsonProperty
    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @JsonProperty
    public String getType() {
        return type;
    }
}
