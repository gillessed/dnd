package com.gillessed.dnd.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gillessed.dnd.model.page.Target;

import java.io.IOException;

public class TargetDeserializer extends JsonDeserializer<Target> {
    @Override
    public Target deserialize(
            JsonParser p,
            DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String text = p.getText();
        return new Target(text);
    }
}
