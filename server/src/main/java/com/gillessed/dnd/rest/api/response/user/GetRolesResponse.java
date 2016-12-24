package com.gillessed.dnd.rest.api.response.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableGetRolesResponse.class)
@JsonDeserialize(as = ImmutableGetRolesResponse.class)
public interface GetRolesResponse {
    List<String> getRoles();

    class Builder extends ImmutableGetRolesResponse.Builder {}
}
