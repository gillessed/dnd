package com.gillessed.dnd.model.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gillessed.dnd.json.TargetDeserializer;
import com.gillessed.dnd.json.TargetSerializer;
import com.gillessed.dnd.util.pathtreemap.PathTreeMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Represents a path to a page. Essentially looks like a file
 * system path but with underscores instead of slashes.
 */
@JsonSerialize(using = TargetSerializer.class)
@JsonDeserialize(using = TargetDeserializer.class)
public class Target implements PathTreeMap.Key<String, Target> {
    private final String value;

    public Target(String value) {
        Preconditions.checkNotNull(value, "Cannot have null value target");
        this.value = value;
    }

    @Override
    public Target getParent() {
        List<String> elements = getElements();
        if (elements.size() == 1) {
            return null;
        }
        return new Target(String.join("_", elements.subList(0, elements.size() - 1)));
    }

    @Override
    public Target getCopy() {
        return new Target(value);
    }

    @Override
    public Target getChild(String value) {
        List<String> elements = getElements();
        elements.add(value);
        return new Target(String.join("_", elements));
    }

    @Override
    public String getValue() {
        List<String> elements = getElements();
        return elements.get(elements.size() - 1);
    }

    public String getStringRepresentation() {
        return value;
    }

    /**
     * Returns the relative path to the file that this target points to.
     */
    public Path getPath() {
        return Paths.get(String.join(File.separator, getElements()));
    }

    /**
     * Returns the target for a relative path.
     */
    public static Target forPath(Path path) {
        return new Target(path.toString().replace(File.separator, "_"));
    }

    private List<String> getElements() {
        return Lists.newArrayList(value.split("_"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Target target = (Target) o;

        return value != null ? value.equals(target.value) : target.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Target{" +
                "value='" + value + '\'' +
                '}';
    }
}
