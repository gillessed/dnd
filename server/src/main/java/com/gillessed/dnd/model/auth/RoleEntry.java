package com.gillessed.dnd.model.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableRoleEntry.class)
@JsonDeserialize(as = ImmutableRoleEntry.class)
public interface RoleEntry {
    String getRole();
    List<String> getUsers();

    class Builder extends ImmutableRoleEntry.Builder {}
}
