package com.gillessed.dnd.services.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.auth.User;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Singleton
public class SessionServiceImpl implements SessionService {
    private final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final Map<String, Session> tokenToSessionMap;
    private final Map<User, Session> userToSessionMap;
    private final ObjectMapper objectMapper;
    private final ReentrantReadWriteLock lock;
    private final Path sessionStoreFile;

    @Inject
    public SessionServiceImpl(DndConfiguration configuration) {
        this.tokenToSessionMap = new HashMap<>();
        this.userToSessionMap = new HashMap<>();
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.lock = new ReentrantReadWriteLock();
        this.sessionStoreFile = Paths.get(configuration.getSessionStoreFile());
        loadSessions();
        startSessionPersister();
    }

    private void loadSessions() {
        log.info("Rehydrating session info from session store file");
        try {
            List<Session> sessions = objectMapper.readValue(sessionStoreFile.toFile(), new TypeReference<List<Session>>(){});
            sessions.forEach((Session session) -> {
                tokenToSessionMap.put(session.getToken(), session);
                userToSessionMap.put(session.getUser(), session);
            });
        } catch (IOException e) {
            log.warn("Could not rehydrate session data from session store file", e);
        }
    }

    private void startSessionPersister() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            List<Session> sessions;
            lock.readLock().lock();
            try {
                sessions = tokenToSessionMap.values().stream().collect(Collectors.toList());
            } finally {
                lock.readLock().unlock();
            }
            try {
                objectMapper.writeValue(sessionStoreFile.toFile(), sessions);
            } catch (IOException e) {
                throw new IllegalStateException("Could not write session data to file", e);
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    public Session createSessionForUser(User user) {
        Session session;
        lock.writeLock().lock();
        try {
            session = userToSessionMap.get(user);
            if (session == null) {
                String token = generateToken();
                session = new Session.Builder().user(user).token(token).build();
                tokenToSessionMap.put(token, session);
                userToSessionMap.put(user, session);
            }
        } finally {
            lock.writeLock().unlock();
        }
        return session;
    }

    private String generateToken() {
        String newToken = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        newToken = newToken.replace("-", "");
        return newToken;
    }

    @Override
    public Optional<Session> verifyToken(String token) {
        Session session;
        lock.readLock().lock();
        try {
            session = tokenToSessionMap.get(token);
        } finally {
            lock.readLock().unlock();
        }
        return Optional.ofNullable(session);
    }
}
