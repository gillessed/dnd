package com.gillessed.dnd.rest.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableLoginResponse.class)
@JsonDeserialize(as = ImmutableLoginResponse.class)
public interface LoginResponse {
    String getToken();
    String getUserId();
}
