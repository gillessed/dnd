package com.gillessed.dnd.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gillessed.dnd.model.page.Target;

import java.io.IOException;

public class TargetSerializer extends JsonSerializer<Target> {
    @Override
    public void serialize(
            Target value,
            JsonGenerator gen,
            SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeString(value.getStringRepresentation());
    }
}
