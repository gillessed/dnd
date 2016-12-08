package com.gillessed.dnd.services.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gillessed.dnd.model.auth.User;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FileUserProvider implements UserProvider {
    private final Map<String, User> usernameToUserMap;
    private final Map<String, User> userIdToUserMap;

    public FileUserProvider(String userFile) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        usernameToUserMap = new HashMap<>();
        userIdToUserMap = new HashMap<>();

        try {
            List<User> userList = objectMapper.readValue(Paths.get(userFile).toFile(), new TypeReference<List<User>>(){});
            userList.forEach((User user) -> {
                usernameToUserMap.put(user.getUsername(), user);
                userIdToUserMap.put(user.getUserId(), user);
            });
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(usernameToUserMap.get(username));
    }

    @Override
    public Optional<User> getUserByUserId(String userId) {
        return Optional.ofNullable(userIdToUserMap.get(userId));
    }
}
