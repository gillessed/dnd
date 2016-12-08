package com.gillessed.dnd.model.auth;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User {
    String getUserId();
    String getUsername();
    String getPassword();

    class Builder extends ImmutableUser.Builder {}
}
