package com.gillessed.dnd.model.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableSession.class)
@JsonDeserialize(as = ImmutableSession.class)
public interface Session {
    String getToken();
    User getUser();

    class Builder extends ImmutableSession.Builder {}
}
