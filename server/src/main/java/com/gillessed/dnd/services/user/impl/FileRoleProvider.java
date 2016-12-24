package com.gillessed.dnd.services.user.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.gillessed.dnd.model.auth.RoleEntry;
import com.gillessed.dnd.services.user.RoleProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Role source for development testing.
 *
 * Reads roles in from a file on disk.
 */
@Singleton
public class FileRoleProvider implements RoleProvider {
    private final Logger log = LoggerFactory.getLogger(FileRoleProvider.class);

    private final Map<String, Set<String>> usersToRolesMap;
    private final Map<String, Set<String>> rolesToUserMap;
    private final ObjectMapper objectMapper;
    private final ReentrantReadWriteLock lock;
    private final Path roleStoreFile;

    @Inject
    public FileRoleProvider() {
        this.usersToRolesMap = new HashMap<>();
        this.rolesToUserMap = new HashMap<>();
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.objectMapper.registerModule(new GuavaModule());
        this.lock = new ReentrantReadWriteLock();
        this.roleStoreFile = Paths.get("files/roles.yml");
        loadSessions();
        startRolePersister();
    }

    private void loadSessions() {
        log.info("Rehydrating role info from role store file");
        try {
            List<RoleEntry> roles = objectMapper.readValue(roleStoreFile.toFile(), new TypeReference<List<RoleEntry>>(){});
            roles.forEach((RoleEntry role) -> {
                if (!rolesToUserMap.containsKey(role.getRole())) {
                    rolesToUserMap.put(role.getRole(), new HashSet<>());
                }
                rolesToUserMap.get(role.getRole()).addAll(role.getUsers());
                role.getUsers().forEach((String user) -> {
                    if (!usersToRolesMap.containsKey(user)) {
                        usersToRolesMap.put(user, new HashSet<>());
                    }
                    usersToRolesMap.get(user).add(role.getRole());
                });
            });
        } catch (IOException e) {
            log.warn("Could not rehydrate session data from session store file", e);
        }
    }

    private void startRolePersister() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            List<RoleEntry> roles = new ArrayList<>();
            lock.readLock().lock();
            try {
                rolesToUserMap.keySet().forEach((String role) -> {
                    roles.add(new RoleEntry.Builder().role(role).users(rolesToUserMap.get(role)).build());
                });
            } finally {
                lock.readLock().unlock();
            }
            try {
                objectMapper.writeValue(roleStoreFile.toFile(), roles);
            } catch (IOException e) {
                throw new IllegalStateException("Could not write session data to file", e);
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    public Collection<String> getRolesForUser(String user) {
        lock.readLock().lock();
        try {
            return usersToRolesMap.getOrDefault(user, Collections.emptySet());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean doesUserHaveRole(String user, String role) {
        lock.readLock().lock();
        try {
            return usersToRolesMap.get(user).contains(role);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addRolesToUser(String user, Collection<String> roles) {
        lock.writeLock().lock();
        try {
            usersToRolesMap.get(user).addAll(roles);
            roles.forEach((String role) -> rolesToUserMap.get(role).add(user));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeRolesFromUser(String user, Collection<String> roles) {
        lock.writeLock().lock();
        try {
            usersToRolesMap.get(user).removeAll(roles);
            if (usersToRolesMap.get(user).isEmpty()) {
                usersToRolesMap.remove(user);
            }
            roles.forEach((String role) -> {
                rolesToUserMap.get(role).remove(user);
                if (rolesToUserMap.get(role).isEmpty()) {
                    rolesToUserMap.remove(role);
                }
            });
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeRolesFromAllUsers(Collection<String> roles) {
        lock.writeLock().lock();
        try {
            roles.forEach(this::removeRoleFromAllUsers);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void removeRoleFromAllUsers(String role) {
        Collection<String> users = rolesToUserMap.get(role);
        rolesToUserMap.remove(role);
        users.forEach((String user) -> {
            usersToRolesMap.get(user).remove(role);
            if (usersToRolesMap.get(user).isEmpty()) {
                usersToRolesMap.remove(user);
            }
        });
    }
}
